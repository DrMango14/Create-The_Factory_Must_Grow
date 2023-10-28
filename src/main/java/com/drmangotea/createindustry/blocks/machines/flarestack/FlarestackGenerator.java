package com.drmangotea.createindustry.blocks.machines.flarestack;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;

public class FlarestackGenerator extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return 0;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {
        // return AssetLookup.forPowered(ctx, prov)
        //         .apply(state);

        return state.getValue(FlarestackBlock.LIT) ? partialBaseModel(ctx, prov, "lit")
                : partialBaseModel(ctx, prov);
    }

}
