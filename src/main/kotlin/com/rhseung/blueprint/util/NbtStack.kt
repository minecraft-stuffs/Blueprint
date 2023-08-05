package com.rhseung.blueprint.api

import com.rhseung.blueprint.util.Nbt
import net.minecraft.item.ItemStack

class NbtStack(
	val stack: ItemStack,
	val nbt: Nbt.NbtCompound
) {
	init {
		// todo: stack.nbt에 nbt handler 적용
	}
	
	companion object {
	}
	
	// todo: implement all itemStack function
}