package com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer;



import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.base.PumpjackBaseBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.crank.PumpjackCrankBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer.parts.PumpjackHammerConnectorBlock;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer.parts.PumpjackHammerHeadBlock;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer.parts.PumpjackHammerPartBlock;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer.parts.large.LargePumpjackHammerConnectorBlock;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer.parts.large.LargePumpjackHammerHeadBlock;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer.parts.large.LargePumpjackHammerPartBlock;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.contraptions.bearing.IBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencerInstructions;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;

import java.util.List;


import static com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer.PumpjackBlock.WIDE;
import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class PumpjackBlockEntity extends GeneratingKineticBlockEntity
	implements IBearingBlockEntity, IDisplayAssemblyExceptions {

	protected ScrollOptionBehaviour<RotationMode> movementMode;
	protected ControlledContraptionEntity movedContraption;
	protected float angle;
	protected boolean running;
	protected boolean assembleNextTick;
	protected float clientAngleDiff;
	protected AssemblyException lastException;
	protected double sequencedAngleLimit;

	private float prevAngle;

	public BlockPos headPosition=null;
	public BlockPos connectorPosition =null;

	public PumpjackCrankBlockEntity crank=null;

	public PumpjackBaseBlockEntity base=null;

	public int connectorDistance = 0;

	public int headDistance = 0;

	public boolean connectorAtFront = false;

	public boolean headAtFront = false;





	public int crankConnectorDistance = 0;

	public int headBaseDistance = 0;




	public PumpjackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		setLazyTickRate(3);
		sequencedAngleLimit = -1;



	}

	@Override
	public boolean isWoodenTop() {
		return false;
	}

	@Override
	protected boolean syncSequenceContext() {
		return true;
	}
	@Override
	protected AABB createRenderBoundingBox() {
		return super.createRenderBoundingBox().inflate(7);
	}
	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);

		registerAwardables(behaviours, AllAdvancements.CONTRAPTION_ACTORS);
	}

	@Override
	public void remove() {
		if (!level.isClientSide)
			disassemble();
		super.remove();
	}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {

	if(connectorPosition!=null) {
		compound.putInt("connectorX", connectorPosition.getX());
		compound.putInt("connectorY", connectorPosition.getY());
		compound.putInt("connectorZ", connectorPosition.getZ());
	}
//
	if(headPosition!=null) {
		compound.putInt("headX", headPosition.getX());
		compound.putInt("headY", headPosition.getY());
		compound.putInt("headZ", headPosition.getZ());
	}


		compound.putBoolean("connectorAtFront", connectorAtFront);
		compound.putBoolean("headAtFront", headAtFront);

		compound.putBoolean("Running", running);
		compound.putFloat("Angle", angle);
		if (sequencedAngleLimit >= 0)
			compound.putDouble("SequencedAngleLimit", sequencedAngleLimit);
		AssemblyException.write(compound, lastException);
		super.write(compound, clientPacket);
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		if (wasMoved) {
			super.read(compound, clientPacket);
			return;
		}

		connectorPosition = new BlockPos(
				compound.getInt("connectorX"),
				compound.getInt("connectorY"),
				compound.getInt("connectorZ")
		);
		headPosition = new BlockPos(
				compound.getInt("headX"),
				compound.getInt("headY"),
				compound.getInt("headZ")
		);

		connectorAtFront = compound.getBoolean("connectorAtFront");
		headAtFront = compound.getBoolean("headAtFront");

		float angleBefore = angle;
		running = compound.getBoolean("Running");
		angle = compound.getFloat("Angle");
		sequencedAngleLimit = compound.contains("SequencedAngleLimit") ? compound.getDouble("SequencedAngleLimit") : -1;
		lastException = AssemblyException.read(compound);
		super.read(compound, clientPacket);
		if (!clientPacket)
			return;
		if (running) {
			if (movedContraption == null || !movedContraption.isStalled()) {
				clientAngleDiff = AngleHelper.getShortestAngleDiff(angleBefore, angle);
				angle = angleBefore;
			}
		} else
			movedContraption = null;
	}

	@Override
	public float getInterpolatedAngle(float partialTicks) {
		if (isVirtual())
			return Mth.lerp(partialTicks + .5f, prevAngle, angle);
		if (movedContraption == null || movedContraption.isStalled() || !running)
			partialTicks = 0;
		float angularSpeed = getAngularSpeed();
		if (sequencedAngleLimit >= 0)
			angularSpeed = (float) Mth.clamp(angularSpeed, -sequencedAngleLimit, sequencedAngleLimit);
		return Mth.lerp(partialTicks, angle, angle + angularSpeed);
	}

	@Override
	public void onSpeedChanged(float prevSpeed) {
		super.onSpeedChanged(prevSpeed);
		assembleNextTick = true;
		sequencedAngleLimit = -1;

		if (movedContraption != null && Math.signum(prevSpeed) != Math.signum(getSpeed()) && prevSpeed != 0) {
			if (!movedContraption.isStalled()) {
				angle = Math.round(angle);
				applyRotation();
			}
			movedContraption.getContraption()
				.stop(level);
		}

		if (sequenceContext != null
			&& sequenceContext.instruction() == SequencerInstructions.TURN_ANGLE)
			sequencedAngleLimit = sequenceContext.getEffectiveValue(getTheoreticalSpeed());
	}

	public float getAngularSpeed() {
		float speed = convertToAngular(getSpeed());


		if (getSpeed() == 0)
			speed = 0;
		if (level.isClientSide) {
			speed *= ServerSpeedProvider.get();
			speed += clientAngleDiff / 3f;
		}
		return speed;
	}

	@Override
	public AssemblyException getLastAssemblyException() {
		return lastException;
	}



	@Override
	public BlockPos getBlockPosition() {
		return worldPosition;
	}

	public void assemble() {


		if (!(level.getBlockState(worldPosition)
			.getBlock() instanceof BearingBlock))
			return;

		Direction direction = getBlockState().getValue(BearingBlock.FACING);
		PumpjackContraption contraption = new PumpjackContraption(direction);


		try {

			if (!contraption.assemble(level, worldPosition))
				return;

			if(connectorPosition==null||headPosition == null)
				return;

			lastException = null;
		} catch (AssemblyException e) {
			lastException = e;
			sendData();

			return;

		}




		int q = 1;

		if(direction.getAxis()== Direction.Axis.X)
			q = -1;

		boolean canAssemble = true;
		boolean foundHead= false;
		boolean foundConnector= false;

		BlockPos headLocalPos = headPosition.subtract(getBlockPos().above());

		for (StructureTemplate.StructureBlockInfo block : contraption.getBlocks().values()) {
			if(block.state().getBlock() instanceof PumpjackHammerHeadBlock ||block.state().getBlock() instanceof LargePumpjackHammerHeadBlock) {
				foundHead = true;
				if (block.pos().getX() != headLocalPos.getX() ||
						block.pos().getY() != q*headLocalPos.getY() ||
						block.pos().getZ() != q*headLocalPos.getZ())
					canAssemble = false;

			}
		}

		BlockPos connectorLocalPos = connectorPosition.subtract(getBlockPos().above());

		for (StructureTemplate.StructureBlockInfo block : contraption.getBlocks().values()) {
			if(block.state().getBlock() instanceof PumpjackHammerConnectorBlock ||block.state().getBlock() instanceof LargePumpjackHammerConnectorBlock) {
				foundConnector = true;
				if (block.pos().getX() !=connectorLocalPos.getX() ||
						block.pos().getY() != q*connectorLocalPos.getY() ||
						block.pos().getZ() != q*connectorLocalPos.getZ())
					canAssemble = false;

			}
		}

		if(!canAssemble||!foundHead||!foundConnector)
			return;


		if(base.controllerHammer!=this&&base.controllerHammer!=null)
			return;



		contraption.removeBlocksFromWorld(level, BlockPos.ZERO);
		movedContraption = ControlledContraptionEntity.create(level, this, contraption);
		BlockPos anchor = worldPosition.above();
		movedContraption.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		movedContraption.setRotationAxis(direction.getClockWise().getAxis());
		level.addFreshEntity(movedContraption);

		AllSoundEvents.MECHANICAL_PRESS_ACTIVATION.playOnServer(level, worldPosition);

		if (contraption.containsBlockBreakers())
			award(AllAdvancements.CONTRAPTION_ACTORS);



		running = true;
		angle = 0;
		sendData();
		updateGeneratedRotation();








	}

	private boolean findHeadAndConnector() {




		Direction direction = getBlockState().getValue(FACING);

		BlockPos checkedPos = this.getBlockPos().above();




		connectorPosition = null;
		headPosition = null;

		for(int i =0;i<7;i++){

			if(connectorPosition!=null&&headPosition!=null
					//&&
					//level.getBlockState(headPosition).getBlock() instanceof PumpjackHammerHeadBlock&&
					//level.getBlockState(connectorPosition).getBlock() instanceof PumpjackHammerConnectorBlock
			) {
				sendData();

				return true;
			}

			if(i!=0)
				if(level.getBlockState(checkedPos).getBlock() instanceof PumpjackHammerHeadBlock||(level.getBlockState(checkedPos).getBlock() instanceof LargePumpjackHammerHeadBlock)){
					headPosition = checkedPos;
					headAtFront = true;
					checkedPos = checkedPos.relative(direction);
					sendData();
					continue;
				}

			if(i!=0)
				if(level.getBlockState(checkedPos).getBlock() instanceof PumpjackHammerConnectorBlock||level.getBlockState(checkedPos).getBlock() instanceof LargePumpjackHammerConnectorBlock){
						if(level.getBlockState(checkedPos).getValue(HorizontalDirectionalBlock.FACING).getAxis()==this.getBlockState().getValue(FACING).getAxis()) {

						connectorPosition = checkedPos;
						connectorAtFront = true;
						checkedPos = checkedPos.relative(direction);

						sendData();
						continue;
					}
				}

			if(!(level.getBlockState(checkedPos).getBlock() instanceof PumpjackHammerPartBlock)&&!(level.getBlockState(checkedPos).getBlock() instanceof LargePumpjackHammerPartBlock)) {

				break;
			}else {
				if(level.getBlockState(checkedPos).getValue(HorizontalDirectionalBlock.FACING).getAxis()!=this.getBlockState().getValue(FACING).getAxis()) {

					break;
//
				}
			}

			checkedPos = checkedPos.relative(direction);


		}

		checkedPos = this.getBlockPos().above();


		for(int i =0;i<7;i++){



			if(connectorPosition!=null&&headPosition!=null) {
				sendData();
				return true;
			}
			if(i!=0)
				if(level.getBlockState(checkedPos).getBlock() instanceof PumpjackHammerHeadBlock||(level.getBlockState(checkedPos).getBlock() instanceof LargePumpjackHammerHeadBlock)){
					headPosition = checkedPos;
					headAtFront = false;
					checkedPos = checkedPos.relative(direction.getOpposite());

					sendData();
					continue;
				}

			if(i!=0)
				if(level.getBlockState(checkedPos).getBlock() instanceof PumpjackHammerConnectorBlock||level.getBlockState(checkedPos).getBlock() instanceof LargePumpjackHammerConnectorBlock){
						if(level.getBlockState(checkedPos).getValue(HorizontalDirectionalBlock.FACING).getAxis()==this.getBlockState().getValue(FACING).getAxis()) {
						connectorPosition = checkedPos;
						connectorAtFront = false;
						checkedPos = checkedPos.relative(direction.getOpposite());

						sendData();
						continue;
					}
				}

			if(!(level.getBlockState(checkedPos).getBlock() instanceof PumpjackHammerPartBlock)&&!(level.getBlockState(checkedPos).getBlock() instanceof LargePumpjackHammerPartBlock)) {

				break;
			}else {
				if(level.getBlockState(checkedPos).getValue(HorizontalDirectionalBlock.FACING).getAxis()!=this.getBlockState().getValue(FACING).getAxis()) {

					break;
				}
			}


			checkedPos = checkedPos.relative(direction.getOpposite());


		}


		sendData();
		return false;
	}

	public void disassemble() {
		if (!running && movedContraption == null)
			return;

		connectorDistance=0;

		headDistance =0;

		//headPosition=null;
		//connectorPosition =null;

		angle = 0;
		sequencedAngleLimit = -1;

		if (movedContraption != null) {
			movedContraption.disassemble();
			AllSoundEvents.MECHANICAL_PRESS_ACTIVATION.playOnServer(level, worldPosition);
		}

		movedContraption = null;
		running = false;
		updateGeneratedRotation();
		assembleNextTick = false;
		sendData();
	}

	@Override
	public void tick() {
		super.tick();


		if(!isRunning())
			findHeadAndConnector();

		if(!isRunning()&&base !=null&&crank!=null
			&&!level.isClientSide
		) {
			assemble();
		}



		if(base!=null)
			if(base.controllerHammer==null){
				if(isRunning())
					base.setControllerHammer(this);
			}

		if(base == null||crank == null)
			if (!level.isClientSide)
				disassemble();


		if(level.getBlockState(getBlockPos().above()).is(TFMGBlocks.LARGE_PUMPJACK_HAMMER_PART.get())&& !getBlockState().getValue(WIDE))
			level.setBlock(getBlockPos(),getBlockState().setValue(WIDE,true),2);

		if(!isRunning())
			if(!level.getBlockState(getBlockPos().above()).is(TFMGBlocks.LARGE_PUMPJACK_HAMMER_PART.get())&& getBlockState().getValue(WIDE))
				level.setBlock(getBlockPos(),getBlockState().setValue(WIDE,false),2);



		Direction direction = getBlockState().getValue(BearingBlock.FACING);
		if(connectorPosition!=null) {
			if (direction.getAxis() == Direction.Axis.Z)
				connectorDistance = Math.abs(getBlockPos().getZ() - connectorPosition.getZ());

			if (direction.getAxis() == Direction.Axis.X)
				connectorDistance = Math.abs(getBlockPos().getX() - connectorPosition.getX());

			if(crank!=null) {
				crankConnectorDistance = Math.abs(crank.getBlockPos().getY() - connectorPosition.getY());

				crank.crankRadius = (float) connectorDistance /5;
			}

		}
		if(headPosition!=null) {
			if (direction.getAxis() == Direction.Axis.Z)
				headDistance = Math.abs(getBlockPos().getZ() - headPosition.getZ());

			if (direction.getAxis() == Direction.Axis.X)
				headDistance = Math.abs(getBlockPos().getX() - headPosition.getX());

			if(base!=null) {
				headBaseDistance = Math.abs(base.getBlockPos().getY() - headPosition.getY());

			}

		}

		if(connectorPosition!=null)
			crank = findCrank();


		if(crank!=null)
			if(!(level.getBlockEntity(crank.getBlockPos()) instanceof PumpjackCrankBlockEntity))
				crank =null;
		/////////////////////
		if(headPosition!=null) {

			base = findBase();
		}


		if(base!=null)
			if(!(level.getBlockEntity(base.getBlockPos()) instanceof PumpjackBaseBlockEntity))
				base =null;


		////////
		prevAngle = angle;
		if (level.isClientSide)
			clientAngleDiff /= 2;

		if (
				!level.isClientSide &&
						assembleNextTick) {
			assembleNextTick = false;
			if (running) {


			} else {

				assemble();
			}
		}

		if (!running)
			return;
		//////////////////////////////////////////////////////////////////////
		if (!(movedContraption != null && movedContraption.isStalled())) {
			if(crank!=null) {


				int x = 1;
				if(connectorAtFront)
					x = -1;

				if(direction == Direction.SOUTH||direction == Direction.WEST) {
					angle = (float) Math.toDegrees(Math.atan(crank.heightModifier*x / connectorDistance));

				} else angle = (float) Math.toDegrees(Math.atan(-crank.heightModifier*x / connectorDistance));




			}
		}

		applyRotation();
	}

	private PumpjackCrankBlockEntity findCrank() {
		BlockPos checkedPos = connectorPosition.below();


		for(int i =0;i<7;i++){

			if(level.getBlockEntity(checkedPos) instanceof PumpjackCrankBlockEntity)
				if(level.getBlockState(checkedPos).getValue(HorizontalDirectionalBlock.FACING).getAxis()==this.getBlockState().getValue(FACING).getAxis())
							return (PumpjackCrankBlockEntity) level.getBlockEntity(checkedPos);



			checkedPos = checkedPos.below();
		}
		return null;
	}
	private PumpjackBaseBlockEntity findBase() {
		BlockPos checkedPos = headPosition.below();



		for(int i =0;i<8;i++){

			if(level.getBlockEntity(checkedPos) instanceof PumpjackBaseBlockEntity)
				return (PumpjackBaseBlockEntity) level.getBlockEntity(checkedPos);

			checkedPos = checkedPos.below();
		}
		return null;
	}


	public boolean isNearInitialAngle() {
		return Math.abs(angle) < 22.5 || Math.abs(angle) > 360 - 22.5;
	}

	@Override
	public void lazyTick() {
		super.lazyTick();
		if (movedContraption != null && !level.isClientSide)
			sendData();
	}

	protected void applyRotation() {
		if (movedContraption == null)
			return;
		movedContraption.setAngle(angle);
		BlockState blockState = getBlockState();
		if (blockState.hasProperty(BlockStateProperties.FACING))
			movedContraption.setRotationAxis(blockState.getValue(BlockStateProperties.FACING).getClockWise()
				.getAxis());
	}

	@Override
	public void attach(ControlledContraptionEntity contraption) {
		BlockState blockState = getBlockState();
		if (!(contraption.getContraption() instanceof PumpjackContraption))
			return;
		if (!blockState.hasProperty(BearingBlock.FACING))
			return;

		this.movedContraption = contraption;
		setChanged();

		//BlockPos anchor = worldPosition.relative(blockState.getValue(BearingBlock.FACING));
		BlockPos anchor = worldPosition.above();
		movedContraption.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		if (!level.isClientSide) {
			this.running = true;
			sendData();
		}
	}

	@Override
	public void onStall() {
		if (!level.isClientSide)
			sendData();
	}

	@Override
	public boolean isValid() {
		return !isRemoved();
	}

	@Override
	public boolean isAttachedTo(AbstractContraptionEntity contraption) {
		return movedContraption == contraption;
	}

	public boolean isRunning() {
		return running;
	}



	public void setAngle(float forcedAngle) {
		angle = forcedAngle;
	}

	public ControlledContraptionEntity getMovedContraption() {
		return movedContraption;
	}

}
