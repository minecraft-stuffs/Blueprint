package com.rhseung.blueprint.registration.key

import com.rhseung.blueprint.file.Loc
import com.rhseung.blueprint.registration.IBaseKey
import com.rhseung.blueprint.util.Languages
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

class BaseBlockItem(
    /**
     * The block of the item.
     */
    val block: BaseBlock,

    /**
     * The settings of the item.
     */
    val properties: BaseItem.Properties
) : BlockItem(block, properties.build()), IBaseKey {

    override val id: Loc = block.id

    override val langs: Languages.LanguageTable = block.langs

    override fun register() {
        Registry.register(Registries.ITEM, id.toIdentifier(), this)
    }
}