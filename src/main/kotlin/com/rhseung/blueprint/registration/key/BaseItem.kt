package com.rhseung.blueprint.registration.key

import com.google.common.collect.ImmutableList
import com.mojang.serialization.Codec
import com.rhseung.blueprint.file.Loc
import com.rhseung.blueprint.registration.IBaseKey
import com.rhseung.blueprint.registration.Translation
import com.rhseung.blueprint.util.Functional.ifNotNull
import com.rhseung.blueprint.util.Languages
import com.rhseung.blueprint.util.Languages.LanguageTable
import com.rhseung.blueprint.util.Utils.langcase
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.jukebox.JukeboxSong
import net.minecraft.component.ComponentMap
import net.minecraft.component.ComponentType
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.resource.featuretoggle.FeatureFlag
import net.minecraft.util.Rarity
import net.minecraft.component.type.FoodComponent as FoodComponent1

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
         * The jukebox song that the item can play. default is null.
         */
        internal var jukeboxPlayable: RegistryKey<JukeboxSong>? = null

        fun jukeboxPlayable(value: () -> RegistryKey<JukeboxSong>) {
            this.jukeboxPlayable = value()
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

        fun lang(value: () -> Translation) {
            langs[value().language] = value().translation
        }

        /**
         * Adds a component to the item.
         */
        internal val components: ComponentMap.Builder = ComponentMap.builder().addAll(DataComponentTypes.DEFAULT_ITEM_COMPONENTS);

        class ComponentBuilder<T : Any> {
            private var codec: Codec<T>? = null;
            private var packetCodec: PacketCodec<in RegistryByteBuf, T>? = null;
            private var cache: Boolean = false;
            
            companion object {
                fun <T : Any> builder(value: Any, block: ComponentBuilder<T>.() -> Unit): ComponentType<T> {
                    return ComponentBuilder<T>().apply(block).build();
                }
            }

            fun codec(value: () -> Codec<T>) {
                 this.codec = value()
            }

            fun packetCodec(value: () -> PacketCodec<in RegistryByteBuf, T>) {
                this.packetCodec = value()
            }

            fun cache(value: () -> Boolean) {
                this.cache = value()
            }

            fun build(): ComponentType<T> {
                return ComponentType.Builder<T>()
                    .codec(codec)
                    .packetCodec(packetCodec)
                    .apply {
                        if (cache)
                            cache()
                    }
                    .build();
            }
        }

        private fun <T : Any> component(component: ComponentType<T>, value: T) {
            components.add(component, value);
        }

        fun <T : Any> componentBuilder(value: T, block: ComponentBuilder<T>.() -> Unit) {
            val component = ComponentBuilder<T>().apply(block).build();
            components.add(component, value);
        }

        fun <T : Any> component(type: ComponentType<T>, lambda: () -> T) {
            components.add(type, lambda());
        }

        /**
         * Adds an [AttributeModifiersComponent] to the item.
         */
        fun attributeModifier(value: () -> AttributeModifiersComponent) {
            component(DataComponentTypes.ATTRIBUTE_MODIFIERS, value());
        }

        class FoodBuilder {
            private var hunger: Int = 0;
            private var saturationModifier: Float = 0.0F;
            private var canAlwaysEat: Boolean = false;
            private var eatTime: Float = 1.6F;
            private var usingConvertsTo: ItemConvertible? = null;
            private var effects: ImmutableList.Builder<Pair<StatusEffectInstance, Float>> = ImmutableList.builder();

            companion object {
                fun builder(block: FoodBuilder.() -> Unit): FoodComponent1 {
                    return FoodBuilder().apply(block).build();
                }
            }

            fun hunger(value: () -> Int) {
                this.hunger = value();
            }

            fun saturationModifier(value: () -> Float) {
                this.saturationModifier = value();
            }

            fun canAlwaysEat(value: () -> Boolean) {
                this.canAlwaysEat = value();
            }

            fun eatTime(value: () -> Float) {
                this.eatTime = value();
            }

            fun usingConvertsTo(value: () -> ItemConvertible) {
                this.usingConvertsTo = value();
            }

            class EffectListBuilder {
                private val effects: MutableList<Pair<StatusEffectInstance, Float>> = mutableListOf();

                operator fun Pair<StatusEffectInstance, Float>.unaryPlus() {
                    effects.add(this);
                }

                fun build(): ImmutableList<Pair<StatusEffectInstance, Float>> {
                    return ImmutableList.copyOf(effects);
                }
            }

            fun effects(block: EffectListBuilder.() -> Unit) {
                val builder = EffectListBuilder().apply(block);
                this.effects.addAll(builder.build());
            }

            fun build(): FoodComponent1 {
                return FoodComponent1.Builder()
                    .nutrition(hunger)
                    .saturationModifier(saturationModifier)
                    .usingConvertsTo(usingConvertsTo)
                    .apply {
                        if (canAlwaysEat)
                            alwaysEdible()

                        val built = effects.build();
                        val iterator = built.iterator();
                        while (iterator.hasNext()) {
                            val entry = iterator.next();
                            statusEffect(entry.first, entry.second);
                        }
                    }
                    .build();
            }
        }

        fun food(block: FoodBuilder.() -> Unit) {
            val food = FoodBuilder.builder(block);
            component(DataComponentTypes.FOOD, food);
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
                    if (jukeboxPlayable != null)
                        jukeboxPlayable(jukeboxPlayable)

                    val built = components.build();
                    val iterator = built.iterator();
                    while (iterator.hasNext()) {
                        val entry = iterator.next();
                        component(entry.type as ComponentType<Any>, entry.value);
                    }
                }
        }
    }
}