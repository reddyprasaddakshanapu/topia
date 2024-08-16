package com.assesment.topia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "exchange_rates", uniqueConstraints = { @UniqueConstraint(columnNames = { "date", "currency" }) })
public class ExchangeRates {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String currency;

    String value;

    String date;
}

