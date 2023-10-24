package com.bevis.common.util

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}

suspend fun <A, B> Iterable<A>.pmapIndexed(f: suspend (index: Int, A) -> B): List<B> = coroutineScope {
    mapIndexed { index, it -> async { f(index, it) } }.awaitAll()
}
