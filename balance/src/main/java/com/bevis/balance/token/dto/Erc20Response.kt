package com.bevis.balance.token.dto

import java.math.BigDecimal

data class Erc20Response(
    var address: String? = null,
    var name: String? = null,
    var symbol: String? = null,
    var logo: String? = null,
    var balance: BigDecimal? = null,
    var decimals: BigDecimal? = null
)
