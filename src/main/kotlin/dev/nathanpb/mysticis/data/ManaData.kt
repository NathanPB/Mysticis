package dev.nathanpb.mysticis.data

import dev.nathanpb.mysticis.acessors.IMysticisLivingEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.CompoundTag
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KProperty1

/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
/**
 * Stores Mana data from generic sources.
 */
@Suppress("unused")
data class ManaData (
    val air: Float = 0F,
    val fire: Float = 0F,
    val water: Float = 0F,
    val nature: Float = 0F,
    val magic: Float = 0F,
    val dark: Float = 0F
) {
    companion object {

        /**
         * Creates a [ManaData] object from a given [CompoundTag].
         *
         * Nullable values are intend as a zero;
         * The keys read are the class's parameter names.
         *
         * @param nbt The [CompoundTag] to get the data from.
         * @return The [ManaData] object with the values parsed from the [nbt].
         */
        fun loadFromTag(nbt: CompoundTag) = nbt.run {
            ManaData(
                getFloat("air"),
                getFloat("fire"),
                getFloat("water"),
                getFloat("nature"),
                getFloat("magic"),
                getFloat("dark")
            )
        }

        /**
         * Checks if a [CompoundTag] has all the necessary keys and values to make a [ManaData] object
         *
         * @param tag The tag to perform the checks
         * @return True if valid, false otherwise
         */
        fun isValidTag(tag: CompoundTag) = listOf("air", "fire", "water", "nature", "dark", "magic").all {
            tag.contains(it, 5)
        }
    }

    /**
     * Creates a [CompoundTag] from the current object. Used to serialize the data to be written into NBT.
     *
     * The keys used are the class's parameters names.
     *
     * @return the data written into a [CompoundTag].
     */
    fun mkCompoundTag() = CompoundTag().apply {
        putFloat("air", air)
        putFloat("fire", fire)
        putFloat("water", water)
        putFloat("nature", nature)
        putFloat("magic", magic)
        putFloat("dark", dark)
    }

    /*
     * Operators
     */

    operator fun minus(addend: ManaData) = this + !addend

    operator fun not() = this * ManaData(-1F, -1F, -1F, -1F, -1F, -1F)

    operator fun times(factor: ManaData) = mapValues { manaValue, type ->
        manaValue * type.get(factor)
    }

    operator fun plus(addend: ManaData) = mapValues { manaValue, type ->
        manaValue + type.get(addend)
    }

    operator fun div(divisor: ManaData) = mapValues { manaValue, type ->
        manaValue / type.get(divisor)
    }

    fun limitMin(limit: Float) = mapValues { it, _ ->
        max(limit, it)
    }

    fun limitMax(limit: Float) = mapValues { it, _ ->
        min(limit, it)
    }

    inline fun mapValues(transformer: (Float, KProperty1<ManaData, Float>)->Float) = ManaData(
        transformer(air, ManaData::air),
        transformer(fire, ManaData::fire),
        transformer(water, ManaData::water),
        transformer(nature, ManaData::nature),
        transformer(magic, ManaData::magic),
        transformer(dark, ManaData::dark)
    )
}

var LivingEntity.manaAffinity: ManaData
    get() = (this as IMysticisLivingEntity).mysticisAffinity
    set(value) {
        (this as IMysticisLivingEntity).mysticisAffinity = value
    }

var LivingEntity.mana: ManaData
    get() = (this as IMysticisLivingEntity).mysticisMana
    set(value) {
        (this as IMysticisLivingEntity).mysticisMana = value
    }

