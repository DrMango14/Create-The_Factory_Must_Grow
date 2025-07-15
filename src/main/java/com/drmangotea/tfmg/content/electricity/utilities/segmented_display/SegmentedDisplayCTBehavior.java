package com.drmangotea.tfmg.content.electricity.utilities.segmented_display;


import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class SegmentedDisplayCTBehavior extends ConnectedTextureBehaviour.Base {

	@Override
	public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
		Direction facing = state.getValue(FACING);


		if(direction == facing)
			return TFMGSpriteShifts.SEGMENTED_DISPLAY_SCREEN;

		//if(direction == facing.getOpposite())
		//	return null;

		return null;
	}

	@Override
	protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {

		return Direction.UP;
	}
	//
//
//
	@Override
	protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {


		return state.getValue(FACING) == Direction.NORTH ||state.getValue(FACING) == Direction.WEST ? state.getValue(FACING).getCounterClockWise() : state.getValue(FACING).getClockWise();
	}



	public boolean buildContextForOccludedDirections() {
		return super.buildContextForOccludedDirections();
	}

	@Override
	public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos,
							  BlockPos otherPos, Direction face) {



		if(other.is(TFMGBlocks.SEGMENTED_DISPLAY.get())){
			if(other.getValue(FACING)==state.getValue(FACING))
				return super.connectsTo(state,other,reader,pos,otherPos,face);
//
		}
		return false;
	}

}
