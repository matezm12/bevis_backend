package com.bevis.nft.nfttoken.dto

data class DataResponse<T>(val data: List<T>, val page: Int? = null, val pageSize: Int? = null, val total: Int? = null)
