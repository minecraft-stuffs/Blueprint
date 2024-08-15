package com.rhseung.blueprint.tooltip.component

import com.rhseung.blueprint.tooltip.TooltipComponentFactory
import com.rhseung.blueprint.tooltip.data.AbstractTooltipData
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.render.VertexConsumerProvider.Immediate
import net.minecraft.item.ItemStack
import org.joml.Matrix4f
import kotlin.reflect.full.primaryConstructor

/**
 * ## Tooltip이 렌더링되는 과정:
 *
 * [HandledScreen.drawMouseoverTooltip] 함수에서 <br>
 * ```
 * context.drawTooltip(..., this.getTooltipFromItem(itemStack), itemStack.getTooltipData(), ...);
 *                               ^^^^^^^^^^^^^^^^^^                       ^^^^^^^^^^^^^^
 *                               List<Text>                               TooltipData
 * ```
 *
 * - [HandledScreen.getTooltipFromItem] -> [ItemStack.getTooltip] 을 사용해 모든 텍스트형 툴팁을 생성, `List<Text>`로 반환.
 * - [DrawContext.drawTooltip]: `List<Text>` -> `List<OrderedTextTooltipComponent>`, `TooltipData` -> `TooltipComponent`로 변환해 합쳐 만든 `List<TooltipComponent>` 에서 [TooltipComponent.drawText]를 전부 호출한 뒤, [TooltipComponent.drawItems]를 호출.
 */
abstract class AbstractTooltipComponent(
    open val data: AbstractTooltipData
) : TooltipComponent {

    init {
        registerTooltipComponent();
    }

    private fun registerTooltipComponent() {
        TooltipComponentFactory.register(data::class.java) { data -> this::class.primaryConstructor!!.call(data) }
    }

    fun getScreen(): Screen? {
        return MinecraftClient.getInstance().currentScreen;
    }

    abstract override fun getHeight(): Int

    abstract override fun getWidth(textRenderer: TextRenderer): Int

    abstract override fun drawText(
        textRenderer: TextRenderer,
        x: Int,
        y: Int,
        matrix: Matrix4f,
        vertexConsumers: Immediate
    )

    abstract override fun drawItems(
        textRenderer: TextRenderer,
        x: Int,
        y: Int,
        context: DrawContext
    )
}