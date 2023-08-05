package com.rhseung.blueprint.example

import com.rhseung.blueprint.data.AbstractLanguageProvider
import com.rhseung.blueprint.util.Languages
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

class ModLanguageProvider(
    override val output: FabricDataOutput,
    override val language: Languages
) : AbstractLanguageProvider(output, language) {

}