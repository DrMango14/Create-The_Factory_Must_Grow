package com.drmangotea.createindustry.blocks.engines.diesel.engine_expansion;

import com.drmangotea.createindustry.registry.TFMGFluids;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class DieselEngineExpansionBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    protected LazyOptional<CombinedTankWrapper> fluidCapability;
    public FluidTank coolantTank;
    public FluidTank lubricationOilTank;
    public FluidTank airTank;




    public DieselEngineExpansionBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        airTank = createUpgradeTankInventory(TFMGFluids.AIR.get());
        coolantTank = createUpgradeTankInventory(TFMGFluids.COOLING_FLUID.get());
        lubricationOilTank = createUpgradeTankInventory(TFMGFluids.LUBRICATION_OIL.get());

        fluidCapability = LazyOptional.of(() -> new CombinedTankWrapper(airTank,lubricationOilTank,coolantTank));


    }

    @Override
    public void invalidate() {
        super.invalidate();
        fluidCapability.invalidate();
    }
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {

        compound.put("LubricationOil", lubricationOilTank.writeToNBT(new CompoundTag()));
        compound.put("Air", airTank.writeToNBT(new CompoundTag()));

        compound.put("Coolant", coolantTank.writeToNBT(new CompoundTag()));


        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        lubricationOilTank.readFromNBT(compound.getCompound("LubricationOil"));

        airTank.readFromNBT(compound.getCompound("Air"));

        coolantTank.readFromNBT(compound.getCompound("Coolant"));



        super.read(compound, clientPacket);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        sendData();
    }
    protected SmartFluidTank createUpgradeTankInventory(Fluid validFluid) {
        return new SmartFluidTank(1000, this::onFluidStackChanged){
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(validFluid);
            }
        };
    }


    @Override
    @SuppressWarnings("removal")
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        //--Fluid Info--//
        LazyOptional<CombinedTankWrapper> handler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        Optional<CombinedTankWrapper> resolve = handler.resolve();
        if (!resolve.isPresent())
            return false;

        CombinedTankWrapper tanks = resolve.get();
        if (tanks.parts.isEmpty())
            return false;

        LangBuilder mb = Lang.translate("generic.unit.millibuckets");
        Lang.translate("goggles.diesel_engine_info")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        boolean isEmpty = true;
        for (int i = 0; i < tanks.parts.size(); i++) {
            Storage<FluidVariant> tank = tanks.parts.get(i);
            FluidStack fluidStack = tank;
            if (fluidStack.isEmpty())
                continue;

            Lang.fluidName(fluidStack)
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);

            Lang.builder()
                    .add(Lang.number(fluidStack.getAmount())
                            .add(mb)
                            .style(ChatFormatting.DARK_GREEN))
                    .text(ChatFormatting.GRAY, " / ")
                    .add(Lang.number(tanks.getTankCapacity(i))
                            .add(mb)
                            .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip, 1);

            isEmpty = false;
        }

        if (tanks.getTanks() > 1) {
            if (isEmpty)
                tooltip.remove(tooltip.size() - 1);
            return true;
        }

        if (!isEmpty)
            return true;

        Lang.translate("gui.goggles.fluid_container.capacity")
                .add(Lang.number(tanks.getTankCapacity(0))
                        .add(mb)
                        .style(ChatFormatting.DARK_GREEN))
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip, 1);


        return true;
    }
}
