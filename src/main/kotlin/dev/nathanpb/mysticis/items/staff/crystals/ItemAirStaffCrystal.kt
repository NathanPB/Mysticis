package dev.nathanpb.mysticis.items.staff.crystals

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.data.staffData
import dev.nathanpb.mysticis.items.ItemBase
import dev.nathanpb.mysticis.items.staff.IContinueUsageStaffCrystal
import dev.nathanpb.mysticis.items.staff.ISingleUseStaffCrystal
import dev.nathanpb.mysticis.items.staff.IStaffCrystal
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
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
class ItemAirStaffCrystal : IContinueUsageStaffCrystal, ISingleUseStaffCrystal, IStaffCrystal, ItemBase() {
    override val color = 0xFFFA66

    override fun continueUseCost(user: LivingEntity, stack: ItemStack): ManaData {
        return if(!user.isSneaking) {
            ManaData(air = 1F)
        } else {
            super.continueUseCost(user, stack)
        }
    }

    override fun singleUseCost(user: LivingEntity, stack: ItemStack, block: Block?, entity: Entity?): ManaData {
        return if(user.isSneaking) {
            ManaData(air = 1.5F)
        } else {
            super.singleUseCost(user, stack, block, entity)
        }
    }

    override fun singleHitCost(user: LivingEntity, stack: ItemStack, block: Block?, entity: Entity?): ManaData {
        return if (user.isSneaking) {
            ManaData(air = 5F)
        } else {
            super.singleHitCost(user, stack, block, entity)
        }
    }

    override fun onSingleUse(user: LivingEntity, stack: ItemStack, block: Block?, entity: Entity?): TypedActionResult<ItemStack> {
        if(user.isSneaking) {
            if(user is PlayerEntity) {
                if (user.itemCooldownManager.isCoolingDown(stack.staffData.crystal?.item)) {
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
                    user.itemCooldownManager.set(stack.staffData.crystal?.item, 320)
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

    override fun onContinueUse(user: LivingEntity, stack: ItemStack): TypedActionResult<ItemStack> {
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

    override fun onSingleHit(
        user: LivingEntity,
        stack: ItemStack,
        block: Block?,
        entity: Entity?
    ): TypedActionResult<ItemStack> {
        return if (user.isSneaking) {
            // TODO sound and particle effects
            // TODO cooldown
            user.velocity = user.rotationVector.multiply(2.0, 1.0, 2.0)
            TypedActionResult.consume(stack)
        } else {
            super.onSingleHit(user, stack, block, entity)
        }
    }
}
