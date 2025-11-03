package com.drmangotea.tfmg.content.electricity.lights;

import com.drmangotea.tfmg.base.blocks.WallMountBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class LampGenerator extends SpecialBlockStateGen {
    public LampGenerator() {
    }

    protected int getXRotation(BlockState state) {
        return switch (state.getValue(WallMountBlock.FACING)) {
            case NORTH, EAST, WEST, SOUTH -> 90;
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
        return state.getValue(LightBulbBlock.LIGHT)>0 ? AssetLookup.partialBaseModel(ctx, prov, "powered") : AssetLookup.partialBaseModel(ctx, prov);
    }
}