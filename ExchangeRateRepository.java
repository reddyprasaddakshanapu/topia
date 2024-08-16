package com.assesment.topia.dao;

import com.assesment.topia.entity.ExchangeRates;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExchangeRateRepository extends CrudRepository<ExchangeRates, UUID> {

    public ExchangeRates findDistinctFirstByDateAndCurrency(String date, String currency);
}
