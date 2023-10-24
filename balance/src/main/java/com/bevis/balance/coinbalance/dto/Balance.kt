package com.bevis.balance.coinbalance.dto

data class Balance(
    var currency: String = "",
    var address: String = "",
    var balance: Double? = null,
    var divider: Double? = null
    )
