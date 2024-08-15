package com.rhseung.blueprint.registration

interface IModInit {
    /**
     * Update [IBaseKey.langs] changed by [Translation] annotations.
     */
    fun register() {
        this::class.java.declaredFields
            .filterNot { it.name == "INSTANCE" }
            .forEach { field ->
                field.isAccessible = true
                field.annotations.forEach { annotation ->
                    if (annotation is Translation) {
                        val language = annotation.language
                        val translation = annotation.translation
                        val key = field.get(this) as IBaseKey

                        key.langs[language] = translation
                    }
                }
            }
    }
}