package com.drmangotea.tfmg.content.electricity.content.devices.voltage_observer;


import com.drmangotea.tfmg.content.electricity.content.devices.base.DirectionalResistiveLoadBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class VoltageObserverGenerator extends SpecialBlockStateGen {
    public VoltageObserverGenerator() {
    }

    protected int getXRotation(BlockState state) {
		return switch (state.getValue(DirectionalResistiveLoadBlock.FACING)) {
			case NORTH, SOUTH, WEST, EAST -> 90;
			case DOWN -> 180;
			case UP -> 0;
		};
    }

    protected int getYRotation(BlockState state) {
		return switch (state.getValue(DirectionalResistiveLoadBlock.FACING)) {
			case NORTH, DOWN, UP -> 0;
			case SOUTH -> 180;
			case WEST -> 270;
			case EAST -> 90;
		};
    }

    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return state.getValue(VoltageObserverBlock.POWERED) ? AssetLookup.partialBaseModel(ctx, prov, "on") : AssetLookup.partialBaseModel(ctx, prov);
    }
}
