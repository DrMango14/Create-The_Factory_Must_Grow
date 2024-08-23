package com.drmangotea.createindustry.blocks.electricity.capacitor;

import com.drmangotea.createindustry.base.TFMGSpriteShifts;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class AccumulatorCTBehavior extends ConnectedTextureBehaviour.Base {

	@Override
	public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {



		if(direction.getAxis().isHorizontal())
			return TFMGSpriteShifts.ACCUMULATOR;


		return null;
	}

	@Override
	protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {



		return Direction.UP;
	}
//
//
//


	public boolean buildContextForOccludedDirections() {
		return super.buildContextForOccludedDirections();
	}



}