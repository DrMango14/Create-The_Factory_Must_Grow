package com.drmangotea.tfmg.content.decoration.pipes.rendering;

import com.drmangotea.tfmg.content.decoration.pipes.block.TFMGPumpBlock;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleTickableVisual;
import net.minecraft.core.Direction;

import java.util.function.Consumer;

public class TFMGMechanicalPumpVisual<T extends PumpBlockEntity> extends KineticBlockEntityVisual<T> implements SimpleTickableVisual {

    protected final RotatingInstance rotatingModel;

    public TFMGMechanicalPumpVisual(VisualizationContext context, T blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        PartialModel partial = AllPartialModels.MECHANICAL_PUMP_COG;
        if (blockEntity.getBlockState().getBlock() instanceof TFMGPumpBlock pumpBlock) {
            partial = TFMGPartialModels.PUMP_COGS.get(pumpBlock.material);
        }
        rotatingModel = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(partial))
                .createInstance()
                .rotateToFace(Direction.SOUTH, rotationAxis())
                .setup(blockEntity)
                .setPosition(getVisualPosition());

        rotatingModel.setChanged();
    }

    @Override
    public void update(float pt) {
        rotatingModel.setup(blockEntity)
                .setChanged();
    }

    @Override
    public void tick(Context context) {
        applyOverstressEffect(blockEntity, rotatingModel);
    }

    @Override
    public void updateLight(float partialTick) {
        relight(rotatingModel);
    }

    @Override
    protected void _delete() {
        rotatingModel.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(rotatingModel);
    }
}
