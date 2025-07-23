package com.drmangotea.tfmg.content.electricity.connection.diagonal;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

import static com.drmangotea.tfmg.content.electricity.connection.diagonal.DiagonalCableBlock.FACING_PRIMARY;
import static com.drmangotea.tfmg.content.electricity.connection.diagonal.DiagonalCableBlock.FACING_SECONDARY;
import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;
import static net.minecraft.core.Direction.*;

public class DiagonalCableGenerator extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        Direction primaryFacing = state.getValue(FACING_PRIMARY);
        Direction secondaryFacing = state.getValue(FACING_SECONDARY);

        if (primaryFacing == UP || primaryFacing == DOWN)
            return switch (secondaryFacing) {
                case NORTH -> 270;
                case SOUTH -> 90;
                case WEST -> 180;
                case EAST -> 0;
                default -> 0;
            };
        else if (secondaryFacing == UP || secondaryFacing == DOWN)
            return switch (primaryFacing) {
                case NORTH -> 270;
                case SOUTH -> 90;
                case WEST -> 180;
                case EAST -> 0;
                default -> 0;
            };
        else if (primaryFacing.getAxis().isHorizontal() && secondaryFacing.getAxis().isHorizontal()) {
            if (primaryFacing == NORTH && secondaryFacing == EAST || primaryFacing == EAST && secondaryFacing == NORTH)
                return 0;
            if (primaryFacing == EAST && secondaryFacing == SOUTH || primaryFacing == SOUTH && secondaryFacing == EAST)
                return 90;
            if (primaryFacing == SOUTH && secondaryFacing == WEST || primaryFacing == WEST && secondaryFacing == SOUTH)
                return 180;
            if (primaryFacing == WEST && secondaryFacing == NORTH || primaryFacing == NORTH && secondaryFacing == WEST)
                return 270;
        }
        return 0;
    }


    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {

        Direction primaryFacing = state.getValue(FACING_PRIMARY);
        Direction secondaryFacing = state.getValue(FACING_SECONDARY);
        if (primaryFacing == secondaryFacing || primaryFacing == secondaryFacing.getOpposite())
            return prov.models().getExistingFile(prov.mcLoc("block/air"));
        if (primaryFacing == UP || secondaryFacing == UP)
            return partialBaseModel(ctx, prov, "up");
        if (primaryFacing == DOWN || secondaryFacing == DOWN)
            return partialBaseModel(ctx, prov, "down");

        return partialBaseModel(ctx, prov, "horizontal");
    }

}
