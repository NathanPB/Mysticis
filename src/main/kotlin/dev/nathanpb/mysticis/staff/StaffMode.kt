package dev.nathanpb.mysticis.staff

import dev.nathanpb.mysticis.InvalidStaffModeException
import dev.nathanpb.mysticis.acessors.IMysticisLivingEntity
import dev.nathanpb.mysticis.event.mysticis.StaffModeChangedCallback
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
enum class StaffMode(val id: Identifier) {
    COMBAT(Identifier("mysticis", "combat")),
    UTILITY(Identifier("mysticis", "utility"));

    companion object {
        /**
         * Searches for a [StaffMode] based on its ID
         *
         * @param id - The ID to search for
         * @return the StaffMode found
         * @throws InvalidStaffModeException if the provided [id] is invalid
         */
        fun from(id: Identifier) = values().firstOrNull(id::equals) ?: throw InvalidStaffModeException(id)

        /**
         * Searches for a [StaffMode] based on its ID. Returns [COMBAT] if the ID is invalid
         *
         * @see from
         */
        fun fromOrDefault(id: Identifier) = id.runCatching(Companion::from).getOrDefault(COMBAT)
    }
}

var LivingEntity.staffMode: StaffMode
    get() = (this as IMysticisLivingEntity).mysticisStaffMode
    set(mode) {
        (this as IMysticisLivingEntity).also {
            StaffModeChangedCallback.EVENT.invoker().onStaffModeChanged(this, mode, it.mysticisStaffMode)
        }.mysticisStaffMode = mode

    }
