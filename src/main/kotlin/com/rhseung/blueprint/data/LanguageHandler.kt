package com.rhseung.blueprint.data

import com.rhseung.blueprint.registration.key.Group
import com.rhseung.blueprint.registration.key.BaseItemGroup
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.stat.StatType
import net.minecraft.util.Identifier

class LanguageHandler(
    val modId: String,
    private val translationBuilder: FabricLanguageProvider.TranslationBuilder
) {
    @JvmName("plusAssignItem")
    operator fun plusAssign(lang: Pair<Item, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignBlock")
    operator fun plusAssign(lang: Pair<Block, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignIdentifier")
    operator fun plusAssign(lang: Pair<Identifier, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignString")
    operator fun plusAssign(lang: Pair<String, String>) {
        translationBuilder.add(Identifier(modId, lang.first), lang.second)
    }

    @JvmName("plusAssignEnchantment")
    operator fun plusAssign(lang: Pair<Enchantment, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignEntityType")
    operator fun plusAssign(lang: Pair<EntityType<*>, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignEntityAttribute")
    operator fun plusAssign(lang: Pair<EntityAttribute, String>) {
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

    @JvmName("plusAssignStatusEffect")
    operator fun plusAssign(lang: Pair<StatusEffect, String>) {
        translationBuilder.add(lang.first, lang.second)
    }

    @JvmName("plusAssignStatType")
    operator fun plusAssign(lang: Pair<StatType<*>, String>) {
        translationBuilder.add(lang.first, lang.second)
    }
    
    companion object {
        infix fun Item.aka(name: String) = this to name
        infix fun Block.aka(name: String) = this to name
        infix fun Identifier.aka(name: String) = this to name
        infix fun String.aka(name: String) = this to name
        infix fun Enchantment.aka(name: String) = this to name
        infix fun EntityType<*>.aka(name: String) = this to name
        infix fun EntityAttribute.aka(name: String) = this to name
        infix fun BaseItemGroup.aka(name: String) = this to name
        infix fun Group.aka(name: String) = this to name
        infix fun StatusEffect.aka(name: String) = this to name
        infix fun StatType<*>.aka(name: String) = this to name
    }
}