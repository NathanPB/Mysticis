package dev.nathanpb.mysticis

import dev.nathanpb.mysticis.event.entity.PlayerTickCallback
import dev.nathanpb.mysticis.event.gui.CrosshairRenderedCallback
import dev.nathanpb.mysticis.hud.AffinityHud
import dev.nathanpb.mysticis.listener.AffinityListener
import dev.nathanpb.mysticis.listener.ManaRegenListener

/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

@Suppress("unused")
fun init() {
    PlayerTickCallback.EVENT.register(AffinityListener())
    PlayerTickCallback.EVENT.register(ManaRegenListener())
    CrosshairRenderedCallback.EVENT.register(AffinityHud())
}

