package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.base;

import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.crank.PumpjackCrankBlockEntity;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.PumpjackBlockEntity;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGFluids;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class PumpjackBaseBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public PumpjackBlockEntity controllerHammer;

    public boolean isRunning = false;

    int depositCheckTimer = 0;

    public int miningRate = 0;

    protected LazyOptional<IFluidHandler> fluidCapability;
    public FluidTank tankInventory;
    public BlockPos deposit;

    public PumpjackBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();
        fluidCapability = LazyOptional.of(() -> tankInventory);
    }

    @Override
    public void tick() {
        super.tick();



        if(controllerHammer!=null)
            if(controllerHammer.crank!=null){


            }

        if(controllerHammer!=null)
            if (!(level.getBlockEntity(controllerHammer.getBlockPos()) instanceof PumpjackBlockEntity))
                controllerHammer = null;


        if(controllerHammer!=null)
            if(controllerHammer.base==null)
                controllerHammer = null;

        if(controllerHammer!=null)
            if(!controllerHammer.isRunning())
                controllerHammer = null;

        if(controllerHammer==null)
            return;

        isRunning = controllerHammer.isRunning();



        if(!isRunning) {
            deposit = null;
            controllerHammer = null;
            miningRate = 0;
            return;
        }
        depositCheckTimer++;
        if (depositCheckTimer > 50) {
            depositCheckTimer = 0;
            findDeposit();

        }
        PumpjackCrankBlockEntity crank=null;
        if(controllerHammer.crank!=null)
            crank = controllerHammer.crank;

        if(crank == null)
            return;





        miningRate =

                (int)
                        Math.abs(crank.getMachineInputSpeed()*
                 (crank.heightModifier));

        process();

    }
    public void findDeposit() {
        for (int i = 0; i < this.getBlockPos().getY() + 64; i++) {
            BlockPos checkedPos = new BlockPos(this.getBlockPos().getX(), (this.getBlockPos().getY() - 1) - i, this.getBlockPos().getZ());

            if (level.getBlockState(new BlockPos(checkedPos)).is(TFMGBlocks.OIL_DEPOSIT.get())) {
                deposit = checkedPos;
                return;
            }

            if (!(level.getBlockState(new BlockPos(checkedPos)).is(TFMGBlocks.INDUSTRIAL_PIPE.get()))) {
                deposit = null;
                return;
            }


        }
        deposit = null;

    }
    public void process() {
        if (deposit == null)
            return;

        if (tankInventory.getFluidAmount() + miningRate > 8000)
            return;


        tankInventory.setFluid(new FluidStack(TFMGFluids.CRUDE_OIL.getSource(), tankInventory.getFluidAmount() + miningRate));

    }

    public void setControllerHammer(PumpjackBlockEntity controllerHammer) {
        this.controllerHammer = controllerHammer;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(8000, this::onFluidStackChanged) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(TFMGFluids.CRUDE_OIL.getSource());
            }
        };
    }
    protected void onFluidStackChanged(FluidStack newFluidStack) {}
    @Override
    @SuppressWarnings("removal")
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        Lang.translate("goggles.pumpjack_info")
                .forGoggles(tooltip);


        LangBuilder mb = Lang.translate("generic.unit.millibuckets");





            //Lang.translate("pumpjack_deposit_amount", this.miningRate)
            //        .style(ChatFormatting.LIGHT_PURPLE)
            //        .forGoggles(tooltip, 1);

        if (deposit == null) {

            Lang.translate("goggles.zero")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip, 1);
        }

        //--Fluid Info--//
        LazyOptional<IFluidHandler> handler = this.getCapability(ForgeCapabilities.FLUID_HANDLER);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent())
            return false;

        IFluidHandler tank = resolve.get();
        if (tank.getTanks() == 0)
            return false;




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
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);


        tankInventory.readFromNBT(compound.getCompound("TankContent"));
    }


    @Override
    public void write(CompoundTag compound, boolean clientPacket) {

        compound.put("TankContent", tankInventory.writeToNBT(new CompoundTag()));
        super.write(compound, clientPacket);


    }
    @Nonnull
    @Override

    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {

                if (cap == ForgeCapabilities.FLUID_HANDLER)
                    return fluidCapability.cast();
        return super.getCapability(cap, side);
    }
}
