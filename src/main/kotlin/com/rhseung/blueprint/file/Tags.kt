package com.rhseung.blueprint.file

import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object Tags {
    fun tag(id: String) = TagKey.of<Block>(RegistryKeys.BLOCK, Identifier.of(id));

    val NEEDS_HAND = tag("needs_hand");
    val NEEDS_WOOD_TOOL = tag("needs_wood_tool");

    val SWORD_MINEABLE = tag("mineable/sword");
    val SHEAR_MINEABLE = tag("mineable/shear");
}