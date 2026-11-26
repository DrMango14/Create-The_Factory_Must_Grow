package com.drmangotea.tfmg.content.engines.base;

import com.drmangotea.tfmg.TFMG;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import static com.drmangotea.tfmg.content.engines.base.EngineBlock.ENGINE_STATE;
import static com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlock.EXTENDED;

public class EngineGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return horizontalAngle(state.getValue(EngineBlock.SHAFT_FACING).getOpposite());
    }


    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {

        String extended = state.getValue(EXTENDED) ? "_extended" : "";

        String path = "block/" +ctx.getName()+"/block_"
                + state.getValue(ENGINE_STATE).getSerializedName() + extended;

        return prov.models()
                .getExistingFile(TFMG.asResource(path));

    }
}
