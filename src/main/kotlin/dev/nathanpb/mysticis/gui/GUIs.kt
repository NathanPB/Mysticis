package dev.nathanpb.mysticis.gui

import dev.nathanpb.mysticis.blocks.entity.StaffAssemblerEntity
import dev.nathanpb.mysticis.containers.STAFF_ASSEMBLER_CONTAINER_IDENTIFIER
import dev.nathanpb.mysticis.containers.StaffAssemblerContainer
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

fun registerGuis() {
    ScreenProviderRegistry.INSTANCE.registerFactory(STAFF_ASSEMBLER_CONTAINER_IDENTIFIER) {
        syncId, _, player, buf ->
        val entity = player.world.getBlockEntity(buf.readBlockPos()) as StaffAssemblerEntity
        StaffAssemblerGui(StaffAssemblerContainer(syncId, player.inventory, entity))

    }
}
