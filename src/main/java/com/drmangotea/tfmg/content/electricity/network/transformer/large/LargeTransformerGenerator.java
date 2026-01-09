package com.drmangotea.tfmg.content.electricity.network.transformer.large;

import com.drmangotea.tfmg.content.electricity.network.large_switch.LargeSwitchBlock;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;

public class LargeTransformerGenerator extends SpecialBlockStateGen {


    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        if (state.getValue(LargeSwitchBlock.IS_MAIN_PART)) {
            return switch (state.getValue(HorizontalKineticBlock.HORIZONTAL_FACING)) {
                case NORTH -> 180;
                case SOUTH -> 0;
                case WEST -> 90;
                case EAST -> 270;
                case DOWN -> 90;
                case UP -> 90;
            };
        } else
            return switch (state.getValue(HorizontalKineticBlock.HORIZONTAL_FACING)) {
                case NORTH -> 0;
                case SOUTH -> 180;
                case WEST -> 270;
                case EAST -> 90;
                case DOWN -> 90;
                case UP -> 90;
            };
    }


    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {



        return state.getValue(LargeTransformerBlock.UNFINISHED_MODEL) ? partialBaseModel(ctx, prov, "unfinished")
                : partialBaseModel(ctx, prov);
    }

}
