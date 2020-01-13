package dev.nathanpb.mysticis.listener

import dev.nathanpb.mysticis.data.mana
import dev.nathanpb.mysticis.data.manaAffinity
import dev.nathanpb.mysticis.enums.ManaChangedCause
import dev.nathanpb.mysticis.event.entity.PlayerTickCallback
import dev.nathanpb.mysticis.event.mysticis.ManaChangedCallback
import net.minecraft.entity.player.PlayerEntity


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class ManaRegenListener : PlayerTickCallback {
    override fun tick(player: PlayerEntity?) {
        if(player?.world?.isClient == false) {
            val prevMana = player.mana
            val newMana = (prevMana + player.manaAffinity.mapValues { value, _ ->
                value / 1000
            }).limitMin(0F).limitMax(100F)

            if(newMana != prevMana) {
                player.mana = newMana
                ManaChangedCallback.EVENT.invoker().onManaChanged(player, newMana, prevMana, ManaChangedCause.PASSIVE_REGEN)
            }
        }
    }
}
