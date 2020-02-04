package dev.nathanpb.mysticis.staff.executors.air

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.staff.StaffSingleUseAirContext
import dev.nathanpb.mysticis.staff.executors.IStaffSingleUseAirExecutor
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import kotlin.math.cos
import kotlin.math.sin


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class AirAeOWaveExecutor : IStaffSingleUseAirExecutor {

    override val effectId = Identifier("mysticis", "air_wave_aeo")
    override val representationColor = 0x0000FF
    override val cooldownOnConsume = 320

    override fun accept(context: StaffSingleUseAirContext) = context.user.isSneaking

    override fun cost(context: StaffSingleUseAirContext) = ManaData(air = 1.5F)

    override fun invoke(context: StaffSingleUseAirContext): TypedActionResult<ItemStack> {
        val (user, stack) = context

        if (!user.world.isClient) {
            // TODO sound and particle effects
            user.world.getEntities(user, Box(user.blockPos).expand(5.0)) {
                it is LivingEntity
            }.forEach {
                val vector = it.posVector.subtract(user.posVector.subtract(Vec3d(0.0, 0.5, 0.0)))
                it.setVelocity(
                    vector.x,
                    vector.y,
                    vector.z
                )
            }
        } else {
            (-180..180)
                .map(Int::toDouble)
                .forEach { yaw ->
                    if (user.world.isClient) {
                        user.apply {
                            world.addParticle(
                                ParticleTypes.CLOUD,
                                x, y, z,
                                cos(yaw),
                                0.05,
                                sin(yaw)
                            )
                        }
                    }
                }
        }

        return TypedActionResult.consume(stack)
    }
}
