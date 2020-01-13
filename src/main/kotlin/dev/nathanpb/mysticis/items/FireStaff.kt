package dev.nathanpb.mysticis.items

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class FireStaff : StaffBase() {
    override fun onTriggeredArea(user: LivingEntity, hand: Hand): TypedActionResult<ItemStack> {
       return TypedActionResult.pass(user.getStackInHand(hand));
    }

    override fun onTriggeredProjectile(user: LivingEntity, hand: Hand): TypedActionResult<ItemStack> {
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    override fun onTriggeredSelf(user: LivingEntity, hand: Hand): TypedActionResult<ItemStack> {
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
