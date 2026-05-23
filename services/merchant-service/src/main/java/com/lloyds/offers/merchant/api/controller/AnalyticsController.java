package com.lloyds.offers.merchant.api.controller;

import com.lloyds.offers.merchant.application.query.GetAnalyticsHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/merchants/{merchantId}/analytics")
public class AnalyticsController {

    private final GetAnalyticsHandler getAnalyticsHandler;

    public AnalyticsController(GetAnalyticsHandler getAnalyticsHandler) {
        this.getAnalyticsHandler = getAnalyticsHandler;
    }

    @GetMapping
    public Map<String, Object> getAnalytics(@PathVariable UUID merchantId) {
        return getAnalyticsHandler.handle(merchantId);
    }
}
