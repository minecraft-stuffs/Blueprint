package com.rhseung.blueprint.util

object Functional {
    fun Boolean.ifElse(ifTrue: Any?, ifFalse: Any?): Any? {
        return if (this) ifTrue else ifFalse
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