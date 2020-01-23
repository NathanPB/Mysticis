package dev.nathanpb.mysticis.recipe

import dev.nathanpb.mysticis.items.ITEM_MYSTICIS
import dev.nathanpb.mysticis.staff.StaffData
import dev.nathanpb.mysticis.staff.staffData
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.util.DefaultedList
import net.minecraft.util.Identifier
import net.minecraft.world.World


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class StaffAssemblerRecipe(
    private val id: Identifier,
    private val inputs: DefaultedList<Ingredient>,
    private val output: ItemStack
) : Recipe<Inventory> {

    override fun getId() = id

    override fun craft(inv: Inventory?): ItemStack = output.copy().also {
        it.staffData = StaffData(
            inv!!.getInvStack(0),
            inv.getInvStack(1),
            inv.getInvStack(2).let { crystal ->
                if (crystal.isEmpty) null else crystal
            }
        )
    }

    override fun getType() = STAFF_ASSEMBLER

    override fun fits(width: Int, height: Int) = true

    override fun getSerializer() = STAFF_ASSEMBLER_SERIALIZER

    override fun getOutput(): ItemStack = output

    override fun getRecipeKindIcon(): ItemStack = ITEM_MYSTICIS.stackForRender

    override fun getPreviewInputs() = inputs

    override fun matches(inv: Inventory?, world: World?): Boolean {
        return (inv?.invSize ?: 0) >= 3 && inputs.withIndex().all {
            it.value.test(inv?.getInvStack(it.index))
       }
    }
}
