package com.rhseung.blueprint.tooltip

import com.rhseung.blueprint.tooltip.component.AbstractTooltipComponent
import net.minecraft.item.tooltip.TooltipData

object TooltipComponentFactory {
    private val componentMap: MutableMap<Class<out TooltipData>, (TooltipData) -> AbstractTooltipComponent> = mutableMapOf();

    @JvmStatic
    fun <T: TooltipData> register(clazz: Class<out T>, creator: (T) -> AbstractTooltipComponent) {
        componentMap[clazz] = creator as (TooltipData) -> AbstractTooltipComponent;
    }

    @JvmStatic
    inline fun <reified T: TooltipData> register(noinline creator: (T) -> AbstractTooltipComponent) {
        register(T::class.java, creator);
    }

    @JvmStatic
    fun <T: TooltipData> create(data: T): AbstractTooltipComponent {
        return componentMap[data::class.java]?.invoke(data) ?: throw IllegalArgumentException("No creator found for ${data::class.java}");
    }
}