package com.drmangotea.createindustry.blocks.machines.metal_processing.coke_oven;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.TFMGSpriteShifts;
import com.drmangotea.createindustry.registry.TFMGBlocks;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import static com.drmangotea.createindustry.blocks.machines.metal_processing.coke_oven.CokeOvenBlock.CONTROLLER_TYPE;
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
