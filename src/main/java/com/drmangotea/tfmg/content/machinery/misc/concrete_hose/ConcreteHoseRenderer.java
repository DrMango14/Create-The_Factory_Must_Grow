package com.drmangotea.tfmg.content.machinery.misc.concrete_hose;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.contraptions.pulley.AbstractPulleyRenderer;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction.Axis;

public class ConcreteHoseRenderer extends AbstractPulleyRenderer<ConcreteHoseBlockEntity> {

	public ConcreteHoseRenderer(BlockEntityRendererProvider.Context context) {
		super(context, AllPartialModels.HOSE_HALF, AllPartialModels.HOSE_HALF_MAGNET);
	}

	@Override
	protected Axis getShaftAxis(ConcreteHoseBlockEntity be) {
		return be.getBlockState()
			.getValue(ConcreteHoseBlock.HORIZONTAL_FACING)
			.getClockWise()
			.getAxis();
	}

	@Override
	protected PartialModel getCoil() {
		return AllPartialModels.HOSE_COIL;
	}

	@Override
	protected SuperByteBuffer renderRope(ConcreteHoseBlockEntity be) {
		return CachedBuffers.partial(AllPartialModels.HOSE, be.getBlockState());
	}

	@Override
	protected SuperByteBuffer renderMagnet(ConcreteHoseBlockEntity be) {
		return CachedBuffers.partial(AllPartialModels.HOSE_MAGNET, be.getBlockState());
	}

	@Override
	protected float getOffset(ConcreteHoseBlockEntity be, float partialTicks) {
		return be.getInterpolatedOffset(partialTicks);
	}
	
	@Override
	protected SpriteShiftEntry getCoilShift() {
		return AllSpriteShifts.HOSE_PULLEY_COIL;
	}

	@Override
	protected boolean isRunning(ConcreteHoseBlockEntity be) {
		return true;
	}

}
