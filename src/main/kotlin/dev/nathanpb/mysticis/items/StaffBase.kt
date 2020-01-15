package dev.nathanpb.mysticis.items

import dev.nathanpb.mysticis.CREATIVE_TAB
import dev.nathanpb.mysticis.data.mana
import dev.nathanpb.mysticis.enums.ManaChangedCause
import dev.nathanpb.mysticis.event.mysticis.ManaChangedCallback
import dev.nathanpb.mysticis.event.mysticis.StaffHitCallback
import dev.nathanpb.mysticis.items.staff.IContinueUsageStaff
import dev.nathanpb.mysticis.items.staff.ISingleUseStaff
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.RangedWeaponItem
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
abstract class StaffBase : RangedWeaponItem(Settings().maxCount(1).group(CREATIVE_TAB)) {

    companion object {
        val staffHit = StaffHitCallback {
            val stack = it.getStackInHand(Hand.MAIN_HAND)
            val item = stack.item

            if (item is ISingleUseStaff) {
                val cost = item.singleUseCost(it, stack, null, null)
                val oldMana = it.mana
                val newMana = it.mana - cost

                if (!newMana.hasNegatives()) {
                    val result = item.onSingleHit(it, stack, null, null).result
                    if (result != ActionResult.FAIL) {
                        it.mana = newMana
                        ManaChangedCallback.EVENT
                            .invoker()
                            .onManaChanged(it, newMana, oldMana, ManaChangedCause.USED_BY_STAFF)
                    }
                }
            }
        }
    }

    override fun postHit(stack: ItemStack?, target: LivingEntity?, user: LivingEntity?): Boolean {
        if(user != null && stack != null && this is ISingleUseStaff) {
            val cost = singleUseCost(user, stack, null,target)
            val oldMana = user.mana
            val newMana = oldMana - cost

            if (!newMana.hasNegatives()) {
                val result = onSingleUse(user, stack, null, target).result

                if (result == ActionResult.CONSUME  && newMana != oldMana) {
                    user.mana = newMana
                    ManaChangedCallback.EVENT
                        .invoker()
                        .onManaChanged(user, newMana, oldMana, ManaChangedCause.USED_BY_STAFF)
                }
            }
        }
        return super.postHit(stack, target, user)
    }

    // TODO implement the staff hit thing with a preHit method

    override fun usageTick(world: World?, user: LivingEntity?, stack: ItemStack?, remainingUseTicks: Int) {
        if (user != null && stack != null && this is IContinueUsageStaff) {
            val cost = continueUseCost(user, stack)
            val oldMana = user.mana
            val newMana = oldMana - cost

            if (!newMana.hasNegatives()) {
                val result = onContinueUse(user, stack).result

                if (result == ActionResult.CONSUME && newMana != oldMana) {
                    user.mana = newMana
                    ManaChangedCallback.EVENT
                        .invoker()
                        .onManaChanged(user, newMana, oldMana, ManaChangedCause.USED_BY_STAFF)
                }
            }
        }
    }
}
