package dev.nathanpb.mysticis.items.staff.crystals

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.items.ItemBase
import dev.nathanpb.mysticis.items.staff.IContinueUsageStaffCrystal
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class ItemAirStaffCrystal : IContinueUsageStaffCrystal, ItemBase() {
    override val color = 0xFFFA66

    override fun continueUseCost(user: LivingEntity, stack: ItemStack): ManaData {
        return if(user.isSneaking) {
            ManaData(air = 1.5F)
        } else {
            ManaData(air = 1F)
        }
    }
}
