package com.rhseung.blueprint.tooltip.data

import com.rhseung.blueprint.tooltip.TooltipComponentFactory
import com.rhseung.blueprint.tooltip.component.AbstractTooltipComponent
import net.minecraft.item.ItemStack

class ArrayTooltipData(stack: ItemStack) : AbstractTooltipData(stack) {
    var components: MutableList<AbstractTooltipComponent> = mutableListOf();

    fun add(data: AbstractTooltipData) {
        components.add(TooltipComponentFactory.create(data));
    }
}