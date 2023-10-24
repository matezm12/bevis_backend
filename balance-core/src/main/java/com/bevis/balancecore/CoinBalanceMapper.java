package com.bevis.balancecore;


import com.bevis.balance.coinbalance.dto.BalanceSource;
import com.bevis.balancecore.domain.CoinBalanceSource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CoinBalanceMapper {

    CoinBalanceMapper INSTANCE = Mappers.getMapper(CoinBalanceMapper.class);

    default BalanceSource mapBalanceSource(CoinBalanceSource src) {
        return new BalanceSource(src.getCurrency(), src.getSourceKey(), src.isMulti(), src.getMultiPost(), src.getMultiLimit());
    }
}
