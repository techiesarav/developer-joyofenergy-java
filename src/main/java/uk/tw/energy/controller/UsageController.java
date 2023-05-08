package uk.tw.energy.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.energy.service.UsageService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("cost-usage")
public class UsageController {

    private final UsageService usageService;

    UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    @GetMapping("weekly/{smartMeterId}")
    public ResponseEntity<Map<String,BigDecimal>> getWeeklyUsage(@PathVariable String smartMeterId) throws Exception {
        Optional<Map<String,BigDecimal>>  resultOpt = usageService.getWeeklyUsage(smartMeterId);
        if(!resultOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultOpt.get());
    }
}
