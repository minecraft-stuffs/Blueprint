package com.rhseung.blueprint.datagen.handler

import com.rhseung.blueprint.file.Loc
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.item.Item

class ItemTagHandler {
    internal val itemMap = mutableMapOf<TagKey<Item>, MutableList<Item>>()
    internal val tagMap = mutableMapOf<TagKey<Item>, MutableList<TagKey<Item>>>()

    inner class Set(val key: TagKey<Item>) {
        @JvmName("addItem")
        operator fun plusAssign(item: Item) {
            itemMap[key]?.add(item) ?: itemMap.put(key, mutableListOf(item))
        }

        @JvmName("addItems")
        operator fun plusAssign(items: List<Item>) {
            items.forEach { this += it }
        }

        @JvmName("addTag")
        operator fun plusAssign(tag: TagKey<Item>) {
            tagMap[key]?.add(tag) ?: tagMap.put(key, mutableListOf(tag))
        }

        @JvmName("addTags")
        operator fun plusAssign(tags: List<TagKey<Item>>) {
            tags.forEach { this += it }
        }
    }

    operator fun get(id: Loc): Set {
        return Set(TagKey.of(RegistryKeys.ITEM, id.toIdentifier()))
    }

    operator fun get(tag: TagKey<Item>): Set {
        return Set(tag)
    }
}