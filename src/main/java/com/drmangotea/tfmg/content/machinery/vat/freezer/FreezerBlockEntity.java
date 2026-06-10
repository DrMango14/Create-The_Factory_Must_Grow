package com.drmangotea.tfmg.content.machinery.vat.freezer;

import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class FreezerBlockEntity extends ElectricBlockEntity {

    public FreezerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getMaxVoltage() { return 20000; }

    @Override
    public int getMaxCurrent() { return 400; }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return true;
    }

    @Override
    public float resistance() {
        return 75;
    }

    public boolean isOperational() {
        return getCurrent() > 3 && !data.notEnoughPower;
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.makeMultimeterTooltip(tooltip, isPlayerSneaking);
        if (!isOperational())
            CreateLang.text("   Insufficient current (need >3A)")
                    .style(ChatFormatting.RED).forGoggles(tooltip, 1);
        return true;
    }

    @Override
    public void onNetworkChanged(int oldVoltage, int oldPower) {
        super.onNetworkChanged(oldVoltage, oldPower);
        VatBlock.updateVatState(getBlockState(), level, getBlockPos().relative(Direction.DOWN));
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos()).setMinY(getBlockPos().getY() - 2);
    }
}
