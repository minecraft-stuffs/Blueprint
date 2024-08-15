package com.rhseung.blueprint.example

import com.rhseung.blueprint.datagen.provider.AbstractLanguageProvider
import com.rhseung.blueprint.util.Languages
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class ModLanguageProvider(
    output: FabricDataOutput,
    registryFuture: CompletableFuture<RegistryWrapper.WrapperLookup>,
    language: Languages
) : AbstractLanguageProvider(output, registryFuture, language) {

}