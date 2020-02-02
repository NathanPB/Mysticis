package dev.nathanpb.mysticis.items.staff

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.staff.StaffSingleUseAirContext
import dev.nathanpb.mysticis.staff.StaffSingleUseBlockContext
import dev.nathanpb.mysticis.staff.StaffSingleUseEntityContext
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.TypedActionResult


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

// TODO fix air usages triggering on entity and block usages
// TODO fix single usages triggering on continue usages too
interface ISingleUseStaffCrystal {

    fun onSingleUseEntity(context: StaffSingleUseEntityContext): TypedActionResult<ItemStack> {
        return TypedActionResult.pass(context.stack)
    }

    fun onSingleUseBlock(context: StaffSingleUseBlockContext): TypedActionResult<ItemStack> {
        return TypedActionResult.pass(context.stack)
    }

    fun onSingleUseAir(context: StaffSingleUseAirContext): TypedActionResult<ItemStack> {
        return TypedActionResult.pass(context.stack)
    }

    fun onSingleHit(user: LivingEntity, stack: ItemStack, block: Block?, entity: Entity?): TypedActionResult<ItemStack> {
        return TypedActionResult.pass(stack)
    }

    fun singleUseEntityCost(context: StaffSingleUseEntityContext) = ManaData()

    fun singleUseBlockCost(context: StaffSingleUseBlockContext) = ManaData()

    /**
     * WARNING: This is actually triggered on entity and block usages too.
     */
    fun singleUseAirCost(context: StaffSingleUseAirContext) = ManaData()

    fun singleHitCost(user: LivingEntity, stack: ItemStack, block: Block?, entity: Entity?) = ManaData()
}
