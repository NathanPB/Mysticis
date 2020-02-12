package dev.nathanpb.mysticis.blocks.entity.renderers

import dev.nathanpb.mysticis.blocks.entity.InfusorPedestalEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class InfusorPedestalEntityRenderer(dispatcher: BlockEntityRenderDispatcher) : BlockEntityRenderer<InfusorPedestalEntity>(dispatcher) {
    override fun render(
        blockEntity: InfusorPedestalEntity?,
        tickDelta: Float,
        matrices: MatrixStack?,
        vertexConsumers: VertexConsumerProvider?,
        light: Int,
        overlay: Int
    ) {
        if (matrices != null && blockEntity?.isInvEmpty == false) {
            matrices.push()
            matrices.translate(0.5, 0.9, 0.5)
            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((blockEntity.world!!.time + tickDelta) * 4))

            MinecraftClient.getInstance().itemRenderer.renderItem(
                blockEntity.getInvStack(0),
                ModelTransformation.Type.GROUND,
                WorldRenderer.getLightmapCoordinates(blockEntity.world, blockEntity.pos.up()),
                overlay, matrices, vertexConsumers
            )

            matrices.pop()
        }
    }
}
