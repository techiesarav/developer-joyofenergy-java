package uk.tw.energy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.tw.energy.service.UsageService;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UsageControllerTests {

    private UsageController usageController;

    @MockBean
    private UsageService usageService;


    @BeforeEach
    public void setUp() {
        usageController = new UsageController(usageService);
    }

    @Test
    public void WhenRequestForWeeklyUsage_ThenDisplayCorrectResult() throws Exception {
        String smartMeterId= "smart-meter-0";
        Map<String,BigDecimal> resultMap = new HashMap<>();
        resultMap.put("price-plan0",new BigDecimal(10));
        when(usageService.getWeeklyUsage(smartMeterId)).thenReturn(Optional.of(resultMap));
        ResponseEntity<Map<String,BigDecimal>> responseEntity = usageController.getWeeklyUsage(smartMeterId);
        Map<String,BigDecimal> weeklyCost = responseEntity.getBody();
        assertEquals(weeklyCost,resultMap);
    }
}
