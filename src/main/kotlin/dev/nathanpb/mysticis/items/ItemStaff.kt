package dev.nathanpb.mysticis.items

import dev.nathanpb.mysticis.CREATIVE_TAB
import dev.nathanpb.mysticis.data.mana
import dev.nathanpb.mysticis.enums.ManaChangedCause
import dev.nathanpb.mysticis.event.mysticis.ManaChangedCallback
import dev.nathanpb.mysticis.event.mysticis.StaffHitCallback
import dev.nathanpb.mysticis.items.staff.IContinueUsageStaffCrystal
import dev.nathanpb.mysticis.items.staff.ISingleUseStaffCrystal
import dev.nathanpb.mysticis.items.staff.IStaffAttachment
import dev.nathanpb.mysticis.staff.staffData
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.RangedWeaponItem
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World
import java.util.function.Predicate


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class ItemStaff : RangedWeaponItem(Item.Settings().maxCount(1).group(CREATIVE_TAB)) {
    companion object {
        val COLOR_PROVIDER = ItemColorProvider { stack, tintIndex ->
            val staffData = stack.staffData
            when(tintIndex) {
                0 -> (staffData.rod.item as IStaffAttachment).color
                1 -> (staffData.head.item as IStaffAttachment).color
                2 -> if(staffData.crystal != null) {
                    (staffData.crystal as IStaffAttachment).color
                } else {
                    0xff00bb
                }
                else -> 0x00000000
            }
        }

        val staffHit = StaffHitCallback {
            val stack = it.getStackInHand(Hand.MAIN_HAND)
            val item = stack.item

            if (item is ISingleUseStaffCrystal) {
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

    override fun postProcessTag(tag: CompoundTag?): Boolean {
        tag?.getCompound("tag")?.put("mysticis.staff", CompoundTag())
        return super.postProcessTag(tag)
    }

    override fun getMaxUseTime(stack: ItemStack?) = 7200

    override fun getProjectiles() = Predicate<ItemStack> {
        true
    }

    override fun getUseAction(stack: ItemStack?): UseAction {
        return if (stack?.staffData?.crystal?.item is IContinueUsageStaffCrystal) {
            UseAction.BOW
        } else {
            UseAction.NONE
        }
    }

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        user?.setCurrentHand(Hand.MAIN_HAND)
        return super.use(world, user, hand)
    }

    override fun postHit(stack: ItemStack?, target: LivingEntity?, user: LivingEntity?): Boolean {
        user?.let {
            stack?.staffData?.crystal?.item?.let { crystalItem ->
                if (crystalItem is ISingleUseStaffCrystal) {
                    val cost = crystalItem.singleUseCost(user, stack, null,target)
                    val oldMana = user.mana
                    val newMana = oldMana - cost

                    if (!newMana.hasNegatives()) {
                        val result = crystalItem.onSingleUse(user, stack, null, target).result

                        if (result == ActionResult.CONSUME  && newMana != oldMana) {
                            user.mana = newMana
                            ManaChangedCallback.EVENT
                                .invoker()
                                .onManaChanged(user, newMana, oldMana, ManaChangedCause.USED_BY_STAFF)
                        }
                    }
                }
            }
        }
        return super.postHit(stack, target, user)
    }

    // TODO implement the staff hit thing with a preHit method

    override fun usageTick(world: World?, user: LivingEntity?, stack: ItemStack?, remainingUseTicks: Int) {
        user?.let {
            stack?.staffData?.crystal?.item?.let { crystalItem ->
                if (crystalItem is IContinueUsageStaffCrystal) {
                    val cost = crystalItem.continueUseCost(user, stack)
                    val oldMana = user.mana
                    val newMana = oldMana - cost

                    if (!newMana.hasNegatives()) {
                        val result = crystalItem.onContinueUse(user, stack).result

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
    }
}
