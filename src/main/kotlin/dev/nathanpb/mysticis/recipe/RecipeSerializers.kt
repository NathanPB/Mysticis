package dev.nathanpb.mysticis.recipe

import dev.nathanpb.mysticis.recipe.serializers.WandAssemblerRecipeSerializer
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.registry.Registry


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

lateinit var STAFF_ASSEMBLER_SERIALIZER: RecipeSerializer<WandAssemblerRecipe>

private fun <S: RecipeSerializer<T>, T: Recipe<*>>register(id: String, serializer: S) = Registry.register(
    Registry.RECIPE_SERIALIZER,
    "mysticis:$id",
    serializer
)

fun registerRecipeSerializers() {
    STAFF_ASSEMBLER_SERIALIZER = register("staff_assembler", WandAssemblerRecipeSerializer())
}
