package dev.nathanpb.mysticis.blocks

import dev.nathanpb.mysticis.blocks.entity.InfusorPedestalEntity
import dev.nathanpb.mysticis.blocks.entity.StaffAssemblerEntity
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry
import java.util.function.Supplier
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


/*
Copyright (C) 2019 Nathan P. Bombana

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
*/

lateinit var staffAssemblerBlockEntity: BlockEntityType<StaffAssemblerEntity>
lateinit var infusorPedestalBlockEntity: BlockEntityType<InfusorPedestalEntity>

private fun <E: BlockEntity>mkSupplier(clazz: KClass<E>) = Supplier {
    clazz.primaryConstructor!!.call()
}

private fun <E: BlockEntity, B: Block>register(block: B, entityClass: KClass<E>) = Registry.register(
    Registry.BLOCK_ENTITY,
    Registry.BLOCK.getId(block),
    BlockEntityType.Builder.create(mkSupplier(entityClass), block).build(null)
)

fun registerBlockEntities() {
    staffAssemblerBlockEntity = register(BLOCK_STAFF_ASSEMBLER, StaffAssemblerEntity::class)
    infusorPedestalBlockEntity = register(BLOCK_INFUSOR_PEDESTAL, InfusorPedestalEntity::class)
}
