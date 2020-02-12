package dev.nathanpb.mysticis.blocks

import dev.nathanpb.mysticis.blocks.entity.InfusorPedestalEntity
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.entity.EntityContext
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
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
class BlockInfusorPedestal :
    Block(FabricBlockSettings.of(Material.STONE).nonOpaque().dynamicBounds().strength(1.5F, 6F).build()),
    BlockEntityProvider {

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

            (world.getBlockEntity(pos) as? InfusorPedestalEntity)?.let { entity ->
                if (!world.isClient && !entity.isInvEmpty) {
                    world.spawnEntity(ItemEntity(
                        world,
                        pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
                        entity.pop()
                    ))
                }
            }
        }
        super.onBreak(world, pos, state, player)
    }

    override fun onUse(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult {
        if (world != null && pos != null && player != null && hand != null) {
            world.getBlockEntity(pos)?.let { blockEntity ->
                if (blockEntity is InfusorPedestalEntity) {
                    if (blockEntity.isInvEmpty) {
                        player.getStackInHand(hand)?.let { stack ->
                            blockEntity.offer(stack.copy().also { it.count = 1 })
                            playPopSound(world, pos)
                            if (!player.isCreative) {
                                stack.decrement(1)
                            }
                            return ActionResult.SUCCESS
                        }
                    } else {
                        pos.apply {
                            world.spawnEntity(ItemEntity(world, x.toDouble(), y.toDouble(), z.toDouble(), blockEntity.pop()))
                        }
                        playPopSound(world, pos)
                        return ActionResult.SUCCESS
                    }
                }
            }
        }
        return super.onUse(state, world, pos, player, hand, hit)
    }

    private fun playPopSound(world: World, pos: BlockPos) {
        if (!world.isClient) {
            pos.apply {
                world.playSound(
                    null,
                    x.toDouble(), y.toDouble(), z.toDouble(),
                    SoundEvents.ENTITY_ITEM_PICKUP,
                    SoundCategory.BLOCKS,
                    1F, 2.5F
                )
            }
        }
    }

    override fun createBlockEntity(view: BlockView?) = InfusorPedestalEntity()
}
