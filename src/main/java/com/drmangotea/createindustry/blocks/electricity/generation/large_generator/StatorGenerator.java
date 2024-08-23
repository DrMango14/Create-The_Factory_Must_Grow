package com.drmangotea.createindustry.blocks.electricity.generation.large_generator;

import com.drmangotea.createindustry.CreateTFMG;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;


public class StatorGenerator extends SpecialBlockStateGen {


    protected int getXRotation(BlockState state) {

        short value;
        switch ((Direction)state.getValue(StatorBlock.FACING)) {
            case NORTH, SOUTH, WEST, EAST:
                value = 0;
                break;
            case DOWN:
                if(state.getValue(StatorBlock.STATOR_STATE)== StatorBlock.StatorState.CORNER) {
                    value = 0;
                }else
                    value = 90;
                break;
            case UP:
                if(state.getValue(StatorBlock.STATOR_STATE)== StatorBlock.StatorState.CORNER) {
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

        short value;
        switch ((Direction)state.getValue(StatorBlock.FACING)) {
            case NORTH, DOWN, UP:
                value = 0;
                break;
            case SOUTH:
                value = 180;
                break;
            case WEST:
                value = 270;
                break;
            case EAST:
                value = 90;
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        return value;
    }



    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {

        String path = "block/stator/block_"
                + state.getValue(StatorBlock.STATOR_STATE).getSerializedName()
                ;

        if(state.getValue(StatorBlock.VALUE)&&state.getValue(StatorBlock.STATOR_STATE)== StatorBlock.StatorState.CORNER)
            path = path + "_up";
        if(state.getValue(StatorBlock.VALUE)&&state.getValue(StatorBlock.STATOR_STATE)== StatorBlock.StatorState.SIDE)
            path = path + "_rotated";

        return prov.models()
                .getExistingFile(CreateTFMG.asResource(path));

    }
}
