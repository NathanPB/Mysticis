package dev.nathanpb.mysticis.blocks

import dev.nathanpb.mysticis.CREATIVE_TAB
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
val BLOCK_STAFF_ASSEMBLER = BlockStaffAssembler()

fun registerBlocks() {
    hashMapOf(
        "staff_assembler" to BLOCK_STAFF_ASSEMBLER
    ).forEach { (id, block) ->
        val identifier = Identifier("mysticis", id)
        Registry.register(Registry.BLOCK, identifier, block)
        Registry.register(Registry.ITEM, identifier, BlockItem(block, Item.Settings().group(CREATIVE_TAB)))
    }
}
