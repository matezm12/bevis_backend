package com.bevis.nft.slp

data class ElectronCashSlpException(override var message: String? = null, var statusCode: Int? = null) : Exception(message)
