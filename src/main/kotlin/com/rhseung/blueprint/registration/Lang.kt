package com.rhseung.blueprint.registration

import com.rhseung.blueprint.util.Languages

@Target(AnnotationTarget.FIELD)
annotation class Lang(val language: Languages, val translation: String)
