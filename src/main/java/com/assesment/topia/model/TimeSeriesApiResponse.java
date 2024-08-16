package com.assesment.topia.model;

import lombok.Data;

import java.util.Map;

@Data
public class TimeSeriesApiResponse {
    String source = "USD";
    Map<String, RateDto> rates;
}
