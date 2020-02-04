package dev.nathanpb.mysticis

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry
import net.minecraft.client.util.InputUtil
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
private const val CATEGORY = "Mysticis"

private fun makeKeybinding(id: String, default: Int): FabricKeyBinding {
    return FabricKeyBinding.Builder.create(
        Identifier("mysticis", id),
        InputUtil.Type.KEYSYM,
        default,
        CATEGORY
    ).build()
}

val switchStaffMode =  makeKeybinding("switch_staff_mode", GLFW.GLFW_KEY_M)

fun registerKeybindings() {
    KeyBindingRegistry.INSTANCE.addCategory(CATEGORY)
    arrayOf(
        switchStaffMode
    ).forEach {
        KeyBindingRegistry.INSTANCE.register(it)
    }
}
