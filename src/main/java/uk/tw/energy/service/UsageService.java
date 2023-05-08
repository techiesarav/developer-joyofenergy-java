package uk.tw.energy.service;

import org.springframework.stereotype.Service;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsageService {

    private final AccountService accountService;

    private final CostCalculationService costCalculationService;

    private final MeterReadingService meterReadingService;
    private final List<PricePlan> pricePlans;

    UsageService(AccountService accountService, CostCalculationService costCalculationService,MeterReadingService meterReadingService,List<PricePlan> pricePlans) {
        this.accountService = accountService;
        this.costCalculationService = costCalculationService;
        this.meterReadingService = meterReadingService;
        this.pricePlans = pricePlans;
    }

    public Optional<Map<String,BigDecimal>> getWeeklyUsage(String smartMeterId) throws Exception {
        String pricePlanId = accountService.getPricePlanIdForSmartMeterId(smartMeterId);
        if(pricePlanId != null) {
            Optional<List<ElectricityReading>> meterReadingOptinal = meterReadingService.getReadings(smartMeterId);
            if(!meterReadingOptinal.isPresent() && meterReadingOptinal.get().isEmpty()){
                return Optional.of(pricePlans.stream().collect(
                        Collectors.toMap(PricePlan::getPlanName, t->BigDecimal.valueOf(0))));
            }
            List<ElectricityReading > specifiedReadings = costCalculationService.calculateReadingsBySpecifiedTime(meterReadingOptinal.get(),7);
            return Optional.of(pricePlans.stream().collect(
                    Collectors.toMap(PricePlan::getPlanName, t -> costCalculationService.calculateCost(specifiedReadings, t))));
        }
        else {
            throw new Exception("Priceplan not attached with meterid");
        }
    }
}
