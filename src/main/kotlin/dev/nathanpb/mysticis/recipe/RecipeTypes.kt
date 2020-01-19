package dev.nathanpb.mysticis.recipe

import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeType
import net.minecraft.util.registry.Registry


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

lateinit var STAFF_ASSEMBLER: RecipeType<WandAssemblerRecipe>

private fun <T : Recipe<*>?> register(id: String) = Registry.register(
    Registry.RECIPE_TYPE,
    "mysticis:$id",
    object : RecipeType<T> {
        override fun toString() = "mysticis:$id"
    })


fun registerRecipeTypes() {
    STAFF_ASSEMBLER = register("staff_assembler")
}
