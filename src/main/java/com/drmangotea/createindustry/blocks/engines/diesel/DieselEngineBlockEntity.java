package com.drmangotea.createindustry.blocks.engines.diesel;


import com.drmangotea.createindustry.blocks.engines.diesel.engine_expansion.DieselEngineExpansionBlockEntity;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGFluids;
import com.drmangotea.createindustry.registry.TFMGSoundEvents;
import com.drmangotea.createindustry.registry.TFMGTags;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineValueBox;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.*;
import mekanism.common.recipe.lookup.cache.type.FluidInputCache;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;

import static com.drmangotea.createindustry.blocks.engines.diesel.DieselEngineBlock.FACE;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class DieselEngineBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

	//protected ScrollOptionBehaviour<WindmillBearingBlockEntity.RotationDirection> movementDirection;

	public WeakReference<PoweredShaftBlockEntity> target;
///////////

	public FluidTank fuelTank;
	protected FluidTank exhaustTank;

	protected FluidTank airTank;

	private int consumptionTimer=0;
	public float engineStrength = 0;


	private Couple<FluidTank> tanks;

	public BlockPos expansionPos;

	public DieselEngineExpansionBlockEntity expansionBE;


	protected LazyOptional<CombinedTankWrapper> fluidCapability;

	public DieselEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		target = new WeakReference<>(null);

		/////////




		fuelTank = createInventory(TFMGTags.TFMGFluidTags.DIESEL.tag, false);



		exhaustTank = createInventory(TFMGFluids.CARBON_DIOXIDE.getSource(),true);
		airTank = createInventory(TFMGFluids.AIR.getSource(),false);


		tanks = Couple.create(fuelTank, exhaustTank);

		fluidCapability = LazyOptional.of(() -> new CombinedTankWrapper(fuelTank,airTank, exhaustTank));


	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		//movementDirection = new ScrollOptionBehaviour<>(WindmillBearingBlockEntity.RotationDirection.class,
		//	Lang.translateDirect("contraptions.windmill.rotation_direction"), this, new DieselEngineValueBox());
		//movementDirection = new ScrollOptionBehaviour<>(WindmillBearingBlockEntity.RotationDirection.class,
		//		Lang.translateDirect("contraptions.windmill.rotation_direction"), this, new SteamEngineValueBox());
		//movementDirection.onlyActiveWhen(() -> {
		//	PoweredShaftBlockEntity shaft = getShaft();
		//	return shaft == null || !shaft.hasSource();
		//});
		//movementDirection.withCallback($ -> onDirectionChanged());
		//behaviours.add(movementDirection);
////////////////////////////////////////////
		behaviours.add(new DirectBeltInputBehaviour(this));


	}

	private void onDirectionChanged() {}

	@Override
	public void tick() {
		super.tick();
		Direction direction;





			if(getBlockState().getValue(FACE)==AttachFace.WALL)
				expansionPos = this.getBlockPos().relative(getBlockState().getValue(FACING).getOpposite());
		if(getBlockState().getValue(FACE)==AttachFace.CEILING)
			expansionPos = this.getBlockPos().above();
		if(getBlockState().getValue(FACE)==AttachFace.FLOOR)
			expansionPos = this.getBlockPos().below();




		if(expansionBE == null){
			if(level.getBlockEntity(expansionPos) instanceof DieselEngineExpansionBlockEntity
			){
				//if(direction==level.getBlockEntity(expansionPos).getBlockState().getValue(DirectionalBlock.FACING)) {
					expansionBE = (DieselEngineExpansionBlockEntity) level.getBlockEntity(expansionPos);
				//}else expansionBE = null;
			}else expansionBE = null;
		}
		if(expansionBE!=null)
			if(!(expansionBE instanceof DieselEngineExpansionBlockEntity))
				expansionBE = null;

		PoweredShaftBlockEntity shaft = getShaft();

		if ( shaft == null) {
			if (level.isClientSide())
				return;
			if (shaft == null)
				return;
			if (!shaft.getBlockPos()
				.subtract(worldPosition)
				.equals(shaft.enginePos))
				return;
			if (shaft.engineEfficiency == 0)
				return;
			Direction facing = DieselEngineBlock.getFacing(getBlockState());
			if (level.isLoaded(worldPosition.relative(facing.getOpposite())))
				shaft.update(worldPosition, 0, 0);
			return;
		}

		boolean verticalTarget = false;
		BlockState shaftState = shaft.getBlockState();
		Axis targetAxis = Axis.X;
		if (shaftState.getBlock()instanceof IRotate ir)
			targetAxis = ir.getRotationAxis(shaftState);
		verticalTarget = targetAxis == Axis.Y;

		BlockState blockState = getBlockState();
		if (!TFMGBlocks.DIESEL_ENGINE.has(blockState))
			return;
		Direction facing = DieselEngineBlock.getFacing(blockState);
		if (facing.getAxis() == Axis.Y)
			facing = blockState.getValue(DieselEngineBlock.FACING);

		if(!level.isClientSide)
			if(getShaft() != null)
				engineProcess(targetAxis,verticalTarget);
		//DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::makeSound);
		if(level.isClientSide)
			makeSound(targetAxis,verticalTarget);

		int conveyedSpeedLevel =
			engineStrength == 0 ? 1 : verticalTarget ? 1 : (int) GeneratingKineticBlockEntity.convertToDirection(1, facing)*2;
		if (targetAxis == Axis.Z)
			conveyedSpeedLevel *= -1;
		//if (movementDirection.get() == WindmillBearingBlockEntity.RotationDirection.COUNTER_CLOCKWISE)
		//	conveyedSpeedLevel *= -1;

		float shaftSpeed = shaft.getTheoreticalSpeed();
		if (shaft.hasSource() && shaftSpeed != 0 && conveyedSpeedLevel != 0
			&& (shaftSpeed > 0) != (conveyedSpeedLevel > 0)) {
			//movementDirection.setValue(1 - movementDirection.get()
			//	.ordinal());
			conveyedSpeedLevel *= -1;
		}

		//shaft.update(worldPosition,  conveyedSpeedLevel, engineStrength);

		//shaft.update(worldPosition,  conveyedSpeedLevel, 40);

	}



	//@OnlyIn(Dist.CLIENT)
	private void makeSound(Axis targetAxis, boolean verticalTarget){
		Float targetAngle = getTargetAngle();
		PoweredShaftBlockEntity ste = target.get();
		if (ste == null)
			return;
		//if (engineStrength == 0)
		//	return;
		PoweredShaftBlockEntity shaft = getShaft();

		if(tanks.get(true).isEmpty()||tanks.get(true).isEmpty()) {
			engineStrength=0;

			shaft.update(worldPosition, getConveyedSpeedLevel(engineStrength,targetAxis,verticalTarget), engineStrength);
			return;
		}

		if(expansionBE != null){
			if(airTank.isEmpty()&&expansionBE.airTank.isEmpty()){
				engineStrength = 0;
				shaft.update(worldPosition, getConveyedSpeedLevel(engineStrength,targetAxis,verticalTarget), engineStrength);
				return;
			}
		}else
		if(airTank.isEmpty()){
			engineStrength = 0;
			shaft.update(worldPosition, getConveyedSpeedLevel(engineStrength,targetAxis,verticalTarget), engineStrength);
			return;
		}

		if(tanks.get(false).getFluidAmount()+5>1000) {
			engineStrength = 0;
			shaft.update(worldPosition, getConveyedSpeedLevel(engineStrength,targetAxis,verticalTarget), engineStrength);
			return;
		}

		if (targetAngle == null)
			return;

		float angle = AngleHelper.deg(targetAngle);
		angle += (angle < 0) ? -180 + 75 : 360 - 75;
		angle %= 360;


		if (shaft == null || shaft.getSpeed() == 0)
			return;

		if (angle >= 0 && !(prevAngle > 180 && angle < 180)) {
			prevAngle = angle;
			return;
		}

		if (angle < 0 && !(prevAngle < -180 && angle > -180)) {
			prevAngle = angle;
			return;
		}

		TFMGSoundEvents.DIESEL_ENGINE.playAt(level, worldPosition, 0.4f, 1f, false);

		prevAngle = angle;
	}

	public int getConveyedSpeedLevel(float strength,Axis targetAxis, boolean verticalTarget){
		Direction facing = SteamEngineBlock.getFacing(getBlockState());
		PoweredShaftBlockEntity shaft = getShaft();
		int conveyedSpeedLevel =
				engineStrength == 0 ? 1 : verticalTarget ? 1 : (int) GeneratingKineticBlockEntity.convertToDirection(1, facing)*2;
		if (targetAxis == Axis.Z)
			conveyedSpeedLevel *= -1;
		//if (movementDirection.get() == WindmillBearingBlockEntity.RotationDirection.COUNTER_CLOCKWISE)
		//	conveyedSpeedLevel *= -1;


		float shaftSpeed = shaft.getTheoreticalSpeed();
		if (shaft.hasSource() && shaftSpeed != 0 && conveyedSpeedLevel != 0
				&& (shaftSpeed > 0) != (conveyedSpeedLevel > 0)) {
			//movementDirection.setValue(1 - movementDirection.get()
			//		.ordinal());
			conveyedSpeedLevel *= -1;
		}

		return conveyedSpeedLevel;
	}

	private void engineProcess(Axis targetAxis, boolean verticalTarget) {
		PoweredShaftBlockEntity shaft = getShaft();
		if(tanks.get(true).isEmpty()||tanks.get(true).isEmpty()) {
			engineStrength=0;

			shaft.update(worldPosition, getConveyedSpeedLevel(engineStrength,targetAxis,verticalTarget), engineStrength);
			return;
		}

		if(expansionBE != null){
			if(airTank.isEmpty()&&expansionBE.airTank.isEmpty()){
				engineStrength = 0;
				shaft.update(worldPosition, getConveyedSpeedLevel(engineStrength,targetAxis,verticalTarget), engineStrength);
				return;
			}
		}else
		if(airTank.isEmpty()){
			engineStrength = 0;
			shaft.update(worldPosition, getConveyedSpeedLevel(engineStrength,targetAxis,verticalTarget), engineStrength);
			return;
		}

		if(tanks.get(false).getFluidAmount()+5>1000) {
			engineStrength = 0;
			shaft.update(worldPosition, getConveyedSpeedLevel(engineStrength,targetAxis,verticalTarget), engineStrength);
			return;
		}




		if(consumptionTimer>=5) {
			//if(signal!=0)
			int fuelModifier = 0;
				if(expansionBE!=null)
					if(!expansionBE.coolantTank.isEmpty())
						fuelModifier = 1;


				fuelTank.setFluid(new FluidStack(TFMGFluids.DIESEL.getSource(),fuelTank.getFluidAmount()-(2-fuelModifier)));


		}
				//airTank.drain(1, IFluidHandler.FluidAction.EXECUTE);
			if(!airTank.isEmpty()) {
				airTank.setFluid(new FluidStack(TFMGFluids.AIR.getSource(), airTank.getFluidAmount() - 5));
			}else expansionBE.airTank.setFluid(new FluidStack(TFMGFluids.AIR.getSource(), expansionBE.airTank.getFluidAmount() - 5));
			exhaustTank.fill(new FluidStack(TFMGFluids.CARBON_DIOXIDE.getSource(),3), IFluidHandler.FluidAction.EXECUTE);
				//tanks.get(false).setFluid(new FluidStack(TFMGFluids.CARBON_DIOXIDE.getSource(), tanks.get(false).getFluidAmount()+1));



		int strengthModifier = 0;

		if(expansionBE !=null) {
			if (!expansionBE.coolantTank.isEmpty()){
				strengthModifier +=3;
				if(consumptionTimer>5)
					expansionBE.coolantTank.setFluid(new FluidStack(TFMGFluids.COOLING_FLUID.getSource(), expansionBE.coolantTank.getFluidAmount() - 1));
			}
			if (!expansionBE.lubricationOilTank.isEmpty()){
				strengthModifier +=5;
				if(consumptionTimer>5)
					expansionBE.lubricationOilTank.setFluid(new FluidStack(TFMGFluids.LUBRICATION_OIL.getSource(), expansionBE.lubricationOilTank.getFluidAmount() - 1));

			}
		}

		if(!(level.getBlockState(expansionPos).is(TFMGBlocks.DIESEL_ENGINE_EXPANSION.get()))){
			strengthModifier =0;
		}


		if(consumptionTimer>5)
			consumptionTimer=0;

		consumptionTimer++;

		engineStrength=40+strengthModifier;
			shaft.update(worldPosition, getConveyedSpeedLevel(engineStrength,targetAxis,verticalTarget), engineStrength);
			sendData();
			setChanged();



	}

	@Override
	public void remove() {
		PoweredShaftBlockEntity shaft = getShaft();
		if (shaft != null)
			shaft.remove(worldPosition);
		super.remove();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected AABB createRenderBoundingBox() {
		return super.createRenderBoundingBox().inflate(2);
	}

	public PoweredShaftBlockEntity getShaft() {
		PoweredShaftBlockEntity shaft = target.get();
		if (shaft == null || shaft.isRemoved() || !shaft.canBePoweredBy(worldPosition)) {
			if (shaft != null)
				target = new WeakReference<>(null);
			Direction facing = DieselEngineBlock.getFacing(getBlockState());
			BlockEntity anyShaftAt = level.getBlockEntity(worldPosition.relative(facing, 2));
			if (anyShaftAt instanceof PoweredShaftBlockEntity ps && ps.canBePoweredBy(worldPosition))
				target = new WeakReference<>(shaft = ps);
		}
		return shaft;
	}



	float prevAngle = 0;



	@Nullable
	@OnlyIn(Dist.CLIENT)
	public Float getTargetAngle() {
		float angle = 0;
		BlockState blockState = getBlockState();
		if (!TFMGBlocks.DIESEL_ENGINE.has(blockState))
			return null;

		Direction facing = SteamEngineBlock.getFacing(blockState);
		PoweredShaftBlockEntity shaft = getShaft();
		Axis facingAxis = facing.getAxis();
		Axis axis = Axis.Y;

		if (shaft == null)
			return null;

		axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);
		angle = KineticBlockEntityRenderer.getAngleForTe(shaft, shaft.getBlockPos(), axis);

		if (axis == facingAxis)
			return null;
		if (axis.isHorizontal() && (facingAxis == Axis.X ^ facing.getAxisDirection() == AxisDirection.POSITIVE))
			angle *= -1;
		if (axis == Axis.X && facing == Direction.DOWN)
			angle *= -1;
		return angle;
	}
/*
	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		PoweredShaftBlockEntity shaft = getShaft();
		return shaft == null ? false : shaft.addToEngineTooltip(tooltip, isPlayerSneaking);
	}
	
 */
////////////////////////////////////

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {

		compound.put("Fuel", fuelTank.writeToNBT(new CompoundTag()));
		compound.put("Air", airTank.writeToNBT(new CompoundTag()));

		compound.put("Exhaust", exhaustTank.writeToNBT(new CompoundTag()));


		super.write(compound, clientPacket);
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		fuelTank.readFromNBT(compound.getCompound("Fuel"));

		airTank.readFromNBT(compound.getCompound("Air"));

		exhaustTank.readFromNBT(compound.getCompound("Exhaust"));



		super.read(compound, clientPacket);
	}


	@Override
	public void invalidate() {
		super.invalidate();

		fluidCapability.invalidate();
	}

	@Nonnull
	@Override
	@SuppressWarnings("removal")
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {

		if (cap == ForgeCapabilities.FLUID_HANDLER)
			return fluidCapability.cast();
		return super.getCapability(cap, side);
	}

	@Override
	public void notifyUpdate() {
		super.notifyUpdate();
	}






	public Couple<FluidTank> getTanks() {
		return tanks;
	}









	@Override
	@SuppressWarnings("removal")
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {







		//--Fluid Info--//
		LazyOptional<IFluidHandler> handler = this.getCapability(ForgeCapabilities.FLUID_HANDLER);
		Optional<IFluidHandler> resolve = handler.resolve();
		if (!resolve.isPresent())
			return false;

		IFluidHandler tank = resolve.get();
		if (tank.getTanks() == 0)
			return false;

		LangBuilder mb = Lang.translate("generic.unit.millibuckets");
		Lang.translate("goggles.diesel_engine.info")
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


	protected void onFluidStackChanged(FluidStack newFluidStack) {
		sendData();
	}
	protected SmartFluidTank createInventory(Fluid validFluid,boolean extractionAllowed) {
		return new SmartFluidTank(1000, this::onFluidStackChanged) {
			@Override
			public boolean isFluidValid(FluidStack stack) {
				return stack.getFluid().isSame(validFluid);
			}



			@Override
			public FluidStack drain(FluidStack resource, FluidAction action) {
				if (!extractionAllowed)
					return FluidStack.EMPTY;
				return super.drain(resource, action);
			}

			@Override
			public FluidStack drain(int maxDrain, FluidAction action) {
				if (!extractionAllowed)
					return FluidStack.EMPTY;
				return super.drain(maxDrain, action);
			}


		};
	}
	protected SmartFluidTank createInventory(TagKey<Fluid> validFluid, boolean extractionAllowed) {
		return new SmartFluidTank(1000, this::onFluidStackChanged) {
			@Override
			public boolean isFluidValid(FluidStack stack) {

				return stack.getFluid().is(validFluid);
			}



			@Override
			public FluidStack drain(FluidStack resource, FluidAction action) {
				if (!extractionAllowed)
					return FluidStack.EMPTY;
				return super.drain(resource, action);
			}

			@Override
			public FluidStack drain(int maxDrain, FluidAction action) {
				if (!extractionAllowed)
					return FluidStack.EMPTY;
				return super.drain(maxDrain, action);
			}


		};
	}

}
