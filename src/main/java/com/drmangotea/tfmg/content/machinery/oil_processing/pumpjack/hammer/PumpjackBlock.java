package com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.hammer;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

//TODO: This shouldn't just blindly extend BearingBlock
//Bearings can face in any direction, while this is meant to be vertical.
//It facing upwards also breaks PumpjackBlockEntity.applyRotation
public class PumpjackBlock extends BearingBlock implements IBE<PumpjackBlockEntity> {
	public static final BooleanProperty WIDE = BooleanProperty.create("wide");

	public PumpjackBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WIDE);
		super.createBlockStateDefinition(builder);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		boolean wide = context.getLevel().getBlockState(context.getClickedPos().above()).is(TFMGBlocks.LARGE_PUMPJACK_HAMMER_PART.get());
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(WIDE,wide);
	}
	@Override
	public Class<PumpjackBlockEntity> getBlockEntityClass() {
		return PumpjackBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends PumpjackBlockEntity> getBlockEntityType() {
		return TFMGBlockEntities.PUMPJACK_HAMMER.get();
	}

}
