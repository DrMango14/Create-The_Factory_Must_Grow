package com.drmangotea.tfmg.content.machinery.misc.flarestack;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.fluid.ForceableFluidTank;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

public class FlarestackBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    public ForceableFluidTank tankInventory;
    public int smokeTimer = 0;

    public FlarestackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = new ForceableFluidTank(2500, this::onFluidStackChanged)
			.blockExtraction()
			.withValidator(
				(stack) ->  stack.is(TFMGTags.Fluids.FLAMMABLE.tag)||stack.is(TFMGTags.Fluids.FUEL.tag)
			);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.FLARESTACK.get(),
                (be, context) -> be.tankInventory
        );
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel()) return;
        sendData();
        setChanged();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return TFMGUtils.createFluidTooltip(tooltip, tankInventory);
    }



    @Override
    public void tick() {
        super.tick();
        if (level == null) return;
        level.invalidateCapabilities(getBlockPos());

        level.setBlock(getBlockPos(), this.getBlockState().setValue(FlarestackBlock.LIT, smokeTimer > 0), 2);

        if (smokeTimer > 0) {
            smokeTimer--;
            makeParticles(level, this.getBlockPos());
        }

        if ((tankInventory.isEmpty() || !tankInventory.isFluidValid(tankInventory.getFluid()))) {
            return;
        }

        if (tankInventory.getFluidAmount() > 0) {
            smokeTimer = 100;
            tankInventory.forceDrain(100, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public static void makeParticles(Level level, BlockPos pos) {
		TFMGUtils.spawnSmokeParticles(level, pos);
		
		RandomSource random = level.getRandom();
		if (random.nextInt(7) != 0) return;
		
		makeFlameParticles(level, pos, random);
		makeFlameParticles(level, pos, random);
		makeFlameParticles(level, pos, random);
	}
	public static void makeFlameParticles(Level level, BlockPos pos, RandomSource random) {
		level.addParticle(
			ParticleTypes.FLAME,
			pos.getX() + random.nextFloat(), pos.getY() + 1, pos.getZ() + random.nextFloat(),
			random.nextDouble()*0.28D - 0.14D, 0.14D, random.nextDouble()*0.28D - 0.14D
		);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        tankInventory.readFromNBT(registries,compound.getCompound("TankContent"));
        smokeTimer = compound.getInt("Timer");
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

