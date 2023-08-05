package com.rhseung.blueprint.data

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.data.server.loottable.BlockLootTableGenerator
import com.rhseung.blueprint.data.BlockLootTableHandler.Companion.dropSelf
import com.rhseung.blueprint.data.BlockLootTableHandler.Companion.drop
import com.rhseung.blueprint.data.BlockLootTableHandler.Companion.dropConditional
import com.rhseung.blueprint.data.BlockLootTableHandler.Companion.dropSilkTouch
import com.rhseung.blueprint.data.BlockLootTableHandler.Companion.dropShear
import com.rhseung.blueprint.data.BlockLootTableHandler.Companion.dropOre
import com.rhseung.blueprint.data.BlockLootTableHandler.Companion.dropOreLikeLapis
import com.rhseung.blueprint.data.BlockLootTableHandler.Companion.dropOreLikeRedstone
import com.rhseung.blueprint.data.BlockLootTableHandler.Companion.dropOreLikeCopper

abstract class AbstractBlockLootTableProvider(
    open val output: FabricDataOutput
) : FabricBlockLootTableProvider(output) {

    override fun generate() {
        val blockLootTable = BlockLootTableHandler(output.modId, this)
        register(blockLootTable)
    }
    
    /**
     * 블록의 loot table을 등록합니다.
     * ```kotlin
     * blockLootTable += 함수([등록할 블록])
     * blockLootTable += [loot table 빌더]
     * blockLootTable += loop(Collection<등록할 블록의 타입>) {
     *   [등록할 블록] -> [loot table 빌더]
     * }
     * ```
     *
     * // remind: [BlockLootTableHandler]에 함수 추가하면 얘도 수정하기
     *
     * 함수에는 [drop], [dropSelf], [dropConditional], [dropSilkTouch], [dropShear], [dropOre], [dropOreLikeCopper], [dropOreLikeLapis], [dropOreLikeRedstone]가 있습니다.
     */
    open fun register(blockLootTable: BlockLootTableHandler) {}
}