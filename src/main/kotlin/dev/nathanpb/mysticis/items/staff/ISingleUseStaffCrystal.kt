package dev.nathanpb.mysticis.items.staff

import dev.nathanpb.mysticis.data.ManaData
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

// TODO fix air usages triggering on entity and block usages
// TODO fix single usages triggering on continue usages too
interface ISingleUseStaffCrystal {

    fun onSingleUseEntity(stack: ItemStack?, user: PlayerEntity, entity: LivingEntity, hand: Hand): TypedActionResult<ItemStack> {
        return TypedActionResult.pass(stack)
    }

    fun onSingleUseBlock(context: ItemUsageContext): TypedActionResult<ItemStack> {
        return TypedActionResult.pass(context.stack)
    }

    fun onSingleUseAir(stack: ItemStack, world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        return TypedActionResult.pass(stack)
    }

    fun onSingleHit(user: LivingEntity, stack: ItemStack, block: Block?, entity: Entity?): TypedActionResult<ItemStack> {
        return TypedActionResult.pass(stack)
    }

    fun singleUseEntityCost(stack: ItemStack?, user: PlayerEntity?, entity: LivingEntity?, hand: Hand?) = ManaData()

    fun singleUseBlockCost(context: ItemUsageContext) = ManaData()

    /**
     * WARNING: This is actually triggered on entity and block usages too.
     */
    fun singleUseAirCost(stack: ItemStack, world: World, user: PlayerEntity, hand: Hand) = ManaData()

    fun singleHitCost(user: LivingEntity, stack: ItemStack, block: Block?, entity: Entity?) = ManaData()
}
