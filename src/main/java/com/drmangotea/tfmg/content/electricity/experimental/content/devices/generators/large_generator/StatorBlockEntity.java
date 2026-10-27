package com.drmangotea.tfmg.content.electricity.experimental.content.devices.generators.large_generator;

import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables.cable_hubs.CableHubBlockEntity;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


public class StatorBlockEntity extends CableHubBlockEntity implements IHaveGoggleInformation {

    public BlockPos rotor = null;

    public int timer = 0;

    public StatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setRotor(RotorBlockEntity be) {
        rotor = be.getBlockPos();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (rotor != null)
            if (!(level.getBlockEntity(rotor) instanceof RotorBlockEntity))
                rotor = null;


    }




    public void updateRotor(){


        Iterable<BlockPos> blocksAround = BlockPos.betweenClosed(getBlockPos().below().north().west(),getBlockPos().above().east().east());

        for (BlockPos blockPos : blocksAround) {
            if(level.getBlockEntity(blockPos) instanceof RotorBlockEntity be) {
               // be.updateNextTick();
                timer = 11;
            }
        }
    }


    @Override
    public void onPlace() {
        super.onPlace();
        updateRotor();
        if (rotor != null)
            if (level.getBlockEntity(rotor) instanceof RotorBlockEntity be) {
                timer =11;
                be.findNextTick = true;
            }
    }

    @Override
    public void destroy() {
        super.destroy();
        updateRotor();
        if (rotor != null)
            if (level.getBlockEntity(rotor) instanceof RotorBlockEntity be) {
              //  be.updateNextTick();
                be.findNextTick = true;
            }
    }
}
