package com.rhseung.blueprint.data

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator

abstract class AbstractModelProvider(
    val output: FabricDataOutput
) : FabricModelProvider(output) {

    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        val blockModel = BlockModelHandler(output.modId, blockStateModelGenerator)
        registerBlock(blockModel)
    }

    open fun registerBlock(blockModel: BlockModelHandler) {}

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        val itemModel = ItemModelHandler(output.modId, itemModelGenerator)
        registerItem(itemModel)
    }

    open fun registerItem(itemModel: ItemModelHandler) {
    }
}