package dev.nathanpb.mysticis.gui

import com.mojang.blaze3d.platform.GlStateManager
import dev.nathanpb.mysticis.containers.WandAssemblerContainer
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class StaffAssemblerGui(
    container: WandAssemblerContainer
) : AbstractContainerScreen<WandAssemblerContainer>(
    container,
    container.playerInventory,
    TranslatableText("wand_assembler")
) {
    override fun drawBackground(delta: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        minecraft?.textureManager?.bindTexture(Identifier("mysticis", "textures/gui/staff_assembler.png"))
        blit((this.width - this.containerWidth) / 2, (this.height - this.containerHeight) / 2, 0, 0, containerWidth, containerHeight)
    }
}
