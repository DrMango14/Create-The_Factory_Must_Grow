package com.drmangotea.tfmg.content.machinery.misc.exhaust;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.fluid.ForceableFluidTank;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

public class ExhaustBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    protected IFluidHandler fluidCapability;
    public final ForceableFluidTank tankInventory;

    public boolean spawnsSmoke = false;
    public int smokeTimer = 0;

    protected boolean updateCapability;

    public ExhaustBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = new ForceableFluidTank(4000, this::onFluidStackChanged)
			.allowInsertion().blockExtraction()
			.withValidator((stack) -> stack.is(TFMGTags.Fluids.EXHAUSTABLE.tag));
        fluidCapability = tankInventory;
        updateCapability = false;
        refreshCapability();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.EXHAUST.get(),
                (be, context) -> be.fluidCapability
        );
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return TFMGUtils.createFluidTooltip(tooltip, fluidCapability);
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        sendData();
        setChanged();
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null) return;

        Direction direction = this.getBlockState().getValue(ExhaustBlock.FACING);

        if(smokeTimer != 0) {
            spawnsSmoke = true;
            smokeTimer--;
        } else spawnsSmoke = false;

        if (spawnsSmoke) { makeParticles(level, this.getBlockPos(), direction); }

        if (tankInventory.getFluidAmount() > 0) {
            smokeTimer = 100;
            spawnsSmoke = true;
            tankInventory.forceDrain(500, IFluidHandler.FluidAction.EXECUTE);
        }

        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        tankInventory.readFromNBT(registries,compound.getCompound("TankContent"));
        smokeTimer = compound.getInt("Timer");

        updateCapability = true;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.put("TankContent", tankInventory.writeToNBT(registries,new CompoundTag()));
        compound.putInt("Timer", smokeTimer);
    }

    public void makeParticles(Level level, BlockPos pos, Direction direction) {
        SubLevelAccess subLevel = SableCompanion.INSTANCE.getContaining(this);
        Vec3 center = pos.getCenter();

        if (subLevel != null) {
            center = subLevel.logicalPose().transformPosition(center);
        }

        RandomSource random = level.getRandom();
        if (random.nextInt(7) != 0) return;

        double offX = 0, offY = 0, offZ = 0;
        switch (direction) {
            case UP    -> { offX = random.nextFloat() * 0.3f; offY = 1;                    offZ = random.nextFloat() * 0.3f; }
            case DOWN  -> { offX = random.nextFloat();        offY = 0;                    offZ = random.nextFloat();        }
            case NORTH -> { offX = random.nextFloat();        offY = random.nextFloat();   offZ = 0;                        }
            case SOUTH -> { offX = random.nextFloat();        offY = random.nextFloat();   offZ = 1;                        }
            case WEST  -> { offX = 1;                         offY = random.nextFloat();   offZ = random.nextFloat();        }
            case EAST  -> { offX = 0;                         offY = random.nextFloat();   offZ = random.nextFloat();        }
        }

        TFMGUtils.spawnSmokeParticles(level,
			center.x + offX, center.y + offY, center.z + offZ
		);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    public void refreshCapability() {
        fluidCapability = tankInventory;
        invalidateCapabilities();
    }
}

