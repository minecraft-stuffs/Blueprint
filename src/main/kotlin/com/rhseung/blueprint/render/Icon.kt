package com.rhseung.blueprint.render

import com.rhseung.blueprint.file.Loc
import com.rhseung.blueprint.file.Loc.Companion.modid
import com.rhseung.blueprint.file.Loc.Companion.rangeTo

class Icon(val name: String, vararg variants: String) {
    private val variants: List<String> = variants.toList()

    operator fun get(index: Int): Loc {
        return modid.."textures/icon/${name.lowercase()}_${variants[index]}.png";
    }

    companion object {
        val PROTECTION = Icon("protection");
        val TOUGHNESS = Icon("toughness");
        val KNOCKBACK_RESISTANCE = Icon("knockback_resistance");
        val ATTACK_DAMAGE = Icon("attack_damage");
        val ATTACK_SPEED = Icon("attack_speed");
        val MINING_LEVEL = Icon("mining_level");
        val MINING_SPEED = Icon("mining_speed");
        val DURABILITY = Icon("durability");
        val HUNGER = Icon("hunger", "1");
        val SATURATION = Icon("saturation","1", "2", "3");
        val ENCHANTMENT = Icon("enchantment");
    }
}