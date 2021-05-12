package ru.ilyafx.titandemo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.ilyafx.titandemo.model.CalculationRequestData;
import ru.ilyafx.titandemo.model.CalculationResponseType;
import ru.ilyafx.titandemo.services.CalculationService;

@RestController
public class CalculationController {

    @Autowired
    private CalculationService calculationService;

    @RequestMapping(
            value = "/calculator",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public Flux<String> calculator(@RequestBody CalculationRequestData request) {
        if (request.getResponseType() == CalculationResponseType.ON_TIME) {
            return calculationService.calculateUnordered(request);
        } else {
            return calculationService.calculateOrdered(request);
        }
    }

}
