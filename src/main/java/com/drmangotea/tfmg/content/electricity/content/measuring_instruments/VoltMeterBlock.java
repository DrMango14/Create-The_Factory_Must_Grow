package com.drmangotea.tfmg.content.electricity.content.measuring_instruments;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.content.electricity.content.devices.base.DirectionalResistiveLoadBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoltMeterBlock extends DirectionalResistiveLoadBlock implements IBE<VoltMeterBlockEntity> {

    public final boolean voltmeter;

    public VoltMeterBlock(Properties p_49795_,boolean voltmeter) {
        super(p_49795_);
        this.voltmeter = voltmeter;
    }

    @Override
    public Class<VoltMeterBlockEntity> getBlockEntityClass() {
        return VoltMeterBlockEntity.class;
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return TFMGShapes.VOLTMETER.get(state.getValue(FACING));
    }
    @Override
    public BlockEntityType<? extends VoltMeterBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.VOLTMETER.get();
    }
}
