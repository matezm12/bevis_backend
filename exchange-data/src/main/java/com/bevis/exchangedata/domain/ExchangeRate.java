package com.bevis.exchangedata.domain;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "exchange_rates")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "usd_price")
    private Double usdPrice;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdDate = Instant.now();

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant lastModifiedDate = Instant.now();
}
