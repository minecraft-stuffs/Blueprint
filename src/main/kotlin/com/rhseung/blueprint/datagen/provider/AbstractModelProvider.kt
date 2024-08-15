package com.rhseung.blueprint.datagen.provider

import com.rhseung.blueprint.datagen.handler.BlockModelHandler
import com.rhseung.blueprint.datagen.handler.ItemModelHandler
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator

abstract class AbstractModelProvider(
    open val output: FabricDataOutput
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