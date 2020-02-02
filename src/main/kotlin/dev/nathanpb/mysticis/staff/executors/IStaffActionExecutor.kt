package dev.nathanpb.mysticis.staff.executors

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.data.mana
import dev.nathanpb.mysticis.enums.ManaChangedCause
import dev.nathanpb.mysticis.event.mysticis.ManaChangedCallback
import dev.nathanpb.mysticis.staff.*
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.TypedActionResult


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
interface IStaffActionExecutor<T : IStaffUsageContext> {

    val isContinue: Boolean
        get() = false

    fun cost(context: T) = ManaData()

    operator fun invoke(context: T): TypedActionResult<ItemStack>

    fun tryUsage(context: T): TypedActionResult<ItemStack> {
        context.crystalItem?.let {
            val user = context.user
            val cost = if (user.isCreative) {
                ManaData()
            } else {
                cost(context)
            }

            val oldMana = user.mana
            val newMana = oldMana - cost

            return if (!newMana.hasNegatives()) {
                val result = this(context)

                if (result.result == ActionResult.CONSUME  && newMana != oldMana) {
                    user.mana = newMana
                    ManaChangedCallback.EVENT
                        .invoker()
                        .onManaChanged(user, newMana, oldMana, ManaChangedCause.USED_BY_STAFF)
                }

                result
            } else {
                TypedActionResult.fail(context.stack)
            }
        }

        return TypedActionResult.pass(context.stack)
    }
}

fun <T : IStaffUsageContext> IStaffActionExecutor<T>?.tryUsageOrPass(context: T): TypedActionResult<ItemStack> {
    return this?.tryUsage(context) ?: TypedActionResult.pass(context.stack)
}

interface IStaffSingleUseBlockExecutor : IStaffActionExecutor<StaffSingleUseBlockContext>
interface IStaffSingleUseEntityExecutor : IStaffActionExecutor<StaffSingleUseEntityContext>
interface IStaffSingleUseAirExecutor : IStaffActionExecutor<StaffSingleUseAirContext>

interface IStaffSingleHitBlockExecutor : IStaffActionExecutor<StaffSingleHitBlockContext>
interface IStaffSingleHitEntityExecutor : IStaffActionExecutor<StaffSingleHitEntityContext>
interface IStaffSingleHitAirExecutor : IStaffActionExecutor<StaffSingleHitAirContext>

interface IStaffContinueUseAirExecutor : IStaffActionExecutor<StaffContinueUseAirContext> {
    override val isContinue: Boolean
        get() = true
}
