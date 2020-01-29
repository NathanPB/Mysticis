package dev.nathanpb.mysticis.enums


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
enum class StaffUseType {
    HIT_ABSTRACT,
    HIT_BLOCK,
    HIT_ENTITY,
    HIT_AIR,
    USE_ABSTRACT,
    USE_BLOCK,
    USE_ENTITY,
    USE_AIR;

    operator fun contains(useType: StaffUseType): Boolean {
        return when(useType) {
            HIT_ABSTRACT -> this in arrayOf(HIT_ABSTRACT, HIT_BLOCK, HIT_ENTITY, HIT_AIR)
            USE_ABSTRACT -> this in arrayOf(USE_ABSTRACT, USE_BLOCK, USE_ENTITY, USE_AIR)
            else ->         this == useType
        }
    }
}
