package com.drmangotea.tfmg.blocks.engines.intake;

import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;

public class AirIntakeGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        Direction facing = state.getValue(MechanicalPistonBlock.FACING);
        return facing.getAxis()
                .isVertical() ? facing == Direction.DOWN ? 180 : 0 : 90;
    }

    @Override
    protected int getYRotation(BlockState state) {
        Direction facing = state.getValue(MechanicalPistonBlock.FACING);
        return facing.getAxis()
                .isVertical() ? 0 : horizontalAngle(facing) + 180;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return state.getValue(AirIntakeBlock.INVISIBLE) ? partialBaseModel(ctx, prov, "empty")
                : partialBaseModel(ctx, prov);
    }

}
