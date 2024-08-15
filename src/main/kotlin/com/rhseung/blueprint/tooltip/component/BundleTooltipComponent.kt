package com.rhseung.blueprint.tooltip.component

import com.rhseung.blueprint.tooltip.TooltipComponentFactory
import com.rhseung.blueprint.tooltip.data.BundleTooltipData
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.tooltip.BundleTooltipComponent
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.util.Identifier
import org.joml.Matrix4f
import kotlin.math.ceil
import kotlin.math.sqrt

class BundleTooltipComponent(
    override val data: BundleTooltipData
) : AbstractTooltipComponent(data) {
    private val texture = Identifier.ofVanilla("container/bundle/background");
    private var inventory = data.inventory;
    private var occupancy = data.bundleOccupancy;

    override fun getHeight(): Int {
        return getRows() * HEIGHT_PER_ROW + 2 + 4
    }

    override fun getWidth(textRenderer: TextRenderer): Int {
        return getColumns() * WIDTH_PER_COLUMN + 2
    }

    override fun drawText(
        textRenderer: TextRenderer,
        x: Int,
        y: Int,
        matrix: Matrix4f,
        vertexConsumers: VertexConsumerProvider.Immediate
    ) {}

    override fun drawItems(
        textRenderer: TextRenderer,
        x: Int,
        y: Int,
        context: DrawContext
    ) {
        val i = this.getColumns()
        val j = this.getRows()
        val bl = this.occupancy >= 64
        var k = 0

        for (l in 0 until j) {
            for (m in 0 until i) {
                val n: Int = x + m * 18 + 1
                val o: Int = y + l * 20 + 1
                this.drawSlot(n, o, k++, bl, context, textRenderer)
            }
        }

        this.drawOutline(x, y, i, j, context)
    }

    private fun drawSlot(
        x: Int,
        y: Int,
        index: Int,
        shouldBlock: Boolean,
        context: DrawContext,
        textRenderer: TextRenderer
    ) {
        if (index >= inventory.size) {
            this.draw(
                context,
                x,
                y,
                if (shouldBlock) Sprite.BLOCKED_SLOT else Sprite.SLOT
            )
        }
        else {
            val itemStack = inventory[index]
            this.draw(context, x, y, Sprite.SLOT)
            context.drawItem(itemStack, x + 1, y + 1, index)
            context.drawItemInSlot(textRenderer, itemStack, x + 1, y + 1)
            if (index == 0) {
                HandledScreen.drawSlotHighlight(context, x + 1, y + 1, 0)
            }
        }
    }

    private fun drawOutline(
        x: Int,
        y: Int,
        columns: Int,
        rows: Int,
        context: DrawContext
    ) {
        this.draw(context, x, y, Sprite.BORDER_CORNER_TOP);
        this.draw(context, x + columns * 18 + 1, y, Sprite.BORDER_CORNER_TOP);

        for (i in 0..<columns) {
            this.draw(context, x + 1 + i * 18, y, Sprite.BORDER_HORIZONTAL_TOP);
            this.draw(context, x + 1 + i * 18, y + rows * 20, Sprite.BORDER_HORIZONTAL_BOTTOM);
        }

        for (i in 0..<rows) {
            this.draw(context, x, y + 1 + i * 20, Sprite.BORDER_VERTICAL);
            this.draw(context, x + columns * 18 + 1, y + 1 + i * 20, Sprite.BORDER_VERTICAL);
        }

        this.draw(context, x, y + rows * 20, Sprite.BORDER_CORNER_BOTTOM);
        this.draw(context, x + columns * 18 + 1, y + rows * 20, Sprite.BORDER_CORNER_BOTTOM);
    }

    private fun draw(context: DrawContext, x: Int, y: Int, sprite: Sprite) {
        context.drawTexture(
            texture,
            x,
            y,
            0,
            sprite.u.toFloat(),
            sprite.v.toFloat(),
            sprite.width,
            sprite.height,
            128,
            128
        );
    }

    private fun getColumns(): Int {
        return 2.coerceAtLeast(ceil(sqrt(inventory!!.size.toDouble() + 1.0)).toInt())
    }

    private fun getRows(): Int {
        return ceil((inventory!!.size.toDouble() + 1.0) / getColumns().toDouble()).toInt()
    }

    @Environment(EnvType.CLIENT)
    private enum class Sprite(
        val u: Int,
        val v: Int,
        val width: Int,
        val height: Int
    ) {
        SLOT(0, 0, WIDTH_PER_COLUMN, HEIGHT_PER_ROW),
        BLOCKED_SLOT(0, 40, WIDTH_PER_COLUMN, HEIGHT_PER_ROW),
        BORDER_VERTICAL(0, WIDTH_PER_COLUMN, 1, HEIGHT_PER_ROW),
        BORDER_HORIZONTAL_TOP(0, HEIGHT_PER_ROW, WIDTH_PER_COLUMN, 1),
        BORDER_HORIZONTAL_BOTTOM(0, 60, WIDTH_PER_COLUMN, 1),
        BORDER_CORNER_TOP(0, HEIGHT_PER_ROW, 1, 1),
        BORDER_CORNER_BOTTOM(0, 60, 1, 1);
    }

    companion object {
        init {
            TooltipComponentFactory.register<BundleTooltipData>(::BundleTooltipComponent);
        }

        const val WIDTH_PER_COLUMN = 18;
        const val HEIGHT_PER_ROW = 20;
        const val TEXTURE_WIDTH = 128;
        const val TEXTURE_HEIGHT = 128;
    }
}