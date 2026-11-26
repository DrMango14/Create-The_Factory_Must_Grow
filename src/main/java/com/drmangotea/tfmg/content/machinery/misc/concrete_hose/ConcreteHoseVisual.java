package com.drmangotea.tfmg.content.machinery.misc.concrete_hose;

import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.contraptions.pulley.AbstractPulleyVisual;
import com.simibubi.create.content.processing.burner.ScrollInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import net.createmod.catnip.render.SpriteShiftEntry;

public class ConcreteHoseVisual extends AbstractPulleyVisual<ConcreteHoseBlockEntity> {

    public ConcreteHoseVisual(VisualizationContext dispatcher, ConcreteHoseBlockEntity blockEntity, float partialTick) {
        super(dispatcher, blockEntity, partialTick);
    }

    @Override
    protected Instancer<TransformedInstance> getRopeModel() {
        return instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.HOSE));
    }

    @Override
    protected Instancer<TransformedInstance> getMagnetModel() {
        return instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(TFMGPartialModels.HOSE_MAGNET));
    }

    @Override
    protected Instancer<TransformedInstance> getHalfMagnetModel() {
        return instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(TFMGPartialModels.HOSE_HALF_MAGNET));
    }

    @Override
    protected Instancer<ScrollInstance> getCoilModel() {
        return instancerProvider().instancer(AllInstanceTypes.SCROLLING, Models.partial(AllPartialModels.HOSE_COIL));
    }

    @Override
    protected Instancer<TransformedInstance> getHalfRopeModel() {
        return instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.HOSE_HALF));
    }

    @Override
    protected float getOffset(float pt) {
        return blockEntity.getInterpolatedOffset(pt);
    }

    @Override
    protected boolean isRunning() {
        return true;
    }

    @Override
    protected SpriteShiftEntry getCoilAnimation() {
        return AllSpriteShifts.HOSE_PULLEY_COIL;
    }
}
