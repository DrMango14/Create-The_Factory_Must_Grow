package com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.base;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.fluid.ForceableFluidTank;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.crank.PumpjackCrankBlockEntity;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.hammer.PumpjackBlockEntity;
import com.drmangotea.tfmg.content.world.resevoir.FluidReservoir;
import com.drmangotea.tfmg.registry.*;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

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

import java.util.List;

public class PumpjackBaseBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    public PumpjackBlockEntity controllerHammer;
    public boolean isRunning = false;
    int depositCheckTimer = 0;
    public int miningRate = 0;
    protected IFluidHandler fluidCapability;
    public ForceableFluidTank tank;
    public BlockPos deposit;

    protected boolean updateCapability;


    public PumpjackBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tank = new ForceableFluidTank(8000, this::onFluidStackChanged).blockInsertion().withValidator(fs -> fs.is(TFMGTags.Fluids.CRUDE_OIL.tag));
        this.fluidCapability = tank;
        updateCapability = false;
        refreshCapability();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.PUMPJACK_BASE.get(),
                (be, context) -> {
                    if (context == be.getBlockState().getValue(PumpjackBaseBlock.FACING)) {
                        return be.fluidCapability;
                    }
                    return null;
                }
        );
    }

    @Override
    public void tick() {
        super.tick();
        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
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

    public void refreshCapability() {
        fluidCapability = tank;
        invalidateCapabilities();
    }

    public void findDeposit() {
        for (int i = 0; i < this.getBlockPos().getY() + 64; i++) {
            BlockPos checkedPos = new BlockPos(this.getBlockPos().getX(), (this.getBlockPos().getY() - 1) - i, this.getBlockPos().getZ());
            if (level.getBlockState(new BlockPos(checkedPos)).is(TFMGBlocks.OIL_DEPOSIT.get())) {
                deposit = checkedPos;
                return;
            }
            if (!(level.getBlockState(new BlockPos(checkedPos)).is(TFMGTags.Blocks.INDUSTRIAL_PIPE.tag))) {
                deposit = null;
                return;
            }
        }
        deposit = null;
    }

    public void process() {
        if (level == null)
            return;
        if (deposit == null)
            return;

        // Old chunks wouldn't have a proper reservoir
        FluidReservoir reservoir = level.getChunkAt(deposit).getExistingDataOrNull(TFMGDataAttachments.FLUID_RESERVOIR);
        if (reservoir == null)
            return;

        if (tank.getFluidAmount() + miningRate > tank.getCapacity())
            return;
        if (!TFMGConfigs.common().worldGen.infiniteDeposits.get() && reservoir.isEmpty()) {
            deposit = null;
            findDeposit();
            return;
        }
        int amountPumped = tank.forceFill(new FluidStack(TFMGFluids.CRUDE_OIL.get().getSource(), miningRate), IFluidHandler.FluidAction.EXECUTE);
        sendData();
        if (amountPumped == 0)
            return;

        if (TFMGConfigs.common().worldGen.infiniteDeposits.get())
            return;

        RandomSource randomSource = level.getRandom();
        //fix
        if (randomSource.nextInt((900000 / amountPumped) + 1) == 0) {
            reservoir.drain(1);
            if (reservoir.isEmpty()) {
                deposit = null;
                findDeposit();
            }
            level.getChunkAt(deposit).setData(TFMGDataAttachments.FLUID_RESERVOIR, reservoir);
        }
    }

    public void setControllerHammer(PumpjackBlockEntity controllerHammer) {
        this.controllerHammer = controllerHammer;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }



    protected void onFluidStackChanged(FluidStack newFluidStack) {
        sendData();
        setChanged();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        TFMGTexts.header("pumpjack").forGoggles(tooltip);
        if (deposit == null) {
            TFMGTexts.invalidMachine().forGoggles(tooltip, 1);
        }
        TFMGUtils.createFluidTooltip(tooltip, fluidCapability);
        return true;
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        tank.readFromNBT(registries,compound.getCompound("TankContent"));
        updateCapability = true;
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
