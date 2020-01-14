package dev.nathanpb.mysticis.mixins;

import dev.nathanpb.mysticis.acessors.IMysticisLivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
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
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "copyFrom")
    public void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        IMysticisLivingEntity oldPlayerMysticis = (IMysticisLivingEntity) oldPlayer;
        IMysticisLivingEntity newPlayer = (IMysticisLivingEntity) this;

        newPlayer.setMysticisMana(oldPlayerMysticis.getMysticisMana());
        newPlayer.setMysticisAffinity(oldPlayerMysticis.getMysticisAffinity());
    }
}
