package ru.ilyafx.titandemo.model;

import lombok.Data;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Data
public class CalculationProperties {

    @DurationUnit(ChronoUnit.MILLIS)
    private Duration intervalInMillis;

}
