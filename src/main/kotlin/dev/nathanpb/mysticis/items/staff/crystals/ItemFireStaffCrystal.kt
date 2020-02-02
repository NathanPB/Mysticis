package dev.nathanpb.mysticis.items.staff.crystals

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.items.ItemBase
import dev.nathanpb.mysticis.items.staff.IContinueUsageStaffCrystal
import dev.nathanpb.mysticis.items.staff.IStaffCrystal
import dev.nathanpb.mysticis.staff.StaffContinueUseAirContext
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import net.minecraft.world.World
import kotlin.random.Random


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class ItemFireStaffCrystal : IContinueUsageStaffCrystal, IStaffCrystal, ItemBase() {
    override val color = 0xFF8000

    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        super.appendTooltip(stack, world, tooltip, context)
        tooltip?.add(LiteralText("§1Do you have the keys to the hotel?"))
        tooltip?.add(LiteralText("§1Cause I'm gonna string this motherfucker on §4fire"))
    }

    override fun continueUseCostAir(context: StaffContinueUseAirContext): ManaData {
        return if (context.user.isSneaking) {
            ManaData(fire = 1.5F)
        } else {
            ManaData(fire = 1F)
        }
    }

    override fun onContinueUseAir(context: StaffContinueUseAirContext): TypedActionResult<ItemStack> {
        val (user, stack) = context
        if(user.isSneaking) {
            (0..100).forEach { _ ->
                user.world.addParticle(
                    ParticleTypes.FLAME,
                    user.x + Random.nextInt(-2, 3) + (Random.nextFloat() - 0.5),
                    user.y + Random.nextInt(-3, 3)  + (Random.nextFloat() - 0.5),
                    user.z + Random.nextInt(-2, 3)  + (Random.nextFloat() - 0.5),
                    (Random.nextDouble() / 5) - .1,
                    (Random.nextDouble() / 5) - .1,
                    (Random.nextDouble() / 5) - .1
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
                user.world.getEntities(user, Box(user.blockPos).expand(4.0)) {
                    it is LivingEntity
                }.forEach {
                    it.damage(DamageSource.IN_FIRE, 7F)
                    it.fireTicks = 5 * 20
                }
            }
        } else {
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
        }
        return TypedActionResult.consume(stack)
    }
}
