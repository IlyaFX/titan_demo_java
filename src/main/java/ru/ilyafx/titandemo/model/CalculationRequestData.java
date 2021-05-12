package ru.ilyafx.titandemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CalculationRequestData {

    private List<String> functions;
    private int calculations;
    private CalculationResponseType responseType;

}
