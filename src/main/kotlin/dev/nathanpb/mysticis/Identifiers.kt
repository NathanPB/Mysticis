package dev.nathanpb.mysticis

import net.minecraft.util.Identifier


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

private fun identifier(id: String) = Identifier("mysticis", id)

val PACKET_MANA_CHANGED = identifier("manachanged")
val PACKET_AFFINITY_CHANGED = identifier("affinitychanged")

val ITEM_MYSTICIS = identifier("mysticis")
val ITEM_AIR_CRYSTAL = identifier("air_crystal")
val ITEM_FIRE_CRYSTAL = identifier("fire_crystal")
val ITEM_WATER_CRYSTAL = identifier("water_crystal")
val ITEM_NATURE_CRYSTAL = identifier("nature_crystal")
