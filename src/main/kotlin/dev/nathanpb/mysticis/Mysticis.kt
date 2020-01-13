package dev.nathanpb.mysticis

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.data.mana
import dev.nathanpb.mysticis.data.manaAffinity
import dev.nathanpb.mysticis.event.entity.PlayerTickCallback
import dev.nathanpb.mysticis.event.gui.CrosshairRenderedCallback
import dev.nathanpb.mysticis.event.mysticis.AffinityChangedCallback
import dev.nathanpb.mysticis.event.mysticis.ManaChangedCallback
import dev.nathanpb.mysticis.event.server.PlayerConnectCallback
import dev.nathanpb.mysticis.hud.AffinityHud
import dev.nathanpb.mysticis.listener.*
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.client.MinecraftClient

/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

@Suppress("unused")
fun init() {
    PlayerTickCallback.EVENT.register(AffinityListener())
    PlayerTickCallback.EVENT.register(ManaRegenListener())
    CrosshairRenderedCallback.EVENT.register(AffinityHud())
    AffinityChangedCallback.EVENT.register(sendAffinity)
    ManaChangedCallback.EVENT.register(sendMana)
    PlayerConnectCallback.EVENT.register(manaPlayerConnect)
}

@Suppress("unused")
fun initClient() {
    ClientSidePacketRegistry.INSTANCE.register(PACKET_AFFINITY_CHANGED) { context, buf ->
        buf.readCompoundTag()?.let { tag ->
            if (ManaData.isValidTag(tag)) {
                context.taskQueue.execute {
                    MinecraftClient.getInstance().player?.manaAffinity = ManaData.loadFromTag(tag)
                }
            }
        }
    }

    ClientSidePacketRegistry.INSTANCE.register(PACKET_MANA_CHANGED) { context, buf ->
        buf.readCompoundTag()?.let { tag ->
            if (ManaData.isValidTag(tag)) {
                context.taskQueue.execute {
                    MinecraftClient.getInstance().player?.mana = ManaData.loadFromTag(tag)
                }
            }
        }
    }
}
