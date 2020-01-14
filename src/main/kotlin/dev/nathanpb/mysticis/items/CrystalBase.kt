package dev.nathanpb.mysticis.items

import dev.nathanpb.mysticis.CREATIVE_TAB
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.loot.ConstantLootTableRange
import net.minecraft.loot.LootTables
import net.minecraft.loot.entry.ItemEntry
import kotlin.random.Random


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class CrystalBase : Item(Settings().group(CREATIVE_TAB)) {

    companion object {
        val crystals = mutableListOf<CrystalBase>()

        val registerLootTables = LootTableLoadingCallback { _, _, identifier, supplier, _ ->
            FabricLootPoolBuilder.builder()
                .withRolls(ConstantLootTableRange(
                    when (identifier) {
                        LootTables.ABANDONED_MINESHAFT_CHEST,
                        LootTables.JUNGLE_TEMPLE_CHEST,
                        LootTables.PILLAGER_OUTPOST_CHEST,
                        LootTables.SIMPLE_DUNGEON_CHEST,
                        LootTables.VILLAGE_TEMPLE_CHEST,
                        LootTables.WOODLAND_MANSION_CHEST,
                        LootTables.DESERT_PYRAMID_CHEST -> Random.nextInt(0, 10)

                        else -> return@LootTableLoadingCallback
                    }
                ))
                .also {
                    crystals.forEach { crystal ->
                        it.withEntry(ItemEntry.builder(crystal))
                    }
                    supplier.withPool(it)
                }
        }
    }

    init {
        crystals.add(this)
    }

    override fun hasEnchantmentGlint(stack: ItemStack?): Boolean {
        return true
    }
}
