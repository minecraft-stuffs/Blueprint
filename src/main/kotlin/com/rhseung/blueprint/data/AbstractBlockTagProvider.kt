package com.rhseung.blueprint.data

import com.rhseung.blueprint.api.ToolType
import com.rhseung.blueprint.data.BlockTagHandler.Companion.mineable
import com.rhseung.blueprint.data.BlockTagHandler.Companion.needs_diamond_tool
import com.rhseung.blueprint.data.BlockTagHandler.Companion.needs_iron_tool
import com.rhseung.blueprint.data.BlockTagHandler.Companion.needs_stone_tool
import com.rhseung.blueprint.data.BlockTagHandler.Companion.plusAssign
import com.rhseung.blueprint.file.Loc.Companion.fabric
import com.rhseung.blueprint.file.Loc.Companion.minecraft
import com.rhseung.blueprint.file.Loc.Companion.rangeTo
import com.rhseung.blueprint.file.Path
import com.rhseung.blueprint.registration.Registries
import com.rhseung.blueprint.util.Functional.ifFalse
import com.rhseung.blueprint.util.Functional.ifTrue
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

abstract class AbstractBlockTagProvider(
    open val output: FabricDataOutput,
    open val registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider<Block>(output, RegistryKeys.BLOCK, registriesFuture) {

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        val handler = BlockTagHandler(output, registriesFuture)

        Registries.BLOCKS.forEach { block ->
            val toolLevel = block.properties.requiredToolLevel

            // tool level (정확히는 mining level만) 구현
            // todo: 모든 tool level 구현
            when (toolLevel.level) {
                1 -> handler[minecraft..needs_stone_tool] += block
                2 -> handler[minecraft..needs_iron_tool] += block
                3 -> handler[minecraft..needs_diamond_tool] += block
                else -> (toolLevel.level > 3).ifTrue {
                    val needs_tool_level_n = Path("needs_tool_level_${toolLevel.level}")
                    handler[fabric..needs_tool_level_n] += block
                }
            }

            // tool type 구현
            // todo: namespace를 lib로 통일하기 + mixin으로 구현하기
            if (toolLevel.type.path != null) {
                if (toolLevel.type == ToolType.SWORD || toolLevel.type == ToolType.SHEAR) {
                    handler[fabric..mineable/toolLevel.type.path] += block
                } else {
                    handler[minecraft..mineable/toolLevel.type.path] += block
                }
            }

            // todo: tool tag 구현하기
            //  - 굳이? 긴 함. 어차피 effective block은 tool class 자체에서 구현될 듯 한데.
        }
    }

    open fun register(handler: BlockTagHandler) {
        // todo: handler 구현
    }
}