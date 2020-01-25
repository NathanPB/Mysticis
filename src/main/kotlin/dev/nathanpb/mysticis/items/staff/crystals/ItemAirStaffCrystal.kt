package dev.nathanpb.mysticis.items.staff.crystals

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.data.staffData
import dev.nathanpb.mysticis.items.ItemBase
import dev.nathanpb.mysticis.items.staff.IContinueUsageStaffCrystal
import dev.nathanpb.mysticis.items.staff.IStaffCrystal
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import kotlin.random.Random


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class ItemAirStaffCrystal : IContinueUsageStaffCrystal, IStaffCrystal, ItemBase() {
    override val color = 0xFFFA66

    override fun continueUseCost(user: LivingEntity, stack: ItemStack): ManaData {
        return if(user.isSneaking) {
            ManaData(air = 1.5F)
        } else {
            ManaData(air = 1F)
        }
    }


    override fun onContinueUse(user: LivingEntity, stack: ItemStack): TypedActionResult<ItemStack> {
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
            }
        } else {
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
