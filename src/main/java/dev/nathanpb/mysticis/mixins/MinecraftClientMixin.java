package dev.nathanpb.mysticis.mixins;

import dev.nathanpb.mysticis.IdentifiersKt;
import dev.nathanpb.mysticis.event.mysticis.StaffHitCallback;
import dev.nathanpb.mysticis.items.ItemStaff;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    ClientPlayerEntity player;

    @Inject(at = @At("RETURN"), method = "doAttack")
    public void doAttack(CallbackInfo ci) {
        StaffHitCallback.EVENT.invoker().onTriggered(player);
        if (
                player.isSneaking() &&
                player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof ItemStaff
        ) {
            PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
            ClientSidePacketRegistry.INSTANCE.sendToServer(IdentifiersKt.getPACKET_STAFF_PROJECTILE_TRIGGERED(), packet);
        }
    }
}
