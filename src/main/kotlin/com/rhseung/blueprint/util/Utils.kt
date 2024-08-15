package com.rhseung.blueprint.util

import com.google.gson.JsonParser
import com.rhseung.blueprint.util.Functional.findFail
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

object Utils {
    fun String.titlecase(): String {
        return this
            .lowercase()
            .split("_")
            .joinToString(" ") { str -> str.replaceFirstChar { it.uppercase() } }
    }

    fun String.langcase(): String {
        return this.split("/").reversed().joinToString("_").titlecase()
    }

    fun parseModId(): String {
        val current = Path(System.getProperty("user.dir"))
        var projectRoot = current

        while (projectRoot.listDirectoryEntries().findFail { it.fileName.toString() == "src" }) {
            projectRoot = projectRoot.parent
        }

        // gradle.properties 파일에서 modid를 가져옴
        val gradleProperties = projectRoot.resolve("gradle.properties")
        if (gradleProperties.toFile().exists()) {
            return gradleProperties.toFile().readLines()
                .find { "modid" in it.replace("_", "").lowercase() }
                ?.substringAfter("=")
                ?.trim() ?: throw IllegalStateException("Could not find modid");
        }

        throw IllegalStateException("Could not find gradle.properties")
    }
}