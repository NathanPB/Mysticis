package dev.nathanpb.mysticis.items

import dev.nathanpb.mysticis.CREATIVE_TAB
import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.data.mana
import dev.nathanpb.mysticis.enums.ManaChangedCause
import dev.nathanpb.mysticis.event.mysticis.ManaChangedCallback
import dev.nathanpb.mysticis.event.mysticis.StaffSelfTriggeredCallback
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.RangedWeaponItem
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
abstract class StaffBase : RangedWeaponItem(Settings().maxCount(1).group(CREATIVE_TAB)) {

    companion object {
        val projectileTriggeredListener =
            StaffSelfTriggeredCallback {
                val item = it.getStackInHand(Hand.MAIN_HAND).item
                if (item is StaffBase) {
                    if(it is PlayerEntity) {
                        val oldMana = it.mana
                        val newMana = it.mana - item.manaConsumeSelf

                        if(newMana.hasNegatives()) {
                            return@StaffSelfTriggeredCallback
                        } else {
                            val tryCall = item.onTriggeredSelf(it, Hand.MAIN_HAND)
                            if(tryCall.result != ActionResult.FAIL) {
                                it.mana = newMana
                                ManaChangedCallback.EVENT
                                    .invoker()
                                    .onManaChanged(it, newMana, oldMana, ManaChangedCause.USED_BY_STAFF)
                            }
                        }
                    } else {
                        item.onTriggeredSelf(it, Hand.MAIN_HAND)
                    }
                }
            }
    }

    abstract val manaConsumeProjectile: ManaData
    abstract val manaConsumeSelf: ManaData
    abstract val manaConsumeArea: ManaData

    override fun getMaxUseTime(stack: ItemStack?): Int {
        return 20
    }

    override fun getProjectiles(): Predicate<ItemStack> {
        return Predicate { true }
    }

    override fun getUseAction(stack: ItemStack?): UseAction {
        return UseAction.BOW
    }

    abstract fun onTriggeredSelf(user: LivingEntity, hand: Hand): TypedActionResult<ItemStack>
    abstract fun onTriggeredArea(user: LivingEntity, hand: Hand): TypedActionResult<ItemStack>
    abstract fun onTriggeredProjectile(user: LivingEntity, hand: Hand) : TypedActionResult<ItemStack>

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        user?.let {
            val stack = user.getStackInHand(hand)

            val cost = if (user.isSneaking) manaConsumeArea else manaConsumeProjectile

            val oldMana = user.mana
            val newMana = user.mana - cost
            return if(!newMana.hasNegatives()) {

                val result = if(user.isSneaking) {
                    onTriggeredArea(user, hand ?: Hand.MAIN_HAND)
                } else {
                    user.setCurrentHand(hand)
                    onTriggeredProjectile(user, hand ?: Hand.MAIN_HAND)
                }

                if(oldMana != newMana && result.result != ActionResult.FAIL) {
                    user.mana = newMana
                    ManaChangedCallback.EVENT
                        .invoker()
                        .onManaChanged(user, newMana, oldMana, ManaChangedCause.USED_BY_STAFF)
                }

                result
            } else {
                TypedActionResult.fail(stack)
            }
        }

        return super.use(world, user, hand)
    }
}
