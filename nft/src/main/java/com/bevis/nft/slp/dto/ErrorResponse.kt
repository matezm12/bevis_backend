package com.bevis.nft.slp.dto

data class ErrorResponse(var statusCode: Int? = null, var message: String? = null) {
    constructor() : this(400, "")

    override fun toString(): String {
        return "ErrorResponse(statusCode=$statusCode, message='$message')"
    }

}
