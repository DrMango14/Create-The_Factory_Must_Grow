package com.drmangotea.tfmg.content.machines.metal_processing.coke_oven;

import com.drmangotea.tfmg.CreateTFMG;
import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Function;

import static com.drmangotea.tfmg.content.machines.metal_processing.coke_oven.CokeOvenBlock.CONTROLLER_TYPE;
import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;

public class CokeOvenGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return horizontalAngle(state.getValue(CokeOvenBlock.FACING));
    }







    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {

        String path = "block/coke_oven/block_"
                + state.getValue(CONTROLLER_TYPE).getSerializedName()
                ;

        return prov.models()
                .getExistingFile(CreateTFMG.asResource(path));

    }
}
