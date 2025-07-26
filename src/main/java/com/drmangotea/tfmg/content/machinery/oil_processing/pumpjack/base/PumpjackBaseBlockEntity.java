package com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.crank.PumpjackCrankBlockEntity;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.hammer.PumpjackBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class PumpjackBaseBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    public PumpjackBlockEntity controllerHammer;
    public boolean isRunning = false;
    int depositCheckTimer = 0;
    public int miningRate = 0;
    protected IFluidHandler fluidCapability;
    public FluidTank tank;
    public BlockPos deposit;


    public PumpjackBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tank = createInventory();
        fluidCapability = tank;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.PUMPJACK_BASE.get(),
                (be, context) -> be.fluidCapability
        );
    }


    @Override
    public void tick() {
        super.tick();


        if (controllerHammer != null)
            if (!(level.getBlockEntity(controllerHammer.getBlockPos()) instanceof PumpjackBlockEntity))
                controllerHammer = null;
        if (controllerHammer != null)
            if (controllerHammer.base == null)
                controllerHammer = null;

        if (controllerHammer != null)
            if (!controllerHammer.isRunning())
                controllerHammer = null;

        if (controllerHammer == null)
            return;
        isRunning = controllerHammer.isRunning();

        if (!isRunning) {
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
        PumpjackCrankBlockEntity crank = null;
        if (controllerHammer.crank != null)
            crank = controllerHammer.crank;

        if (crank == null)
            return;
        miningRate = (int) Math.abs(crank.getMachineInputSpeed() * (crank.heightModifier));
        process();


    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        // TFMG.DEPOSITS.removeEmptyDeposits();
    }

    public void findDeposit() {
        for (int i = 0; i < this.getBlockPos().getY() + 64; i++) {
            BlockPos checkedPos = new BlockPos(this.getBlockPos().getX(), (this.getBlockPos().getY() - 1) - i, this.getBlockPos().getZ());
            if (level.getBlockState(new BlockPos(checkedPos)).is(TFMGBlocks.OIL_DEPOSIT.get())) {
                deposit = checkedPos;
                return;
            }
            if (!(level.getBlockState(new BlockPos(checkedPos)).is(TFMGTags.TFMGBlockTags.INDUSTRIAL_PIPE.tag))) {
                deposit = null;
                return;
            }
        }
        deposit = null;
    }

    public void process() {
        if (deposit == null)
            return;


        //if (TFMG.DEPOSITS.depositData == null) {
        //    return;
        //}
        if (!level.isClientSide)
            if (!TFMG.DEPOSITS.containsDeposit(deposit.asLong())) {
                TFMG.DEPOSITS.addDeposit(level, deposit.asLong());
                TFMG.DEPOSITS.markDirty();
                sendData();
            }


        if (tank.getFluidAmount() + miningRate > tank.getCapacity())
            return;
        int amountPumped = tank.fill(new FluidStack(TFMGFluids.CRUDE_OIL.get().getSource(), miningRate), IFluidHandler.FluidAction.EXECUTE);
        sendData();

        if (amountPumped == 0)
            return;

        if (TFMGConfigs.common().worldgen.infiniteDeposits.get())
            return;

        RandomSource randomSource = level.getRandom();
        //fix
        //if (randomSource.nextInt(((900000) / amountPumped) + 1) == 0) {
//
        //    TFMG.DEPOSITS.getReservoirFor(deposit.asLong()).oilReserves--;
        //    //  TFMG.DEPOSITS.depositData.setDirty();
        //    if (TFMG.DEPOSITS.getReservoirFor(deposit.asLong()).oilReserves <= 0) {
        //        TFMG.LOGGER.debug("EPIC REMOVAL");
        //        TFMG.DEPOSITS.removeDeposit(deposit.asLong());
        //        level.setBlock(deposit, Blocks.BEDROCK.defaultBlockState(), 3);
        //        deposit = null;
        //        findDeposit();
        //    }
        //}


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

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        sendData();
        setChanged();
    }

    @Override
    @SuppressWarnings("removal")
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CreateLang.translate("goggles.pumpjack_info")
                .forGoggles(tooltip);
        if (deposit == null) {
            CreateLang.translate("goggles.zero")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip, 1);
        }

        TFMGUtils.createFluidTooltip(this, tooltip);
        return true;
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        tank.readFromNBT(registries,compound.getCompound("TankContent"));
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {

        compound.put("TankContent", tank.writeToNBT(registries,new CompoundTag()));
        super.write(compound,registries , clientPacket);
    }


    //@Nonnull
    //@Override
    //@SuppressWarnings("removal")
    //public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    //    if (cap == ForgeCapabilities.FLUID_HANDLER)
    //        return fluidCapability.cast();
    //    return super.getCapability(cap, side);
    //}
}
