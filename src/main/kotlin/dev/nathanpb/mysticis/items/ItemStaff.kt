package dev.nathanpb.mysticis.items

import dev.nathanpb.mysticis.items.staff.IStaffAttachment
import dev.nathanpb.mysticis.staff.getStaffData
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.nbt.CompoundTag


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class ItemStaff : ItemBase() {
    companion object {
        val COLOR_PROVIDER = ItemColorProvider { stack, tintIndex ->
            val staffData = stack.getStaffData()
            when(tintIndex) {
                0 -> (staffData.rod.item as IStaffAttachment).color
                1 -> (staffData.head.item as IStaffAttachment).color
                2 -> if(staffData.crystal != null) {
                    (staffData.crystal as IStaffAttachment).color
                } else {
                    0xff00bb
                }
                else -> 0x00000000
            }
        }
    }

    override fun postProcessTag(tag: CompoundTag?): Boolean {
        tag?.getCompound("tag")?.put("mysticis.staff", CompoundTag())
        return super.postProcessTag(tag)
    }
}
