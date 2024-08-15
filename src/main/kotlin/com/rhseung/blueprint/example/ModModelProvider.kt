package com.rhseung.blueprint.example

import com.rhseung.blueprint.datagen.provider.AbstractModelProvider
import com.rhseung.blueprint.datagen.handler.BlockModelHandler
import com.rhseung.blueprint.datagen.handler.ItemModelHandler
import com.rhseung.blueprint.datagen.handler.ItemModelHandler.Companion.simple
import com.rhseung.blueprint.registration.Registries
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

class ModModelProvider(
    output: FabricDataOutput
) : AbstractModelProvider(output) {

    override fun registerItem(itemModel: ItemModelHandler) {
        Registries.ITEMS.forEach { itemModel += simple(it) }
    }

    override fun registerBlock(blockModel: BlockModelHandler) {
        Registries.BLOCKS.forEach { blockModel.simple(it) }
    }
}