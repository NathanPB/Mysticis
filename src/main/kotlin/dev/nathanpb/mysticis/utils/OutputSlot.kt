package dev.nathanpb.mysticis.utils

import net.minecraft.container.Slot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class OutputSlot(
    inventory: Inventory,
    slotIndex: Int,
    x: Int,
    y: Int
) : Slot(inventory, slotIndex, x, y) {

    override fun canInsert(itemStack_1: ItemStack?) = false

    override fun onTakeItem(playerEntity_1: PlayerEntity?, itemStack_1: ItemStack?): ItemStack? {
        this.onCrafted(itemStack_1)
        super.onTakeItem(playerEntity_1, itemStack_1)
        return itemStack_1
    }
}
