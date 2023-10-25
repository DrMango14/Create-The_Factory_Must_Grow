package com.drmangotea.tfmg.blocks.engines.small;

import com.drmangotea.createindustry.content.oil.exhaust.ExhaustBlock;
import com.drmangotea.createindustry.registry.CIFluids;
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
import net.minecraft.world.level.block.entity.BlockEntity;
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

 public class EngineBackBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {



    public int  consumptionTimer=0;

    protected LazyOptional<IFluidHandler> fluidCapability;
    public FluidTank tankInventory;



    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;



    public EngineBackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();
        fluidCapability = LazyOptional.of(() -> tankInventory);

        refreshCapability();
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        LangBuilder mb = Lang.translate("generic.unit.millibuckets");
        Lang.translate("goggles.engine_exhaust_stats")
                .forGoggles(tooltip);


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
        return new SmartFluidTank(1000, this::onFluidStackChanged){
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(CIFluids.CARBON_DIOXIDE.getSource());
            }
        };
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        sendData();
    }
    public BlockEntity getFrontPart(){
        Direction direction = this.getBlockState().getValue(ExhaustBlock.FACING);

        if(direction == Direction.UP)
            return level.getBlockEntity(this.getBlockPos().above());
        if(direction == Direction.DOWN)
            return level.getBlockEntity(this.getBlockPos().below());
        if(direction == Direction.NORTH)
            return level.getBlockEntity(this.getBlockPos().north());
        if(direction == Direction.SOUTH)
            return level.getBlockEntity(this.getBlockPos().south());
        if(direction == Direction.WEST)
            return level.getBlockEntity(this.getBlockPos().west());
        if(direction == Direction.EAST)
            return level.getBlockEntity(this.getBlockPos().east());

        return this;
    }
    @Override
    public void tick() {
        super.tick();
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

