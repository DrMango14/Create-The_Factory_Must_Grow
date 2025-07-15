package com.drmangotea.tfmg.content.electricity.utilities.voltage_observer;



import com.drmangotea.tfmg.base.blocks.WallMountBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class VoltageObserverGenerator extends SpecialBlockStateGen {
    public VoltageObserverGenerator() {
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
        return (Boolean)state.getValue(VoltageObserverBlock.POWERED) ? AssetLookup.partialBaseModel(ctx, prov, new String[]{"on"}) : AssetLookup.partialBaseModel(ctx, prov, new String[0]);
    }
}
