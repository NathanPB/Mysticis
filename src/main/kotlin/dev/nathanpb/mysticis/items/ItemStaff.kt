package dev.nathanpb.mysticis.items

import dev.nathanpb.mysticis.CREATIVE_TAB
import dev.nathanpb.mysticis.data.staffData
import dev.nathanpb.mysticis.event.mysticis.StaffHitCallback
import dev.nathanpb.mysticis.items.staff.IStaffAttachment
import dev.nathanpb.mysticis.items.staff.IStaffCrystal
import dev.nathanpb.mysticis.staff.*
import dev.nathanpb.mysticis.staff.executors.*
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
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
class ItemStaff : RangedWeaponItem(Settings().maxCount(1).group(CREATIVE_TAB)) {
    companion object {
        val COLOR_PROVIDER = ItemColorProvider { stack, tintIndex ->
            val staffData = stack.staffData
            when(tintIndex) {
                0 -> (staffData.rod.item as IStaffAttachment).color
                1 -> (staffData.head.item as IStaffAttachment).color
                2 -> if(staffData.crystal?.item != null) {
                    (staffData.crystal.item as IStaffAttachment).color
                } else {
                    0xff00bb
                }
                else -> 0x00000000
            }
        }

        val staffHit = StaffHitCallback { player, stack ->

            if (player !is PlayerEntity) {
                return@StaffHitCallback
            }

            val context = StaffSingleHitAirContext(player, stack)
            context.crystalItem?.executors
                ?.filterIsInstance<IStaffSingleHitAirExecutor>()
                ?.firstOrNull()
                ?.tryUsage(context)
        }
    }

    override fun postProcessTag(tag: CompoundTag?): Boolean {
        if (tag?.getCompound("tag")?.contains("mysticis.staff", 10) == false) {
            tag.getCompound("tag").put("mysticis.staff", CompoundTag())
        }
        return super.postProcessTag(tag)
    }

    override fun getMaxUseTime(stack: ItemStack?) = 7200

    override fun getProjectiles() = Predicate<ItemStack> {
        true
    }

    override fun getUseAction(stack: ItemStack?): UseAction {
        return if (stack?.staffData?.crystal?.item?.let { crystalItem ->
             crystalItem is IStaffCrystal && crystalItem.hasContinueExecutor()
        } == true) {
            UseAction.BOW
        } else {
            UseAction.NONE
        }
    }

    override fun useOnBlock(itemUsageContext: ItemUsageContext?): ActionResult {
        itemUsageContext?.let {
            val context = StaffSingleUseBlockContext(itemUsageContext)
            return context.crystalItem
                ?.executors
                ?.filterIsInstance<IStaffSingleUseBlockExecutor>()
                ?.firstOrNull()
                ?.tryUsage(context)
                ?.result ?: ActionResult.FAIL
        }

        return super.useOnBlock(itemUsageContext)
    }

    override fun useOnEntity(stack: ItemStack?, user: PlayerEntity?, entity: LivingEntity?, hand: Hand?): Boolean {
        if (user != null && hand != null && entity != null && stack != null) {
            val context = StaffSingleUseEntityContext(user, stack, entity, hand)
            return context.crystalItem?.executors
                ?.filterIsInstance<IStaffSingleUseEntityExecutor>()
                ?.firstOrNull()
                ?.tryUsage(context)
                ?.result !== ActionResult.FAIL
        }
        return super.useOnEntity(stack, user, entity, hand)
    }

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        user?.setCurrentHand(hand)
        if (world != null && user != null && hand != null) {
            user.getStackInHand(hand)?.let { stack ->
                val context = StaffSingleUseAirContext(user, stack, hand)
                return context.crystalItem?.executors
                    ?.filterIsInstance<IStaffSingleUseAirExecutor>()
                    ?.firstOrNull()
                    ?.tryUsage(context) ?: TypedActionResult.fail(context.stack)
            }
        }
        return super.use(world, user, hand)
    }

    override fun postHit(stack: ItemStack?, target: LivingEntity?, user: LivingEntity?): Boolean {
        if (user is PlayerEntity && stack != null && target != null) {
            val context = StaffSingleHitEntityContext(user, stack, target)
            return context.crystalItem?.executors
                ?.filterIsInstance<IStaffSingleHitEntityExecutor>()
                ?.firstOrNull()
                ?.tryUsage(context)
                ?.result !== ActionResult.FAIL
        }
        return super.postHit(stack, target, user)
    }

    // TODO implement the staff hit thing with a preHit method

    override fun usageTick(world: World?, user: LivingEntity?, stack: ItemStack?, remainingUseTicks: Int) {
        if (user is PlayerEntity && stack != null) {
            val context = StaffContinueUseAirContext(user, stack)
            context.crystalItem?.executors
                ?.filterIsInstance<IStaffContinueUseAirExecutor>()
                ?.firstOrNull()
                ?.tryUsage(context)
        }
    }
}
