package com.drmangotea.tfmg.content.electricity.content.devices.segmented_display;

import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.decoration.encasing.CasingConnectivity;
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
		return face.getAxis().isHorizontal() ? Direction.UP : state.getValue(FACING);
	}
	
	@Override
	protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
		Direction facing = state.getValue(FACING);
		if (face == facing)
			return facing == Direction.NORTH || facing == Direction.WEST ? facing.getCounterClockWise() : facing.getClockWise();
		
		if (face.getAxis().isVertical()) return facing.getCounterClockWise();
		
		return super.getRightDirection(reader, pos, state, face);
	}

	@Override
	public boolean connectsTo(
		BlockState state, BlockState other, BlockAndTintGetter reader,
		BlockPos pos, BlockPos otherPos, Direction face
	) {
		if (isBeingBlocked(state, reader, pos, otherPos, face))
			return false;
		
		Direction facing = state.getValue(FACING);
		if (face == facing)
			return other.is(TFMGBlocks.SEGMENTED_DISPLAY.get()) && other.getValue(FACING) == facing;
		
		//EncasedCTBehaviour Logic
		CasingConnectivity cc = CreateClient.CASING_CONNECTIVITY;
		CasingConnectivity.Entry entry = cc.get(state);
		CasingConnectivity.Entry otherEntry = cc.get(other);
		if (entry == null || otherEntry == null)
			return false;
		if (!entry.isSideValid(state, face) || !otherEntry.isSideValid(other, face))
			return false;
		
		return entry.getCasing() == otherEntry.getCasing();
	}

}
