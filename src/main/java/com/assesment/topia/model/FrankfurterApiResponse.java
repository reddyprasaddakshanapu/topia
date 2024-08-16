package com.assesment.topia.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class FrankfurterApiResponse {

    int amount;
    String base;
    Date date;
    Map<String, Double> rates;
}
