package com.demo.weatherapp.utils

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Class to make oneTimeEvent
 */
class OneTimeEvent<T>(
    private val value: T
) {

    private val isConsumed = AtomicBoolean(false)

    private fun getValue(): T? =
        if (isConsumed.compareAndSet(false, true)) value else null

    fun consume(block: (T) -> Unit): T? =
        getValue()?.also(block)
}

fun <T> T.toOneTimeEvent() = OneTimeEvent(this)