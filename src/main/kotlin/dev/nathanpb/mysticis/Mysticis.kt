package dev.nathanpb.mysticis

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.data.mana
import dev.nathanpb.mysticis.data.manaAffinity
import dev.nathanpb.mysticis.event.entity.PlayerTickCallback
import dev.nathanpb.mysticis.event.gui.CrosshairRenderedCallback
import dev.nathanpb.mysticis.event.mysticis.AffinityChangedCallback
import dev.nathanpb.mysticis.event.mysticis.ManaChangedCallback
import dev.nathanpb.mysticis.event.mysticis.StaffSelfTriggeredCallback
import dev.nathanpb.mysticis.event.server.PlayerConnectCallback
import dev.nathanpb.mysticis.hud.AffinityHud
import dev.nathanpb.mysticis.items.CrystalBase
import dev.nathanpb.mysticis.items.StaffBase
import dev.nathanpb.mysticis.items.registerItems
import dev.nathanpb.mysticis.listener.*
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.Identifier

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
    LootTableLoadingCallback.EVENT.register(CrystalBase.registerLootTables)
    StaffSelfTriggeredCallback.EVENT.register(StaffBase.projectileTriggeredListener)

    ServerSidePacketRegistry.INSTANCE.register(PACKET_STAFF_PROJECTILE_TRIGGERED) { context, _ ->
        context.taskQueue.execute {
            val item = context.player.getStackInHand(Hand.MAIN_HAND).item
            if (context.player.isSneaking && item is StaffBase) {
                StaffSelfTriggeredCallback.EVENT.invoker().onTriggered(context.player)
            }
        }
    }

    registerItems()
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

val CREATIVE_TAB = FabricItemGroupBuilder.build(Identifier("mysticis", "tab_mysticis")) {
    ItemStack(dev.nathanpb.mysticis.items.ITEM_MYSTICIS)
}
