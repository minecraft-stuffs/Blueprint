package com.rhseung.blueprint.tool

import com.rhseung.blueprint.file.Path
import com.rhseung.blueprint.file.Path.Companion.axe
import com.rhseung.blueprint.file.Path.Companion.hoe
import com.rhseung.blueprint.file.Path.Companion.pickaxe
import com.rhseung.blueprint.file.Path.Companion.shear
import com.rhseung.blueprint.file.Path.Companion.shovel
import com.rhseung.blueprint.file.Path.Companion.sword
import com.rhseung.blueprint.file.Tags
import net.minecraft.block.Block
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey

// TODO: ToolType이 아니라 ToolItem을 상속받는 클래스의 팩토리로 해야할지 고민해보기

data class ToolType(
    val path: Path? = null
) {
    operator fun invoke(tier: Tier): ToolLevel {
        if (this == NONE && tier > Tier.HAND)
            throw IllegalArgumentException("ToolType.NONE cannot have a level")

        return ToolLevel(tier, this)
    }

    fun toTag(): TagKey<Block> = when (path) {
        axe -> BlockTags.AXE_MINEABLE
        hoe -> BlockTags.HOE_MINEABLE
        pickaxe -> BlockTags.PICKAXE_MINEABLE
        shovel -> BlockTags.SHOVEL_MINEABLE
        sword -> Tags.SWORD_MINEABLE
        shear -> Tags.SHEAR_MINEABLE
        null -> throw IllegalArgumentException("ToolType.NONE cannot be converted to a tag")
        else -> Tags.tag("mineable/${path.current}")
    }

    companion object {
        val NONE = ToolType()
        val AXE = ToolType(axe)
        val HOE = ToolType(hoe)
        val PICKAXE = ToolType(pickaxe)
        val SHOVEL = ToolType(shovel)
        val SWORD = ToolType(sword)
        val SHEAR = ToolType(shear)
    }
}