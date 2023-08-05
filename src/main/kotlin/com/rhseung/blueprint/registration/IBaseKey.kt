package com.rhseung.blueprint.registration

import com.rhseung.blueprint.file.Loc
import com.rhseung.blueprint.util.Languages.LanguageTable
import net.minecraft.registry.Registry

interface IBaseKey {
    /**
     * The ID of the item. This is used to register the key.
     */
    val id: Loc

    /**
     * The language table of the item. The key is the language code, and the value is the translated string.
     */
    val langs: LanguageTable

    /**
     * Registers the key to the [Registry].
     */
    fun register()

    /**
     * Returns the string representation of the key.
     */
    override fun toString(): String
}