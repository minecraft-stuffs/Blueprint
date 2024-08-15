package com.rhseung.blueprint.example

import com.rhseung.blueprint.datagen.provider.AbstractBlockLootTableProvider
import com.rhseung.blueprint.datagen.handler.BlockLootTableHandler
import com.rhseung.blueprint.datagen.handler.BlockLootTableHandler.Companion.dropSelf
import com.rhseung.blueprint.registration.Registries
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class ModBlockLootTableProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : AbstractBlockLootTableProvider(output, registriesFuture) {

    override fun register(blockLootTable: BlockLootTableHandler) {
        Registries.BLOCKS.forEach { blockLootTable += dropSelf(it) }
    }
}