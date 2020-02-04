package dev.nathanpb.mysticis.staff.executors.fire

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.staff.StaffContinueUseAirContext
import dev.nathanpb.mysticis.staff.executors.IStaffContinueUseAirExecutor
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
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
class FireWaveExecutor : IStaffContinueUseAirExecutor {

    override val effectId = Identifier("mysticis", "fire_wave")
    override val representationColor = 0xFF0000

    override fun accept(context: StaffContinueUseAirContext) = !context.user.isSneaking

    override fun cost(context: StaffContinueUseAirContext) = ManaData(fire = 1F)

    override fun invoke(context: StaffContinueUseAirContext): TypedActionResult<ItemStack> {
        val (user, stack) = context
        (0..100).forEach { _ ->
            user.world.addParticle(
                ParticleTypes.FLAME,
                user.x,
                user.y + 1.5,
                user.z,
                user.rotationVector.x + ((Random.nextFloat() - .5) / 1.5),
                user.rotationVector.y + ((Random.nextFloat() - .5) / 1.5),
                user.rotationVector.z + ((Random.nextFloat() - .5) / 1.5)
            )
        }
        if(!user.world.isClient) {
            user.world.playSound(
                null,
                user.x, user.y, user.z,
                SoundEvents.BLOCK_FIRE_AMBIENT,
                SoundCategory.PLAYERS,
                1F, 1F
            )
            user.world.getEntities(user, Box(user.blockPos).expand(9.0).offset(user.rotationVector)) {
                it is LivingEntity &&
                        it.posVector
                            .subtract(user.posVector)
                            .normalize()
                            .dotProduct(user.rotationVector) >= 0.85
            }.forEach {
                it.damage(DamageSource.IN_FIRE, 7F)
                it.fireTicks = 5 * 20
            }
        }
        return TypedActionResult.consume(stack)
    }
}
