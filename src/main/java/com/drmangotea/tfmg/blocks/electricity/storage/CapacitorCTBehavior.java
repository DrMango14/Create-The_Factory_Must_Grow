package com.drmangotea.tfmg.blocks.electricity.storage;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class CapacitorCTBehavior extends ConnectedTextureBehaviour.Base {

	protected CTSpriteShiftEntry layerShift;

	public CapacitorCTBehavior(CTSpriteShiftEntry layerShift){
		this.layerShift = layerShift;
	}

	@Override
	public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {

		if(!other.is(state.getBlock()))
			return false;


		Direction direction = state.getValue(FACING);
		Direction otherDirection = other.getValue(FACING);

		if(direction != otherDirection)
			return false;
		//if(otherPos == pos.relative(direction))
		//	return true;
//
		//if(otherPos == pos.relative(direction.getOpposite()))
		//	return true;

		return true;
	}

	@Override
	public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {

		//if(direction == state.getValue(FACING)||direction == state.getValue(FACING).getOpposite())
		//	return null;


		return layerShift;
	}

	@Override
	protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {



		return state.getValue(FACING);
	}
//
//
//


	public boolean buildContextForOccludedDirections() {
		return super.buildContextForOccludedDirections();
	}



}