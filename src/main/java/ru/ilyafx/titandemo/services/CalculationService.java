package ru.ilyafx.titandemo.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.ilyafx.titandemo.config.CalculationConfig;
import ru.ilyafx.titandemo.model.CalculationRequestData;

import javax.script.ScriptException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CalculationService implements ICalculationService {

    @Autowired
    private JavaScriptEvaluationService javaScriptService;

    @Autowired
    private CalculationConfig config;

    @Override
    public Flux<String> calculateUnordered(CalculationRequestData request) {
        Flux<BenchRecord> out = Flux.empty();
        List<String> functions = request.getFunctions();
        for (int i = 0; i < functions.size(); i++) {
            out = out.mergeWith(benchFlux(request.getCalculations(), i + 1, functions.get(i)));
        }
        return out.map(BenchRecord::toString);
    }

    @Override
    public Flux<String> calculateOrdered(CalculationRequestData request) {
        List<String> functions = request.getFunctions();
        int initSize = functions.size();
        List<AtomicInteger> counters = new ArrayList<>(initSize);
        List<Flux<BenchRecord>> fluxes = new ArrayList<>(initSize);
        for (int i = 0; i < functions.size(); i++) {
            counters.add(new AtomicInteger());
            int finalI = i;
            fluxes.add(benchFlux(request.getCalculations(), i + 1, functions.get(i)).map(record -> {
                counters.get(finalI).incrementAndGet();
                return record;
            }));
        }
        return Flux.zip(fluxes, records -> {
            List<BenchRecord> recordsTyped = Arrays.stream(records).map(o -> (BenchRecord) o).collect(Collectors.toList());
            int iteration = recordsTyped.get(0).getIteration();
            return new ComparisonRecord(iteration, recordsTyped, counters.stream().map(i -> i.get() - iteration).collect(Collectors.toList())).toString();
        });
    }

    private Flux<BenchRecord> benchFlux(int rangeEnd, int fnId, String code) {
        return Flux.range(1, rangeEnd).delayElements(config.properties().getIntervalInMillis()).map(value -> {
            Instant start = Instant.now();
            Integer result = null;
            try {
                result = javaScriptService.eval(code, value);
            } catch (ScriptException | NoSuchMethodException e) {
                // TODO: Send a short exception to client
            }
            return new BenchRecord(value, fnId, result, Duration.between(start, Instant.now()));
        });
    }

    private static String formatDuration(Duration duration) {
        return duration.toMillis() + "ms";
    }

    @Data
    @AllArgsConstructor
    private static final class BenchRecord {

        private int iteration;
        private int function;
        private Integer result;
        private Duration duration;

        public String toString() {
            return iteration + ", " + function + ", " + result + ", " + formatDuration(duration);
        }

        public String toShort() {
            return result + ", " + formatDuration(duration);
        }

    }

    @Data
    @AllArgsConstructor
    private static final class ComparisonRecord {

        private int iteration;
        private List<BenchRecord> records;
        private List<Integer> ahead;

        public String toString() {
            List<String> results = new ArrayList<>(records.size());
            for (int i = 0; i < records.size(); i++) {
                results.add(records.get(i).toShort() + ", " + ahead.get(i));
            }
            return iteration + ", " + String.join(", ", results);
        }

    }
}
