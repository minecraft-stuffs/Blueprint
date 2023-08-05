package com.rhseung.blueprint.data

import com.rhseung.blueprint.api.ToolType.Companion.axe
import com.rhseung.blueprint.api.ToolType.Companion.hoe
import com.rhseung.blueprint.api.ToolType.Companion.pickaxe
import com.rhseung.blueprint.api.ToolType.Companion.shovel
import com.rhseung.blueprint.api.ToolType.Companion.sword
import com.rhseung.blueprint.file.Loc
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import com.rhseung.blueprint.file.Path
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class BlockTagHandler(
    val output: FabricDataOutput,
    val registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider<Block>(output, RegistryKeys.BLOCK, registriesFuture) {

    companion object {
        operator fun FabricTagProvider<Block>.FabricTagBuilder.plusAssign(block: Block) {
            this.add(block)
        }

        operator fun FabricTagProvider<Block>.FabricTagBuilder.plusAssign(tag: TagKey<Block>) {
            this.forceAddTag(tag)
        }

        val mineable = Path("mineable")
        val needs_stone_tool = Path("needs_stone_tool")
        val needs_iron_tool = Path("needs_iron_tool")
        val needs_diamond_tool = Path("needs_diamond_tool")
    }

    operator fun get(id: Loc): FabricTagBuilder {
        return this.getOrCreateTagBuilder(TagKey.of(RegistryKeys.BLOCK, id.toIdentifier()))
    }

    override fun configure(arg: RegistryWrapper.WrapperLookup) {}
}