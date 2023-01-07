package com.org.datingapp.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersProvider {
    fun io() : CoroutineDispatcher = Dispatchers.IO
    fun main() : CoroutineDispatcher = Dispatchers.Main
}