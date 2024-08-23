package com.drmangotea.createindustry.blocks.machines.firebox;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class FireboxGenerator extends SpecialBlockStateGen {
    public FireboxGenerator() {
    }

    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }


    protected int getYRotation(BlockState state) {
        short value;
        switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
            case NORTH:
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

    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return (Boolean)(state.getValue(FireboxBlock.HEAT_LEVEL)!= BlazeBurnerBlock.HeatLevel.SMOULDERING) ? AssetLookup.partialBaseModel(ctx, prov, "lit") : AssetLookup.partialBaseModel(ctx, prov);
    }
}
