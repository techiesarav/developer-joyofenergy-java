package uk.tw.energy.service;

import org.springframework.stereotype.Service;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PricePlanService {

    private final List<PricePlan> pricePlans;
    private final MeterReadingService meterReadingService;

    private final CostCalculationService costCalculationService;

    public PricePlanService(List<PricePlan> pricePlans, MeterReadingService meterReadingService, CostCalculationService costCalculationService) {
        this.pricePlans = pricePlans;
        this.meterReadingService = meterReadingService;
        this.costCalculationService = costCalculationService;
    }


    public Optional<Map<String, BigDecimal>> getConsumptionCostOfElectricityReadingsForEachPricePlan(String smartMeterId) {
        Optional<List<ElectricityReading>> electricityReadings = meterReadingService.getReadings(smartMeterId);

        if (!electricityReadings.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(pricePlans.stream().collect(
                Collectors.toMap(PricePlan::getPlanName, t -> costCalculationService.calculateCost(electricityReadings.get(), t))));
    }

}
