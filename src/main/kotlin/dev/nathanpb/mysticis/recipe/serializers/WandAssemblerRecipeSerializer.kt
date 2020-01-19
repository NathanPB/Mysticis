package dev.nathanpb.mysticis.recipe.serializers

import com.google.gson.JsonObject
import dev.nathanpb.mysticis.recipe.WandAssemblerRecipe
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.DefaultedList
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf
import net.minecraft.util.registry.Registry


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class WandAssemblerRecipeSerializer : RecipeSerializer<WandAssemblerRecipe> {
    override fun write(buf: PacketByteBuf?, recipe: WandAssemblerRecipe?) {
        if (buf != null && recipe != null) {
            recipe.previewInputs.forEach {
                it.write(buf)
            }
            buf.writeItemStack(recipe.output)
        }
    }

    override fun read(id: Identifier?, json: JsonObject?): WandAssemblerRecipe {
        val input = DefaultedList.ofSize(3, Ingredient.EMPTY)
        json?.let {
            arrayOf("rod", "head", "crystal").forEachIndexed { index, key ->
                json[key]?.let {
                    if(it is JsonObject) {
                        input[index] = Ingredient.fromJson(it)
                    }
                }
            }
        }
        val output: ItemStack = json?.getAsJsonPrimitive("result")?.asString?.let { itemId ->
            val item = Registry.ITEM.getOrEmpty(Identifier(itemId))
            if(item.isPresent) {
                ItemStack(item.get())
            } else null
        } ?: ItemStack.EMPTY


        return WandAssemblerRecipe(id!!, input, output)
    }

    override fun read(id: Identifier?, buf: PacketByteBuf?): WandAssemblerRecipe {
        val input = DefaultedList.ofSize(3, Ingredient.EMPTY)
        buf?.let {
            0.until(input.size).forEach { index ->
                input[index] = Ingredient.fromPacket(buf)
            }
        }

        val output = buf?.readItemStack()

        return WandAssemblerRecipe(id!!, input, output ?: ItemStack.EMPTY)
    }

}
