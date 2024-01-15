package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer;

import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;


import static com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.PumpjackBlock.WIDE;
import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;

public class PumpjackGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {

        if(state.getValue(BearingBlock.FACING).getAxis() == Direction.Axis.Y)
            return horizontalAngle(Direction.NORTH);

        return horizontalAngle(state.getValue(BearingBlock.FACING).getClockWise());
    }


    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {

        return state.getValue(WIDE) ? partialBaseModel(ctx, prov, "wide")
                : partialBaseModel(ctx, prov);

    }
}