package dev.nathanpb.mysticis.blocks.entity

import dev.nathanpb.mysticis.blocks.WAND_ASSEMBLER_BLOCK_ENTITY
import dev.nathanpb.mysticis.items.staff.IStaffCrystal
import dev.nathanpb.mysticis.items.staff.IStaffHead
import dev.nathanpb.mysticis.items.staff.IStaffRod
import dev.nathanpb.mysticis.utils.ImplementedInventory
import net.minecraft.block.entity.BlockEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.DefaultedList


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class WandAssemblerEntity : BlockEntity(WAND_ASSEMBLER_BLOCK_ENTITY), ImplementedInventory {

    private val _items: DefaultedList<ItemStack> = DefaultedList.ofSize(3, ItemStack.EMPTY)

    override fun getItems() = _items

    override fun isValidInvStack(slot: Int, stack: ItemStack?): Boolean {
        return stack != null && when(slot) {
            0 -> stack.item is IStaffRod
            1 -> stack.item is IStaffHead
            2 -> stack.item is IStaffCrystal
            else -> false
        } && items[slot].count < invMaxStackAmount
    }

    override fun getInvMaxStackAmount(): Int {
        return 1
    }

    override fun fromTag(tag: CompoundTag?) {
        super.fromTag(tag)
        Inventories.fromTag(tag, items)
    }

    override fun toTag(tag: CompoundTag?): CompoundTag {
        Inventories.toTag(tag, items)
        return super.toTag(tag)
    }

    override fun markDirty() {
        super<ImplementedInventory>.markDirty()
    }
}
