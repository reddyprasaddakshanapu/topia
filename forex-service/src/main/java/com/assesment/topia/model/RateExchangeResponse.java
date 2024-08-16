package com.assesment.topia.model;

import lombok.Data;

import java.util.List;

@Data
public class RateExchangeResponse {
    String date;
    String source = "USD";
    List<RateDto> rates;

}
