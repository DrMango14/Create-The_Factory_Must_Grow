package com.drmangotea.createindustry.blocks.machines.exhaust;


import com.drmangotea.createindustry.registry.TFMGFluids;
import com.simibubi.create.Create;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("removal")
public class ExhaustBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {



    protected LazyOptional<IFluidHandler> fluidCapability;
    public FluidTank tankInventory;

    public boolean spawnsSmoke=false;
    public int smokeTimer=0;

    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;



    public ExhaustBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();
        fluidCapability = LazyOptional.of(() -> tankInventory);

        refreshCapability();
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        LangBuilder mb = Lang.translate("generic.unit.millibuckets");

        /////////
        LazyOptional<IFluidHandler> handler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent())
            return false;

        IFluidHandler tank = resolve.get();
        if (tank.getTanks() == 0)
            return false;

        Lang.translate("goggles.pumpjack_fluid_storage")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);


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
                            .style(ChatFormatting.GOLD))
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
                        .style(ChatFormatting.GOLD))
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip, 1);


        return true;



    }




    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(500, this::onFluidStackChanged) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(TFMGFluids.CARBON_DIOXIDE.getSource());
            }
        };
    }


    protected void onFluidStackChanged(FluidStack newFluidStack) {
        sendData();
    }

    @Override
    public void tick() {
        super.tick();
        Direction direction = this.getBlockState().getValue(ExhaustBlock.FACING);

        if(smokeTimer!=0) {
            spawnsSmoke = true;
            smokeTimer--;
        }else spawnsSmoke = false;
        if (direction == Direction.UP)
            if(spawnsSmoke)
                ExhaustBlock.makeParticles(level, this.getBlockPos(), 0);
        if (direction == Direction.DOWN)
            if(spawnsSmoke)
                ExhaustBlock.makeParticles(level, this.getBlockPos(), 1);
        if (direction == Direction.NORTH)
            if(spawnsSmoke)
                ExhaustBlock.makeParticles(level, this.getBlockPos(), 2);
        if (direction == Direction.SOUTH)
            if(spawnsSmoke)
                ExhaustBlock.makeParticles(level, this.getBlockPos(), 3);
        if (direction == Direction.EAST)
            if(spawnsSmoke)
                ExhaustBlock.makeParticles(level, this.getBlockPos(), 4);
        if (direction == Direction.WEST)
            if(spawnsSmoke)
                ExhaustBlock.makeParticles(level, this.getBlockPos(), 5);
        if(tankInventory.getFluidAmount()>0) {

            smokeTimer = 100;
            spawnsSmoke = true;


            tankInventory.drain(20, IFluidHandler.FluidAction.EXECUTE);



        }
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }


    }



    @Override
    public void initialize() {
        super.initialize();
        sendData();
        if (level.isClientSide)
            invalidateRenderBoundingBox();
    }



    @Override
    public void sendData() {
        if (syncCooldown > 0) {
            queuedSync = true;
            return;
        }
        super.sendData();
        queuedSync = false;
        syncCooldown = SYNC_RATE;
    }


    private void refreshCapability() {
        LazyOptional<IFluidHandler> oldCap = fluidCapability;
        fluidCapability = LazyOptional.of(() -> handlerForCapability());
        oldCap.invalidate();
    }

    private IFluidHandler handlerForCapability() {

        return tankInventory;
    }




    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        tankInventory.setCapacity(500);
        tankInventory.readFromNBT(compound.getCompound("TankContent"));
    }





    @Override
    public void write(CompoundTag compound, boolean clientPacket) {

        compound.put("TankContent", tankInventory.writeToNBT(new CompoundTag()));
        super.write(compound, clientPacket);


    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!fluidCapability.isPresent())
            refreshCapability();
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public IFluidTank getTankInventory() {
        return tankInventory;
    }






}

