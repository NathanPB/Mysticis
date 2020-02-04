package dev.nathanpb.mysticis.staff.executors.air

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.staff.StaffSingleHitAirContext
import dev.nathanpb.mysticis.staff.executors.IStaffSingleHitAirExecutor
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class AirSelfPushExecutor : IStaffSingleHitAirExecutor {

    override val effectId = Identifier("mysticis", "air_push_self")
    override val representationColor = 0x00FF00

    override fun accept(context: StaffSingleHitAirContext) = context.user.isSneaking

    override fun cost(context: StaffSingleHitAirContext) = ManaData(air = 5F)

    override fun invoke(context: StaffSingleHitAirContext): TypedActionResult<ItemStack> {
        val (user, stack) = context
        // TODO sound and particle effects
        // TODO cooldown
        user.velocity = user.rotationVector.multiply(2.0, 1.0, 2.0)
        return TypedActionResult.consume(stack)
    }
}
