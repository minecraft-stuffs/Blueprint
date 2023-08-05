package com.rhseung.blueprint.registration

import com.rhseung.blueprint.Blueprint

interface IModInit {
    /**
     * Update [IBaseKey.langs] changed by [Lang] annotations.
     */
    fun register() {
        this::class.java.declaredFields
            .filterNot { it.name == "INSTANCE" }
            .forEach { field ->
                field.isAccessible = true
                field.annotations.forEach { annotation ->
                    if (annotation is Lang) {
                        val language = annotation.language
                        val translation = annotation.translation
                        val key = field.get(this) as IBaseKey

                        key.langs[language] = translation
                    }
                }
            }
    }
}