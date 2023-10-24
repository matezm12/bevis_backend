package com.bevis.balance.dto


data class CryptoToken(
    var address: String?,
    var logo: String?,
    var name: String?,
    var currency: String?,
    var balance: Double?
)
