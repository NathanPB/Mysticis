package dev.nathanpb.mysticis.mixins;

import dev.nathanpb.mysticis.event.gui.CrosshairRenderedCallback;
import net.minecraft.client.gui.hud.InGameHud;
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
@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(at = @At("RETURN"), method = "renderCrosshair")
    public void renderCrosshair(CallbackInfo ci) {
        CrosshairRenderedCallback.EVENT.invoker().render();
    }
}
