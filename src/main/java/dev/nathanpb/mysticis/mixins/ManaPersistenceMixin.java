package dev.nathanpb.mysticis.mixins;

import dev.nathanpb.mysticis.data.ManaData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
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
@Mixin(LivingEntity.class)
public class ManaPersistenceMixin {

    public ManaData mysticisMana = new ManaData();
    public ManaData mysticisAffinity = new ManaData(50, 50, 50, 50, 50, 50);

    @Inject(at = @At("HEAD"), method = "readCustomDataFromTag")
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
        mysticisMana = ManaData.Companion.loadFromTag(tag.getCompound("mysticis.mana"));
        mysticisAffinity = ManaData.Companion.loadFromTag(tag.getCompound("mysticis.affinity"));
    }

    @Inject(at = @At("HEAD"), method = "writeCustomDataToTag")
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
        tag.put("mysticis.mana", mysticisMana.mkCompoundTag());
        tag.put("mysticis.affinity", mysticisAffinity.mkCompoundTag());
    }
}
