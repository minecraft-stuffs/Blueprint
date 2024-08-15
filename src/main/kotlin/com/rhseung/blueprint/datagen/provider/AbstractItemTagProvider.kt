package com.rhseung.blueprint.datagen.provider

import com.rhseung.blueprint.datagen.handler.ItemTagHandler
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

abstract class AbstractItemTagProvider(
    open val output: FabricDataOutput,
    open val registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider<Item>(output, RegistryKeys.ITEM, registriesFuture) {

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        val handler = ItemTagHandler()
        register(handler);

        handler.itemMap.forEach { (key, items) ->
            val tag = this.getOrCreateTagBuilder(key)
            items.forEach { tag.add(it) }
        }

        handler.tagMap.forEach { (key, tags) ->
            val tag = this.getOrCreateTagBuilder(key)
            tags.forEach { tag.forceAddTag(it) }
        }
    }

    open fun register(handler: ItemTagHandler) {}
}