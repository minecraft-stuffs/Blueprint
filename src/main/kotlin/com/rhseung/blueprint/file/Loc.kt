package com.rhseung.blueprint.file

import com.rhseung.blueprint.util.Utils
import com.rhseung.blueprint.Blueprint
import net.minecraft.util.Identifier

data class Loc(
    val namespace: Path,
    val path: Path
) {
    override fun toString() = toIdentifier().toString()

    fun toIdentifier() = Identifier(namespace.toString(), path.toString())

    companion object {
        operator fun Path.rangeTo(path: Path) = Loc(this, path)
        operator fun Path.rangeTo(path: String) = Loc(this, Path(path))

        val minecraft = Path("minecraft")
        val realms = Path("realms")
        val fabric = Path("fabric")
        val lib = Path(Blueprint.modid)
        val modid = Path(Utils.parseModId())
    }
}