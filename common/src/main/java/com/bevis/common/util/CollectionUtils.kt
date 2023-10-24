package com.bevis.common.util

fun <T> concatenate(vararg lists: List<T>): List<T> {
    return listOf(*lists).flatten()
}
