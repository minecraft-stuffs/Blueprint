package com.rhseung.blueprint.datagen.provider

import com.rhseung.blueprint.datagen.handler.LanguageHandler
import com.rhseung.blueprint.registration.IBaseKey
import com.rhseung.blueprint.registration.Registries
import com.rhseung.blueprint.util.Languages
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import net.minecraft.stat.StatType
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

abstract class AbstractLanguageProvider(
    open val output: FabricDataOutput,
    open val registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>,
    open val language: Languages
) : FabricLanguageProvider(output, language.code, registriesFuture) {

    override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, translationBuilder: TranslationBuilder) {
        val lang = LanguageHandler(output.modId, translationBuilder)
        register(lang)
    }
    
    /** `languageCode`에 해당하는 언어로 번역을 등록합니다.
     * ```kotlin
     * lang += [번역할 대상] to [번역할 대상의 번역 이름]
     * lang += [Collection<번역할 대상의 타입>] to { [번역할 대상] -> [번역할 대상의 번역 이름] }
     * ```
     * 번역할 대상에는 [Item], [Block], [Identifier], [String], [Enchantment], [EntityType], [EntityAttribute], [BasicItemGroup], [RegistryKey], [StatusEffect], [StatType]가 가능합니다.
     *
     * @param lang: LanguageHandler
     * @see LanguageHandler
     */
    open fun register(lang: LanguageHandler) {
        fun getLangSafe(it: IBaseKey)
            = it.langs[language] ?: it.langs[Languages.EN_US]!!

        Registries.ITEMS.forEach { lang += it to getLangSafe(it) }
        Registries.BLOCKS.forEach { lang += it to getLangSafe(it) }
        Registries.ITEM_GROUPS.forEach { lang += it to getLangSafe(it) }
    }
}