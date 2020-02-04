package dev.nathanpb.mysticis.cooldown

import dev.nathanpb.mysticis.acessors.IStaffCooldownManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class StaffCooldownManager(override val player: PlayerEntity) : AbstractCooldownManager<Identifier>(), IPlayerAttachedCooldown

val PlayerEntity.staffCooldownManager: StaffCooldownManager
    get() = (this as IStaffCooldownManager).staffCooldownManager
