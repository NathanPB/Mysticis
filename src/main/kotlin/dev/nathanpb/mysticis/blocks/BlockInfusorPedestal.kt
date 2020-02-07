package dev.nathanpb.mysticis.blocks

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.entity.EntityContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class BlockInfusorPedestal : Block(FabricBlockSettings.of(Material.STONE).nonOpaque().dynamicBounds().strength(1.5F, 6F).build()) {
    override fun onPlaced(
        world: World?,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack?
    ) {
        if (world != null && pos != null && state != null) {
            val topPos = pos.offset(Direction.UP)

            if (world.canPlace(BLOCK_INFUSOR_PEDESTAL_BASE.defaultState, topPos, EntityContext.of(placer))) {
                world.setBlockState(pos.offset(Direction.UP), state)
                world.setBlockState(pos, BLOCK_INFUSOR_PEDESTAL_BASE.defaultState)
                BLOCK_INFUSOR_PEDESTAL_BASE.onPlaced(world, pos,  BLOCK_INFUSOR_PEDESTAL_BASE.defaultState, placer, itemStack)
            }
        }
        super.onPlaced(world, pos, state, placer, itemStack)
    }

    override fun onBreak(world: World?, pos: BlockPos?, state: BlockState?, player: PlayerEntity?) {
        if (world != null && pos != null) {
            val bottomPos = pos.offset(Direction.DOWN)
            val bottomState = world.getBlockState(bottomPos)

            if (bottomState.block is BlockInfusorPedestalBase) {
                world.breakBlock(bottomPos, false, player)
            }
        }
        super.onBreak(world, pos, state, player)
    }
}
