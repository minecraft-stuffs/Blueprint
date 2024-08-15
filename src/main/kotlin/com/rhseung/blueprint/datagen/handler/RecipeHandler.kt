package com.rhseung.blueprint.datagen.handler

import com.rhseung.blueprint.file.Loc
import com.rhseung.blueprint.util.Functional.ifElse
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.block.Blocks
import net.minecraft.data.server.recipe.*
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder.getItemId
import net.minecraft.data.server.recipe.RecipeProvider.*
import net.minecraft.item.HoneycombItem
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.*
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.resource.featuretoggle.FeatureSet
import net.minecraft.util.Identifier
import kotlin.properties.Delegates

class RecipeHandler(
    private val exporter: RecipeExporter
) {
    operator fun plusAssign(builder: ShapelessRecipeBuilder) {
        builder.build().offerTo(exporter);
    }

    operator fun plusAssign(builder: ShapedRecipeBuilder) {
        builder.build().offerTo(exporter);
    }

    operator fun plusAssign(builder: CookingRecipeBuilder<out AbstractCookingRecipe>) {
        builder.build().offerTo(exporter);
    }

    operator fun plusAssign(builder: StonecuttingRecipeBuilder) {
        builder.build().offerTo(exporter);
    }

    operator fun plusAssign(builder: RecipeBuilder) {
        val built = builder.build();

        if (built is IRecipeJsonBuilder)
            built.offerTo(exporter, getItemId(built.outputItem));
        else
            throw IllegalArgumentException("Unsupported recipe builder type: ${built::class.simpleName}");
    }

    // -----------------

    operator fun plusAssign(pair: Pair<Loc, ShapelessRecipeBuilder>) {
        val (recipeId, builder) = pair;
        builder.build().offerTo(exporter, recipeId.toIdentifier());
    }

    operator fun plusAssign(pair: Pair<Loc, ShapedRecipeBuilder>) {
        val (recipeId, builder) = pair;
        builder.build().offerTo(exporter, recipeId.toIdentifier());
    }

    operator fun plusAssign(pair: Pair<Loc, CookingRecipeBuilder<out AbstractCookingRecipe>>) {
        val (recipeId, builder) = pair;
        builder.build().offerTo(exporter, recipeId.toIdentifier());
    }

    operator fun plusAssign(pair: Pair<Loc, SmithingTransformRecipeBuilder>) {
        val (recipeId, builder) = pair;
        builder.build().offerTo(exporter, recipeId.toIdentifier());
    }

    operator fun plusAssign(pair: Pair<Loc, SmithingTrimRecipeBuilder>) {
        val (recipeId, builder) = pair;
        builder.build().offerTo(exporter, recipeId.toIdentifier());
    }

    operator fun plusAssign(pair: Pair<Loc, RecipeBuilder>) {
        val (recipeId, builder) = pair;
        val built = builder.build();

        if (built is IRecipeJsonBuilder)
            built.offerTo(exporter, recipeId.toIdentifier());
        else
            throw IllegalArgumentException("Unsupported recipe builder type: ${built::class.simpleName}");
    }

    // -----------------

    operator fun plusAssign(pair: Pair<String, RecipeBuilder>) {
        val (recipeId, builder) = pair;
        this += Loc(recipeId) to builder;
    }

    // -----------------

    operator fun plusAssign(list: List<RecipeBuilder>) {
        list.forEach { this += it }
    }

    /**
     * [RecipeBuilder]를 상속받은 사용자 정의 클래스의 [RecipeBuilder.build] 메서드의 반환 타입으로 사용하는 용도
     */
    interface IRecipeJsonBuilder {
        val outputItem: Item;
        fun offerTo(exporter: RecipeExporter, recipeId: Identifier);
    }

    abstract class RecipeBuilder(protected val category: RecipeCategory) {
        var idTransform: (String) -> String = { it };
        protected val criteria: MutableMap<String, AdvancementCriterion<*>> = mutableMapOf();

        fun idTransformer(lambda: (String) -> String): RecipeBuilder {
            this.idTransform = lambda;
            return this;
        }

        class CriterionListBuilder {
            private val criterions = mutableMapOf<String, AdvancementCriterion<*>>();

            operator fun Pair<String, AdvancementCriterion<*>>.unaryPlus() {
                criterions[this.first] = this.second;
            }

            fun build(): Map<String, AdvancementCriterion<*>> {
                return criterions;
            }
        }

        fun criterion(name: String, lambda: () -> AdvancementCriterion<*>): RecipeBuilder {
            criteria[name] = lambda();
            return this;
        }

        fun criterions(block: CriterionListBuilder.() -> Unit): RecipeBuilder {
            criteria.putAll(CriterionListBuilder().apply(block).build());
            return this;
        }

        abstract fun build(): Any
    }

    class ShapelessRecipeBuilder(category: RecipeCategory) : RecipeBuilder(category) {
        private val inputs = mutableListOf<Ingredient>()
        lateinit var output: Item
        private var count by Delegates.notNull<Int>()
        private var group: String? = null

        override fun build(): ShapelessRecipeJsonBuilder {
            var builder = ShapelessRecipeJsonBuilder.create(category, output, count).group(group);
            inputs.forEach { builder = builder.input(it) }
            criteria.forEach { (name, criterion) -> builder = builder.criterion(name, criterion) }

            return builder;
        }

        @JvmName("inputTag")
        fun inputTag(lambda: () -> TagKey<Item>): ShapelessRecipeBuilder {
            inputs.add(Ingredient.fromTag(lambda()));
            return this;
        }

        @JvmName("inputTagSize")
        fun inputTag(size: Int, lambda: () -> TagKey<Item>): ShapelessRecipeBuilder {
            for (i in 0..<size) {
                inputs.add(Ingredient.fromTag(lambda()));
            }
            return this;
        }

        @JvmName("inputItem")
        fun input(lambda: () -> ItemConvertible): ShapelessRecipeBuilder {
            inputs.add(Ingredient.ofItems(lambda()));
            return this;
        }

        @JvmName("inputItemSize")
        fun input(size: Int, lambda: () -> ItemConvertible): ShapelessRecipeBuilder {
            for (i in 0..<size) {
                inputs.add(Ingredient.ofItems(lambda()));
            }
            return this;
        }

        @JvmName("inputIngredient")
        fun inputIngredient(lambda: () -> Ingredient): ShapelessRecipeBuilder {
            inputs.add(lambda());
            return this;
        }

        @JvmName("inputIngredientSize")
        fun inputIngredient(size: Int, lambda: () -> Ingredient): ShapelessRecipeBuilder {
            for (i in 0..<size) {
                inputs.add(lambda());
            }
            return this;
        }

        class InputBuilder {
            private val inputs = mutableListOf<Ingredient>();

            operator fun ItemConvertible.unaryPlus() {
                inputs.add(Ingredient.ofItems(this));
            }

            operator fun TagKey<Item>.unaryPlus() {
                inputs.add(Ingredient.fromTag(this));
            }

            operator fun Ingredient.unaryPlus() {
                inputs.add(this);
            }

            fun build(): List<Ingredient> {
                return inputs;
            }
        }

        fun inputs(block: InputBuilder.() -> Unit): ShapelessRecipeBuilder {
            inputs.addAll(InputBuilder().apply(block).build());
            return this;
        }

        @JvmName("output")
        fun output(lambda: () -> ItemConvertible): ShapelessRecipeBuilder {
            this.output = lambda().asItem();
            this.count = 1;
            return this;
        }

        @JvmName("outputCount")
        fun output(count: Int, lambda: () -> ItemConvertible): ShapelessRecipeBuilder {
            this.output = lambda().asItem();
            this.count = count;
            return this;
        }

        @JvmName("group")
        fun group(lambda: () -> String?): ShapelessRecipeBuilder {
            this.group = lambda();
            return this;
        }
    }

    class ShapedRecipeBuilder(category: RecipeCategory) : RecipeBuilder(category) {
        lateinit var output: Item
        private var count by Delegates.notNull<Int>()
        private var group: String? = null
        private var showNotification: Boolean = true;
        private val patterns = mutableListOf<String>();
        private val subs = mutableMapOf<Char, Ingredient>();

        override fun build(): ShapedRecipeJsonBuilder {
            var builder = ShapedRecipeJsonBuilder.create(category, output, count).group(group).showNotification(showNotification);
            subs.forEach { (ch, ingredient) -> builder = builder.input(ch, ingredient) }
            criteria.forEach { (name, criterion) -> builder = builder.criterion(name, criterion) }
            patterns.forEach { builder = builder.pattern(it) }

            return builder;
        }

        fun sub(lambda: () -> Pair<Char, ItemConvertible>): ShapedRecipeBuilder {
            return subIngredient { lambda().first to Ingredient.ofItems(lambda().second) }
        }

        fun subTag(lambda: () -> Pair<Char, TagKey<Item>>): ShapedRecipeBuilder {
            return subIngredient { lambda().first to Ingredient.fromTag(lambda().second) }
        }

        fun subIngredient(lambda: () -> Pair<Char, Ingredient>): ShapedRecipeBuilder {
            val ret = lambda();

            if (ret.first in subs)
                throw IllegalArgumentException("Symbol '${ret.first}' is already defined!")
            else if (ret.first == ' ')
                throw IllegalArgumentException("Symbol ' ' is reserved!")

            subs[ret.first] = ret.second;
            return this;
        }

        class PattenBuilder {
            private val pattern = mutableListOf<String>();

            operator fun String.unaryPlus() {
                pattern.add(this);
            }

            fun build(): List<String> {
                return pattern;
            }
        }

        fun patterns(block: PattenBuilder.() -> Unit): ShapedRecipeBuilder {
            patterns.addAll(PattenBuilder().apply(block).build());
            return this;
        }

        @JvmName("output")
        fun output(lambda: () -> ItemConvertible): ShapedRecipeBuilder {
            this.output = lambda().asItem();
            this.count = 1;
            return this;
        }

        @JvmName("outputCount")
        fun output(count: Int, lambda: () -> ItemConvertible): ShapedRecipeBuilder {
            this.output = lambda().asItem();
            this.count = count;
            return this;
        }

        @JvmName("group")
        fun group(lambda: () -> String?): ShapedRecipeBuilder {
            this.group = lambda();
            return this;
        }

        @JvmName("showNotification")
        fun showNotification(lambda: () -> Boolean): ShapedRecipeBuilder {
            this.showNotification = lambda();
            return this;
        }
    }

    class CookingRecipeBuilder<T: AbstractCookingRecipe>(category: RecipeCategory) : RecipeBuilder(category) {
        private lateinit var input: Ingredient
        private lateinit var output: Item
        private var experience by Delegates.notNull<Float>()
        private var cookingTime by Delegates.notNull<Int>()
        private var group: String? = null
        private lateinit var recipeSerializer: RecipeSerializer<T>;
        private lateinit var recipeFactory: AbstractCookingRecipe.RecipeFactory<T>;
        private var cookingMethod: CookingMethod? = null;

        enum class CookingMethod {
            CAMPFIRE_COOKING,
            BLASTING,
            SMOKING,
            SMELTING;
        }

        override fun build(): CookingRecipeJsonBuilder {
            var builder = when (cookingMethod) {
                CookingMethod.CAMPFIRE_COOKING -> CookingRecipeJsonBuilder.createCampfireCooking(input, category, output, experience, cookingTime)
                CookingMethod.BLASTING -> CookingRecipeJsonBuilder.createBlasting(input, category, output, experience, cookingTime)
                CookingMethod.SMOKING -> CookingRecipeJsonBuilder.createSmoking(input, category, output, experience, cookingTime)
                CookingMethod.SMELTING -> CookingRecipeJsonBuilder.createSmelting(input, category, output, experience, cookingTime)
                else -> CookingRecipeJsonBuilder.create<T>(input, category, output, experience, cookingTime, recipeSerializer, recipeFactory)
            }.group(group);
            criteria.forEach { (name, criterion) -> builder = builder.criterion(name, criterion) }

            return builder;
        }

        @JvmName("inputTag")
        fun inputTag(lambda: () -> TagKey<Item>): CookingRecipeBuilder<T> {
            input = Ingredient.fromTag(lambda());
            return this;
        }

        @JvmName("inputItem")
        fun input(lambda: () -> ItemConvertible): CookingRecipeBuilder<T> {
            input = Ingredient.ofItems(lambda());
            return this;
        }

        @JvmName("inputIngredient")
        fun inputIngredient(lambda: () -> Ingredient): CookingRecipeBuilder<T> {
            input = lambda();
            return this;
        }

        @JvmName("output")
        fun output(lambda: () -> ItemConvertible): CookingRecipeBuilder<T> {
            this.output = lambda().asItem();
            return this;
        }

        @JvmName("experience")
        fun experience(lambda: () -> Float): CookingRecipeBuilder<T> {
            this.experience = lambda();
            return this;
        }

        @JvmName("cookingTime")
        fun cookingTime(lambda: () -> Int): CookingRecipeBuilder<T> {
            this.cookingTime = lambda();
            return this;
        }

        @JvmName("group")
        fun group(lambda: () -> String?): CookingRecipeBuilder<T> {
            this.group = lambda();
            return this;
        }

        @JvmName("serializer")
        fun serializer(lambda: () -> RecipeSerializer<T>): CookingRecipeBuilder<T> {
            this.recipeSerializer = lambda();
            return this;
        }

        @JvmName("cookingMethod")
        fun cookingMethod(lambda: () -> CookingMethod): CookingRecipeBuilder<T> {
            cookingMethod = lambda();
            return this;
        }

        fun recipeFactory(lambda: () -> AbstractCookingRecipe.RecipeFactory<T>): CookingRecipeBuilder<T> {
            recipeFactory = lambda();
            return this;
        }
    }

    class SmithingTransformRecipeBuilder(category: RecipeCategory) : RecipeBuilder(category) {
        private lateinit var base: Ingredient
        private lateinit var addition: Ingredient
        private lateinit var template: Ingredient
        lateinit var output: Item

        override fun build(): SmithingTransformRecipeJsonBuilder {
            var builder = SmithingTransformRecipeJsonBuilder(template, base, addition, category, output);
            criteria.forEach { (name, criterion) -> builder = builder.criterion(name, criterion) }

            return builder;
        }

        @JvmName("baseTag")
        fun baseTag(lambda: () -> TagKey<Item>): SmithingTransformRecipeBuilder {
            base = Ingredient.fromTag(lambda());
            return this;
        }

        @JvmName("baseItem")
        fun base(lambda: () -> ItemConvertible): SmithingTransformRecipeBuilder {
            base = Ingredient.ofItems(lambda());
            return this;
        }

        @JvmName("baseIngredient")
        fun baseIngredient(lambda: () -> Ingredient): SmithingTransformRecipeBuilder {
            base = lambda();
            return this;
        }

        @JvmName("additionTag")
        fun additionTag(lambda: () -> TagKey<Item>): SmithingTransformRecipeBuilder {
            addition = Ingredient.fromTag(lambda());
            return this;
        }

        @JvmName("additionItem")
        fun addition(lambda: () -> ItemConvertible): SmithingTransformRecipeBuilder {
            addition = Ingredient.ofItems(lambda());
            return this;
        }

        @JvmName("additionIngredient")
        fun additionIngredient(lambda: () -> Ingredient): SmithingTransformRecipeBuilder {
            addition = lambda();
            return this;
        }

        @JvmName("templateTag")
        fun templateTag(lambda: () -> TagKey<Item>): SmithingTransformRecipeBuilder {
            template = Ingredient.fromTag(lambda());
            return this;
        }

        @JvmName("templateItem")
        fun template(lambda: () -> ItemConvertible): SmithingTransformRecipeBuilder {
            template = Ingredient.ofItems(lambda());
            return this;
        }

        @JvmName("templateIngredient")
        fun templateIngredient(lambda: () -> Ingredient): SmithingTransformRecipeBuilder {
            template = lambda();
            return this;
        }

        @JvmName("output")
        fun output(lambda: () -> ItemConvertible): SmithingTransformRecipeBuilder {
            output = lambda().asItem();
            return this;
        }
    }

    class SmithingTrimRecipeBuilder(category: RecipeCategory) : RecipeBuilder(category) {
        private lateinit var base: Ingredient
        private lateinit var addition: Ingredient
        private lateinit var template: Ingredient

        override fun build(): SmithingTrimRecipeJsonBuilder {
            val builder = SmithingTrimRecipeJsonBuilder(category, template, base, addition);
            criteria.forEach(builder::criterion);
            return builder;
        }

        @JvmName("baseTag")
        fun baseTag(lambda: () -> TagKey<Item>): SmithingTrimRecipeBuilder {
            base = Ingredient.fromTag(lambda());
            return this;
        }

        @JvmName("baseItem")
        fun base(lambda: () -> ItemConvertible): SmithingTrimRecipeBuilder {
            base = Ingredient.ofItems(lambda());
            return this;
        }

        @JvmName("baseIngredient")
        fun baseIngredient(lambda: () -> Ingredient): SmithingTrimRecipeBuilder {
            base = lambda();
            return this;
        }

        @JvmName("additionTag")
        fun additionTag(lambda: () -> TagKey<Item>): SmithingTrimRecipeBuilder {
            addition = Ingredient.fromTag(lambda());
            return this;
        }

        @JvmName("additionItem")
        fun addition(lambda: () -> ItemConvertible): SmithingTrimRecipeBuilder {
            addition = Ingredient.ofItems(lambda());
            return this;
        }

        @JvmName("additionIngredient")
        fun additionIngredient(lambda: () -> Ingredient): SmithingTrimRecipeBuilder {
            addition = lambda();
            return this;
        }

        @JvmName("templateTag")
        fun templateTag(lambda: () -> TagKey<Item>): SmithingTrimRecipeBuilder {
            template = Ingredient.fromTag(lambda());
            return this;
        }

        @JvmName("templateItem")
        fun template(lambda: () -> ItemConvertible): SmithingTrimRecipeBuilder {
            template = Ingredient.ofItems(lambda());
            return this;
        }

        @JvmName("templateIngredient")
        fun templateIngredient(lambda: () -> Ingredient): SmithingTrimRecipeBuilder {
            template = lambda();
            return this;
        }
    }

    class StonecuttingRecipeBuilder(category: RecipeCategory) : RecipeBuilder(category) {
        private lateinit var input: Ingredient
        private lateinit var output: Item
        private var count by Delegates.notNull<Int>()
        private var recipeFactory: CuttingRecipe.RecipeFactory<*> = CuttingRecipe.RecipeFactory(::StonecuttingRecipe);
        private var group: String? = null;

        override fun build(): StonecuttingRecipeJsonBuilder {
            val builder = StonecuttingRecipeJsonBuilder(category, recipeFactory, input, output, count).group(group);
            criteria.forEach { (name, criterion) -> builder.criterion(name, criterion) };

            return builder;
        }

        @JvmName("inputTag")
        fun inputTag(lambda: () -> TagKey<Item>): StonecuttingRecipeBuilder {
            input = Ingredient.fromTag(lambda());
            return this;
        }

        @JvmName("inputItem")
        fun input(lambda: () -> ItemConvertible): StonecuttingRecipeBuilder {
            input = Ingredient.ofItems(lambda());
            return this;
        }

        @JvmName("inputIngredient")
        fun inputIngredient(lambda: () -> Ingredient): StonecuttingRecipeBuilder {
            input = lambda();
            return this;
        }

        @JvmName("output")
        fun output(lambda: () -> ItemConvertible): StonecuttingRecipeBuilder {
            this.output = lambda().asItem();
            this.count = 1;
            return this;
        }

        @JvmName("outputCount")
        fun output(count: Int, lambda: () -> ItemConvertible): StonecuttingRecipeBuilder {
            this.output = lambda().asItem();
            this.count = count;
            return this;
        }

        fun recipeFactory(lambda: () -> CuttingRecipe.RecipeFactory<*>): StonecuttingRecipeBuilder {
            recipeFactory = lambda();
            return this;
        }

        fun group(lambda: () -> String?): StonecuttingRecipeBuilder {
            this.group = lambda();
            return this;
        }
    }

    object Builder {
        /**
         * Shapeless recipe builder
         *
         * @param [criterion] initialize using [criterion], [criterions]
         * @param [inputs] initialize using [input], [inputTag], [inputIngredient], [inputs]
         * @param [output] initialize using [output]
         * @param [group] **optional**, initialize using [group]
         * @param [idTransformer] **optional**, initialize using [idTransformer]
         */
        fun shapeless(
            category: RecipeCategory,
            block: ShapelessRecipeBuilder.() -> Unit
        ): ShapelessRecipeBuilder {
            return ShapelessRecipeBuilder(category).apply(block);
        }

        /**
         * Shaped recipe builder
         *
         * @param [criterion] initialize using [criterion], [criterions]
         * @param [output] initialize using [output]
         * @param [sub] initialize using [sub], [subTag], [subIngredient]
         * @param [patterns] initialize using [patterns]
         * @param [group] **optional**, initialize using [group]
         * @param [showNotification] **optional**, initialize using [showNotification]
         */
        fun shaped(
            category: RecipeCategory,
            block: ShapedRecipeBuilder.() -> Unit
        ): ShapedRecipeBuilder {
            return ShapedRecipeBuilder(category).apply(block);
        }

        /**
         * Cooking recipe builder
         *
         * @param [criterion] initialize using [criterion], [criterions]
         * @param [input] initialize using [input], [inputTag], [inputIngredient]
         * @param [output] initialize using [output]
         * @param [experience] initialize using [experience]
         * @param [cookingTime] initialize using [cookingTime]
         * @param [recipeSerializer] initialize using [serializer]
         * @param [recipeFactory] initialize using [recipeFactory]
         * @param [cookingMethod] initialize using [cookingMethod]
         * @param [group] **optional**, initialize using [group]
         * @param [idTransformer] **optional**, initialize using [idTransformer]
         */
        fun <T : AbstractCookingRecipe> cooking(
            category: RecipeCategory,
            block: CookingRecipeBuilder<T>.() -> Unit
        ): CookingRecipeBuilder<T> {
            return CookingRecipeBuilder<T>(category).apply(block);
        }

        /**
         * Cooking recipe builder
         *
         * @param [criterion] initialize using [criterion], [criterions]
         * @param [input] initialize using [input], [inputTag], [inputIngredient]
         * @param [output] initialize using [output]
         * @param [experience] initialize using [experience]
         * @param [cookingTime] initialize using [cookingTime]
         * @param [recipeSerializer] initialize using [serializer]
         * @param [recipeFactory] initialize using [recipeFactory]
         * @param [group] **optional**, initialize using [group]
         * @param [idTransformer] **optional**, initialize using [idTransformer]
         */
        fun campfireCooking(
            category: RecipeCategory,
            block: CookingRecipeBuilder<CampfireCookingRecipe>.() -> Unit
        ): CookingRecipeBuilder<CampfireCookingRecipe> {
            return CookingRecipeBuilder<CampfireCookingRecipe>(category).apply(block)
                .apply { cookingMethod { CookingRecipeBuilder.CookingMethod.CAMPFIRE_COOKING } };
        }

        /**
         * Cooking recipe builder
         *
         * @param [criterion] initialize using [criterion], [criterions]
         * @param [input] initialize using [input], [inputTag], [inputIngredient]
         * @param [output] initialize using [output]
         * @param [experience] initialize using [experience]
         * @param [cookingTime] initialize using [cookingTime]
         * @param [recipeSerializer] initialize using [serializer]
         * @param [recipeFactory] initialize using [recipeFactory]
         * @param [group] **optional**, initialize using [group]
         * @param [idTransformer] **optional**, initialize using [idTransformer]
         */
        fun blasting(
            category: RecipeCategory,
            block: CookingRecipeBuilder<BlastingRecipe>.() -> Unit
        ): CookingRecipeBuilder<BlastingRecipe> {
            return CookingRecipeBuilder<BlastingRecipe>(category).apply(block)
                .apply {
                    cookingMethod { CookingRecipeBuilder.CookingMethod.BLASTING }
                };
        }

        /**
         * Cooking recipe builder
         *
         * @param [criterion] initialize using [criterion], [criterions]
         * @param [input] initialize using [input], [inputTag], [inputIngredient]
         * @param [output] initialize using [output]
         * @param [experience] initialize using [experience]
         * @param [cookingTime] initialize using [cookingTime]
         * @param [recipeSerializer] initialize using [serializer]
         * @param [recipeFactory] initialize using [recipeFactory]
         * @param [group] **optional**, initialize using [group]
         * @param [idTransformer] **optional**, initialize using [idTransformer]
         */
        fun smoking(
            category: RecipeCategory,
            block: CookingRecipeBuilder<SmokingRecipe>.() -> Unit
        ): CookingRecipeBuilder<SmokingRecipe> {
            return CookingRecipeBuilder<SmokingRecipe>(category).apply(block)
                .apply { cookingMethod { CookingRecipeBuilder.CookingMethod.SMOKING } };
        }

        /**
         * Cooking recipe builder
         *
         * @param [criterion] initialize using [criterion], [criterions]
         * @param [input] initialize using [input], [inputTag], [inputIngredient]
         * @param [output] initialize using [output]
         * @param [experience] initialize using [experience]
         * @param [cookingTime] initialize using [cookingTime]
         * @param [recipeSerializer] initialize using [serializer]
         * @param [recipeFactory] initialize using [recipeFactory]
         * @param [group] **optional**, initialize using [group]
         * @param [idTransformer] **optional**, initialize using [idTransformer]
         */
        fun smelting(
            category: RecipeCategory,
            block: CookingRecipeBuilder<SmeltingRecipe>.() -> Unit
        ): CookingRecipeBuilder<SmeltingRecipe> {
            return CookingRecipeBuilder<SmeltingRecipe>(category).apply(block)
                .apply {
                    cookingMethod { CookingRecipeBuilder.CookingMethod.SMELTING }
                };
        }

        /**
         * Smithing transform recipe builder
         *
         * @param [criterion] initialize using [criterion], [criterions]
         * @param [base] initialize using [base], [baseTag], [baseIngredient]
         * @param [addition] initialize using [addition], [additionTag], [additionIngredient]
         * @param [template] initialize using [template], [templateTag], [templateIngredient]
         * @param [output] initialize using [output]
         */
        fun smithingTransform(
            category: RecipeCategory,
            block: SmithingTransformRecipeBuilder.() -> Unit
        ): SmithingTransformRecipeBuilder {
            return SmithingTransformRecipeBuilder(category).apply(block);
        }

        /**
         * Smithing trim recipe builder
         *
         * @param [criterion] initialize using [criterion], [criterions]
         * @param [base] initialize using [base], [baseTag], [baseIngredient]
         * @param [addition] initialize using [addition], [additionTag], [additionIngredient]
         * @param [template] initialize using [template], [templateTag], [templateIngredient]
         */
        fun smithingTrim(
            category: RecipeCategory,
            block: SmithingTrimRecipeBuilder.() -> Unit
        ): SmithingTrimRecipeBuilder {
            return SmithingTrimRecipeBuilder(category).apply(block);
        }

        /**
         * Stonecutting recipe builder
         *
         * @param [criterion] initialize using [criterion], [criterions]
         * @param [input] initialize using [input], [inputTag], [inputIngredient]
         * @param [output] initialize using [output], [output]
         * @param [recipeFactory] **optional**, initialize using [recipeFactory]
         * @param [group] **optional**, initialize using [group]
         * @param [idTransformer] **optional**, initialize using [idTransformer]
         */
        fun stonecutting(
            category: RecipeCategory,
            block: StonecuttingRecipeBuilder.() -> Unit
        ): StonecuttingRecipeBuilder {
            return StonecuttingRecipeBuilder(category).apply(block);
        }
    }

    companion object {
        ///////////////// Helper functions

        fun single(
            input: ItemConvertible,
            output: ItemConvertible,
            count: Int = 1,
            group: String? = null,
            category: RecipeCategory = RecipeCategory.MISC,
            criterion: String = hasItem(input),
        ): ShapelessRecipeBuilder {
            return Builder.shapeless(category) {
                input { input }
                output(count) { output }
                group { group }
                criterion(criterion) { conditionsFromItem(input) }
                idTransformer { convertBetween(output, input) }
            }
        }

        fun compact2x2(
            input: ItemConvertible,
            output: ItemConvertible,
            count: Int = 1,
            group: String? = null,
            category: RecipeCategory = RecipeCategory.MISC,
            criterion: String = hasItem(input),
        ): ShapedRecipeBuilder {
            return Builder.shaped(category) {
                output(count) { output }
                patterns {
                    + "##"
                    + "##"
                }
                sub { '#' to input }
                group { group }
                criterion(criterion) { conditionsFromItem(input) }
            }
        }

        fun compact3x3(
            input: ItemConvertible,
            output: ItemConvertible,
            count: Int = 1,
            group: String? = null,
            category: RecipeCategory = RecipeCategory.MISC,
            criterion: String = hasItem(input),
        ): ShapedRecipeBuilder {
            return Builder.shaped(category) {
                output(count) { output }
                patterns {
                    + "###"
                    + "###"
                    + "###"
                }
                sub { '#' to input }
                group { group }
                criterion(criterion) { conditionsFromItem(input) }
            }
        }

        fun smelting(
            input: ItemConvertible,
            output: ItemConvertible,
            experience: Float,
            cookingTime: Int,
            group: String? = null,
            category: RecipeCategory = RecipeCategory.MISC,
        ): CookingRecipeBuilder<SmeltingRecipe> {
            return Builder.smelting(category) {
                input { input }
                output { output }
                experience { experience }
                cookingTime { cookingTime }
                group { group }
                serializer { RecipeSerializer.SMELTING }
                recipeFactory { AbstractCookingRecipe.RecipeFactory<SmeltingRecipe>(::SmeltingRecipe) }
                criterion(hasItem(input)) { conditionsFromItem(input) }
                idTransformer { getItemPath(output) + "_from_smelting_" + getItemPath(input) }
            }
        }

        fun blasting(
            input: ItemConvertible,
            output: ItemConvertible,
            experience: Float,
            cookingTime: Int,
            group: String? = null,
            category: RecipeCategory = RecipeCategory.MISC,
        ): CookingRecipeBuilder<BlastingRecipe> {
            return Builder.blasting(category) {
                input { input }
                output { output }
                experience { experience }
                cookingTime { cookingTime }
                group { group }
                serializer { RecipeSerializer.BLASTING }
                recipeFactory { AbstractCookingRecipe.RecipeFactory<BlastingRecipe>(::BlastingRecipe) }
                criterion(hasItem(input)) { conditionsFromItem(input) }
                idTransformer { getItemPath(output) + "_from_blasting_" + getItemPath(input) }
            }
        }

        fun netheriteUpgrade(
            input: ItemConvertible,
            output: ItemConvertible,
            category: RecipeCategory = RecipeCategory.MISC,
        ): SmithingTransformRecipeBuilder {
            return Builder.smithingTransform(category) {
                template { Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE }
                base { input }
                addition { Items.NETHERITE_INGOT }
                output { output }
                criterion("has_netherite_ingot") { conditionsFromItem(Items.NETHERITE_INGOT) }
                idTransformer { getItemPath(output) + "_smithing" }
            }
        }

        fun smithingTrim(
            template: ItemConvertible
        ): SmithingTrimRecipeBuilder {
            return Builder.smithingTrim(RecipeCategory.MISC) {
                template { template }
                baseTag { ItemTags.TRIMMABLE_ARMOR }
                additionTag { ItemTags.TRIM_MATERIALS }
                criterion("has_smithing_trim_template") { conditionsFromItem(template) }
            }
        }

        fun plank(
            input: TagKey<Item>,
            output: ItemConvertible,
            count: Int
        ): ShapelessRecipeBuilder {
            return Builder.shapeless(RecipeCategory.BUILDING_BLOCKS) {
                inputTag { input }
                output(count) { output }
                group { "planks" }
                criterion("has_log") { conditionsFromTag(input) }
                idTransformer { getItemPath(output) + "_from_plank" }
            }
        }

        fun barkBlock(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return compact2x2(input, output, count=3, group="bark", category=RecipeCategory.BUILDING_BLOCKS, criterion="has_log");
        }

        fun boat(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.TRANSPORTATION) {
                sub { '#' to input }
                patterns {
                    + "# #"
                    + "###"
                }
                group { "boat" }
                criterion("in_water") { requireEnteringFluid(Blocks.WATER) }
            }
        }

        fun chestBoat(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapelessRecipeBuilder {
            return Builder.shapeless(RecipeCategory.TRANSPORTATION) {
                inputs {
                    + Blocks.CHEST
                    + input
                }
                output { output }
                group { "chest_boat" }
                criterion("has_boat") { conditionsFromTag(ItemTags.BOATS) }
            }
        }

        // TODO: criterion 없이 작동함? 테스트
        fun door(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.REDSTONE) {
                sub { '#' to input }
                patterns {
                    + "##"
                    + "##"
                    + "##"
                }
                output(3) { output }
            }
        }

        fun fence(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.DECORATIONS) {
                sub { 'A' to input }
                sub { 'B' to (output == Blocks.NETHER_BRICK_FENCE).ifElse(Items.NETHER_BRICK, Items.STICK) }
                patterns {
                    + "ABA"
                    + "ABA"
                }
                output((output == Blocks.NETHER_BRICK_FENCE).ifElse(6, 3)) { output }
            }
        }

        fun pressurePlate(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.REDSTONE) {
                sub { '#' to input }
                patterns {
                    + "##"
                }
                output { output }
                criterion(hasItem(input)) { conditionsFromItem(input) }
            }
        }

        fun slab(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.BUILDING_BLOCKS) {
                sub { '#' to input }
                patterns {
                    + "###"
                }
                output(6) { output }
                criterion(hasItem(input)) { conditionsFromItem(input) }
            }
        }

        fun sign(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.DECORATIONS) {
                sub { '#' to input }
                sub { 'X' to Items.STICK }
                patterns {
                    + "###"
                    + "###"
                    + " X "
                }
                output(3) { output }
                group { "sign" }
            }
        }

        fun hangingSign(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.DECORATIONS) {
                sub { '#' to input }
                sub { 'X' to Items.CHAIN }
                patterns {
                    + "X X"
                    + "###"
                    + "###"
                }
                output(6) { output }
                group { "hanging_sign" }
                criterion("has_stripped_logs") { conditionsFromItem(input) }
            }
        }

        fun dyeable(
            dyes: List<Item>,
            dyeables: List<Item>,
            group: String? = null
        ): List<ShapelessRecipeBuilder> {
            val ret = mutableListOf<ShapelessRecipeBuilder>();
            dyes.forEachIndexed { i, dye ->
                val dyeable = dyeables[i];

                ret.add(Builder.shapeless(RecipeCategory.BUILDING_BLOCKS) {
                    input { dye }
                    inputIngredient { Ingredient.ofStacks(dyeables.filter { it != dyeable }.map(::ItemStack).stream()) }
                    output { dyeable }
                    group { group }
                    criterion("has_needed_dye") { conditionsFromItem(dye) }
                    idTransformer { "dye_" + getItemPath(dyeable) }
                });
            }

            return ret;
        }

        fun carpet(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.DECORATIONS) {
                sub { '#' to input }
                patterns {
                    + "##"
                }
                group { "carpet" }
                output(3) { output }
                criterion(hasItem(input)) { conditionsFromItem(input) }
            }
        }

        fun bed(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.DECORATIONS) {
                sub { '#' to input }
                subTag { 'X' to ItemTags.PLANKS }
                patterns {
                    + "###"
                    + "XXX"
                }
                group { "bed" }
                output { output }
                criterion(hasItem(input)) { conditionsFromItem(input) }
            }
        }

        fun banner(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.DECORATIONS) {
                sub { '#' to input }
                sub { 'X' to Items.STICK }
                patterns {
                    + "###"
                    + "###"
                    + " X "
                }
                group { "banner" }
                output { output }
                criterion(hasItem(input)) { conditionsFromItem(input) }
            }
        }

        fun stainedGlassDyeing(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.BUILDING_BLOCKS) {
                sub { '#' to Blocks.GLASS }
                sub { 'X' to input }
                patterns {
                    + "###"
                    + "#X#"
                    + "###"
                }
                group { "stained_glass" }
                output(8) { output }
                criterion("has_glass") { conditionsFromItem(Blocks.GLASS) }
            }
        }

        fun stainedGlassPane(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.DECORATIONS) {
                sub { '#' to Blocks.GLASS_PANE }
                sub { 'X' to input }
                patterns {
                    + "###"
                    + "###"
                }
                group { "stained_glass_pane" }
                output(16) { output }
                criterion("has_glass") { conditionsFromItem(Blocks.GLASS_PANE) }
            }
        }

        fun stainedGlassPaneDyeing(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.DECORATIONS) {
                sub { '#' to Blocks.GLASS_PANE }
                sub { 'X' to input }
                patterns {
                    + "###"
                    + "#X#"
                    + "###"
                }
                group { "stained_glass_pane" }
                output(8) { output }
                criterions {
                    + ("has_glass_pane" to conditionsFromItem(Blocks.GLASS_PANE))
                    + (hasItem(input) to conditionsFromItem(input))
                }
                idTransformer { convertBetween(output, Blocks.GLASS_PANE) }
            }
        }

        fun terracotaDyeing(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.BUILDING_BLOCKS) {
                sub { '#' to Blocks.TERRACOTTA }
                sub { 'X' to input }
                patterns {
                    + "###"
                    + "#X#"
                    + "###"
                }
                group { "stained_terracotta" }
                output(8) { output }
                criterion("has_terracotta") { conditionsFromItem(Blocks.TERRACOTTA) }
            }
        }

        fun concretePowderDyeing(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapelessRecipeBuilder {
            return Builder.shapeless(RecipeCategory.BUILDING_BLOCKS) {
                input { input }
                input(4) { Blocks.SAND }
                input(4) { Blocks.GRAVEL }
                output(8) { output }
                group { "concrete_powder" }
                criterions {
                    + ("has_sand" to conditionsFromItem(Blocks.SAND))
                    + ("has_gravel" to conditionsFromItem(Blocks.GRAVEL))
                }
            }
        }

        fun candleDyeing(
            input: ItemConvertible,
            output: ItemConvertible
        ): ShapelessRecipeBuilder {
            return Builder.shapeless(RecipeCategory.DECORATIONS) {
                input { input }
                input { Blocks.CANDLE }
                output { output }
                group { "dyed_candle" }
                criterion(hasItem(input)) { conditionsFromItem(input) }
            }
        }

        fun wall(
            input: ItemConvertible,
            output: ItemConvertible,
            category: RecipeCategory = RecipeCategory.BUILDING_BLOCKS
        ): ShapedRecipeBuilder {
            return Builder.shaped(category) {
                sub { '#' to input }
                patterns {
                    + "###"
                    + "###"
                }
                output(6) { output }
                criterion(hasItem(input)) { conditionsFromItem(input) }
            }
        }

        fun polishedStone(
            input: ItemConvertible,
            output: ItemConvertible,
            category: RecipeCategory = RecipeCategory.BUILDING_BLOCKS
        ): ShapedRecipeBuilder {
            return compact2x2(input, output, count=4);
        }

        fun cutCopper(
            input: ItemConvertible,
            output: ItemConvertible,
            category: RecipeCategory = RecipeCategory.BUILDING_BLOCKS
        ): ShapedRecipeBuilder {
            return compact2x2(input, output, count=4);
        }

        fun chiseledBlock(
            input: ItemConvertible,
            output: ItemConvertible,
            category: RecipeCategory = RecipeCategory.BUILDING_BLOCKS
        ): ShapedRecipeBuilder {
            return Builder.shaped(category) {
                sub { '#' to input }
                patterns {
                    + "#"
                    + "#"
                }
                output { output }
                criterion(hasItem(input)) { conditionsFromItem(input) }
            }
        }

        fun mosaic(
            input: ItemConvertible,
            output: ItemConvertible,
            category: RecipeCategory = RecipeCategory.BUILDING_BLOCKS
        ): ShapedRecipeBuilder {
            return chiseledBlock(input, output, category);
        }

        fun stonecutting(
            input: ItemConvertible,
            output: ItemConvertible,
            category: RecipeCategory = RecipeCategory.BUILDING_BLOCKS,
            count: Int = 1
        ): StonecuttingRecipeBuilder {
            return Builder.stonecutting(category) {
                input { input }
                output(count) { output }
                criterion(hasItem(input)) { conditionsFromItem(input) }
                idTransformer { convertBetween(output, input) + "_stonecutting" }
            }
        }

        fun cracking(
            input: ItemConvertible,
            output: ItemConvertible
        ): CookingRecipeBuilder<SmeltingRecipe> {
            return Builder.smelting(RecipeCategory.BUILDING_BLOCKS) {
                input { input }
                output { output }
                experience { 0.1F }
                cookingTime { 200 }
                group { "cracking" }
                criterion(hasItem(input)) { conditionsFromItem(input) }
            }
        }

        fun reversible3x3(
            base: ItemConvertible,
            compact: ItemConvertible,
            compactCategory: RecipeCategory,
            reverseCategory: RecipeCategory,
            compactGroup: String? = null,
            reverseGroup: String? = null
        ): List<RecipeBuilder> {
            return listOf(
                compact3x3(base, compact, group=compactGroup, category=compactCategory),
                single(compact, base, group=reverseGroup, category=reverseCategory)
            )
        }

        fun reversible2x2(
            base: ItemConvertible,
            compact: ItemConvertible,
            compactCategory: RecipeCategory,
            reverseCategory: RecipeCategory,
            compactGroup: String? = null,
            reverseGroup: String? = null
        ): List<RecipeBuilder> {
            return listOf(
                compact2x2(base, compact, group=compactGroup, category=compactCategory),
                single(compact, base, group=reverseGroup, category=reverseCategory)
            )
        }

        fun smithingTemplateCopy(
            template: ItemConvertible,
            resource: ItemConvertible,
        ): ShapedRecipeBuilder {
            return Builder.shaped(RecipeCategory.MISC) {
                sub { 'D' to Items.DIAMOND }
                sub { 'S' to resource }
                sub { 'T' to template }
                patterns {
                    + "DTD"
                    + "DSD"
                    + "DDD"
                }
                output(2) { template }
                criterion(hasItem(template)) { conditionsFromItem(template) }
            }
        }

        fun <T : AbstractCookingRecipe> foodCooking(
            input: ItemConvertible,
            output: ItemConvertible,
            cooker: String,
            serializer: RecipeSerializer<T>,
            recipeFactory: AbstractCookingRecipe.RecipeFactory<T>,
            experience: Float,
            cookingTime: Int,
        ): CookingRecipeBuilder<T> {
            return Builder.cooking(RecipeCategory.FOOD) {
                input { input }
                output { output }
                experience { experience }
                cookingTime { cookingTime }
                serializer { serializer }
                recipeFactory { recipeFactory }
                criterion(hasItem(input)) { conditionsFromItem(input) }
                idTransformer { getItemPath(output) + "_from_" + cooker }
            }
        }

        fun waxing(
            enabledFeatures: FeatureSet
        ): List<ShapelessRecipeBuilder> {
            val ret = mutableListOf<ShapelessRecipeBuilder>();

            HoneycombItem.UNWAXED_TO_WAXED_BLOCKS.get().forEach { (unwaxed, waxed) ->
                if (waxed.requiredFeatures.isSubsetOf(enabledFeatures)) {
                    ret.add(Builder.shapeless(RecipeCategory.BUILDING_BLOCKS) {
                        inputs {
                            + unwaxed
                            + Items.HONEYCOMB
                        }
                        output { waxed }
                        group { getItemPath(waxed) }
                        criterion(hasItem(unwaxed)) { conditionsFromItem(unwaxed) }
                        idTransformer { convertBetween(waxed, Items.HONEYCOMB) }
                    })
                }
            }

            return ret;
        }
    }
}
