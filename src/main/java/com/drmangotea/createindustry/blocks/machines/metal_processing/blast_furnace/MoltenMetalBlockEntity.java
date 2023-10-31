package com.drmangotea.createindustry.blocks.machines.metal_processing.blast_furnace;

import com.drmangotea.createindustry.CreateTFMG;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MoltenMetalBlockEntity extends SmartBlockEntity {

    public int discardTimer;

    public MoltenMetalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        discardTimer=20;

    }

    public void tick(){
        super.tick();
        discardTimer--  ;
        if(discardTimer == 0)
          //  if(Create.RANDOM.nextInt(25)==5)
                level.setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(),3);
    }





    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
