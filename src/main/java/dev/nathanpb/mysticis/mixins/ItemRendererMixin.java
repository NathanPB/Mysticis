package dev.nathanpb.mysticis.mixins;

import dev.nathanpb.mysticis.hud.StaffCooldownOverlay;
import dev.nathanpb.mysticis.items.ItemStaff;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(
            at = @At("HEAD"),
            method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"
    )
    public void renderGuiItemOverlay(TextRenderer fontRenderer, ItemStack stack, int x, int y, String amountText, CallbackInfo ci) {
        if (stack.getItem() instanceof ItemStaff) {
            if(MinecraftClient.getInstance().player != null) {
                StaffCooldownOverlay.Companion.render(x, y, stack, MinecraftClient.getInstance().player);
            }
        }
    }
}
