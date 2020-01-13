package dev.nathanpb.mysticis.items

import dev.nathanpb.mysticis.CREATIVE_TAB
import dev.nathanpb.mysticis.event.mysticis.StaffSelfTriggeredCallback
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
abstract class StaffBase : Item(Settings().maxCount(1).group(CREATIVE_TAB)) {

    companion object {
        val projectileTriggeredListener =
            StaffSelfTriggeredCallback {
                val item = it.getStackInHand(Hand.MAIN_HAND).item
                if (item is StaffBase) {
                    item.onTriggeredSelf(it, Hand.MAIN_HAND)
                }
            }
    }

    abstract fun onTriggeredProjectile(user: LivingEntity, hand: Hand): TypedActionResult<ItemStack>
    abstract fun onTriggeredSelf(user: LivingEntity, hand: Hand): TypedActionResult<ItemStack>
    abstract fun onTriggeredArea(user: LivingEntity, hand: Hand): TypedActionResult<ItemStack>

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        user?.let {
            return if (user.isSneaking) {
                onTriggeredArea(user, hand ?: Hand.MAIN_HAND)
            } else {
                onTriggeredProjectile(user, hand ?: Hand.MAIN_HAND)
            }
        }

        return super.use(world, user, hand);
    }
}
