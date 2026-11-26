package com.drmangotea.tfmg.content.electricity;

import com.drmangotea.tfmg.base.blocks.WallMountBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.RealConnectorBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class CableConnectorGenerator extends SpecialBlockStateGen {
    public CableConnectorGenerator() { }

    protected int getXRotation(BlockState state) {
        return switch (state.getValue(WallMountBlock.FACING)) {
	        case NORTH, SOUTH, WEST, EAST -> 90;
	        case DOWN -> 180;
	        case UP -> 0;
		};
    }

    protected int getYRotation(BlockState state) {
		return switch (state.getValue(WallMountBlock.FACING)) {
			case NORTH, DOWN, UP -> 0;
			case SOUTH -> 180;
			case WEST -> 270;
			case EAST -> 90;
		};
    }

    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        String suffix = "";
        if(state.getValue(RealConnectorBlock.EXTENSION))
            suffix = "extension_extracting";
        if(state.getValue(RealConnectorBlock.EXTENSION))
            suffix = "extension";
        if(!state.getValue(RealConnectorBlock.EXTENSION))
            return AssetLookup.partialBaseModel(ctx, prov);
        if(!state.getValue(RealConnectorBlock.EXTENSION))
            suffix = "extracting";

		return AssetLookup.partialBaseModel(ctx, prov, suffix);
    }
}