package com.drmangotea.tfmg.content.machinery.misc.firebox;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;

public class FireboxGenerator extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return 0;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        if (FireboxBlock.getHeatLevelOf(state) != BlazeBurnerBlock.HeatLevel.NONE) {
            return partialBaseModel(ctx, prov, "lit");
        }
        return partialBaseModel(ctx, prov);
    }
}
