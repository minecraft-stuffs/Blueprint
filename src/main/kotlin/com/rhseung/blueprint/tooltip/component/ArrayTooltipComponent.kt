package com.rhseung.blueprint.tooltip.component

import com.rhseung.blueprint.tooltip.data.ArrayTooltipData
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.VertexConsumerProvider
import org.joml.Matrix4f

class ArrayTooltipComponent(
    override val data: ArrayTooltipData
) : AbstractTooltipComponent(data) {

    override fun getHeight(): Int {
        return data.components.sumOf { it.height }
    }

    override fun getWidth(textRenderer: TextRenderer): Int {
        return data.components.maxOf { it.getWidth(textRenderer) }
    }

    override fun drawText(
        textRenderer: TextRenderer,
        x: Int,
        y: Int,
        matrix: Matrix4f,
        vertexConsumers: VertexConsumerProvider.Immediate
    ) {
        var y0 = y;
        for (component in data.components) {
            component.drawText(textRenderer, x, y0, matrix, vertexConsumers);
            y0 += component.height;
        }
    }

    override fun drawItems(
        textRenderer: TextRenderer,
        x: Int,
        y: Int,
        context: DrawContext
    ) {
        var y0 = y;
        for (component in data.components) {
            component.drawItems(textRenderer, x, y0, context);
            y0 += component.height;
        }
    }
}