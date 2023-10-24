package com.bevis.balance.impl;

import com.bevis.balance.coinbalance.dto.WalletRequest;
import com.bevis.balance.dto.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
interface BalanceMapper {

    @Mapping(source = "balance", target = "value")
    Balance toBalance(com.bevis.balance.coinbalance.dto.Balance balance);

    List<Balance> toBalanceList(List<com.bevis.balance.coinbalance.dto.Balance> requests);

    List<WalletRequest> mapRequest(List<com.bevis.balance.dto.WalletRequest> requests);

    default WalletRequest mapRequest(com.bevis.balance.dto.WalletRequest value) {
        return new WalletRequest(value.getCurrency(), value.getAddress());
    }

}
