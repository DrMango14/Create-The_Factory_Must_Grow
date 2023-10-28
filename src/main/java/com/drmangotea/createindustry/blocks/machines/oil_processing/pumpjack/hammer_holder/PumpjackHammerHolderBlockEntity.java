package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer_holder;

import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.crank.PumpjackCrankBlockEntity;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.machine_input.MachineInputBlockEntity;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.foundation.utility.animation.LerpedFloat.Chaser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;


public class PumpjackHammerHolderBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {
	float	targetSpeed;
	LerpedFloat visualSpeed = LerpedFloat.linear();
	LerpedFloat angle = LerpedFloat.angular();
	float debugMogus = 0.4f;
	float speedModifier=0;
	public BlockPos crankPos;
	public PumpjackCrankBlockEntity crank;


	public float crankAngle;
	public Direction direction = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);;
	public Direction direction2 = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING).getCounterClockWise();;
	public PumpjackHammerHolderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		angle.setValue(14);

	}

	@Override
	protected AABB createRenderBoundingBox() {
		return new AABB(this.getBlockPos()).inflate(2);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

	}
	public boolean hasCrank(){
		BlockPos theoreticalPos;
		if(direction == Direction.NORTH){
			theoreticalPos = this.getBlockPos().south(2).below();
			if(level.getBlockEntity(theoreticalPos) instanceof PumpjackCrankBlockEntity){
				crankPos = theoreticalPos;
				crank = (PumpjackCrankBlockEntity) level.getBlockEntity(crankPos);
				return true;
			}
		}
		if(direction == Direction.SOUTH){
			theoreticalPos = this.getBlockPos().north(2).below();
			if(level.getBlockEntity(theoreticalPos) instanceof PumpjackCrankBlockEntity){
				crankPos = theoreticalPos;
				crank = (PumpjackCrankBlockEntity) level.getBlockEntity(crankPos);
			return true;
			}
		}
		if(direction == Direction.WEST){
			theoreticalPos = this.getBlockPos().east(2).below();
			if(level.getBlockEntity(theoreticalPos) instanceof PumpjackCrankBlockEntity){
				crankPos = theoreticalPos;
				crank = (PumpjackCrankBlockEntity) level.getBlockEntity(crankPos);
			return true;
			}
		}
		if(direction == Direction.EAST){
			theoreticalPos = this.getBlockPos().west(2).below();
			if(level.getBlockEntity(theoreticalPos) instanceof PumpjackCrankBlockEntity) {
				crankPos = theoreticalPos;
				crank = (PumpjackCrankBlockEntity) level.getBlockEntity(crankPos);
				return true;
			}
		}
		return false;
	}


	@Override
	public void tick() {
		super.tick();
		direction = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
		if (!level.isClientSide)
			return;
		if(!hasCrank()) {
			angle.setValue(14);
			return;
		}

		if(!(level.getBlockEntity(crankPos.below()) instanceof MachineInputBlockEntity)){
			angle.setValue(14);
			return;
		}
		if(((MachineInputBlockEntity) level.getBlockEntity(crankPos.below())).powerLevel==0) {
			angle.setValue(14);
			return;
		}
		if(!(crank.isValid())){
			angle.setValue(14);
			return;
		}
		angle.tickChaser();

			if(angle.getValue()>0){
				speedModifier=(angle.getValue()/25)*-1;
			}else
				speedModifier=angle.getValue()/25;

			crankAngle=crank.angle;

			//if(crankAngle==90||crankAngle==270) {
			//	angle.chase(13, 0.125f, Chaser.EXP);
			//}

		angle.updateChaseSpeed(.8f+speedModifier);
			if(crankAngle==180){
				angle.chase(-14, .8f+speedModifier, Chaser.LINEAR);

				}

			if(crankAngle==0) {
				angle.chase(14, .8f+speedModifier, Chaser.LINEAR);
				//angle.updateChaseSpeed(angle.getValue());
			}
	}
	/*
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		if(hasCrank()){
			Lang.translate("goggles.surface_scanner.no_rotation")
					.style(ChatFormatting.GREEN)
					.forGoggles(tooltip);
			return true;
		}
		return false;
	}

	 */
	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		if (clientPacket) {
			compound.putFloat("Angle", angle.getValue());
		}
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		if (clientPacket) {

			angle.setValue(compound.getFloat("Angle"));
		}
	}
}
