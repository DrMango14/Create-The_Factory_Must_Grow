package com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven;


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

public class CokeOvenCTBehavior extends ConnectedTextureBehaviour.Base {

	@Override
	public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
		Direction cokeOvenDirection = state.getValue(FACING);

		if (cokeOvenDirection == null)
			return null;


		if (direction == Direction.UP)
			return TFMGSpriteShifts.COKE_OVEN_TOP;
		if (direction == Direction.DOWN)
			return TFMGSpriteShifts.COKE_OVEN_BOTTOM;
		return TFMGSpriteShifts.COKE_OVEN_SIDE;
	}

	@Override
	protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {

		if(face.getAxis().isVertical())
			return state.getValue(FACING).getOpposite();

		return Direction.UP;
	}
//
//
//
@Override
protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
	//Axis vaultBlockAxis = ItemVaultBlock.getVaultBlockAxis(state);
	//if (face.getAxis()
	//		.isVertical() && vaultBlockAxis == Axis.X)
	//	return super.getRightDirection(reader, pos, state, face).getClockWise();
	//if (face.getAxis() == vaultBlockAxis || face.getAxis()
	//		.isVertical())
	//	return super.getRightDirection(reader, pos, state, face);
	//return Direction.fromAxisAndDirection(Axis.Y, face.getAxisDirection());



	return state.getValue(FACING).getClockWise();
}

	public boolean buildContextForOccludedDirections() {
		return super.buildContextForOccludedDirections();
	}

	@Override
	public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos,
		BlockPos otherPos, Direction face) {



//return true;

		if(other.is(TFMGBlocks.COKE_OVEN.get())){
				if(other.getValue(FACING)==state.getValue(FACING))
					return super.connectsTo(state,other,reader,pos,otherPos,face);

		}
		return false;
	}

}
