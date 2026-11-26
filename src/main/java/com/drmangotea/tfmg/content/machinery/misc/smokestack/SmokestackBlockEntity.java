package com.drmangotea.tfmg.content.machinery.misc.smokestack;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.fluid.ForceableFluidTank;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

import static com.drmangotea.tfmg.content.machinery.misc.smokestack.SmokestackBlock.TOP;

public class SmokestackBlockEntity extends SmartBlockEntity {
    int smokeTimer = 0;

    public ForceableFluidTank tankInventory;
    protected IFluidHandler fluidCapability;

    protected boolean updateCapability;

    public SmokestackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = new ForceableFluidTank(16000, this::onFluidStackChanged)
			.allowInsertion().blockExtraction()
			.withValidator((stack) -> stack.is(TFMGTags.Fluids.EXHAUSTABLE.tag));
        fluidCapability = tankInventory;
        updateCapability = false;
        refreshCapability();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.SMOKESTACK.get(),
                (be, context) -> be.fluidCapability
        );
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    public void refreshCapability() {
        fluidCapability = tankInventory;
        invalidateCapabilities();
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        tankInventory.readFromNBT(registries,compound.getCompound("TankContent"));
        smokeTimer = compound.getInt("Timer");
        updateCapability = true;
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;
        setChanged();
        sendData();
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null) return;
        if (smokeTimer > 0) {
			TFMGUtils.spawnSmokeParticles(level, getBlockPos());
            smokeTimer--;
        }

        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }

        if (tankInventory.isEmpty())
            return;

        if (getBlockState().getValue(TOP)) {
            tankInventory.forceDrain(1000, IFluidHandler.FluidAction.EXECUTE);
            smokeTimer = 500;
        }

        if (level != null && level.getBlockEntity(getBlockPos().above()) instanceof SmokestackBlockEntity be) {
			int drain = be.tankInventory.fill(tankInventory.getFluid(), IFluidHandler.FluidAction.EXECUTE);
            tankInventory.forceDrain(drain, IFluidHandler.FluidAction.EXECUTE);
		}
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.put("TankContent", tankInventory.writeToNBT(registries,new CompoundTag()));
        compound.putInt("Timer", smokeTimer);
    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }
}
