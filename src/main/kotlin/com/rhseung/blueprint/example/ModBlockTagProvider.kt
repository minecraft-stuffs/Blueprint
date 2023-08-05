package com.rhseung.blueprint.example

import com.rhseung.blueprint.data.AbstractBlockTagProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class ModBlockTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : AbstractBlockTagProvider(output, registriesFuture) {

}