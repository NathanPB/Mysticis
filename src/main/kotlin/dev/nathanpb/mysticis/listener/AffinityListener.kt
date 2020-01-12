package dev.nathanpb.mysticis.listener

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.data.manaAffinity
import dev.nathanpb.mysticis.event.entity.PlayerTickCallback
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

                if(Random.nextInt(0, 2499) == 0) {
                    modifiedAffinity = when (player.world.getBiome(BlockPos(player)).category) {
                        Biome.Category.EXTREME_HILLS -> affinity + ManaData(air = 2, nature = -1)

                        Biome.Category.MESA,
                        Biome.Category.DESERT,
                        Biome.Category.SAVANNA,
                        Biome.Category.NETHER -> affinity + ManaData(fire = 2, water = -1)

                        Biome.Category.BEACH,
                        Biome.Category.OCEAN,
                        Biome.Category.ICY,
                        Biome.Category.RIVER -> affinity + ManaData(water = 2, fire = -1)

                        Biome.Category.FOREST,
                        Biome.Category.JUNGLE,
                        Biome.Category.SWAMP,
                        Biome.Category.MUSHROOM -> affinity + ManaData(nature = 2, air = -1)

                        else -> affinity + if (Random.nextInt(0, 15) == 1)  ManaData(1, 1, 1, 1) else ManaData()
                    }
                }

                if(affinity != modifiedAffinity) {
                    player.manaAffinity = modifiedAffinity
                    println("Affinity of ${player.displayName.asString()} Changed: ${player.manaAffinity}")
                }
            }
        }
    }
}
