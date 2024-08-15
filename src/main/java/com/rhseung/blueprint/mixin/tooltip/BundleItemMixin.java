package com.rhseung.blueprint.mixin.tooltip;

import com.rhseung.blueprint.tooltip.data.BundleTooltipData;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Optional;

@Mixin(BundleItem.class)
public class BundleItemMixin {
//    public Optional<TooltipData> getTooltipData(ItemStack stack) {
//        DefaultedList<ItemStack> defaultedList = DefaultedList.of();
//        Stream var10000 = getBundledStacks(stack);
//        Objects.requireNonNull(defaultedList);
//        var10000.forEach(defaultedList::add);
//        return Optional.of(new BundleTooltipData(defaultedList, getBundleOccupancy(stack)));
//    }

    /**
     * @author rhseung
     * @reason Overwrite the method to return custom tooltip data
     */
    @Overwrite
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return Optional.of(new BundleTooltipData(stack));
    }
}
