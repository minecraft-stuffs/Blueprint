package com.rhseung.blueprint.tool

import com.rhseung.blueprint.file.Tags
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

data class Tier(
    val level: Int
) {
    fun toInt() = level
    fun toTag(): TagKey<Block> = when (level) {
        -1 -> Tags.NEEDS_HAND
        0 -> Tags.NEEDS_WOOD_TOOL
        1 -> BlockTags.NEEDS_STONE_TOOL
        2 -> BlockTags.NEEDS_IRON_TOOL
        3 -> BlockTags.NEEDS_DIAMOND_TOOL
        in 4..Int.MAX_VALUE -> Tags.tag("needs_tool_level_$level")
        else -> throw IllegalArgumentException("unknown tool level")
    }

    operator fun compareTo(other: Tier) = level.compareTo(other.level)
    operator fun rangeTo(other: Tier) = (level..other.level).map { Tier(it) }

    companion object {
        val HAND = Tier(-1)
        val WOOD = Tier(0)
        val STONE = Tier(1)
        val IRON = Tier(2)
        val DIAMOND = Tier(3)
        val NETHERITE = Tier(4)
        val MAX = Tier(Int.MAX_VALUE)
    }
}