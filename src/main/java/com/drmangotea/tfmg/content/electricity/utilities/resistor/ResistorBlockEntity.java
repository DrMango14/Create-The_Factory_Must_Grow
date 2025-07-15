package com.drmangotea.tfmg.content.electricity.utilities.resistor;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.tfmg.base.blocks.WallMountBlock.FACING;

public class ResistorBlockEntity extends ElectricBlockEntity {

    public int resistance = 500;

    public ResistorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite();
    }

    @Override
    public boolean canBeInGroups() {
        return true;
    }

    @Override
    public float resistance() {
        return resistance;
    }

    public void setResistance(ItemStack stack){
        if(stack.get(TFMGDataComponents.RESISTANCE)!=null)
        this.resistance = stack.get(TFMGDataComponents.RESISTANCE);
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.putInt("Resistance",resistance);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        resistance = compound.getInt("Resistance");
    }
}
