package com.bevis.balance.coinbalance

data class BalanceException(override val message: String, val statusCode: Int? = null) : Exception(message)

