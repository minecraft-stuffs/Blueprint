package com.rhseung.blueprint.example

import com.rhseung.blueprint.datagen.provider.AbstractItemTagProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class ModItemTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : AbstractItemTagProvider(output, registriesFuture) {
}