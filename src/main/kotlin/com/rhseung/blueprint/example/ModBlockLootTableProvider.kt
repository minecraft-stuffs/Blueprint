package com.rhseung.blueprint.example

import com.rhseung.blueprint.data.AbstractBlockLootTableProvider
import com.rhseung.blueprint.data.BlockLootTableHandler
import com.rhseung.blueprint.data.BlockLootTableHandler.Companion.dropSelf
import com.rhseung.blueprint.registration.Registries
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

class ModBlockLootTableProvider(
    override val output: FabricDataOutput,
) : AbstractBlockLootTableProvider(output) {

    override fun register(blockLootTable: BlockLootTableHandler) {
        Registries.BLOCKS.forEach { blockLootTable += dropSelf(it) }
    }
}