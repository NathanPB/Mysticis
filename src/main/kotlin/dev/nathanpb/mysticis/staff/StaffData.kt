package dev.nathanpb.mysticis.staff

import dev.nathanpb.mysticis.InvalidStaffException
import dev.nathanpb.mysticis.items.ITEM_GOLDEN_STAFF_HEAD
import dev.nathanpb.mysticis.items.ITEM_WOODEN_STAFF_ROD
import dev.nathanpb.mysticis.items.ItemStaff
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
data class StaffData(val rod: ItemStack, val head: ItemStack, val crystal: ItemStack?)

var ItemStack.staffData: StaffData
    get() {
        if(this.item is ItemStaff) {
            val staffTag = orCreateTag.getCompound("mysticis.staff") ?: CompoundTag()
            val rod = if(staffTag.contains("rod", 10)) {
                ItemStack.fromTag(staffTag.getCompound("rod"))
            } else ITEM_WOODEN_STAFF_ROD.stackForRender
            val head = if(staffTag.contains("head", 10)) {
                ItemStack.fromTag(staffTag.getCompound("head"))
            } else ITEM_GOLDEN_STAFF_HEAD.stackForRender
            val crystal = if (staffTag.contains("crystal", 10)){
                ItemStack.fromTag(staffTag.getCompound("crystal"))
            } else null

            return StaffData(rod, head, crystal)
        } else throw InvalidStaffException(this)
    }
    set(data) {
        CompoundTag().apply {
            put("rod", data.rod.toTag(CompoundTag()))
            put("head", data.head.toTag(CompoundTag()))
            data.crystal?.let {
                put("crystal", data.crystal.toTag(CompoundTag()))
            }
        }.also {
            this.orCreateTag.put("mysticis.staff", it)
        }
    }
