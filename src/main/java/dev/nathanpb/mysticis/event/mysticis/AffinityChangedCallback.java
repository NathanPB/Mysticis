package dev.nathanpb.mysticis.event.mysticis;

import dev.nathanpb.mysticis.data.ManaData;
import dev.nathanpb.mysticis.enums.ManaChangedCause;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
public interface AffinityChangedCallback {
    Event<AffinityChangedCallback> EVENT = EventFactory.createArrayBacked(AffinityChangedCallback.class,
       listeners -> (newAffinity ,prevAffinity, cause) -> {
           for(AffinityChangedCallback listener : listeners) {
               listener.onAffinityChanged(newAffinity, prevAffinity, cause);
           }
    });

    void onAffinityChanged(ManaData newAffinity, ManaData prevAffinity, ManaChangedCause cause);
}
