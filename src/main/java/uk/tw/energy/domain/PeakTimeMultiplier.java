package uk.tw.energy.domain;

import java.math.BigDecimal;
import java.time.DayOfWeek;

public class PeakTimeMultiplier {

    DayOfWeek dayOfWeek;
    BigDecimal multiplier;

    public PeakTimeMultiplier(DayOfWeek dayOfWeek, BigDecimal multiplier) {
        this.dayOfWeek = dayOfWeek;
        this.multiplier = multiplier;
    }
}
