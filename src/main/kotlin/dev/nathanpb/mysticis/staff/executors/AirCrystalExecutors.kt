package dev.nathanpb.mysticis.staff.executors

import dev.nathanpb.mysticis.cooldown.StaffCooldownEntry
import dev.nathanpb.mysticis.cooldown.staffCooldownManager
import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.enums.StaffSingleUseType
import dev.nathanpb.mysticis.staff.StaffContinueUseAirContext
import dev.nathanpb.mysticis.staff.StaffSingleHitAirContext
import dev.nathanpb.mysticis.staff.StaffSingleUseAirContext
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

class AirCrystalContinueAirUseExecutor : IStaffContinueUseAirExecutor {
    override fun cost(context: StaffContinueUseAirContext): ManaData {
        return if(!context.user.isSneaking) {
            ManaData(air = 1F)
        } else {
            super.cost(context)
        }
    }

    override fun invoke(context: StaffContinueUseAirContext): TypedActionResult<ItemStack> {
        val (user, stack) = context
        if(!user.isSneaking)  {
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
        }
        return TypedActionResult.consume(stack)
    }
}

class AirCrystalSingleAirUseExecutor : IStaffSingleUseAirExecutor {
    override fun cost(context: StaffSingleUseAirContext): ManaData {
        return if(context.user.isSneaking) {
            ManaData(air = 1.5F)
        } else {
            return super.cost(context)
        }
    }

    override fun invoke(context: StaffSingleUseAirContext): TypedActionResult<ItemStack> {
        val (user, stack) = context
        if(context.user.isSneaking) {

            context.crystalItem?.let { crystalItem ->

                // If the staff mode is cooling down then fail the action
                // Otherwise send it to the cooldown manager and let the method keep executing

                val staffCooldownEntry = StaffCooldownEntry(crystalItem, StaffSingleUseType.AEO)
                if (staffCooldownEntry in user.staffCooldownManager) {
                    if(user.world.isClient) {
                        user.world.playSound(
                            user, user.x, user.y, user.z,
                            SoundEvents.BLOCK_LAVA_EXTINGUISH,
                            SoundCategory.PLAYERS,
                            1F, 1F
                        )
                    }
                    return TypedActionResult.fail(stack)
                } else {
                    user.staffCooldownManager[staffCooldownEntry] = 320
                }
            }

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
        }

        return TypedActionResult.consume(stack)
    }
}

class AirCrystalSingleAirHitExecutor : IStaffSingleHitAirExecutor {
    override fun cost(context: StaffSingleHitAirContext): ManaData {
        return if (context.user.isSneaking) {
            ManaData(air = 5F)
        } else {
            super.cost(context)
        }
    }

    override fun invoke(context: StaffSingleHitAirContext): TypedActionResult<ItemStack> {
        val (user, stack) = context
        return if (user.isSneaking) {
            // TODO sound and particle effects
            // TODO cooldown
            user.velocity = user.rotationVector.multiply(2.0, 1.0, 2.0)
            TypedActionResult.consume(stack)
        } else {
            TypedActionResult.pass(stack)
        }
    }
}
