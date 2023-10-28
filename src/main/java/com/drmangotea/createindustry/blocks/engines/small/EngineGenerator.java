package com.drmangotea.createindustry.blocks.engines.small;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;

public class EngineGenerator extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        return switch (state.getValue(EngineBlock.FACING)) {
            case NORTH -> 0;
            case SOUTH -> 0;
            case WEST -> 0;
            case EAST -> 0;
            case DOWN -> 90;
            case UP -> 270;
        };
    }

    @Override
    protected int getYRotation(BlockState state) {
        return switch (state.getValue(EngineBlock.FACING)) {
            case NORTH -> 0;
            case SOUTH -> 180;
            case WEST -> 270;
            case EAST -> 90;
            case DOWN -> 180;
            case UP -> 180;
        };
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {
        // return AssetLookup.forPowered(ctx, prov)
        //         .apply(state);

        return partialBaseModel(ctx, prov);
    }

}
