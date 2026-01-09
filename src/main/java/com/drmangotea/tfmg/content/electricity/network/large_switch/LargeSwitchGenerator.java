package com.drmangotea.tfmg.content.electricity.network.large_switch;

import com.drmangotea.tfmg.content.electricity.utilities.converter.ConverterBlock;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;

public class LargeSwitchGenerator extends SpecialBlockStateGen {


    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {

        return switch (state.getValue(HorizontalKineticBlock.HORIZONTAL_FACING)) {
            case NORTH -> 180;
            case SOUTH -> 0;
            case WEST -> 90;
            case EAST -> 270;
            case DOWN -> 90;
            case UP -> 90;
        };
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {
        return !state.getValue(LargeSwitchBlock.IS_MAIN_PART) ? partialBaseModel(ctx, prov, "secondary")
                : partialBaseModel(ctx, prov,"primary");
    }

}
