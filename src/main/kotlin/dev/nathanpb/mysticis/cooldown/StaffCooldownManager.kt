package dev.nathanpb.mysticis.cooldown

import dev.nathanpb.mysticis.acessors.IStaffCooldownManager
import dev.nathanpb.mysticis.enums.StaffSingleUseType
import dev.nathanpb.mysticis.items.staff.IStaffCrystal
import net.minecraft.entity.player.PlayerEntity


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class StaffCooldownManager(override val player: PlayerEntity) : AbstractCooldownManager<StaffCooldownEntry>(), IPlayerAttachedCooldown {

    fun `in`(crystal: IStaffCrystal, use: StaffSingleUseType): Boolean {
        return StaffCooldownEntry(crystal, use) in this
    }

    operator fun set(crystal: IStaffCrystal, use: StaffSingleUseType, duration: Int) {
        this[StaffCooldownEntry(crystal, use)] = duration
    }

}

data class StaffCooldownEntry(val crystal: IStaffCrystal, val use: StaffSingleUseType)

val PlayerEntity.staffCooldownManager: StaffCooldownManager
    get() = (this as IStaffCooldownManager).staffCooldownManager
