package com.drmangotea.tfmg.blocks.engines.small.turbine;


import com.drmangotea.tfmg.blocks.engines.small.EngineBackBlockEntity;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TurbineEngineBackTileEntity extends EngineBackBlockEntity implements IHaveGoggleInformation {


    public TurbineEngineBackTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    public void tick() {
        super.tick();
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }





    }
}

