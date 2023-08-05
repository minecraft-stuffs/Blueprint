package com.rhseung.blueprint.registration.key

import com.rhseung.blueprint.file.Loc
import com.rhseung.blueprint.registration.IBaseKey
import com.rhseung.blueprint.registration.Lang
import com.rhseung.blueprint.util.Languages
import com.rhseung.blueprint.util.Languages.LanguageTable
import com.rhseung.blueprint.util.Functional.ifNotNull
import com.rhseung.blueprint.util.Utils.langcase
import com.rhseung.blueprint.util.Utils.titlecase
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlag
import net.minecraft.util.Rarity

class BaseItem(
    /**
     * The ID of the item. This is used to register the item.
     */
    override val id: Loc,

    /**
     * The settings of the item.
     */
    val properties: Properties
) : Item(properties.build()), IBaseKey {

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
     * Registers the item to the [Registry].
     */
    override fun register() {
        Registry.register(Registries.ITEM, id.toIdentifier(), this)

        properties.itemGroup.ifNotNull {
            ItemGroupEvents.modifyEntriesEvent(it)
                .register(ItemGroupEvents.ModifyEntries { entries -> entries.add(this) })
        }
    }

    /**
     * Returns the string representation of the item.
     */
    override fun toString(): String {
        return "Item($id)"
    }

    /**
     * The settings of the item.
     * @property itemGroup
     * @property maxStackCount
     * @property rarity
     * @property fireproof
     * @property requiredFeatures
     * @property recipeRemainder
     */
    class Properties {
        /**
         * The item group that the item will be in. If null, the item will not be in any item group.
         */
        internal var itemGroup: Group? = null

        fun vanillaGroup(value: () -> Group) {
            this.itemGroup = value()
        }

        fun itemGroup(value: () -> BaseItemGroup) {
            this.itemGroup = value().registrykey
        }

        /**
         * The maximum stack count of the item. default is 64.
         */
        internal var maxStackCount: Int = 64

        fun maxStackCount(value: () -> Int) {
            this.maxStackCount = value()
        }

        /**
         * The rarity of the item. default is [Rarity.COMMON].
         */
        internal var rarity: Rarity = Rarity.COMMON

        fun rarity(value: () -> Rarity) {
            this.rarity = value()
        }

        /**
         * Whether the item is fireproof. default is false.
         */
        internal var fireproof: Boolean = false

        fun fireproof(value: () -> Boolean) {
            this.fireproof = value()
        }

        /**
         * The required features for the item. default is [FeatureFlags.VANILLA_FEATURES].
         */
        internal var requiredFeatures: Array<FeatureFlag> = arrayOf()

        @JvmName("requiredFeaturesArray")
        fun requiredFeatures(value: () -> Array<FeatureFlag>) {
            this.requiredFeatures = value()
        }

        @JvmName("requiredFeaturesCollection")
        fun requiredFeatures(value: () -> Collection<FeatureFlag>) {
            this.requiredFeatures = value().toTypedArray()
        }

        /**
         * The item that will be left over after crafting. default is null.
         *
         * e.g. if you craft a cake using milk bucket, the empty bucket will be left over.
         */
        internal var recipeRemainder: Item? = null

        fun recipeRemainder(value: () -> Item) {
            this.recipeRemainder = value()
        }

        /**
         * The language table of the item. The key is the language code, and the value is the translated string.
         */
        internal val langs = LanguageTable()

        fun lang(value: () -> Lang) {
            langs[value().language] = value().translation
        }

        /**
         * Builds the settings into an [Item.Settings] object.
         */
        fun build(): Settings {
            return Settings()
                .maxCount(maxStackCount)
                .rarity(rarity)
                .recipeRemainder(recipeRemainder)
                .requires(*requiredFeatures)
                .apply {
                    if (fireproof)
                        fireproof()
                }
        }
    }
}