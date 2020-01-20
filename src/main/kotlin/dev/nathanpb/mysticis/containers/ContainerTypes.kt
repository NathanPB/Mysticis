package dev.nathanpb.mysticis.containers

import dev.nathanpb.mysticis.blocks.entity.WandAssemblerEntity
import net.fabricmc.fabric.api.container.ContainerFactory
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.container.Container
import net.minecraft.util.Identifier


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

val WAND_ASSEMBLER_CONTAINER_IDENTIFIER = Identifier("mysticis", "wand_assembler")

private fun register(id: Identifier, factory: ContainerFactory<Container>) {
    ContainerProviderRegistry.INSTANCE.registerFactory(id, factory)
}

fun registerContainers() {
    register(WAND_ASSEMBLER_CONTAINER_IDENTIFIER, ContainerFactory {
        syncId, _, player, buf ->
        WandAssemblerContainer(
            syncId, player.inventory,
            (player.world.getBlockEntity(buf.readBlockPos()) as WandAssemblerEntity)
        )
    })
}
