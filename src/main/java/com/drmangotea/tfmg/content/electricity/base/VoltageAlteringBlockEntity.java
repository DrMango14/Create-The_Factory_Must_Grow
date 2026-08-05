package com.drmangotea.tfmg.content.electricity.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class VoltageAlteringBlockEntity extends ElectricBlockEntity {

    // Subclasses (TransformerBlockEntity, ElectricDiodeBlockEntity) may shadow this field with
    // their own declaration. The field here lets ElectricalNetwork.handleInsufficientPower() set
    // it directly on any VoltageAlteringBlockEntity without casting.
    public boolean updateInFront = false;

    public VoltageAlteringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract int getOutputVoltage();

    public abstract int getOutputPower();

    public abstract IElectric getControlledBlock();

    /**
     * Maximum watts this transformer can output. Used by powerGeneration() in place of the
     * old getPowerUsage()+1 hack to avoid false undersupply on the generator side.
     */
    public int getMaxPowerOutput() {
        return getOutputPower() == 0 ? 0 : Integer.MAX_VALUE / 2;
    }

    // Subclasses override this to push voltage changes to the downstream network.
    public void updateInFront() {}
}
