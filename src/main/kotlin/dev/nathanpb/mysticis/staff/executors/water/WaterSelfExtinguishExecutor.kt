package dev.nathanpb.mysticis.staff.executors.water

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.staff.StaffSingleHitAirContext
import dev.nathanpb.mysticis.staff.executors.IStaffSingleHitAirExecutor
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class WaterSelfExtinguishExecutor : IStaffSingleHitAirExecutor {

    override fun accept(context: StaffSingleHitAirContext): Boolean {
        return context.user.isSneaking && (context.user.world.isClient || context.user.fireTicks > 0)
    }
    override fun cost(context: StaffSingleHitAirContext) = ManaData(water = (context.user.fireTicks / 5).toFloat())
    override val representationColor = 0x00E1FF
    override val effectId = Identifier("mysticis", "water_self_stinguish")
    override val cooldownOnConsume = 100

    override fun invoke(context: StaffSingleHitAirContext): TypedActionResult<ItemStack> {
        if (!context.user.world.isClient) {
            context.user.fireTicks = 0
        } else {
            context.user.apply {
                this.world.playSound(
                     this,
                     this.blockPos,
                     SoundEvents.BLOCK_FIRE_EXTINGUISH,
                     SoundCategory.PLAYERS,
                    1F, 1F
                 )

                (0..32).forEach { _ ->
                    this.world.addParticle(
                        ParticleTypes.FALLING_WATER,
                        this.x + Math.random() - .5,
                        this.eyeY + Math.random() - .5,
                        this.z + Math.random() - .5,
                        0.0, 0.0, 0.0
                    )
                }
            }
        }

        return TypedActionResult.consume(context.stack)
    }
}
