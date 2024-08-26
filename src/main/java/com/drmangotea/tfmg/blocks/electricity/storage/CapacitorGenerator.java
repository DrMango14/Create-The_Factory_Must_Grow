package com.drmangotea.tfmg.blocks.electricity.storage;

import com.simibubi.create.content.decoration.palettes.ConnectedPillarBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class CapacitorGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        if(state.getValue(ConnectedPillarBlock.AXIS).isHorizontal())
            return 90;



        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        if(state.getValue(ConnectedPillarBlock.AXIS) == Direction.Axis.X)
            return 90;



        return 0;
    }


    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return (Boolean)state.getValue(ConnectedPillarBlock.AXIS).isHorizontal() ? AssetLookup.partialBaseModel(ctx, prov, "horizontal") : AssetLookup.partialBaseModel(ctx, prov, new String[0]);
    }

}