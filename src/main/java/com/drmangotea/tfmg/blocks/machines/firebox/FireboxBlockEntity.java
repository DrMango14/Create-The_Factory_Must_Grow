package com.drmangotea.tfmg.blocks.machines.firebox;


import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class FireboxBlockEntity extends SmartBlockEntity {

	public static final int MAX_HEAT_CAPACITY = 25000;

	protected FuelType activeFuel;
	protected int remainingBurnTime;


	protected LazyOptional<IFluidHandler> fluidCapability;

	public FluidTank tankInventory;

	public FluidTank exhaustTank;





	public FireboxBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		activeFuel = FuelType.NONE;


		tankInventory = createInventory(true);
		exhaustTank = createInventory(false);
		remainingBurnTime = 0;
		fluidCapability = LazyOptional.of(() -> new CombinedTankWrapper(tankInventory, exhaustTank));


	}



	@Override
	public void tick() {
		super.tick();


		if (level.isClientSide&&!isVirtual()) {
			spawnParticles(getHeatLevelFromBlock(), 1);
			return;
		}

		if(tankInventory.getFluid().getAmount()==1000&&remainingBurnTime<=3) {
			tankInventory.setFluid(FluidStack.EMPTY);
			remainingBurnTime = 4000;
			activeFuel = FuelType.NORMAL;
			spawnParticleBurst(false);
			playSound();
		}

		if (remainingBurnTime > 0) {
			exhaustTank.fill(new FluidStack(TFMGFluids.CARBON_DIOXIDE.getSource(),15), IFluidHandler.FluidAction.EXECUTE);
			remainingBurnTime--;
		}

		if(exhaustTank.getSpace()==0){
			remainingBurnTime = 0;
		}


		if (activeFuel == FuelType.NORMAL)
			updateBlockState();
		if (remainingBurnTime > 0)
			return;

		if (activeFuel == FuelType.SPECIAL) {
			activeFuel = FuelType.NORMAL;
			remainingBurnTime = MAX_HEAT_CAPACITY / 2;
		} else
			activeFuel = FuelType.NONE;

		updateBlockState();
	}


	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {


			tankInventory.writeToNBT(compound.getCompound("TankContent"));
			exhaustTank.writeToNBT(compound.getCompound("ExhaustContent"));


			compound.putInt("fuelLevel", activeFuel.ordinal());
			compound.putInt("burnTimeRemaining", remainingBurnTime);


		super.write(compound, clientPacket);
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		activeFuel = FuelType.values()[compound.getInt("fuelLevel")];
		remainingBurnTime = compound.getInt("burnTimeRemaining");

		tankInventory.readFromNBT(compound.getCompound("TankContent"));
		exhaustTank.readFromNBT(compound.getCompound("ExhaustContent"));


		super.read(compound, clientPacket);
	}

	@Nonnull
	@Override
	@SuppressWarnings("removal")
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {

		if (cap == ForgeCapabilities.FLUID_HANDLER)
			return fluidCapability.cast();
		return super.getCapability(cap, side);
	}

	public HeatLevel getHeatLevelFromBlock() {
		return FireboxBlock.getHeatLevelOf(getBlockState());
	}

	public void updateBlockState() {
		setBlockHeat(getHeatLevel());
	}

	protected void setBlockHeat(HeatLevel heat) {
		HeatLevel inBlockState = getHeatLevelFromBlock();
		if (inBlockState == heat)
			return;
		level.setBlockAndUpdate(worldPosition, getBlockState().setValue(FireboxBlock.HEAT_LEVEL, heat));
		notifyUpdate();
	}
	protected SmartFluidTank createInventory(boolean mainTank) {
		return new SmartFluidTank(mainTank ? 1000 : 8000, this::onFluidStackChanged) {


			@Override
			public FluidStack drain(FluidStack resource, FluidAction action) {
				if (mainTank)
					return FluidStack.EMPTY;
				return super.drain(resource, action);
			}

			@Override
			public FluidStack drain(int maxDrain, FluidAction action) {
				if (mainTank)
					return FluidStack.EMPTY;
				return super.drain(maxDrain, action);
			}

			@Override
			public boolean isFluidValid(FluidStack stack) {
				if(!mainTank){
					return stack.getFluid() == TFMGFluids.CARBON_DIOXIDE.getSource();
				}



				return stack.getFluid().is(TFMGTags.TFMGFluidTags.DIESEL.tag)
						||stack.getFluid().is(TFMGTags.TFMGFluidTags.LPG.tag)
						||stack.getFluid().is(TFMGTags.TFMGFluidTags.KEROSENE.tag)
						;
			}
		};
	}
	protected void onFluidStackChanged(FluidStack newFluidStack) {




		sendData();
	}

	public boolean isValidBlockAbove() {
		if (isVirtual())
			return false;
		BlockState blockState = level.getBlockState(worldPosition.above());
		return AllBlocks.BASIN.has(blockState) || blockState.getBlock() instanceof FluidTankBlock;
	}

	protected void playSound() {
		level.playSound(null, worldPosition, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS,
			.125f + level.random.nextFloat() * .125f, .75f - level.random.nextFloat() * .25f);
	}

	protected HeatLevel getHeatLevel() {
		HeatLevel level = HeatLevel.SMOULDERING;
		switch (activeFuel) {
		case SPECIAL:
			level = HeatLevel.SEETHING;
			break;
		case NORMAL:
			boolean lowPercent = (double) remainingBurnTime / MAX_HEAT_CAPACITY < 0.0125;
			level = lowPercent ? HeatLevel.FADING : HeatLevel.KINDLED;
			break;
		default:
		case NONE:
			break;
		}
		return level;
	}

	protected void spawnParticles(HeatLevel heatLevel, double burstMult) {
		if (level == null)
			return;
		if (heatLevel == HeatLevel.NONE)
			return;

		RandomSource r = level.getRandom();

		Vec3 c = VecHelper.getCenterOf(worldPosition);
		Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .125f)
			.multiply(1, 0, 1));
		
		if (r.nextInt(4) != 0)
			return;

		boolean empty = level.getBlockState(worldPosition.above())
			.getCollisionShape(level, worldPosition.above())
			.isEmpty();
		
		//if (empty || r.nextInt(8) == 0)
		//	level.addParticle(ParticleTypes.LARGE_SMOKE, v.x, v.y, v.z, 0, 0, 0);

		double yMotion = empty ? .0625f : r.nextDouble() * .0125f;
		Vec3 v2 = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .5f)
			.multiply(1, .25f, 1)
			.normalize()
			.scale((empty ? .25f : .5) + r.nextDouble() * .125f))
			.add(0, .5, 0);

		if (heatLevel.isAtLeast(HeatLevel.SEETHING)) {
			level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, v2.x, v2.y, v2.z, 0, yMotion, 0);
		} else if (heatLevel.isAtLeast(HeatLevel.FADING)) {
			level.addParticle(ParticleTypes.FLAME, v2.x, v2.y, v2.z, 0, yMotion, 0);
		}
		return;
	}

	public void spawnParticleBurst(boolean soulFlame) {
		Vec3 c = VecHelper.getCenterOf(worldPosition);
		RandomSource r = level.random;
		for (int i = 0; i < 20; i++) {
			Vec3 offset = VecHelper.offsetRandomly(Vec3.ZERO, r, .5f)
				.multiply(1, .25f, 1)
				.normalize();
			Vec3 v = c.add(offset.scale(.5 + r.nextDouble() * .125f))
				.add(0, .125, 0);
			Vec3 m = offset.scale(1 / 32f);

			level.addParticle(soulFlame ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, v.x, v.y, v.z, m.x, m.y,
				m.z);
		}
	}

	public enum FuelType {
		NONE, NORMAL, SPECIAL
	}

}
