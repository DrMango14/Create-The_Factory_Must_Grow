package com.drmangotea.tfmg.content.electricity.experimental.blocks;

import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class RealConnectorBlockEntity extends SmartBlockEntity implements IRealisticElectric {

    ElectricalProperties properties = new ConnectorProperties(getPos());

    public RealConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    public void remove() {
        super.remove();
        this.removeBlock();

    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public ElectricalProperties getProperties() {
        return properties;
    }

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public Level getWorld() {
        return level;
    }
}
