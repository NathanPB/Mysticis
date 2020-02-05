package dev.nathanpb.mysticis.hud

import com.mojang.blaze3d.systems.RenderSystem
import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.data.mana
import dev.nathanpb.mysticis.data.manaAffinity
import dev.nathanpb.mysticis.enums.ManaColor
import dev.nathanpb.mysticis.event.gui.CrosshairRenderedCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.util.Identifier
import java.text.DecimalFormat
import kotlin.math.roundToInt


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

class AffinityHud : DrawableHelper(), CrosshairRenderedCallback {

    private val BARS = Identifier("textures/gui/bars.png")

    override fun render() {
        MinecraftClient.getInstance().player?.let {
            renderManaData(2, 0, it.mana)
            renderAffinity(102, 0, it.manaAffinity)
        }
    }

    private fun renderManaData(x: Int, y: Int, data: ManaData) {
        linkedMapOf(
            4 to data.air,
            2 to data.fire,
            1 to data.water,
            3 to data.nature,
            0 to data.magic,
            5 to data.dark
        ).entries.forEachIndexed { index, (colorIndex, value) ->
            RenderSystem.pushMatrix()
            RenderSystem.disableBlend()
            RenderSystem.enableRescaleNormal()
            RenderSystem.color4f(1F, 1F, 1F, 1F)
            RenderSystem.scalef(.35F, 1.25F, 1F)
            MinecraftClient.getInstance().textureManager.bindTexture(BARS)

            this.blit(x, (y + index * 6 + 2), 0, colorIndex * 5 * 2, 183, 5)

            val i = (value * 183) / 100
            if (i > 0) {
                this.blit(x, (y + index * 6 + 2), 0, colorIndex * 5 * 2 + 5, i.roundToInt(), 5)
            }

            RenderSystem.enableBlend()
            RenderSystem.disableRescaleNormal()
            RenderSystem.popMatrix()
        }
    }

    private fun renderAffinity(x: Int, y: Int, data: ManaData) {
        val format = DecimalFormat("#.##")
        arrayOf(
            Pair(ManaColor.AIR, data.air),
            Pair(ManaColor.FIRE, data.fire),
            Pair(ManaColor.WATER, data.water),
            Pair(ManaColor.NATURE, data.nature),
            Pair(ManaColor.MAGIC, data.magic),
            Pair(ManaColor.DARK, data.dark)
        ).forEachIndexed { index, (color, value) ->
            RenderSystem.pushMatrix()
            RenderSystem.scalef(.65F, .65F, 1F)
            drawString(
                MinecraftClient.getInstance().textRenderer,
                format.format(value),
                x,
                y + index * 11 + 6,
                color.rgb
            )
            RenderSystem.popMatrix()
        }
    }
}

