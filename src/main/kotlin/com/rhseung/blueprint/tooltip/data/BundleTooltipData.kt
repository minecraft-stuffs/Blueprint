package com.rhseung.blueprint.tooltip.data

import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.collection.DefaultedList
import java.util.*
import java.util.stream.Stream

/**
 * [net.minecraft.item.BundleItem.getTooltipData]
 */
class BundleTooltipData(
    stack: ItemStack
) : AbstractTooltipData(stack) {
    val inventory: DefaultedList<ItemStack>;
    val bundleOccupancy: Int;

    init {
        val defaultedList: DefaultedList<ItemStack> = DefaultedList.of();
        val stream = getBundledStacks(stack);
        Objects.requireNonNull(defaultedList);
        stream.forEach(defaultedList::add);

        this.inventory = defaultedList;
        this.bundleOccupancy = getBundleOccupancy(stack);
    }

    private fun getBundledStacks(stack: ItemStack): Stream<ItemStack> {
        val nbtCompound = stack.nbt;

        return if (nbtCompound == null) {
            Stream.empty();
        } else {
            val nbtList = nbtCompound.getList("Items", 10);
            Objects.requireNonNull(NbtCompound::class.java);

            nbtList.stream()
                .map { NbtCompound::class.java.cast(it) }
                .map { ItemStack.fromNbt(it) };
        }
    }

    private fun getItemOccupancy(stack: ItemStack): Int {
        if (stack.isOf(Items.BUNDLE)) {
            return 4 + getBundleOccupancy(stack)
        } else {
            if ((stack.isOf(Items.BEEHIVE) || stack.isOf(Items.BEE_NEST)) && stack.hasNbt()) {
                val nbtCompound = BlockItem.getBlockEntityNbt(stack)
                if (nbtCompound != null && !nbtCompound.getList("Bees", 10).isEmpty()) {
                    return 64
                }
            }

            return 64 / stack.maxCount
        }
    }

    private fun getBundleOccupancy(stack: ItemStack): Int {
        return getBundledStacks(stack).mapToInt { getItemOccupancy(it) * it.count }.sum()
    }
}