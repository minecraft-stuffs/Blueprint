package com.rhseung.blueprint.tooltip.data

import com.rhseung.blueprint.tooltip.TooltipDataFactory
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipData
import kotlin.reflect.full.primaryConstructor

abstract class AbstractTooltipData(
    open val stack: ItemStack
) : TooltipData {

    init {
        registerTooltipData()
    }

    private fun registerTooltipData() {
        TooltipDataFactory.register(stack.item::class.java) { stack -> this::class.primaryConstructor!!.call(stack) }
    }
}