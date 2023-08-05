package com.rhseung.blueprint.util

import com.rhseung.blueprint.file.Path
import net.minecraft.nbt.NbtInt
import net.minecraft.nbt.NbtString
import java.util.NoSuchElementException

typealias NbtString = Nbt.NbtValue<String>
typealias NbtInt = Nbt.NbtValue<Int>
typealias NbtFloat = Nbt.NbtValue<Float>
typealias NbtList<T> = Nbt.NbtValue<List<T>>
typealias NbtLong = Nbt.NbtValue<Long>
typealias NbtShort = Nbt.NbtValue<Short>
typealias NbtByte = Nbt.NbtValue<Byte>
typealias NbtDouble = Nbt.NbtValue<Double>

object Nbt {
	interface NbtElement {
		val key: Path

		operator fun get(key: Path): NbtElement
		operator fun set(key: Path, value: Any?)
	}

	data class NbtCompound(override val key: Path, val elements: List<NbtElement> = listOf()): NbtElement {
		override fun get(key: Path): NbtElement {
			var current: NbtElement = this

			key.toRoot { path ->
				check(current is NbtCompound)
				current = (current as NbtCompound).elements.find { it.key == path }	// 메모리까지 같아야됨
					?: throw NoSuchElementException("NbtElement $path not found")
			}

			return current
		}

		override fun set(key: Path, value: Any?) {
			val current = get(key)

			check(current !is NbtCompound)
			current[key] = value
		}
	}

	data class NbtValue<T: Any>(override val key: Path, var value: T? = null): NbtElement {
		override fun get(key: Path) = throw NotImplementedError()

		override fun set(key: Path, value: Any?) {
			@Suppress("UNCHECKED_CAST")
			this.value = (value as? T) ?: throw IllegalArgumentException("NbtValue $key is not T")
		}
	}

	fun builder(block: NbtCompoundBuilder.() -> Unit): NbtCompound {
		return NbtCompoundBuilder(Path.root).apply(block).build()
	}

	class NbtCompoundBuilder(val key: Path) {
		private var elements: MutableList<NbtElement> = mutableListOf()

		fun compound(key: Path, block: NbtCompoundBuilder.() -> Unit) {
			elements.add(NbtCompoundBuilder(key).apply(block).build())
		}

		fun string(key: Path) {
			elements.add(NbtString(key))
		}

		fun int(key: Path) {
			elements.add(NbtInt(key))
		}

		fun float(key: Path) {
			elements.add(NbtFloat(key))
		}

		fun <T> list(key: Path) {
			elements.add(NbtList<T>(key))
		}

		fun long(key: Path) {
			elements.add(NbtLong(key))
		}

		fun short(key: Path) {
			elements.add(NbtShort(key))
		}

		fun byte(key: Path) {
			elements.add(NbtByte(key))
		}

		fun double(key: Path) {
			elements.add(NbtDouble(key))
		}

		fun build() = NbtCompound(key, elements)
	}
}