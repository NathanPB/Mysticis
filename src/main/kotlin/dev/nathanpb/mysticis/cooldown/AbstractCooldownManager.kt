package dev.nathanpb.mysticis.cooldown

import net.minecraft.util.math.MathHelper


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
abstract class AbstractCooldownManager<T> {
    private val entries = mutableMapOf<T, CooldownTimeEntry>()
    private var tick = 0

    fun update() {
        tick++
        entries.filterNot { (_, time) ->
            time.endTick > tick
        }.forEach { (entry, _) ->
            entries.remove(entry)
        }
    }

    open fun getCooldownProgress(itemEntry: T, partialTicks: Float): Float {
        return entries[itemEntry]?.let { entry ->
            val f = entry.endTick - entry.startTick
            val g = entry.endTick.toFloat() - (tick + partialTicks)
            MathHelper.clamp(g / f, 0F, 1F)
        } ?: 0F
    }


    operator fun contains(itemEntry: T) = this[itemEntry] > 0F

    operator fun get(entry: T) = getCooldownProgress(entry, 0F)

    operator fun set(itemEntry: T, duration: Int) {
        entries[itemEntry] = CooldownTimeEntry(tick, tick + duration)
    }

}

data class CooldownTimeEntry(val startTick: Int, val endTick: Int)
