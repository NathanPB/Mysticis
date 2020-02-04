package dev.nathanpb.mysticis.staff.executors.air

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.staff.StaffContinueUseAirContext
import dev.nathanpb.mysticis.staff.executors.IStaffContinueUseAirExecutor
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import kotlin.random.Random


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class AirWindWaveExecutor : IStaffContinueUseAirExecutor {

    override val effectId = Identifier("mysticis", "air_wave")
    override val representationColor = 0xFF0000

    override fun accept(context: StaffContinueUseAirContext) = !context.user.isSneaking

    override fun cost(context: StaffContinueUseAirContext) = ManaData(air = 1F)

    override fun invoke(context: StaffContinueUseAirContext): TypedActionResult<ItemStack> {
        val (user, stack) = context
        if(!user.world.isClient) {
            // TODO sound and particle effects
            user.world.getEntities(user, Box(user.blockPos).expand(9.0).offset(user.rotationVector)) {
                it is LivingEntity &&
                        it.posVector
                            .subtract(user.posVector)
                            .normalize()
                            .dotProduct(user.rotationVector) >= 0.85
            }.forEach {
                it.setVelocity(
                    user.rotationVector.x + ((Random.nextFloat() - .5) / 1.5),
                    user.rotationVector.y + ((Random.nextFloat() - .5) / 1.5),
                    user.rotationVector.z + ((Random.nextFloat() - .5) / 1.5)
                )
            }
        }
        return TypedActionResult.consume(stack)
    }
}
