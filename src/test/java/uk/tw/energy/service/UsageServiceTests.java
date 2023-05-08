package uk.tw.energy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class UsageServiceTests {

    private UsageService usageService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private CostCalculationService costCalculationService;

    @MockBean
    private MeterReadingService meterReadingService;
    @MockBean
    private List<PricePlan> pricePlans;

    @BeforeEach
    public void setup() {
        pricePlans = Arrays.asList(new PricePlan("price-plan-0", "Dr Evil's Dark Energy", BigDecimal.TEN, emptyList()));
        usageService = new UsageService(accountService,costCalculationService,meterReadingService,pricePlans);
    }

    @Test
    public void WhenRequestForWeeklyUsageCost_ThenReturnResult() throws Exception {
        List<ElectricityReading> electricityReadings = Arrays.asList(
                new ElectricityReading(Instant.now(),BigDecimal.valueOf(0.12)),
                new ElectricityReading(Instant.now().minus(8, ChronoUnit.DAYS),BigDecimal.valueOf(0.12))
        );
        Mockito.when(accountService.getPricePlanIdForSmartMeterId("smart-meter-0")).thenReturn("price-plan-0");
        Mockito.when(meterReadingService.getReadings("smart-meter-0")).thenReturn(Optional.of(electricityReadings));
        Mockito.when(costCalculationService.calculateReadingsBySpecifiedTime(electricityReadings,7)).thenReturn(
                Arrays.asList(new ElectricityReading(Instant.now(),BigDecimal.valueOf(0.12))));
        Optional<Map<String,BigDecimal>> result = usageService.getWeeklyUsage("smart-meter-0");
        Map<String,BigDecimal> expected = new HashMap<>();
        expected.put("price-plan-0",BigDecimal.valueOf(0.12));
        assertEquals(result.get(),expected);
    }

    @Test
    public void WhenRequestForWeeklyUsageCostWithSmartMeterId_ThenReturnErrorMessageIfPricePlanNotPresent() {

    }
}
