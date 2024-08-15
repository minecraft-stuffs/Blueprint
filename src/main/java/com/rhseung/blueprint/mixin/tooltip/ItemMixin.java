package com.rhseung.blueprint.mixin.tooltip;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.rhseung.blueprint.tooltip.TooltipDataFactory;
import com.rhseung.blueprint.tooltip.data.AbstractTooltipData;
import com.rhseung.blueprint.tooltip.data.ArrayTooltipData;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(Item.class)
public class ItemMixin {
    @ModifyReturnValue(
            method = "getTooltipData",
            at = @At("RETURN")
    )
    public Optional<TooltipData> getTooltipData_mixin(Optional<TooltipData> original, ItemStack stack) {
        var arrayTooltipData = new ArrayTooltipData(stack);

        original.ifPresent(tooltipData -> arrayTooltipData.add((AbstractTooltipData) tooltipData));
        arrayTooltipData.add(TooltipDataFactory.create(stack));

        return Optional.of(arrayTooltipData);
    }
}
