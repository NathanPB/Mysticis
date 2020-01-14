package dev.nathanpb.mysticis.items

import dev.nathanpb.mysticis.data.ManaData
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import kotlin.random.Random


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/
class FireStaff : StaffBase() {

    override val manaConsumeProjectile = ManaData(fire = 1F)
    override val manaConsumeSelf = ManaData()
    override val manaConsumeArea= ManaData()

    override fun onTriggeredArea(user: LivingEntity, hand: Hand): TypedActionResult<ItemStack> {
       return TypedActionResult.pass(user.getStackInHand(hand))
    }

    override fun onTriggeredProjectile(user: LivingEntity, hand: Hand): TypedActionResult<ItemStack> {
        (0..200).forEach { _ ->
            user.world.addParticle(
                ParticleTypes.FLAME,
                user.x,
                user.y + 1.5,
                user.z,
                user.rotationVector.x + ((Random.nextFloat() - .5) / 1.5),
                user.rotationVector.y + ((Random.nextFloat() - .5) / 1.5),
                user.rotationVector.z + ((Random.nextFloat() - .5) / 1.5)
            )
        }


        return TypedActionResult.consume(user.getStackInHand(hand))
    }

    override fun onTriggeredSelf(user: LivingEntity, hand: Hand): TypedActionResult<ItemStack> {
        return TypedActionResult.pass(user.getStackInHand(hand))
    }
}
