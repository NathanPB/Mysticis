package dev.nathanpb.mysticis.hud

import dev.nathanpb.mysticis.event.gui.CrosshairRenderedCallback
import dev.nathanpb.mysticis.items.ItemStaff
import dev.nathanpb.mysticis.staff.staffMode
import dev.nathanpb.mysticis.utils.renderItemStack
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.util.Hand


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class StaffModeHud : DrawableHelper(), CrosshairRenderedCallback {
    override fun render() {
        MinecraftClient.getInstance().let { mc ->
            mc.player?.let { player ->
                player.getStackInHand(Hand.MAIN_HAND)?.let { stack ->
                    if (stack.item is ItemStaff) {
                        val x = (mc.window.scaledWidth * 90) / 100
                        val y = mc.window.scaledHeight
                        mc.itemRenderer.renderItemStack(stack, x.toFloat(), (y - 32).toFloat(), scale = 32F)
                        drawCenteredString(
                            mc.textRenderer,
                            player.staffMode.text().string,
                            x + 8, y - 10, 0
                        )
                    }
                }
            }
        }
    }
}
