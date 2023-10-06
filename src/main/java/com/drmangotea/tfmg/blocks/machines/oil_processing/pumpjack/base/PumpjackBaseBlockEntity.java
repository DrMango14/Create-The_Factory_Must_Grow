package com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.base;


import com.drmangotea.tfmg.blocks.deposits.FluidDepositBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.crank.PumpjackCrankBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer_holder.PumpjackHammerHolderBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.machine_input.MachineInputBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
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

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class PumpjackBaseBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public BlockPos crankPos = this.getBlockPos();

    protected LazyOptional<IFluidHandler> fluidCapability;
    public FluidTank tankInventory;
    public FluidDepositBlockEntity deposit;
    public Direction direction = this.getBlockState().getValue(FACING).getOpposite();
    int debugCounter = 0;
    public int miningRate = 0;
    int depositCheckTimer = 0;

    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;


    public PumpjackBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();
        fluidCapability = LazyOptional.of(() -> tankInventory);

        refreshCapability();
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        Lang.translate("goggles.pumpjack_info")
                .forGoggles(tooltip);
        if (!isComplete()) {
            Lang.translate("goggles.pumpjack.part_missing")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip);
            if(isWronglyRotated()){
                Lang.translate("goggles.pumpjack.wrong_rotation1")
                        .style(ChatFormatting.GOLD)
                        .forGoggles(tooltip);
                Lang.translate("goggles.pumpjack.wrong_rotation2")
                        .style(ChatFormatting.GOLD)
                        .forGoggles(tooltip);
            }
            return true;
        }


        LangBuilder mb = Lang.translate("generic.unit.millibuckets");


        Lang.translate("goggles.pumpjack.deposit_info")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);


        if (!(deposit == null || deposit.fluidAmount == 0)) {
            Lang.translate("goggles.pumpjack.fluid_amount")
                    .style(ChatFormatting.DARK_GRAY)
                    .add(
                            Lang.translate("pumpjack_deposit_amount", this.deposit.baseFluidAmount)
                                    .style(ChatFormatting.BLUE)
                            //   .add(mb))
                    ).forGoggles(tooltip, 1);


        } else {

            Lang.translate("goggles.zero")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip, 1);
        }


        return true;


    }


    public void process() {
        if (deposit == null || deposit.fluidAmount == 0)
            return;

        if (tankInventory.getFluidAmount() + miningRate > 1000)
            return;

        deposit.fluidAmount -= miningRate;
        tankInventory.setFluid(new FluidStack(deposit.getDepositFluid(), tankInventory.getFluidAmount() + miningRate));
    }

    public boolean hasPipe() {

        for (int i = -62; i != getBlockPos().getY(); i++) {
            BlockPos pos = new BlockPos(getBlockPos().getX(), i, getBlockPos().getZ());
            if (!(level.getBlockState(pos).is(TFMGBlocks.INDUSTRIAL_PIPE.get())))
                return false;
        }

        return true;

    }

    public void findDeposit() {
        for (int i = 0; i < this.getBlockPos().getY() + 64; i++) {
            debugCounter = this.getBlockPos().getY() - i;
            BlockPos checkedPos = new BlockPos(this.getBlockPos().getX(), (this.getBlockPos().getY() - 1) - i, this.getBlockPos().getZ());

            if (level.getBlockState(new BlockPos(checkedPos)).is(TFMGBlocks.OIL_DEPOSIT.get())) {
                deposit = (FluidDepositBlockEntity) level.getBlockEntity(checkedPos);
                return;
            }

            if (!(level.getBlockState(new BlockPos(checkedPos)).is(TFMGBlocks.INDUSTRIAL_PIPE.get()))) {
                deposit = null;
                return;
            }


        }
        debugCounter = 0;
        deposit = null;
        return;

        /*
        if(!hasPipe()) {
            deposit = null;
            return;
        }

        if(level.getBlockEntity(new BlockPos(getBlockPos().getX(),-63,getBlockPos().getZ())) instanceof FluidDepositTileEntity) {
            deposit = (FluidDepositTileEntity) (level.getBlockEntity(new BlockPos(getBlockPos().getX(),-64,getBlockPos().getZ())));
        }else {
            deposit=null;
        }

         */
    }

    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(1000, this::onFluidStackChanged) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(TFMGFluids.CRUDE_OIL.getSource());
            }
        };
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
    }

    @Override
    public void tick() {
        super.tick();
        if (!isComplete())
            return;


        MachineInputBlockEntity input = null;
        if (level.getBlockEntity(crankPos.below()) instanceof MachineInputBlockEntity)
            input = (MachineInputBlockEntity) level.getBlockEntity(crankPos.below());
        if (input == null)
            return;
        miningRate = input.powerLevel * 12;


        depositCheckTimer++;
        if (depositCheckTimer > 50) {
            depositCheckTimer = 0;
            findDeposit();

        }


        direction = this.getBlockState().getValue(FACING).getOpposite();

        process();
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

        tankInventory.setCapacity(1000);
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

    public boolean isComplete() {
        BlockPos hammerPos = this.getBlockPos();
        crankPos = this.getBlockPos();
        if (direction == Direction.WEST) {
            hammerPos = new BlockPos(this.getBlockPos().west(2).above(2));
            crankPos = new BlockPos(this.getBlockPos().west(4).above(1));
        }
        if (direction == Direction.EAST) {
            hammerPos = new BlockPos(this.getBlockPos().east(2).above(2));
            crankPos = new BlockPos(this.getBlockPos().east(4).above(1));
        }
        if (direction == Direction.NORTH) {
            hammerPos = new BlockPos(this.getBlockPos().north(2).above(2));
            crankPos = new BlockPos(this.getBlockPos().north(4).above(1));
        }
        if (direction == Direction.SOUTH) {
            hammerPos = new BlockPos(this.getBlockPos().south(2).above(2));
            crankPos = new BlockPos(this.getBlockPos().south(4).above(1));
        }


        if (!(level.getBlockEntity(hammerPos) instanceof PumpjackHammerHolderBlockEntity &&
                level.getBlockEntity(crankPos) instanceof PumpjackCrankBlockEntity)) {
            return false;
        }
        //MachineInputTileEntity input = (MachineInputTileEntity) level.getBlockEntity(crankPos.below());
        // if(input.powerLevel==0)
        //   return false;


        if (level.getBlockEntity(hammerPos).getBlockState().getValue(FACING).getOpposite() == direction
                && level.getBlockEntity(crankPos).getBlockState().getValue(FACING).getOpposite() == direction
        )
            return true;

        return false;

    }

    public boolean isWronglyRotated() {
        if (isComplete())
            return false;


        BlockPos hammerPos1 = this.getBlockPos();
        BlockPos hammerPos2 = this.getBlockPos();
        BlockPos hammerPos3 = this.getBlockPos();
        crankPos = this.getBlockPos();
        if (direction == Direction.WEST) {
            hammerPos1 = new BlockPos(this.getBlockPos().east(2).above(2));
            hammerPos2 = new BlockPos(this.getBlockPos().north(2).above(2));
            hammerPos3 = new BlockPos(this.getBlockPos().south(2).above(2));
        }
        if (direction == Direction.EAST) {
            hammerPos1 = new BlockPos(this.getBlockPos().west(2).above(2));
            hammerPos2 = new BlockPos(this.getBlockPos().north(2).above(2));
            hammerPos3 = new BlockPos(this.getBlockPos().south(2).above(2));
        }
        if (direction == Direction.NORTH) {
            hammerPos1 = new BlockPos(this.getBlockPos().south(2).above(2));
            hammerPos2 = new BlockPos(this.getBlockPos().west(2).above(2));
            hammerPos3 = new BlockPos(this.getBlockPos().east(2).above(2));
        }
        if (direction == Direction.SOUTH) {
            hammerPos1 = new BlockPos(this.getBlockPos().north(2).above(2));
            hammerPos2 = new BlockPos(this.getBlockPos().east(2).above(2));
            hammerPos3 = new BlockPos(this.getBlockPos().west(2).above(2));
        }


        BlockState hammer1 = level.getBlockState(hammerPos1);
        BlockState hammer2 = level.getBlockState(hammerPos2);
        BlockState hammer3 = level.getBlockState(hammerPos3);


        return  hammer1.is(TFMGBlocks.PUMPJACK_HAMMER_HOLDER.get())||
                hammer2.is(TFMGBlocks.PUMPJACK_HAMMER_HOLDER.get())||
                hammer3.is(TFMGBlocks.PUMPJACK_HAMMER_HOLDER.get());


    }
}
