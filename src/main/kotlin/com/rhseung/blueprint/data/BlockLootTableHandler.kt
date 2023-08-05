package com.rhseung.blueprint.data

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
import net.minecraft.predicate.NumberRange
import net.minecraft.predicate.item.EnchantmentPredicate
import net.minecraft.predicate.item.ItemPredicate

class BlockLootTableHandler(
    val modId: String,
    val generator: BlockLootTableGenerator
) {
    operator fun plusAssign(builder: Builder) {
        generator.addDrop(builder.from, LootTable.builder().pools(builder.pools.map { it.build() }))
    }

    companion object {
        infix fun ItemConvertible.counts(num: Int): Drop {
            return Drop(this, num, num)
        }

        infix fun ItemConvertible.counts(numRange: IntRange): Drop {
            return Drop(this, numRange.first, numRange.last)
        }

        infix fun LootCondition.Builder.and(builder: LootCondition.Builder): LootCondition.Builder {
            return this.and(builder)
        }
        
        fun builder(block: Builder.() -> Unit): Builder = Builder().apply(block)
        
        fun dropSelf(block: Block): Builder {
            return builder {
                from { block }
                default {
                    drop { block }
                }
            }
        }
        
        fun drop(block: Block, drop: ItemConvertible): Builder {
            return builder {
                from { block }
                default {
                    drop { drop }
                }
            }
        }
        
        fun drop(block: Block, drop: Drop): Builder {
            return builder {
                from { block }
                default {
                    drops { drop }
                }
            }
        }
        
        fun dropConditional(block: Block, drop: ItemConvertible, condition: LootCondition.Builder): Builder {
            return builder {
                from { block }
                case (condition) {
                    drop { drop }
                }
            }
        }
        
        fun dropConditional(block: Block, drop: Drop, condition: LootCondition.Builder): Builder {
            return builder {
                from { block }
                case (condition) {
                    drops { drop }
                }
            }
        }
        
        fun dropSilkTouch(block: Block, drop: ItemConvertible): Builder
                = this.dropConditional(block, drop, Condition.SILK_TOUCH)
        
        fun dropSilkTouch(block: Block, drop: Drop): Builder
                = this.dropConditional(block, drop, Condition.SILK_TOUCH)
        
        fun dropShear(block: Block, drop: ItemConvertible)
                = this.dropConditional(block, drop, Condition.SHEAR)
        
        fun dropShear(block: Block, drop: Drop)
                = this.dropConditional(block, drop, Condition.SHEAR)
        
        fun dropOre(block: Block, drop: ItemConvertible): Builder {
            return builder {
                from { block }
                case (Condition.SILK_TOUCH) {
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
        fun dropOre(block: Block, drop: Drop): Builder {
            return builder {
                from { block }
                case (Condition.SILK_TOUCH) {
                    drop { block }
                }
                default {
                    drops { drop }
                    applyFortune { true }
                }
            }
        }
        
        fun dropOreLikeRedstone(block: Block, drop: ItemConvertible): Builder
                = this.dropOre(block, drop counts 4..5)
        
        fun dropOreLikeLapis(block: Block, drop: ItemConvertible): Builder
                = this.dropOre(block, drop counts 4..9)
        
        fun dropOreLikeCopper(block: Block, drop: ItemConvertible): Builder
                = this.dropOre(block, drop counts 2..5)
    }

    /**
     * todo: [LootCondition.Builder]를 DSL 빌더로 재구성하기
     */
    object Condition {
        val SILK_TOUCH = MatchToolLootCondition.builder(
            ItemPredicate.Builder.create().enchantment(
                EnchantmentPredicate(Enchantments.SILK_TOUCH, NumberRange.IntRange.atLeast(1))
            )
        )
        
        val SHEAR = MatchToolLootCondition.builder(
            ItemPredicate.Builder.create().items(
                Items.SHEARS
            )
        )
    }
    
    class Builder {
        val pools = mutableListOf<LootPool.Builder>()
        lateinit var from: Block

        fun from(lambda: () -> Block): Builder {
            from = lambda()
            return this
        }

        fun case(condition: LootCondition.Builder, block: PoolBuilder.() -> Unit): Builder {
            val pool = PoolBuilder()
            pool.condition = condition
            pools.add(pool.apply(block).build())
            return this
        }

        fun default(block: PoolBuilder.() -> Unit): Builder {
            pools.add(PoolBuilder().apply(block).build())
            return this
        }
    }
    
    // Pool은 전체 LootTable에서, 각각의 Case를 담당하는 구조라고 보면 됨
    class PoolBuilder {
        private lateinit var drop: Drop
        var condition: LootCondition.Builder? = null
        private var rolls: Int = 1
        private var applyFortune: Boolean = false

        fun drop(lambda: () -> ItemConvertible): PoolBuilder {
            drop = lambda() counts 1
            return this
        }

        fun drops(lambda: () -> Drop): PoolBuilder {
            drop = lambda()
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
        fun build(): LootPool.Builder {
            var pool = LootPool.builder().rolls(ConstantLootNumberProvider.create(rolls.toFloat()))

            if (condition != null)
                pool = pool.conditionally(condition!!)

            pool = pool.with(ItemEntry.builder(drop.itemConvertible)).apply(
                SetCountLootFunction.builder(
                    UniformLootNumberProvider.create(
                        drop.min.toFloat(),
                        drop.max.toFloat()
                    )
                )
            )

            if (applyFortune)
                pool = pool.apply(ApplyBonusLootFunction.uniformBonusCount(Enchantments.FORTUNE))

            return pool
        }
    }

    data class Drop(
        val itemConvertible: ItemConvertible,
        val min: Int,
        val max: Int
    )
}