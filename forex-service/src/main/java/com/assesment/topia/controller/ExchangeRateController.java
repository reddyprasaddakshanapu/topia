package com.assesment.topia.controller;

import com.assesment.topia.model.RateExchangeResponse;
import com.assesment.topia.model.TimeSeriesApiResponse;
import com.assesment.topia.service.ExchangeRateService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("fx")
@AllArgsConstructor
public class ExchangeRateController {

    ExchangeRateService exchangeRateService;
    @GetMapping
    public RateExchangeResponse getForexRateFromFrankfurter(@RequestParam(required = false) String target) throws Exception {
        return exchangeRateService.getForexRateFromFrankfurter(target);
    }

    @GetMapping(path = "/{targetCurrency}")
    public TimeSeriesApiResponse getForexRateFromTimeSeries(@PathVariable(name = "targetCurrency") String targetCurrency){
        try {
            return exchangeRateService.getTargetCurrencyFromTimeSeriesAPI(targetCurrency);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
