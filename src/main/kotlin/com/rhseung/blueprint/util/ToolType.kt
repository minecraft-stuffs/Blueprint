package com.rhseung.blueprint.api

import com.rhseung.blueprint.file.Path
import com.rhseung.blueprint.util.ToolLevel

data class ToolType(
    val path: Path? = null
) {
    operator fun invoke(level: Int): ToolLevel {
        if (this == NONE)
            throw IllegalArgumentException("ToolType.NONE cannot have a level")

        return ToolLevel(level, this)
    }

    companion object {
        val axe = Path("axe")
        val hoe = Path("hoe")
        val pickaxe = Path("pickaxe")
        val shovel = Path("shovel")
        val sword = Path("sword")
        val shear = Path("shear")

        val NONE = ToolType()
        val ANY = ToolType()
        val AXE = ToolType(axe)
        val HOE = ToolType(hoe)
        val PICKAXE = ToolType(pickaxe)
        val SHOVEL = ToolType(shovel)
        val SWORD = ToolType(sword)
        val SHEAR = ToolType(shear)
    }
}