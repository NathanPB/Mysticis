package dev.nathanpb.mysticis.staff

import com.sun.javaws.exceptions.InvalidArgumentException
import dev.nathanpb.mysticis.data.staffData
import dev.nathanpb.mysticis.items.staff.IStaffCrystal
import dev.nathanpb.mysticis.staff.executors.IStaffActionExecutor
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
interface IStaffUsageContext {
    val user: PlayerEntity
    val stack: ItemStack
    val crystalItem: IStaffCrystal?
        get() = stack.staffData.crystal?.item as IStaffCrystal

}

inline fun <reified T: IStaffActionExecutor<*>> IStaffUsageContext.findFirstExecutor() : T? {
    return crystalItem?.executors?.filterIsInstance<T>()?.firstOrNull()
}

data class StaffSingleUseBlockContext(
    override val user: PlayerEntity,
    override val stack: ItemStack,
    val hand: Hand,
    val hitPos: Vec3d,
    val blockPos: BlockPos,
    val side: Direction,
    val hitsInsideBlock: Boolean
) : IStaffUsageContext {
    constructor(usageContext: ItemUsageContext) : this(
        usageContext.player ?: throw InvalidArgumentException(arrayOf("Player cannot be null")),
        usageContext.stack,
        usageContext.hand,
        usageContext.hitPos,
        usageContext.blockPos,
        usageContext.side,
        usageContext.hitsInsideBlock()
    )
}

data class StaffSingleUseEntityContext(
    override val user: PlayerEntity,
    override val stack: ItemStack,
    val entity: Entity,
    val hand: Hand
) : IStaffUsageContext

data class StaffSingleUseAirContext(
    override val user: PlayerEntity,
    override val stack: ItemStack,
    val hand: Hand
) : IStaffUsageContext

data class StaffSingleHitEntityContext(
    override val user: PlayerEntity,
    override val stack: ItemStack,
    val target: Entity
) : IStaffUsageContext

data class StaffSingleHitBlockContext(
    override val user: PlayerEntity,
    override val stack: ItemStack,
    val blockPos: BlockPos
) : IStaffUsageContext

data class StaffSingleHitAirContext(
    override val user: PlayerEntity,
    override val stack: ItemStack
) : IStaffUsageContext

data class StaffContinueUseAirContext(
    override val user: PlayerEntity,
    override val stack: ItemStack
) : IStaffUsageContext
