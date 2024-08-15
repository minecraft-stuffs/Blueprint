package com.rhseung.blueprint

import com.rhseung.blueprint.example.ModInit
import com.rhseung.blueprint.render.Icon
import net.fabricmc.api.ModInitializer
import net.minecraft.client.MinecraftClient
import org.slf4j.LoggerFactory

object Blueprint : ModInitializer {
	const val modid = "blueprint";
    val logger = LoggerFactory.getLogger(modid);

	// TODO: Nbt
	// TODO: Data Component Test
	// TODO; add codec data provider
	// TODO: Adaptive Icon Tooltip (쿼크 그거)
	// TODO: Tint (Client)

	override fun onInitialize() {
		ModInit.register();
	}
}