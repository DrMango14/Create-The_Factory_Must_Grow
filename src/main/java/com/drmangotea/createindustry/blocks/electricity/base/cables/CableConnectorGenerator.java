package com.drmangotea.createindustry.blocks.electricity.base.cables;

import com.drmangotea.createindustry.blocks.electricity.base.WallMountBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class CableConnectorGenerator extends SpecialBlockStateGen {
    public CableConnectorGenerator() {
    }

    protected int getXRotation(BlockState state) {
        short value;
        switch ((Direction)state.getValue(WallMountBlock.FACING)) {
            case NORTH:
                value = 90;
                break;
            case SOUTH:
                value = 90;
                break;
            case WEST:
                value = 90;
                break;
            case EAST:
                value = 90;
                break;
            case DOWN:
                value = 180;
                break;
            case UP:
                value = 0;
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        return value;
    }

    protected int getYRotation(BlockState state) {
        short value;
        switch ((Direction)state.getValue(WallMountBlock.FACING)) {
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
            case DOWN:
                value = 0;
                break;
            case UP:
                value = 0;
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        return value;
    }

    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return (Boolean)state.getValue(CableConnectorBlock.EXTENSION) ? AssetLookup.partialBaseModel(ctx, prov, new String[]{"extension"}) : AssetLookup.partialBaseModel(ctx, prov, new String[0]);
    }
}
