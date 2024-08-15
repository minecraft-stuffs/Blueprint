package com.rhseung.blueprint.file

import com.rhseung.blueprint.util.Utils
import com.rhseung.blueprint.Blueprint
import net.minecraft.util.Identifier

class Loc {
    val namespace: Path
    val path: Path

    constructor(id: String) {
        val parts = id.split(":")
        this.namespace = Path(parts[0]);
        this.path = Path(parts[1]);
    }

    constructor(namespace: Path, path: Path) {
        this.namespace = namespace;
        this.path = path;
    }

    constructor(namespace: String, path: String) {
        this.namespace = Path(namespace);
        this.path = Path(path);
    }

    fun toIdentifier() = Identifier.of(namespace.toString(), path.toString());
    override fun toString() = toIdentifier().toString();

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