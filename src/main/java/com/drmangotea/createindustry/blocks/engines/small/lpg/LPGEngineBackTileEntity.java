package com.drmangotea.createindustry.blocks.engines.small.lpg;


import com.drmangotea.createindustry.blocks.engines.small.EngineBackBlockEntity;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LPGEngineBackTileEntity extends EngineBackBlockEntity implements IHaveGoggleInformation {


    public LPGEngineBackTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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

