package com.drmangotea.createindustry.blocks.decoration;

import com.drmangotea.createindustry.blocks.electricity.cable_blocks.DiagonalCableBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

import static com.drmangotea.createindustry.blocks.electricity.base.WallMountBlock.FACING;
import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;

public class LithiumTorchGenerator extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        return state.getValue(FACING)== Direction.DOWN ? 180 : 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return switch (state.getValue(FACING)) {
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


        return state.getValue(FACING).getAxis().isHorizontal() ? partialBaseModel(ctx, prov, "wall")
                : partialBaseModel(ctx, prov);
    }

}