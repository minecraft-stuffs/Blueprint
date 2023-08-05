package com.rhseung.blueprint.registration

import com.rhseung.blueprint.file.Loc.Companion.modid
import com.rhseung.blueprint.file.Loc.Companion.rangeTo
import com.rhseung.blueprint.registration.key.BaseBlock
import com.rhseung.blueprint.registration.key.BaseItem
import com.rhseung.blueprint.registration.key.BaseItemGroup

object Registries {
    object ITEMS {
        private val entries = mutableListOf<BaseItem>()

        fun register(name: String, properties: BaseItem.Properties.() -> Unit = {}): BaseItem {
            return BaseItem(modid..name, BaseItem.Properties().apply(properties)).also {
                entries.add(it)
                it.register()
            }
        }

        fun forEach(action: (BaseItem) -> Unit) {
            entries.forEach(action)
        }
    }

    object BLOCKS {
        private val entries = mutableListOf<BaseBlock>()

        // todo: blockitem property 문법
        fun register(name: String, properties: BaseBlock.Properties.() -> Unit = {}): BaseBlock {
            return BaseBlock(modid..name, BaseBlock.Properties().apply(properties)).also {
                entries.add(it)
                it.register()
            }
        }

        fun forEach(action: (BaseBlock) -> Unit) {
            entries.forEach(action)
        }
    }

    object ITEM_GROUPS {
        private val entries = mutableListOf<BaseItemGroup>()

        fun register(name: String, properties: BaseItemGroup.Properties.() -> Unit = {}): BaseItemGroup {
            return BaseItemGroup(modid..name, BaseItemGroup.Properties().apply(properties)).also {
                entries.add(it)
                it.register()
            }
        }

        fun forEach(action: (BaseItemGroup) -> Unit) {
            entries.forEach(action)
        }
    }
}