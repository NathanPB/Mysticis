package dev.nathanpb.mysticis.utils

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.DiffuseLighting
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Items


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

fun ItemRenderer.renderItemStack(
        stack: ItemStack,
        x: Float,
        y: Float,
        transformationType: ModelTransformation.Type = ModelTransformation.Type.GUI,
        leftHanded: Boolean = false,
        light: Int = 15728880,
        overlay: Int = OverlayTexture.DEFAULT_UV,
        model: BakedModel = this.getHeldItemModel(stack, null, MinecraftClient.getInstance().player),
        scale: Float = 16F
) {
    RenderSystem.pushMatrix()
    RenderSystem.enableRescaleNormal()
    RenderSystem.enableAlphaTest()
    RenderSystem.defaultAlphaFunc()
    RenderSystem.enableBlend()
    RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA)
    RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
    RenderSystem.translatef(x, y, 100.0f + this.zOffset)
    RenderSystem.translatef(8.0f, 8.0f, 0.0f)
    RenderSystem.scalef(1.0f, -1.0f, 1.0f)
    RenderSystem.scalef(scale, scale, scale)
    val bl = !model.hasDepthInGui() || stack.item == Items.SHIELD || stack.item == Items.TRIDENT
    if (bl) {
        DiffuseLighting.disableGuiDepthLighting()
    }
    val vertexConsumers = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers
    this.renderItem(
        stack,
        transformationType,
        leftHanded,
        MatrixStack(),
        vertexConsumers,
        light,
        overlay,
        model
    )
    if (bl) {
        DiffuseLighting.disableGuiDepthLighting()
    }

    vertexConsumers.draw()
    RenderSystem.enableDepthTest()

    RenderSystem.disableAlphaTest()
    RenderSystem.disableRescaleNormal()
    RenderSystem.popMatrix()
}
