package com.bevis.balance.coinbalance.dto

data class BalanceSource(
    val currency: String,
    val source: String,
    val multi: Boolean,
    val multiPost: Boolean? = null,
    val limit: Int? = null
)
