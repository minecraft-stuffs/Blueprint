package com.rhseung.blueprint.data

import com.rhseung.blueprint.registration.key.BaseBlock
import net.minecraft.data.client.BlockStateModelGenerator

class BlockModelHandler(
    val modId: String,
    val generator: BlockStateModelGenerator
) {
    fun simple(baseBlock: BaseBlock) {
        generator.registerSimpleCubeAll(baseBlock)
    }

    // note: builder는 일단 내가 blockstate 문법을 몰라서 이해하면 만들자
}