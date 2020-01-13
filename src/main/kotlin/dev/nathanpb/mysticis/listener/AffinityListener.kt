package dev.nathanpb.mysticis.listener

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.data.manaAffinity
import dev.nathanpb.mysticis.enums.ManaChangedCause
import dev.nathanpb.mysticis.event.entity.PlayerTickCallback
import dev.nathanpb.mysticis.event.mysticis.AffinityChangedCallback
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import kotlin.random.Random

/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

class AffinityListener : PlayerTickCallback {
    override fun tick(player: PlayerEntity?) {
        if(player?.world?.isClient == false) {
            player.manaAffinity.let { affinity ->
                var modifiedAffinity = affinity

                if(Random.nextInt(0, 999) == 0) {
                    modifiedAffinity = when (player.world.getBiome(BlockPos(player)).category) {
                        Biome.Category.EXTREME_HILLS -> affinity + ManaData(air = 1F, water = -.5F)

                        Biome.Category.MESA,
                        Biome.Category.DESERT,
                        Biome.Category.SAVANNA,
                        Biome.Category.NETHER -> affinity + ManaData(fire = 1F, nature = -.5F)

                        Biome.Category.BEACH,
                        Biome.Category.OCEAN,
                        Biome.Category.ICY,
                        Biome.Category.RIVER -> affinity + ManaData(water = 1F, fire = -.5F)

                        Biome.Category.FOREST,
                        Biome.Category.JUNGLE,
                        Biome.Category.SWAMP,
                        Biome.Category.MUSHROOM -> affinity + ManaData(nature = 1F, air = -.5F)

                        else -> affinity + ManaData(.2F, .2F, .2F, .2F)
                    }
                }

                if(affinity != modifiedAffinity) {
                    player.manaAffinity = modifiedAffinity
                    AffinityChangedCallback.EVENT.invoker().onAffinityChanged(player, affinity, modifiedAffinity, ManaChangedCause.PASSIVE_REGEN)
                }
            }
        }
    }
}
