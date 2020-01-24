package dev.nathanpb.mysticis.items.staff.crystals

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.items.ItemBase
import dev.nathanpb.mysticis.items.staff.IContinueUsageStaffCrystal
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import kotlin.random.Random


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class ItemAirStaffCrystal : IContinueUsageStaffCrystal, ItemBase() {
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
            if (!user.world.isClient) {
                // TODO AoE implementation
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
