package dev.nathanpb.mysticis.blocks

import dev.nathanpb.mysticis.blocks.entity.StaffAssemblerEntity
import dev.nathanpb.mysticis.containers.STAFF_ASSEMBLER_CONTAINER_IDENTIFIER
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World

/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

class BlockStaffAssembler :
    HorizontalFacingBlock(FabricBlockSettings.of(Material.WOOD).nonOpaque().dynamicBounds().build()),
    //BlockWithEntity(FabricBlockSettings.of(Material.WOOD).nonOpaque().dynamicBounds().build()),
    BlockEntityProvider {

    init {
        defaultState = stateManager.defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
    }

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        return defaultState.with(Properties.HORIZONTAL_FACING, ctx?.playerFacing?.opposite)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        super.appendProperties(builder)
        builder?.add(Properties.HORIZONTAL_FACING)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state.get(FACING) as Direction))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(FACING) as Direction))
    }

    override fun onUse(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult {
        world?.getBlockEntity(pos)?.let { blockEntity ->
            return if (blockEntity is StaffAssemblerEntity) {
                if(player is ServerPlayerEntity) {
                    ContainerProviderRegistry.INSTANCE.openContainer(STAFF_ASSEMBLER_CONTAINER_IDENTIFIER, player) {
                        it.writeBlockPos(pos)
                    }
                }
                ActionResult.SUCCESS
            } else ActionResult.FAIL
        }
        return ActionResult.PASS
    }

    override fun createBlockEntity(view: BlockView?) = StaffAssemblerEntity()
}
