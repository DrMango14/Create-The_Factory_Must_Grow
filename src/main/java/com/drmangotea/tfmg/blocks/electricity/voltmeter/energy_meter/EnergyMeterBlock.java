package com.drmangotea.tfmg.blocks.electricity.voltmeter.energy_meter;


import com.drmangotea.tfmg.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class EnergyMeterBlock extends TFMGHorizontalDirectionalBlock implements IBE<EnergyMeterBlockEntity>, IWrenchable {


    public EnergyMeterBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.VOLTMETER.get(pState.getValue(FACING));
    }



    @Override
    public Class<EnergyMeterBlockEntity> getBlockEntityClass() {
        return EnergyMeterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends EnergyMeterBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.ENERGY_METER.get();
    }
}
