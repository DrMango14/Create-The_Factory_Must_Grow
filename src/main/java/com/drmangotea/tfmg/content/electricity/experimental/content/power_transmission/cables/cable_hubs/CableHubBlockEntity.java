package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables.cable_hubs;

import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables.AbstractCableBlockEntity;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables.CableProperties;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CableHubBlockEntity extends AbstractCableBlockEntity {
    public CableHubBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);


        this.p = new CableProperties(getPos(), Arrays.stream(Direction.values()).toList());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {

    }
}
