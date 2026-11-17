package com.drmangotea.tfmg.content.electricity.utilities.segmented_display;


import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
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
		if (direction == state.getValue(FACING))
			return TFMGSpriteShifts.SEGMENTED_DISPLAY_SCREEN;
		
		return TFMGSpriteShifts.HEAVY_MACHINERY_CASING;
	}

	@Override
	protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
		return Direction.UP;
	}
	
	@Override
	protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
		Direction facing = state.getValue(FACING);
		if (face != facing) return super.getRightDirection(reader, pos, state, face);
		return facing == Direction.NORTH || facing == Direction.WEST ? facing.getCounterClockWise() : facing.getClockWise();
	}

	@Override
	public boolean connectsTo(
		BlockState state, BlockState other, BlockAndTintGetter reader,
		BlockPos pos, BlockPos otherPos, Direction face
	) {
		if (other.is(TFMGBlocks.SEGMENTED_DISPLAY.get()) && other.getValue(FACING)==state.getValue(FACING))
			return super.connectsTo(state,other,reader,pos,otherPos,face);
		if (face != state.getValue(FACING)) {
			if (other.is(TFMGBlocks.HEAVY_MACHINERY_CASING.get())) return true;
			return other.getBlock() instanceof EncasedBlock block && block.getCasing() ==  TFMGBlocks.HEAVY_MACHINERY_CASING.get();
		}
		
		return false;
	}

}
