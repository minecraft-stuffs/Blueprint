package com.rhseung.blueprint.tool

data class ToolLevel internal constructor(val level: Tier, val type: ToolType) {
    fun isHand() = level == Tier.HAND
    fun hasType() = type != ToolType.NONE
}