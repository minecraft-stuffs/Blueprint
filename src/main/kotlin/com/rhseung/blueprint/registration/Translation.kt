package com.rhseung.blueprint.registration

import com.rhseung.blueprint.util.Languages

@Repeatable
@Target(AnnotationTarget.FIELD)
annotation class Translation(val language: Languages, val translation: String)
