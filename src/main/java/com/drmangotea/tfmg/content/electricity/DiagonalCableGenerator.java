package com.drmangotea.tfmg.content.electricity;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import static com.drmangotea.tfmg.content.electricity.DiagonalCableBlock.FACING_PRIMARY;
import static com.drmangotea.tfmg.content.electricity.DiagonalCableBlock.FACING_SECONDARY;
import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;

public class DiagonalCableGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

	@Override
	protected int getYRotation(BlockState state) {
		Direction primaryFacing = state.getValue(FACING_PRIMARY);
		Direction secondaryFacing = state.getValue(FACING_SECONDARY);
		
		if (primaryFacing == Direction.UP || primaryFacing == Direction.DOWN)
			return switch (secondaryFacing) {
				case NORTH -> 270;
				case SOUTH -> 90;
				case WEST -> 180;
				default -> 0;
			};
		else if (secondaryFacing == Direction.UP || secondaryFacing == Direction.DOWN)
			return switch (primaryFacing) {
				case NORTH -> 270;
				case SOUTH -> 90;
				case WEST -> 180;
				default -> 0;
			};
		else if (primaryFacing.getAxis().isHorizontal() && secondaryFacing.getAxis().isHorizontal()) {
			if (primaryFacing == Direction.NORTH && secondaryFacing == Direction.EAST || primaryFacing == Direction.EAST && secondaryFacing == Direction.NORTH)
				return 0;
			if (primaryFacing == Direction.EAST && secondaryFacing == Direction.SOUTH || primaryFacing == Direction.SOUTH && secondaryFacing == Direction.EAST)
				return 90;
			if (primaryFacing == Direction.SOUTH && secondaryFacing == Direction.WEST || primaryFacing == Direction.WEST && secondaryFacing == Direction.SOUTH)
				return 180;
			if (primaryFacing == Direction.WEST && secondaryFacing == Direction.NORTH || primaryFacing == Direction.NORTH && secondaryFacing == Direction.WEST)
				return 270;
		}
		return 0;
	}
	

    @Override
    public <T extends Block> ModelFile getModel (
		DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state
	) {

		Direction primaryFacing = state.getValue(FACING_PRIMARY);
		Direction secondaryFacing = state.getValue(FACING_SECONDARY);
		if (primaryFacing == Direction.UP || secondaryFacing == Direction.UP)
			return partialBaseModel(ctx, prov, "up");
		if (primaryFacing == Direction.DOWN || secondaryFacing == Direction.DOWN)
			return partialBaseModel(ctx, prov, "down");
		
		return partialBaseModel(ctx, prov, "horizontal");
	}

}