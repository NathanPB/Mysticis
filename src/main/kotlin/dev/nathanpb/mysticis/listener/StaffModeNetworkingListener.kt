package dev.nathanpb.mysticis.listener

import dev.nathanpb.mysticis.PACKET_STAFF_MODE_CHANGED
import dev.nathanpb.mysticis.event.mysticis.StaffModeChangedCallback
import dev.nathanpb.mysticis.event.server.PlayerConnectCallback
import dev.nathanpb.mysticis.staff.staffMode
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.PacketByteBuf


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

val onStaffModeChanged = StaffModeChangedCallback { entity, mode, _ ->
    if (entity is ServerPlayerEntity) {
        val packet = PacketByteBuf(Unpooled.buffer()).apply {
            writeIdentifier(mode.id)
        }
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(entity, PACKET_STAFF_MODE_CHANGED, packet)
    }
}

val onStaffModeChangedPlayerConnect = PlayerConnectCallback { _, player ->
    val packet = PacketByteBuf(Unpooled.buffer()).apply {
        writeIdentifier(player.staffMode.id)
    }
    ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_STAFF_MODE_CHANGED, packet)
}
