package com.assesment.topia.service;

import com.assesment.topia.dao.ExchangeRateRepository;
import com.assesment.topia.model.RateDto;
import com.assesment.topia.model.RateExchangeResponse;
import com.assesment.topia.model.FrankfurterApiResponse;
import com.assesment.topia.model.TimeSeriesApiResponse;
import com.assesment.topia.entity.ExchangeRates;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@AllArgsConstructor
@Service
@Slf4j
public class ExchangeRateService {

    ExchangeRateRepository exchangeRateRepository;
    public RateExchangeResponse getForexRateFromFrankfurter(String target) throws Exception {
        var currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Date.from(Instant.now()));
        var response = new RateExchangeResponse();
        response.setDate(currentDate);
        List<ExchangeRates> data = new ArrayList<>();
        if (target == null || target.isBlank()) {
            target="EUR,GBP,JPY,CZK";
        }
        for (var t: target.split(",")) {
            List<ExchangeRates> d = new ArrayList<>();
            var res = exchangeRateRepository.findDistinctFirstByDateAndCurrency(currentDate, t);
            // If data for particular target is empty
            if (res == null) {
                d = getForexRateFromFrankFurterApi(t, currentDate);
            } else {
                d.add(res);
            }
            data.addAll(d);
        }
        // If entire data is empty
        if (data.isEmpty()) {
            data = getForexRateFromFrankFurterApi(target, currentDate);
        }
        List<RateDto> rates = new ArrayList<>();
        data.forEach(dat -> rates.add(new RateDto(dat.getCurrency(), dat.getValue())));
        response.setRates(rates);
        return response;
    }

    private List<ExchangeRates> getForexRateFromFrankFurterApi(String target, String date) throws Exception {
        var data = getResponseFromFrankFurterAPI(target, date);
        List<ExchangeRates> response = new ArrayList<>();
        data.getRates().forEach((k, v) -> {
            ExchangeRates rate = new ExchangeRates();
            rate.setDate(date);
            rate.setCurrency(k);
            rate.setValue(v.toString());
            response.add(rate);
            saveDataToDB(date, k, v.toString());
        });
        return response;
    }

    public void saveDataToDB(String date, String currency, String value) {
        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.setDate(date);
        exchangeRates.setCurrency(currency);
        exchangeRates.setValue(value);
        exchangeRateRepository.save(exchangeRates);
    }

    private  FrankfurterApiResponse getResponseFromFrankFurterAPI(String target, String date) throws URISyntaxException, IOException, InterruptedException {
        log.info("Fetching from API - Currency = {}, Date = {}", target, date);
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl("https://api.frankfurter.app/{date}")
                .queryParam("from", "USD")
                .queryParam("to", target)
                .buildAndExpand(date)
                .toUriString();

        return restTemplate.getForObject(url, FrankfurterApiResponse.class);
    }

    public TimeSeriesApiResponse getTargetCurrencyFromTimeSeriesAPI(String targetCurrency) throws Exception {
        Map<String, RateDto> timeSeries = new HashMap<>();
        for(int i = 0 ; i <= 2; i++){
            var currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Date.from(Instant.now().minus(i, ChronoUnit.DAYS)));
            List<ExchangeRates> data = new ArrayList<>();
            var res = exchangeRateRepository.findDistinctFirstByDateAndCurrency(currentDate, targetCurrency);
            if(res == null){
                data = getForexRateFromFrankFurterApi(targetCurrency, currentDate);
            } else  {
                data.add(res);
            }

            data.forEach(dat -> {
                RateDto rateDto = new RateDto(dat.getCurrency(), dat.getValue());
                timeSeries.put(currentDate, rateDto);
            });

        }

        TimeSeriesApiResponse timeSeriesApiResponse = new TimeSeriesApiResponse();
        timeSeriesApiResponse.setRates(timeSeries);
        return timeSeriesApiResponse;
    }


}
