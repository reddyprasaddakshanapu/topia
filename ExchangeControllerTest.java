package com.assesment.topia.controller;

import com.assesment.topia.model.RateDto;
import com.assesment.topia.model.RateExchangeResponse;
import com.assesment.topia.model.TimeSeriesApiResponse;
import com.assesment.topia.service.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


public class ExchangeControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ExchangeRateController exchangeRateController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = standaloneSetup(exchangeRateController).build();
    }

    @Test
    public void testGetForexRateFromFrankfurter_DefaultTarget() throws Exception {
        RateDto rateDto = new RateDto("EUR","98.04");
        RateExchangeResponse mockResponse = new RateExchangeResponse();
        mockResponse.setDate("16-08-2024");
        mockResponse.setSource("USD");
        mockResponse.setRates(Arrays.asList(rateDto));


        when(exchangeRateService.getForexRateFromFrankfurter(null)).thenReturn(mockResponse);

        mockMvc.perform(get("/fx"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.source").value("USD"));
    }

    @Test
    public void testGetForexRateFromFrankfurter_SpecificTarget() throws Exception {
        RateDto rateDto = new RateDto("GBP","0.78");
        RateExchangeResponse mockResponse = new RateExchangeResponse();
        mockResponse.setDate("16-08-2024");
        mockResponse.setSource("USD");
        mockResponse.setRates(Arrays.asList(rateDto));

        when(exchangeRateService.getForexRateFromFrankfurter("GBP")).thenReturn(mockResponse);

        mockMvc.perform(get("/fx").param("target", "GBP"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.source").value("USD"));
    }

    @Test
    public void testGetForexRateFromTimeSeries() throws Exception {
        TimeSeriesApiResponse mockResponse = new TimeSeriesApiResponse();
        RateDto rateDto = new RateDto("GBP","0.78");
        mockResponse.setRates(Map.of("GBP", rateDto));
        when(exchangeRateService.getTargetCurrencyFromTimeSeriesAPI("JPY")).thenReturn(mockResponse);

        mockMvc.perform(get("/fx/JPY"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.source").value("USD"));
    }
}
