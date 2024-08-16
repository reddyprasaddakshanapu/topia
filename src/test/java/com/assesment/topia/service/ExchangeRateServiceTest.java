package com.assesment.topia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import com.assesment.topia.dao.ExchangeRateRepository;
import com.assesment.topia.entity.ExchangeRates;
import com.assesment.topia.model.RateExchangeResponse;
import com.assesment.topia.model.TimeSeriesApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
public class ExchangeRateServiceTest {
    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetForexRateFromFrankfurter_WhenDataExistsInDb() throws Exception {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Date.from(Instant.now()));
        ExchangeRates mockRate = new ExchangeRates();
        mockRate.setDate(currentDate);
        mockRate.setCurrency("EUR");
        mockRate.setValue("0.92");

        when(exchangeRateRepository.findDistinctFirstByDateAndCurrency(currentDate, "EUR"))
                .thenReturn(mockRate);

        RateExchangeResponse response = exchangeRateService.getForexRateFromFrankfurter("EUR");

        assertEquals(currentDate, response.getDate());
        assertEquals(1, response.getRates().size());
        assertEquals("EUR", response.getRates().get(0).getTarget());
        assertEquals("0.92", response.getRates().get(0).getValue());
    }

    @Test
    public void testGetTargetCurrencyFromTimeSeriesAPI() throws Exception {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Date.from(Instant.now()));
        ExchangeRates mockRate = new ExchangeRates();
        mockRate.setDate(currentDate);
        mockRate.setCurrency("JPY");
        mockRate.setValue("110.0");

        when(exchangeRateRepository.findDistinctFirstByDateAndCurrency(any(String.class), eq("JPY")))
                .thenReturn(mockRate);

        TimeSeriesApiResponse response = exchangeRateService.getTargetCurrencyFromTimeSeriesAPI("JPY");

        assertEquals(3, response.getRates().size());
        assertEquals("JPY", response.getRates().get(currentDate).getTarget());
        assertEquals("110.0", response.getRates().get(currentDate).getValue());
    }

    @Test
    public void testSaveDataToDB() {
        String currentDate = "2024-08-16";
        String currency = "EUR";
        String value = "0.92";

        exchangeRateService.saveDataToDB(currentDate, currency, value);

        verify(exchangeRateRepository, times(1)).save(any(ExchangeRates.class));
    }
}
