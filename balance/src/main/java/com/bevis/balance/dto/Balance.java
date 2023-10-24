package com.bevis.balance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Balance {

    private String currency;
    
    private String address;

    private Double value;

    private Double divider;

    public Optional<Double> getRealBalanceNullable(){
        return (Objects.nonNull(value) && Objects.nonNull(divider)) ? Optional.of(value / divider) : Optional.empty();
    }

    public static Double getRealBalance(Balance balance) {
        Double value = balance.value;
        Double divider = balance.divider;
        return (Objects.nonNull(value) && Objects.nonNull(divider)) ? (value / divider) : null;
    }
}
