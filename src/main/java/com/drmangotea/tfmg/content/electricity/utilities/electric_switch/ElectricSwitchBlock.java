package com.drmangotea.tfmg.content.electricity.utilities.electric_switch;

import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.IVoltageChanger;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ElectricSwitchBlock extends TFMGDirectionalBlock implements IBE<ElectricSwitchBlockEntity>, IVoltageChanger {
    public ElectricSwitchBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        withBlockEntityDo(level, pos, IElectric::onPlaced);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }


    @Override
    public Class<ElectricSwitchBlockEntity> getBlockEntityClass() {
        return ElectricSwitchBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ElectricSwitchBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.ELECTRIC_SWITCH.get();
    }
}
