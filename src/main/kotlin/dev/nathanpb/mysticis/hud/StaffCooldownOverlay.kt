package dev.nathanpb.mysticis.hud

import com.mojang.blaze3d.systems.RenderSystem
import dev.nathanpb.mysticis.cooldown.staffCooldownManager
import dev.nathanpb.mysticis.data.staffData
import dev.nathanpb.mysticis.items.staff.IStaffCrystal
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormats
import net.minecraft.item.ItemStack


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class StaffCooldownOverlay {
    companion object {
        fun render(x: Float, y: Float, itemStack: ItemStack, player: ClientPlayerEntity) {
            itemStack.staffData.crystal?.item?.let { crystalItem ->
                if (crystalItem is IStaffCrystal) {
                    crystalItem.executors.values.flatten()
                    .filter {
                        it.effectId in player.staffCooldownManager
                    }.map {
                        it.representationColor
                    }.map {
                        Pair(it shr 16 and 255, Pair(it shr 8 and 255, it and 255))
                    }.forEachIndexed { index, color ->
                        val red = color.first
                        val green = color.second.first
                        val blue = color.second.second
                        val i = index * 2

                        RenderSystem.disableDepthTest()
                        RenderSystem.disableTexture()
                        RenderSystem.disableAlphaTest()
                        RenderSystem.disableBlend()
                        val tessellator = Tessellator.getInstance()
                        val buffer = tessellator.buffer

                        // Thank you for being private, ItemRenderer#renderGuiQuad
                        buffer.begin(7, VertexFormats.POSITION_COLOR)
                        buffer.vertex((x + 13.0), (y + i + 1.0), 0.0).color(red, green, blue, 255).next()
                        buffer.vertex((x + 13.0), (y + i + 3.0), 0.0).color(red, green, blue, 255).next()
                        buffer.vertex((x + 15.0), (y + i + 3.0), 0.0).color(red, green, blue, 255).next()
                        buffer.vertex((x + 15.0), (y + i + 1.0), 0.0).color(red, green, blue, 255).next()
                        Tessellator.getInstance().draw()

                        RenderSystem.enableBlend()
                        RenderSystem.enableAlphaTest()
                        RenderSystem.enableTexture()
                        RenderSystem.enableDepthTest()
                    }
                }
            }
        }
    }
}
