package dev.nathanpb.mysticis.staff.executors

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.staff.StaffContinueUseAirContext
import dev.nathanpb.mysticis.staff.StaffSingleUseBlockContext
import net.minecraft.block.Blocks
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.inventory.BasicInventory
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.recipe.RecipeType
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import kotlin.random.Random


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

class FireCrystalContinueUseAir : IStaffContinueUseAirExecutor {
    override fun cost(context: StaffContinueUseAirContext): ManaData {
        return if (context.user.isSneaking) {
            ManaData(fire = 1.5F)
        } else {
            ManaData(fire = 1F)
        }
    }

    override fun invoke(context: StaffContinueUseAirContext): TypedActionResult<ItemStack> {
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

class FireCrystalBlockSmelt : IStaffSingleUseBlockExecutor {

    override fun cost(context: StaffSingleUseBlockContext) = ManaData(fire = 10F)

    override fun invoke(context: StaffSingleUseBlockContext): TypedActionResult<ItemStack> {
        val blockPos = context.blockPos
        val world = context.user.world
        val itemStack = ItemStack(world.getBlockState(context.blockPos).block.asItem())
        val match = world.recipeManager.getFirstMatch(RecipeType.SMELTING, BasicInventory(itemStack), world)

        return if (match.isPresent && !match.get().output.isEmpty) {
            val output = match.get().output
            val blockTurnsInto = if (output.item is BlockItem && !context.user.isSneaking) {
                (output.item as BlockItem).block.stateManager.defaultState
            } else {
                context.user.inventory.offerOrDrop(world, output.copy())
                if (world.isClient) {
                    world.playSound(
                        context.user,
                        blockPos,
                        SoundEvents.ENTITY_ITEM_PICKUP,
                        SoundCategory.BLOCKS,
                        1F, 2.5F
                    )
                }
                Blocks.AIR.defaultState
            }
            world.setBlockState(context.blockPos, blockTurnsInto)
            (0..16).forEach { _ ->
                world.addParticle(
                    ParticleTypes.FLAME,
                    blockPos.x.toDouble() + ((Math.random() - 0.5)),
                    blockPos.y.toDouble() + ((Math.random() - 0.5)),
                    blockPos.z.toDouble() + ((Math.random() - 0.5)),
                    0.0, 0.0, 0.0
                )
            }
            TypedActionResult.consume(context.stack)
        } else {
            TypedActionResult.fail(context.stack)
        }
    }
}
