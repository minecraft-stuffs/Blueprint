package com.rhseung.blueprint.tooltip

import com.rhseung.blueprint.tooltip.data.AbstractTooltipData
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

object TooltipDataFactory {
    private val dataMap: MutableMap<Class<out Item>, (ItemStack) -> AbstractTooltipData> = mutableMapOf();

    @JvmStatic
    fun <T: Item> register(clazz: Class<out T>, creator: (ItemStack) -> AbstractTooltipData) {
        dataMap[clazz] = creator;
    }

    @JvmStatic
    inline fun <reified T: Item> register(noinline creator: (ItemStack) -> AbstractTooltipData) {
        register(T::class.java, creator);
    }

    @JvmStatic
    fun create(stack: ItemStack): AbstractTooltipData {
        return dataMap[stack.item::class.java]?.invoke(stack) ?: throw IllegalArgumentException("No creator found for ${stack.item::class.java}");
    }
}