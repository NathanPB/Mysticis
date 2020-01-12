package dev.nathanpb.mysticis.data

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
    val air: Int = 0,
    val fire: Int = 0,
    val water: Int = 0,
    val nature: Int = 0,
    val magic: Int = 0,
    val dark: Int = 0
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
                getInt("air"),
                getInt("fire"),
                getInt("water"),
                getInt("nature"),
                getInt("magic"),
                getInt("dark")
            )
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
        putInt("air", air)
        putInt("fire", fire)
        putInt("water", water)
        putInt("nature", nature)
        putInt("magic", magic)
        putInt("dark", dark)
    }

    /*
     * Operators
     */

    operator fun minus(addend: ManaData) = this + !addend

    operator fun not() = this * ManaData(-1, -1, -1, -1, -1 , -1)

    operator fun times(factor: ManaData) = mapValues { manaValue, type ->
        manaValue * type.get(factor)
    }

    operator fun plus(addend: ManaData) = mapValues { manaValue, type ->
        manaValue + type.get(addend)
    }

    operator fun div(divisor: ManaData) = mapValues { manaValue, type ->
        manaValue / type.get(divisor)
    }

    fun limitMin(limit: Int) = mapValues { it, _ ->
        max(limit, it)
    }

    fun limitMax(limit: Int) = mapValues { it, _ ->
        min(limit, it)
    }

    inline fun mapValues(transformer: (Int, KProperty1<ManaData, Int>)->Int) = ManaData(
        transformer(air, ManaData::air),
        transformer(fire, ManaData::fire),
        transformer(water, ManaData::water),
        transformer(nature, ManaData::nature),
        transformer(magic, ManaData::magic),
        transformer(dark, ManaData::dark)
    )
}
