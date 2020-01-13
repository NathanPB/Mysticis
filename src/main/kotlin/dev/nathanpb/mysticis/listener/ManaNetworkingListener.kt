package dev.nathanpb.mysticis.listener

import dev.nathanpb.mysticis.AFFINITY_CHANGED
import dev.nathanpb.mysticis.MANA_CHANGED
import dev.nathanpb.mysticis.event.mysticis.AffinityChangedCallback
import dev.nathanpb.mysticis.event.mysticis.ManaChangedCallback
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.PacketByteBuf


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

val sendAffinity = AffinityChangedCallback { entity, newMana, _, _ ->
    if (entity is PlayerEntity) {
        val packet = PacketByteBuf(Unpooled.buffer()).writeCompoundTag(newMana.mkCompoundTag())
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(entity, AFFINITY_CHANGED, packet)
    }
}

val sendMana = ManaChangedCallback { entity, newMana, _, _ ->
    if (entity is PlayerEntity) {
       val packet = PacketByteBuf(Unpooled.buffer()).writeCompoundTag(newMana.mkCompoundTag())
       ServerSidePacketRegistry.INSTANCE.sendToPlayer(entity, MANA_CHANGED, packet)
    }
}