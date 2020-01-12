package dev.nathanpb.mysticis.hud

import dev.nathanpb.mysticis.data.ManaData
import dev.nathanpb.mysticis.data.mana
import dev.nathanpb.mysticis.data.manaAffinity
import dev.nathanpb.mysticis.enum.ManaColor
import dev.nathanpb.mysticis.event.gui.CrosshairRenderedCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

class AffinityHud : DrawableHelper(), CrosshairRenderedCallback {

    override fun render() {
        MinecraftClient.getInstance().player?.let {
            renderManaData(4, 4, it.mana)
            renderManaData(32, 4, it.manaAffinity)
        }
    }

    private fun renderManaData(x: Int, y: Int, data: ManaData) {
        arrayOf(
            Pair(ManaColor.AIR, data.air),
            Pair(ManaColor.FIRE, data.fire),
            Pair(ManaColor.WATER, data.water),
            Pair(ManaColor.NATURE, data.nature),
            Pair(ManaColor.MAGIC, data.magic),
            Pair(ManaColor.DARK, data.dark)
        ).forEachIndexed { index, it ->
            drawString(
                MinecraftClient.getInstance().textRenderer,
                String.format("%.2f", it.second),
                x,
                y + index * 10,
                it.first.rgb
            )
        }
    }
}

