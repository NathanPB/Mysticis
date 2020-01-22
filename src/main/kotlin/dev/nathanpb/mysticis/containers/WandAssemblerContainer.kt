package dev.nathanpb.mysticis.containers

import dev.nathanpb.mysticis.blocks.entity.WandAssemblerEntity
import dev.nathanpb.mysticis.items.staff.IStaffCrystal
import dev.nathanpb.mysticis.items.staff.IStaffHead
import dev.nathanpb.mysticis.items.staff.IStaffRod
import dev.nathanpb.mysticis.recipe.STAFF_ASSEMBLER
import dev.nathanpb.mysticis.utils.InputRestrictedSlot
import dev.nathanpb.mysticis.utils.OutputSlot
import net.minecraft.client.network.packet.GuiSlotUpdateS2CPacket
import net.minecraft.container.Container
import net.minecraft.container.ContainerListener
import net.minecraft.container.CraftingContainer
import net.minecraft.container.Slot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeFinder
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.DefaultedList


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
) : CraftingContainer<Inventory>(null, syncId) {

    init {
        addSlot(InputRestrictedSlot(blockEntity, 0, 56, 6) {
            it?.item is IStaffRod
        })
        addSlot(InputRestrictedSlot(blockEntity, 1, 56, 26) {
            it?.item is IStaffHead
        })
        addSlot(InputRestrictedSlot(blockEntity, 2, 56, 26) {
            it?.item is IStaffCrystal
        })

        addSlot(object: OutputSlot(blockEntity, 3, 116, 26) {
            override fun onTakeItem(playerEntity_1: PlayerEntity?, itemStack_1: ItemStack?): ItemStack? {
                (0..2).forEach {
                    this@WandAssemblerContainer.slotList[it]?.let { slot ->
                        slot.stack.decrement(1)
                    }
                }
                return super.onTakeItem(playerEntity_1, itemStack_1)
            }
        })

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

        addListener(object: ContainerListener {
            override fun onContainerRegistered(container: Container?, defaultedList: DefaultedList<ItemStack>?) {
                // Not Implemented
            }

            override fun onContainerPropertyUpdate(container: Container?, propertyId: Int, i: Int) {
                // Not Implemented
            }

            override fun onContainerSlotUpdate(container: Container?, slotId: Int, itemStack: ItemStack?) {
                if (container == this@WandAssemblerContainer && slotId <= 3) {
                   this@WandAssemblerContainer.onContentChanged(blockEntity)
                }
            }
        })
    }

    override fun onContentChanged(inventory: Inventory?) {
        super.onContentChanged(inventory)
        blockEntity.markDirty()

        if (blockEntity.world?.isClient == false) {
            blockEntity.world?.server?.let { server ->
                val stack = server.recipeManager
                    .getFirstMatch(STAFF_ASSEMBLER, blockEntity, blockEntity.world)
                    .orElse(null)?.craft(blockEntity) ?: ItemStack.EMPTY

                getSlot(craftingResultSlotIndex).stack = stack
                (playerInventory.player as ServerPlayerEntity)
                    .networkHandler
                    .sendPacket(GuiSlotUpdateS2CPacket(syncId, craftingResultSlotIndex, stack))
            }
        }
    }

    override fun canUse(player: PlayerEntity?) = blockEntity.canPlayerUseInv(player)

    override fun populateRecipeFinder(recipeFinder: RecipeFinder?) {
        recipeFinder?.let {
            this.blockEntity.items.forEach(recipeFinder::addItem)
        }
    }

    override fun getCraftingResultSlotIndex() = 3

    override fun getCraftingWidth() = 1

    override fun getCraftingHeight() = 3

    override fun matches(recipe: Recipe<in Inventory>?) = recipe?.matches(blockEntity, blockEntity.world) == true

    override fun getCraftingSlotCount() = 3

    override fun clearCraftingSlots() {
        blockEntity.clear()
    }
}
