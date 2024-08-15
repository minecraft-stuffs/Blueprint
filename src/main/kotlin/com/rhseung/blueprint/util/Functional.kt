package com.rhseung.blueprint.util

object Functional {
    fun <T> Boolean.ifElse(`true`: T, `false`: T): T {
        return if (this) `true` else `false`
    }

    inline fun Boolean.ifTrue(run: () -> Unit) {
        if (this) run()
    }

    inline fun Boolean.ifFalse(run: () -> Unit) {
        if (!this) run()
    }

    inline fun <T: Any> T?.ifNull(run: (T?) -> Unit) {
        if (this == null) run(this)
    }

    inline fun <T: Any> T?.ifNotNull(run: (T) -> Unit) {
        if (this != null) run(this)
    }

    inline fun <T> Iterable<T>.findFail(predicate: (T) -> Boolean): Boolean {
        return this.find { predicate(it) } == null
    }
}