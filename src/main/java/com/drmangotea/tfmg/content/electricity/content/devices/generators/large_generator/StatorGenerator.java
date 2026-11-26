package com.drmangotea.tfmg.content.electricity.content.devices.generators.large_generator;


import com.drmangotea.tfmg.TFMG;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;


public class StatorGenerator extends SpecialBlockStateGen {
    protected int getXRotation(BlockState state) {

        short value;
        switch (state.getValue(StatorBlock.FACING)) {
            case NORTH, SOUTH, WEST, EAST:
                value = 0;
                break;
            case DOWN:
                if(state.getValue(StatorBlock.STATOR_STATE) == StatorBlock.StatorState.CORNER) {
                    value = 0;
                }else
                    value = 90;
                break;
            case UP:
                if(state.getValue(StatorBlock.STATOR_STATE) == StatorBlock.StatorState.CORNER) {
                    value = 0;
                }else
                    value = 270;
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        return value;
    }

    protected int getYRotation(BlockState state) {
        return switch (state.getValue(StatorBlock.FACING)) {
            case NORTH, DOWN, UP -> 0;
            case SOUTH -> 180;
            case WEST -> 270;
            case EAST -> 90;
        };
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {
        String path = "block/stator/block_"
                + state.getValue(StatorBlock.STATOR_STATE).getSerializedName();

        if(state.getValue(StatorBlock.VALUE)&&state.getValue(StatorBlock.STATOR_STATE) == StatorBlock.StatorState.CORNER)
            path = path + "_up";
        if(state.getValue(StatorBlock.VALUE)&&state.getValue(StatorBlock.STATOR_STATE) == StatorBlock.StatorState.SIDE)
            path = path + "_rotated";

        return prov.models()
                .getExistingFile(TFMG.asResource(path));

    }
}
