package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer;


import com.drmangotea.createindustry.registry.TFMGContraptions;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.bearing.AnchoredLighter;
import com.simibubi.create.content.contraptions.bearing.BearingContraption;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;

public class PumpjackContraption extends BearingContraption {


	//protected Direction facing;



	public PumpjackContraption() {}

	public PumpjackContraption(Direction facing) {
		this.facing = facing;
	}

	@Override
	public boolean assemble(Level world, BlockPos pos) throws AssemblyException {
		BlockPos offset = pos.above();
		if (!searchMovedStructure(world, offset, null))
			return false;
		startMoving(world);
		expandBoundsAroundAxis(facing.getAxis());
		if (blocks.isEmpty())
			return false;
		return true;
	}

	@Override
	public ContraptionType getType() {
		return TFMGContraptions.PUMPJACK_CONTRAPTION;
	}

	@Override
	protected boolean isAnchoringBlockAt(BlockPos pos) {
		return pos.equals(anchor.below());
	}

	@Override
	public void addBlock(BlockPos pos, Pair<StructureBlockInfo, BlockEntity> capture) {
		BlockPos localPos = pos.subtract(anchor);

		super.addBlock(pos, capture);
	}

		//@Override
		//public CompoundTag writeNBT(boolean spawnPacket) {
		//	CompoundTag tag = super.writeNBT(spawnPacket);
		//	tag.putInt("Facing", facing.get3DDataValue());
		//	return tag;
		//}
//
		//@Override
		//public void readNBT(Level world, CompoundTag tag, boolean spawnData) {
		//	facing = Direction.from3DDataValue(tag.getInt("Facing"));
		//	super.readNBT(world, tag, spawnData);
		//}



	//public Direction getFacing() {
	//	return facing;
	//}

	@Override
	public boolean canBeStabilized(Direction facing, BlockPos localPos) {
		if (facing.getOpposite() == this.facing && BlockPos.ZERO.equals(localPos))
			return false;
		return facing.getAxis() == this.facing.getAxis();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ContraptionLighter<?> makeLighter() {
		return new AnchoredLighter(this);
	}
}
