package dev.nathanpb.mysticis.items.staff.crystals

import dev.nathanpb.mysticis.items.ItemBase
import dev.nathanpb.mysticis.items.staff.IStaffCrystal
import dev.nathanpb.mysticis.staff.StaffMode
import dev.nathanpb.mysticis.staff.executors.FireCrystalBlockSmelt
import dev.nathanpb.mysticis.staff.executors.FireCrystalContinueUseAir
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.world.World


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

class ItemFireStaffCrystal : IStaffCrystal, ItemBase() {
    override val color = 0xFF8000

    override val executors = mapOf(
        StaffMode.COMBAT to listOf(FireCrystalContinueUseAir()),
        StaffMode.UTILITY to listOf(FireCrystalBlockSmelt())
    )

    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        super.appendTooltip(stack, world, tooltip, context)
        tooltip?.add(LiteralText("§1Do you have the keys to the hotel?"))
        tooltip?.add(LiteralText("§1Cause I'm gonna string this motherfucker on §4fire"))
    }
}
