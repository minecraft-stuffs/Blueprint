package com.rhseung.blueprint.datagen.handler

import net.minecraft.block.Block
import net.minecraft.data.server.loottable.BlockLootTableGenerator
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.MatchToolLootCondition
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.ApplyBonusLootFunction
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

typealias BuilderLambda = (RegistryWrapper.WrapperLookup) -> BlockLootTableHandler.Builder;

class BlockLootTableHandler(
    val modId: String,
    val registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>,
    val generator: BlockLootTableGenerator
) {
    val registryLookup: RegistryWrapper.WrapperLookup = registriesFuture.join();

    operator fun plusAssign(builderLambda: BuilderLambda) {
        val builder = builderLambda(registryLookup)
        generator.addDrop(builder.from, LootTable.builder().pools(builder.pools.map { it.build(generator).build() }))
    }

    companion object {
        infix fun ItemConvertible.counts(num: Int): Drop {
            return Drop(this, num, num)
        }

        infix fun ItemConvertible.counts(numRange: IntRange): Drop {
            return Drop(this, numRange.first, numRange.last)
        }
        
        fun builder(block: Builder.() -> Unit): BuilderLambda = { Builder(it).apply(block) }

        ////////////// helper functions

        fun dropSelf(block: Block): BuilderLambda {
            return builder {
                from { block }
                default {
                    drop { block }
                }
            }
        }
        
        fun drop(block: Block, drop: ItemConvertible): BuilderLambda {
            return builder {
                from { block }
                default {
                    drop { drop }
                }
            }
        }
        
        fun drop(block: Block, drop: Drop): BuilderLambda {
            return builder {
                from { block }
                default {
                    drop(drop.min..drop.max) { drop.itemConvertible }
                }
            }
        }
        
        fun dropConditional(block: Block, drop: ItemConvertible, condition: Condition): BuilderLambda {
            return builder {
                from { block }
                case (condition) {
                    drop { drop }
                }
            }
        }
        
        fun dropConditional(block: Block, drop: Drop, condition: Condition): BuilderLambda {
            return builder {
                from { block }
                case (condition) {
                    drop(drop.min..drop.max) { drop.itemConvertible }
                }
            }
        }
        
        fun dropSilkTouch(block: Block, drop: ItemConvertible): BuilderLambda
                = dropConditional(block, drop, Condition.SILK_TOUCH)
        
        fun dropSilkTouch(block: Block, drop: Drop): BuilderLambda
                = dropConditional(block, drop, Condition.SILK_TOUCH)
        
        fun dropShear(block: Block, drop: ItemConvertible): BuilderLambda
                = dropConditional(block, drop, Condition.SHEAR)
        
        fun dropShear(block: Block, drop: Drop): BuilderLambda
                = dropConditional(block, drop, Condition.SHEAR)
        
        fun dropOre(block: Block, drop: ItemConvertible): BuilderLambda {
            return builder {
                from { block }
                case (Condition.SILK_TOUCH or Condition.SHEAR) {
                    drop { block }
                }
                default {
                    drop { drop }
                    applyFortune { true }
                }
            }
        }
        
        /**
         * @param drop `Items.DIAMOND counts 3..5` 처럼 사용할 수 있습니다.
         */
        fun dropOre(block: Block, drop: Drop): BuilderLambda {
            return builder {
                from { block }
                case (Condition.SILK_TOUCH) {
                    drop { block }
                }
                default {
                    drop(drop.min..drop.max) { drop.itemConvertible }
                    applyFortune { true }
                }
            }
        }
        
        fun dropOreLikeRedstone(block: Block, drop: ItemConvertible): BuilderLambda
                = dropOre(block, drop counts 4..5)
        
        fun dropOreLikeLapis(block: Block, drop: ItemConvertible): BuilderLambda
                = dropOre(block, drop counts 4..9)
        
        fun dropOreLikeCopper(block: Block, drop: ItemConvertible): BuilderLambda
                = dropOre(block, drop counts 2..5)
    }

    /**
     * todo: [LootCondition.Builder]를 DSL 빌더로 재구성하기
     */

    class Condition(val builder: (BlockLootTableGenerator) -> LootCondition.Builder) {
        companion object {
            val SILK_TOUCH = Condition {
                it.createSilkTouchCondition()
            };

            val SHEAR = Condition {
                MatchToolLootCondition.builder(
                    ItemPredicate.Builder.create().items(Items.SHEARS)
                )
            };
        }

        infix fun and(other: Condition): Condition {
            return Condition { generator ->
                builder(generator).and(other.builder(generator))
            }
        }

        infix fun or(other: Condition): Condition {
            return Condition { generator ->
                builder(generator).or(other.builder(generator))
            }
        }

        operator fun not(): Condition {
            return Condition { generator ->
                builder(generator).invert()
            }
        }
    }
    
    class Builder(val registryLookUp: RegistryWrapper.WrapperLookup) {
        val pools = mutableListOf<PoolBuilder>()
        lateinit var from: Block

        fun from(lambda: () -> Block): Builder {
            from = lambda()
            return this
        }

        fun case(condition: Condition, block: PoolBuilder.() -> Unit): Builder {
            pools.add(PoolBuilder(registryLookUp, condition).apply(block))
            return this
        }

        fun default(block: PoolBuilder.() -> Unit): Builder {
            pools.add(PoolBuilder(registryLookUp).apply(block))
            return this
        }
    }
    
    // Pool은 전체 LootTable에서, 각각의 Case를 담당하는 구조라고 보면 됨
    class PoolBuilder(val registryLookUp: RegistryWrapper.WrapperLookup, val condition: Condition? = null) {
        private lateinit var drop: Drop
        private var rolls: Int = 1
        private var applyFortune: Boolean = false

        @JvmName("drop")
        fun drop(lambda: () -> ItemConvertible): PoolBuilder {
            drop = lambda() counts 1
            return this
        }

        @JvmName("dropRange")
        fun drop(range: IntRange, lambda: () -> ItemConvertible): PoolBuilder {
            drop = lambda() counts range
            return this
        }

        fun rolls(lambda: () -> Int): PoolBuilder {
            rolls = lambda()
            return this
        }

        fun applyFortune(lambda: () -> Boolean): PoolBuilder {
            applyFortune = lambda()
            return this
        }

        /**
         * todo: [LootPool.Builder]를 DSL 빌더로 재구성하기
         *  - applyExplosionDecay, etc... 등의 모든 메서드를 호환시키기
         */
        fun build(generator: BlockLootTableGenerator): LootPool.Builder {
            var pool = LootPool.builder().rolls(ConstantLootNumberProvider.create(rolls.toFloat()))

            if (condition != null) {
                pool = pool.conditionally(condition.builder(generator).build())
            }

            pool = pool.with(ItemEntry.builder(drop.itemConvertible)).apply(
                SetCountLootFunction.builder(
                    UniformLootNumberProvider.create(
                        drop.min.toFloat(),
                        drop.max.toFloat()
                    )
                )
            )

            if (applyFortune) {
                val impl = registryLookUp.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
                pool = pool.apply(ApplyBonusLootFunction.uniformBonusCount(impl.getOrThrow(Enchantments.FORTUNE)));
            }

            return pool
        }
    }

    data class Drop(
        val itemConvertible: ItemConvertible,
        val min: Int,
        val max: Int
    )
}