package com.rhseung.blueprint.util

enum class Languages {
    EN_US,
    KO_KR;

    val code = name.lowercase()

    companion object {
        fun forEach(action: (Languages) -> Unit) {
            values().forEach(action)
        }
    }

    class LanguageTable(vararg langs: Pair<Languages, String>) {
        private val table: MutableMap<Languages, String?> = Languages.values().associateWith { null }.toMutableMap()

        init {
            langs.forEach { (lang, value) ->
                table[lang] = value
            }
        }

        operator fun get(language: Languages): String? {
            return table[language]
        }

        operator fun set(language: Languages, value: String) {
            table[language] = value
        }

        fun forEach(action: (Languages, String?) -> Unit) {
            table.forEach(action)
        }
    }
}