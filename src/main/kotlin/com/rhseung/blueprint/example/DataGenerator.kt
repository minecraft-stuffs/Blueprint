package com.rhseung.blueprint.example

import com.rhseung.blueprint.example.ModBlockTagProvider
import com.rhseung.blueprint.example.ModLanguageProvider
import com.rhseung.blueprint.example.ModModelProvider
import com.rhseung.blueprint.util.Languages
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

object DataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		val pack = fabricDataGenerator.createPack()

		pack.addProvider { output: FabricDataOutput, registriesFuture -> ModBlockTagProvider(output, registriesFuture) }
		pack.addProvider { output: FabricDataOutput -> ModModelProvider(output) }
		pack.addProvider { output: FabricDataOutput -> ModBlockLootTableProvider(output) }
		pack.addProvider { output: FabricDataOutput -> ModLanguageProvider(output, Languages.EN_US) }
	}
}