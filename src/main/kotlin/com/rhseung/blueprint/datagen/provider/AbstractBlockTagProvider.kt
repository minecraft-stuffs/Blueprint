package com.rhseung.blueprint.datagen.provider

import com.rhseung.blueprint.tool.ToolType
import com.rhseung.blueprint.datagen.handler.BlockTagHandler
import com.rhseung.blueprint.file.Loc.Companion.fabric
import com.rhseung.blueprint.file.Loc.Companion.rangeTo
import com.rhseung.blueprint.registration.Registries
import com.rhseung.blueprint.tool.Tier
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import java.util.concurrent.CompletableFuture

abstract class AbstractBlockTagProvider(
    open val output: FabricDataOutput,
    open val registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider<Block>(output, RegistryKeys.BLOCK, registriesFuture) {

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        val handler = BlockTagHandler()
        register(handler)

        handler.blockMap.forEach { (key, blocks) ->
            val tag = this.getOrCreateTagBuilder(key)
            blocks.forEach { tag.add(it) }
        }

        handler.tagMap.forEach { (key, tags) ->
            val tag = this.getOrCreateTagBuilder(key)
            tags.forEach { tag.forceAddTag(it) }
        }
    }

    open fun register(handler: BlockTagHandler) {
        Registries.BLOCKS.forEach { block ->
            val toolLevel = block.properties.requiredToolLevel

            handler[toolLevel.level.toTag()] += block;
            if (toolLevel.hasType())
                handler[toolLevel.type.toTag()] += block;

            // todo: tool tag 구현하기 [https://fabricmc.net/wiki/tutorial:mining_levels]
        }
    }
}