package com.rhseung.blueprint.example

import com.rhseung.blueprint.registration.IModInit
import com.rhseung.blueprint.registration.Lang
import com.rhseung.blueprint.registration.Registries
import com.rhseung.blueprint.registration.Registries.ITEMS
import com.rhseung.blueprint.util.Languages

object ModItems : IModInit {
    @Lang(Languages.EN_US, "Example Example Item")
    val EXAMPLE_ITEM = ITEMS.register("example_item")
}