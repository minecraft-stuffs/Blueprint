package com.rhseung.blueprint.datagen.provider

import com.rhseung.blueprint.datagen.handler.RecipeHandler
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

abstract class AbstractRecipeProvider(
    open val output: FabricDataOutput,
    open val registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricRecipeProvider(output, registriesFuture) {

    override fun generate(exporter: RecipeExporter) {
        val handler = RecipeHandler(exporter)
        register(handler)
    }

    open fun register(handler: RecipeHandler) {}
}