package dev.nathanpb.mysticis.staff.executors.fire

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.staff.StaffSingleUseBlockContext
import dev.nathanpb.mysticis.staff.executors.IStaffSingleUseBlockExecutor
import net.minecraft.block.Blocks
import net.minecraft.inventory.BasicInventory
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.recipe.RecipeType
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class FireBlockSmeltExecutor : IStaffSingleUseBlockExecutor {

    override val effectId = Identifier("mysticis", "block_smelt")
    override val representationColor = 0xFFFF00

    override fun accept(context: StaffSingleUseBlockContext) = true

    override fun cost(context: StaffSingleUseBlockContext) = ManaData(fire = 10F)

    override fun invoke(context: StaffSingleUseBlockContext): TypedActionResult<ItemStack> {
        val blockPos = context.blockPos
        val world = context.user.world
        val itemStack = ItemStack(world.getBlockState(context.blockPos).block.asItem())
        val match = world.recipeManager.getFirstMatch(RecipeType.SMELTING, BasicInventory(itemStack), world)

        return if (match.isPresent && !match.get().output.isEmpty) {
            val output = match.get().output
            val blockTurnsInto = if (output.item is BlockItem && !context.user.isSneaking) {
                (output.item as BlockItem).block.stateManager.defaultState
            } else {
                context.user.inventory.offerOrDrop(world, output.copy())
                if (world.isClient) {
                    world.playSound(
                        context.user,
                        blockPos,
                        SoundEvents.ENTITY_ITEM_PICKUP,
                        SoundCategory.BLOCKS,
                        1F, 2.5F
                    )
                }
                Blocks.AIR.defaultState
            }
            world.setBlockState(context.blockPos, blockTurnsInto)
            (0..16).forEach { _ ->
                world.addParticle(
                    ParticleTypes.FLAME,
                    blockPos.x.toDouble() + ((Math.random() - 0.5)),
                    blockPos.y.toDouble() + ((Math.random() - 0.5)),
                    blockPos.z.toDouble() + ((Math.random() - 0.5)),
                    0.0, 0.0, 0.0
                )
            }
            TypedActionResult.consume(context.stack)
        } else {
            TypedActionResult.fail(context.stack)
        }
    }
}
