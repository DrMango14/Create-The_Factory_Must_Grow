package com.drmangotea.tfmg.content.decoration.cogs;

import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TFMGEncasedCogVisual extends KineticBlockEntityVisual<KineticBlockEntity> {


    protected final RotatingInstance rotatingModel;
    @Nullable
    protected final RotatingInstance rotatingTopShaft;
    @Nullable
    protected final RotatingInstance rotatingBottomShaft;

    public static TFMGEncasedCogVisual steelSmall(VisualizationContext modelManager, KineticBlockEntity blockEntity, float partialTick) {
        return new TFMGEncasedCogVisual(modelManager, blockEntity, false, partialTick, Models.partial(TFMGPartialModels.SHAFTLESS_STEEL_COGHWEEL));
    }

    public static TFMGEncasedCogVisual steelLarge(VisualizationContext modelManager, KineticBlockEntity blockEntity, float partialTick) {
        return new TFMGEncasedCogVisual(modelManager, blockEntity, true, partialTick, Models.partial(TFMGPartialModels.SHAFTLESS_LARGE_STEEL_COGHWEEL));
    }

    public static TFMGEncasedCogVisual aluminumSmall(VisualizationContext modelManager, KineticBlockEntity blockEntity, float partialTick) {
        return new TFMGEncasedCogVisual(modelManager, blockEntity, false, partialTick, Models.partial(TFMGPartialModels.SHAFTLESS_ALUMINUM_COGHWEEL));
    }

    public static TFMGEncasedCogVisual aluminumLarge(VisualizationContext modelManager, KineticBlockEntity blockEntity, float partialTick) {
        return new TFMGEncasedCogVisual(modelManager, blockEntity, true, partialTick, Models.partial(TFMGPartialModels.SHAFTLESS_LARGE_ALUMINUM_COGHWEEL));
    }

    public TFMGEncasedCogVisual(VisualizationContext modelManager, KineticBlockEntity blockEntity, boolean large, float partialTick, Model model) {
        super(modelManager, blockEntity, partialTick);

        rotatingModel = instancerProvider().instancer(AllInstanceTypes.ROTATING, model)
                .createInstance();

        rotatingModel.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(rotationAxis())
                .setChanged();

        RotatingInstance rotatingTopShaft = null;
        RotatingInstance rotatingBottomShaft = null;

        Block block = blockState.getBlock();
        if (block instanceof IRotate def) {
            for (Direction d : Iterate.directionsInAxis(rotationAxis())) {
                if (!def.hasShaftTowards(blockEntity.getLevel(), blockEntity.getBlockPos(), blockState, d))
                    continue;
                RotatingInstance instance = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                        .createInstance();
                instance.setup(blockEntity)
                        .setPosition(getVisualPosition())
                        .rotateToFace(Direction.SOUTH, d)
                        .setChanged();

                if (large) {
                    instance.setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(rotationAxis(), pos));
                }

                if (d.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                    rotatingTopShaft = instance;
                } else {
                    rotatingBottomShaft = instance;
                }
            }
        }

        this.rotatingTopShaft = rotatingTopShaft;
        this.rotatingBottomShaft = rotatingBottomShaft;
    }

    @Override
    public void update(float pt) {
        rotatingModel.setup(blockEntity)
                .setChanged();
        if (rotatingTopShaft != null) rotatingTopShaft.setup(blockEntity)
                .setChanged();
        if (rotatingBottomShaft != null) rotatingBottomShaft.setup(blockEntity)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(rotatingModel);

        if (rotatingTopShaft != null) relight(rotatingTopShaft);
        if (rotatingBottomShaft != null) relight(rotatingBottomShaft);
    }

    @Override
    protected void _delete() {
        rotatingModel.delete();
        if (rotatingTopShaft != null) rotatingTopShaft.delete();
        if (rotatingBottomShaft != null) rotatingBottomShaft.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(rotatingModel);
        consumer.accept(rotatingTopShaft);
        consumer.accept(rotatingBottomShaft);
    }
}