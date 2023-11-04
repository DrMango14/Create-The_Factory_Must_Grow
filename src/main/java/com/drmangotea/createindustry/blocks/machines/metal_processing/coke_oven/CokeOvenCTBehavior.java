package com.drmangotea.createindustry.blocks.machines.metal_processing.coke_oven;

import com.drmangotea.createindustry.base.TFMGSpriteShifts;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class CokeOvenCTBehavior extends ConnectedTextureBehaviour.Base {

	@Override
	public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
		Direction cokeOvenDirection = state.getValue(FACING);
		boolean small = !ItemVaultBlock.isLarge(state);
		if (cokeOvenDirection == null)
			return null;

		if (direction == cokeOvenDirection.getOpposite())
			return TFMGSpriteShifts.COKE_OVEN_BACK;
		if (direction == Direction.UP)
			return TFMGSpriteShifts.COKE_OVEN_TOP;
		if (direction == Direction.DOWN)
			return TFMGSpriteShifts.COKE_OVEN_BOTTOM;

		return TFMGSpriteShifts.COKE_OVEN_SIDE;
	}

	@Override
	protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
		Direction cokeOvenDirection =state.getValue(FACING);
		boolean alongX = cokeOvenDirection.getAxis() == Axis.X;
		if (face.getAxis()
				.isVertical() && alongX)
			return super.getUpDirection(reader, pos, state, face).getClockWise();
		if (face.getAxis() == cokeOvenDirection.getAxis() || face.getAxis()
				.isVertical())
			return super.getUpDirection(reader, pos, state, face);
		return Direction.fromAxisAndDirection(cokeOvenDirection.getAxis(), alongX ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE);
	}



	@Override
	protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
		Direction cokeOvenDirection =state.getValue(FACING);
		if (face.getAxis()
			.isVertical() && cokeOvenDirection.getAxis() == Axis.X)
			return super.getRightDirection(reader, pos, state, face).getClockWise();
		if (face.getAxis() == cokeOvenDirection.getAxis() || face.getAxis()
			.isVertical())
			return super.getRightDirection(reader, pos, state, face);
		return Direction.fromAxisAndDirection(Axis.Y, face.getAxisDirection());
	}

	public boolean buildContextForOccludedDirections() {
		return super.buildContextForOccludedDirections();
	}

	@Override
	public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos,
		BlockPos otherPos, Direction face) {
		return state == other && ConnectivityHandler.isConnected(reader, pos, otherPos); //ItemVaultConnectivityHandler.isConnected(reader, pos, otherPos);
	}

}
