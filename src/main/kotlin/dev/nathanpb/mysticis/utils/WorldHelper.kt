package dev.nathanpb.mysticis.utils

import net.minecraft.block.FluidFillable
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.BaseFluid
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.tag.FluidTags
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

fun World.placeFluid(player: PlayerEntity, pos: BlockPos, fluid: BaseFluid): Boolean {
    val state = world.getBlockState(pos)
    val material = state.material
    val canPlace = state.canBucketPlace(fluid)

    return if(state.block is FluidFillable) {
        (state.block as? FluidFillable)?.let { fillable ->
            fillable.canFillWithFluid(this, pos, state, fluid) && fillable.tryFillWithFluid(this, pos, state, fluid.getStill(false))
        } ?: false
    } else if (this.dimension.doesWaterVaporize() && fluid.matches(FluidTags.WATER)) {
        world.playSound(
            player, pos,
            SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS,
            0.5f, 2.6f + (world.random.nextFloat() - world.random.nextFloat()) * 0.8f
        )

        (0..7).forEach { _ ->
            world.addParticle(
                ParticleTypes.LARGE_SMOKE,
                pos.x.toDouble() + Math.random(),
                pos.y.toDouble() + Math.random(),
                pos.z.toDouble() + Math.random(),
                0.0, 0.0, 0.0
            )
        }
        false
    } else {
        if (!world.isClient && canPlace && !material.isLiquid) {
            world.breakBlock(pos, true)
        }

        world.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f)
        world.setBlockState(pos, fluid.defaultState.blockState, 11)
        true
    }
}
