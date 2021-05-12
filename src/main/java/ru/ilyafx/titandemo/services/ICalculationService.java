package ru.ilyafx.titandemo.services;

import reactor.core.publisher.Flux;
import ru.ilyafx.titandemo.model.CalculationRequestData;

public interface ICalculationService {

    Flux<String> calculateUnordered(CalculationRequestData request);

    Flux<String> calculateOrdered(CalculationRequestData request);

}
