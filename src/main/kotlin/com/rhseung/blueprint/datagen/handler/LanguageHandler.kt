package com.rhseung.blueprint.datagen.handler

import com.rhseung.blueprint.file.Loc
import com.rhseung.blueprint.registration.key.Group
import com.rhseung.blueprint.registration.key.BaseItemGroup
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.stat.StatType
import net.minecraft.util.Identifier
import java.nio.file.Path

class LanguageHandler(
    val modId: String,
    private val translationBuilder: FabricLanguageProvider.TranslationBuilder
) {
    @JvmName("plusAssignString")
    operator fun plusAssign(lang: Pair<String, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignItem")
    operator fun plusAssign(lang: Pair<Item, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignBlock")
    operator fun plusAssign(lang: Pair<Block, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignItemGroup")
    operator fun plusAssign(lang: Pair<BaseItemGroup, String>) {
        translationBuilder.add(lang.first.registrykey, lang.second)
    }

    @JvmName("plusAssignGroup")
    operator fun plusAssign(lang: Pair<Group, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignEntityType")
    operator fun plusAssign(lang: Pair<EntityType<*>, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignEnchantment")
    operator fun plusAssign(lang: Pair<RegistryKey<Enchantment>, String>) {
        translationBuilder.addEnchantment(lang.first, lang.second)
    }

    @JvmName("plusAssignEntityRegistryAttribute")
    operator fun plusAssign(lang: Pair<RegistryEntry<EntityAttribute>, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignEntityAttribute")
    operator fun plusAssign(lang: Pair<EntityAttribute, String>) {
        translationBuilder.add(lang.first.translationKey, lang.second)
    }


    @JvmName("plusAssignStatType")
    operator fun plusAssign(lang: Pair<StatType<*>, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignStatusEffect")
    operator fun plusAssign(lang: Pair<StatusEffect, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignIdentifier")
    operator fun plusAssign(lang: Pair<Identifier, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignLoc")
    operator fun plusAssign(lang: Pair<Loc, String>) {
        translationBuilder.add(lang.first.toIdentifier(), lang.second)
    }

    @JvmName("plusAssignTag")
    operator fun plusAssign(lang: Pair<TagKey<*>, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignPath")
    operator fun plusAssign(existingLanguageFile: Path) {
        translationBuilder.add(existingLanguageFile)
    }
}