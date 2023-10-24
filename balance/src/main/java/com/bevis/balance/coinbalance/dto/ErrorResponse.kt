package com.bevis.balance.coinbalance.dto

data class ErrorResponse(val statusCode: Int, val message: String) {
    constructor(): this(400, "")

    override fun toString(): String {
        return "ErrorResponse(statusCode=$statusCode, message='$message')"
    }

}
