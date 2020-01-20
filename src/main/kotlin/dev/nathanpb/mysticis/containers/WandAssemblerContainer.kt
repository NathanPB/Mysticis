package dev.nathanpb.mysticis.containers

import dev.nathanpb.mysticis.blocks.entity.WandAssemblerEntity
import dev.nathanpb.mysticis.utils.OutputSlot
import net.minecraft.container.Container
import net.minecraft.container.Slot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class WandAssemblerContainer(
    syncId: Int,
    val playerInventory: PlayerInventory,
    private val blockEntity: WandAssemblerEntity
) : Container(null, syncId) {

    init {
        (0..2).forEach { y ->
            addSlot(Slot(blockEntity, y, 56, 6 + (y * 20)))
        }

        addSlot(OutputSlot(blockEntity, 3, 116, 26))

        (0..2).forEach { i ->
            (0..8).forEach { m ->
                addSlot(Slot(playerInventory,
                            m + i * 9 + 9,
                        8 + m * 18,
                        84 + i * 18
                ))
            }
        }

        (0..8).forEach { i ->
            addSlot(Slot(playerInventory, i, 8 + i * 18, 142))
        }
    }

    override fun canUse(player: PlayerEntity?) = blockEntity.canPlayerUseInv(player)
}
