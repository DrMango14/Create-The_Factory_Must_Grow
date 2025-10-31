package com.drmangotea.tfmg.content.decoration.doors;


import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class TFMGSlidingDoorBlock extends SlidingDoorBlock implements IWrenchable, IBE<SlidingDoorBlockEntity> {

	public TFMGSlidingDoorBlock(Properties properties, BlockSetType type, boolean folds) {
		super(properties, type, folds);
	}

	@Override
	public Class<SlidingDoorBlockEntity> getBlockEntityClass() {
		return SlidingDoorBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends SlidingDoorBlockEntity> getBlockEntityType() {
		return TFMGBlockEntities.TFMG_SLIDING_DOOR.get();
	}

}
