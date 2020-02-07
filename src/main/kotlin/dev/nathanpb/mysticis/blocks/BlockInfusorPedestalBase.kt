package dev.nathanpb.mysticis.blocks

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class BlockInfusorPedestalBase : Block(
    FabricBlockSettings
        .of(Material.STONE)
        .strength(1.5F, 6F)
        .dropsNothing()
        .nonOpaque()
        .dynamicBounds()
        .build()
) {

    override fun onBreak(world: World?, pos: BlockPos?, state: BlockState?, player: PlayerEntity?) {
        val topPos = pos?.offset(Direction.UP)
        val topBlockState = world?.getBlockState(topPos)
        if (topPos != null && topBlockState?.block is BlockInfusorPedestal) {
            world.breakBlock(topPos, true, player)
        }
        super.onBreak(world, pos, state, player)
    }
}
