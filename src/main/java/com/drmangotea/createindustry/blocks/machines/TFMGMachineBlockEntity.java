package com.drmangotea.createindustry.blocks.machines;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.security.DrbgParameters;
import java.util.List;
import java.util.Optional;

public class TFMGMachineBlockEntity extends SmartBlockEntity  implements IHaveGoggleInformation {
    public SmartFluidTankBehaviour tank1;
    public SmartFluidTankBehaviour tank2;

    private boolean contentsChanged;

    protected LazyOptional<CombinedTankWrapper> fluidCapability;

    public TFMGMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);


        contentsChanged = true;

    }
// im like 90% sure we dont need this
//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull DrbgParameters.Capability<T> cap, Direction side) {
//
//
//
//        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//            return fluidCapability.cast();
//        return super.getCapability(cap, side);
//    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank1 = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, 1000, true)
                .whenFluidUpdates(() -> contentsChanged = true);

        tank2 = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 1, 1000, true)
                .whenFluidUpdates(() -> contentsChanged = true);




        behaviours.add(tank1);
        behaviours.add(tank2);

        fluidCapability = LazyOptional.of(() -> {
            Storage<FluidVariant> inputCap = tank1.getCapability();
            Storage<FluidVariant> outputCap = tank2.getCapability();
            return new CombinedTankWrapper(outputCap, inputCap);
        });


    }
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        sendData();
    }

    @Override
    public void invalidate() {
        super.invalidate();

        fluidCapability.invalidate();
    }


    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }




    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {



        //--Fluid Info--//
        LazyOptional<IFluidHandler> handler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent())
            return false;

        IFluidHandler tank = resolve.get();
        if (tank.getTanks() == 0)
            return false;

        LangBuilder mb = Lang.translate("generic.unit.millibuckets");


        boolean isEmpty = true;
        for (int i = 0; i < tank.getTanks(); i++) {
            FluidStack fluidStack = tank.getFluidInTank(i);
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
                    .add(Lang.number(tank.getTankCapacity(i))
                            .add(mb)
                            .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip, 1);

            isEmpty = false;
        }

        if (tank.getTanks() > 1) {
            if (isEmpty)
                tooltip.remove(tooltip.size() - 1);
            return true;
        }

        if (!isEmpty)
            return true;

        Lang.translate("gui.goggles.fluid_container.capacity")
                .add(Lang.number(tank.getTankCapacity(0))
                        .add(mb)
                        .style(ChatFormatting.DARK_GREEN))
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip, 1);


        return true;
    }


}
