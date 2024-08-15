package com.rhseung.blueprint.datagen.handler

import com.rhseung.blueprint.file.Loc
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

class BlockTagHandler {
    internal val blockMap = mutableMapOf<TagKey<Block>, MutableList<Block>>()
    internal val tagMap = mutableMapOf<TagKey<Block>, MutableList<TagKey<Block>>>()

    inner class Set(val key: TagKey<Block>) {
        @JvmName("addBlock")
        operator fun plusAssign(block: Block) {
            blockMap[key]?.add(block) ?: blockMap.put(key, mutableListOf(block))
        }

        @JvmName("addBlocks")
        operator fun plusAssign(blocks: List<Block>) {
            blocks.forEach { this += it }
        }

        @JvmName("addTag")
        operator fun plusAssign(tag: TagKey<Block>) {
            tagMap[key]?.add(tag) ?: tagMap.put(key, mutableListOf(tag))
        }

        @JvmName("addTags")
        operator fun plusAssign(tags: List<TagKey<Block>>) {
            tags.forEach { this += it }
        }
    }

    operator fun get(id: Loc): Set {
        return Set(TagKey.of(RegistryKeys.BLOCK, id.toIdentifier()))
    }

    operator fun get(tag: TagKey<Block>): Set {
        return Set(tag)
    }
}