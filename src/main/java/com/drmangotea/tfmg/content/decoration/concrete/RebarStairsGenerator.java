package com.drmangotea.tfmg.content.decoration.concrete;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.neoforged.neoforge.client.model.generators.ModelFile;


import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;

public class RebarStairsGenerator extends SpecialBlockStateGen {


    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return switch (state.getValue(StairBlock.FACING)) {
            case NORTH -> 270;
            case SOUTH -> 90;
            case WEST -> 180;
            case EAST -> 0;
            case DOWN -> 0;
            case UP -> 0;
        };
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {
        return state.getValue(StairBlock.HALF)== Half.TOP ? partialBaseModel(ctx, prov, "upside_down")
                : partialBaseModel(ctx, prov);
    }

}
