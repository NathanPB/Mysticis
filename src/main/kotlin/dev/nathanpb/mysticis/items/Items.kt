package dev.nathanpb.mysticis.items

import dev.nathanpb.mysticis.items.staff.BoneStaffRod
import dev.nathanpb.mysticis.items.staff.GoldenStaffHead
import dev.nathanpb.mysticis.items.staff.IronStaffHead
import dev.nathanpb.mysticis.items.staff.WoodenStaffRod
import dev.nathanpb.mysticis.items.staff.crystals.ItemFireStaffCrystal
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
val ITEM_MYSTICIS = Item(Item.Settings().maxCount(1).rarity(Rarity.EPIC))
val ITEM_AIR_CRYSTAL = CrystalBase()
val ITEM_FIRE_CRYSTAL = CrystalBase()
val ITEM_WATER_CRYSTAL = CrystalBase()
val ITEM_NATURE_CRYSTAL = CrystalBase()
val ITEM_WOODEN_STAFF_ROD = WoodenStaffRod()
val ITEM_BONE_STAFF_ROD = BoneStaffRod()
val ITEM_GOLDEN_STAFF_HEAD = GoldenStaffHead()
val ITEM_IRON_STAFF_HEAD = IronStaffHead()
val ITEM_STAFF = ItemStaff()
val ITEM_FIRE_STAFF_CRYSTAL = ItemFireStaffCrystal()

fun registerItems() {
    mapOf(
        "mysticis" to ITEM_MYSTICIS,
        "staff" to ITEM_STAFF,

        // Generic Crystals
        "air_crystal" to ITEM_AIR_CRYSTAL,
        "fire_crystal" to ITEM_FIRE_CRYSTAL,
        "water_crystal" to ITEM_WATER_CRYSTAL,
        "nature_crystal" to ITEM_NATURE_CRYSTAL,

        // Staff Rods
        "wooden_staff_rod" to ITEM_WOODEN_STAFF_ROD,
        "bone_staff_rod" to ITEM_BONE_STAFF_ROD,

        // Staff Heads
        "golden_staff_head" to ITEM_GOLDEN_STAFF_HEAD,
        "iron_staff_head" to ITEM_IRON_STAFF_HEAD,

        // Staff Crystals
        "fire_staff_crystal" to ITEM_FIRE_STAFF_CRYSTAL
    ).forEach { (identifier, item) ->
        Registry.register(Registry.ITEM, Identifier("mysticis", identifier), item)
    }
}
