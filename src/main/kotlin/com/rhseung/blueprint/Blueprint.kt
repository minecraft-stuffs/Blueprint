package com.rhseung.blueprint

import com.rhseung.blueprint.example.ModItems
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Blueprint : ModInitializer {
	val modid = "blueprint"
    val logger = LoggerFactory.getLogger(modid)

	override fun onInitialize() {
		ModItems.register()
	}
}