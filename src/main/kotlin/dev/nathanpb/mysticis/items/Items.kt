package dev.nathanpb.mysticis.items

import net.minecraft.item.Item
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
val ITEM_MYSTICIS = Item(Item.Settings().maxCount(1).rarity(Rarity.EPIC))
val ITEM_FIRE_STAFF = FireStaff()

fun registerItems() {
    mapOf(
        Pair(dev.nathanpb.mysticis.ITEM_MYSTICIS, ITEM_MYSTICIS),
        Pair(dev.nathanpb.mysticis.ITEM_FIRE_STAFF, ITEM_FIRE_STAFF)
    ).forEach { (identifier, item) ->
        Registry.register(Registry.ITEM, identifier, item)
    }
}
