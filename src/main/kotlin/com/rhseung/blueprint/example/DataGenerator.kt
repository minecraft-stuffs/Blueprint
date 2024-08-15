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
		val pack = fabricDataGenerator.createPack();

		pack.addProvider { output, registriesFuture -> ModItemTagProvider(output, registriesFuture) }
		pack.addProvider { output, registriesFuture -> ModBlockTagProvider(output, registriesFuture) }
		pack.addProvider { output, registriesFuture -> ModRecipeProvider(output, registriesFuture) }
		pack.addProvider { output, registriesFuture -> ModBlockLootTableProvider(output, registriesFuture) }
		pack.addProvider { output, _ -> ModModelProvider(output) }
		Languages.forEach { pack.addProvider { output, registriesFuture -> ModLanguageProvider(output, registriesFuture, it) } }
	}
}