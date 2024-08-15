package com.rhseung.blueprint.example

import com.rhseung.blueprint.tool.ToolType
import com.rhseung.blueprint.registration.IModInit
import com.rhseung.blueprint.registration.Translation
import com.rhseung.blueprint.registration.Registries.ITEMS
import com.rhseung.blueprint.registration.Registries.BLOCKS
import com.rhseung.blueprint.registration.Registries.ITEM_GROUPS
import com.rhseung.blueprint.util.Languages
import com.rhseung.blueprint.tool.Tier
import net.minecraft.block.Blocks
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ToolComponent
import net.minecraft.component.type.ToolComponent.Rule
import net.minecraft.item.ItemStack
import net.minecraft.sound.BlockSoundGroup

object ModInit : IModInit {
    @Translation(Languages.KO_KR, "청사진")
    val BLUEPRINT = ITEMS.register("blueprint")

    @Translation(Languages.KO_KR, "예제 그룹")
    val EXAMPLE_GROUP = ITEM_GROUPS.register("example_group") {
        icon { ItemStack(BLUEPRINT) }
    }

    @Translation(Languages.KO_KR, "예제 아이템")
    val EXAMPLE_ITEM = ITEMS.register("example_item") {
        itemGroup { EXAMPLE_GROUP }
        component(DataComponentTypes.TOOL) { ToolComponent(listOf(Rule.ofAlwaysDropping(listOf(Blocks.GLASS), 15.0F)), 1.0F, 2) }
    }

    @Translation(Languages.KO_KR, "예제 광석")
    @Translation(Languages.EN_US, "Example Ore!!!")
    val EXAMPLE_ORE = BLOCKS.register("example_ore") {
        requiredToolLevel { ToolType.PICKAXE(Tier.DIAMOND) }
        hardness { 3.0F }
        resistance { 3.0F }
        soundGroup { BlockSoundGroup.STONE }
        itemGroup { EXAMPLE_GROUP }
    }
}