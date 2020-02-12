package dev.nathanpb.mysticis.blocks.entity

import dev.nathanpb.mysticis.blocks.infusorPedestalBlockEntity
import dev.nathanpb.mysticis.utils.ImplementedInventory
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.DefaultedList

/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

class InfusorPedestalEntity : BlockEntity(infusorPedestalBlockEntity), ImplementedInventory {
    private var inventory = DefaultedList.ofSize(1, ItemStack.EMPTY)

    override fun getItems(): DefaultedList<ItemStack> = inventory

    fun offer(stack: ItemStack) {
        if (isInvEmpty) {
            setInvStack(0, stack)
        }
    }

    fun pop() = removeInvStack(0)

    override fun markDirty() {
        super<ImplementedInventory>.markDirty()
    }
}
