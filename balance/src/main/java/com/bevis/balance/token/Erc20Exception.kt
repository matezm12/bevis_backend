package com.bevis.balance.token

data class Erc20Exception(override val message: String, val statusCode: Int? = null) : Exception(message)

