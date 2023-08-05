package com.rhseung.blueprint.registration.key

import com.rhseung.blueprint.file.Loc
import com.rhseung.blueprint.registration.IBaseKey
import com.rhseung.blueprint.registration.Lang
import com.rhseung.blueprint.util.Functional.ifNotNull
import com.rhseung.blueprint.util.Languages
import com.rhseung.blueprint.util.Languages.LanguageTable
import com.rhseung.blueprint.util.Utils.langcase
import com.rhseung.blueprint.util.Utils.titlecase
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text

typealias Group = RegistryKey<ItemGroup>

class BaseItemGroup(
    /**
     * The ID of the item. This is used to register the item.
     */
    override val id: Loc,

    /**
     * The settings of the item.
     */
    val properties: Properties
) : IBaseKey {

    /**
     * The language table of the item. The key is the language code, and the value is the translated string.
     */
    override val langs = LanguageTable(Languages.EN_US to id.path.toString().langcase())

    init {
        properties.langs.forEach { lang, name ->
            name.ifNotNull { langs[lang] = it }
        }
    }

    /**
     * The item group.
     */
    val group = properties.build(id)

    /**
     * The registry key of the item group.
     */
    val registrykey = RegistryKey.of(RegistryKeys.ITEM_GROUP, id.toIdentifier())

    /**
     * Registers the item group to the [Registry].
     */
    override fun register() {
        Registry.register(Registries.ITEM_GROUP, registrykey, group)
    }

    /**
     * Returns the string representation of the item group.
     */
    override fun toString(): String {
        return "ItemGroup($id)"
    }

    class Properties {
        /**
         * The icon of the item group.
         */
        internal var icon: ItemStack = ItemStack.EMPTY

        fun icon(value: () -> ItemStack) {
            this.icon = value()
        }

        /**
         * Sets the show scrollbar of the item group.
         */
        internal var scrollbar = true

        fun scrollbar(value: () -> Boolean) {
            this.scrollbar = value()
        }

        /**
         * Sets the special of the item group.
         * idk: what is special?
         */
        internal var special = false

        fun special(value: () -> Boolean) {
            this.special = value()
        }

        /**
         * Sets the show rendered name of the item group.
         */
        internal var showRenderName = true

        fun showRenderName(value: () -> Boolean) {
            this.showRenderName = value()
        }

        /**
         * The language table of the item. The key is the language code, and the value is the translated string.
         */
        internal val langs = LanguageTable()

        fun lang(value: () -> Lang) {
            langs[value().language] = value().translation
        }

        fun build(id: Loc): ItemGroup {
            return FabricItemGroup.builder()
                .icon { icon }
                .displayName(Text.translatable("${id.namespace}.${id.path}"))
                .apply {
                    if (!scrollbar)
                        noScrollbar()
                    if (special)
                        special()
                    if (!showRenderName)
                        noRenderedName()
                }
                .build()
        }
    }
}