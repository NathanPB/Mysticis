package dev.nathanpb.mysticis.staff.executors

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.staff.StaffSingleUseBlockContext
import dev.nathanpb.mysticis.utils.placeFluid
import net.minecraft.block.FluidFillable
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemStack
import net.minecraft.util.TypedActionResult


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class WaterCrystalSingleUseBlock : IStaffSingleUseBlockExecutor {

    override fun cost(context: StaffSingleUseBlockContext): ManaData {
        return ManaData(water = 4F)
    }

    override fun invoke(context: StaffSingleUseBlockContext): TypedActionResult<ItemStack> {
        val (user, stack, _, _, blockPos, side, hitsInsideBlock) = context
        return if (!hitsInsideBlock) {
            val targetPosition = if (user.world.getBlockState(blockPos).block is FluidFillable) {
                blockPos
            } else {
                blockPos.offset(side)
            }

            return if (user.world.placeFluid(user, targetPosition, Fluids.WATER)) {
                TypedActionResult.consume(stack)
            } else {
                TypedActionResult.fail(stack)
            }
        } else {
            TypedActionResult.fail(stack)
        }
    }
}
