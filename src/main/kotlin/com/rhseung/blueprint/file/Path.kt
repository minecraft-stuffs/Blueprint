package com.rhseung.blueprint.file

import com.rhseung.blueprint.util.Functional.ifNotNull

class Path(val current: String) {
    var parent: Path? = null
        private set
    val childs: Set<Path>
        get() = _childs.toSet()
    private var _childs = mutableSetOf<Path>()
        private set

    constructor(path: Path) : this(path.current) {
        parent = path.parent
        _childs = path._childs
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Path) return false
        return this === other
    }

    fun toRoot(action: (Path) -> Unit) {
        var tmp = this
        while (true) {
            action(tmp)
            if (tmp.parent == null) break

            tmp = tmp.parent!!
        }
    }

    override fun toString(): String {
        val ret = StringBuilder()

        var root = this
        while (true) {
            ret.insert(0, "/${root.current}")

            if (root.parent == null) break
            root = root.parent!!
        }

        return ret.substring(1).toString()
    }

    operator fun div(subPath: Path): Path {
        subPath.parent.ifNotNull { throw Exception("subPath($subPath) should not have parent") }

        subPath.parent = this
        _childs.add(subPath)

        return subPath
    }

    operator fun div(subPath: String): Path {
        return this / Path(subPath)
    }

    companion object {
        val root  = Path("")

        fun join(vararg paths: Path): Path {
            return paths.reduce { acc, path -> acc / path }
        }
    }
}