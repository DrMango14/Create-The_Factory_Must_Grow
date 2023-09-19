package com.drmangotea.tfmg.content.machines.oil_processing.pumpjack.crank;


import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.core.Direction;


public class PumpjackCrankInstance extends KineticBlockEntityInstance<PumpjackCrankBlockEntity> implements DynamicInstance {


    protected final ModelData hammer;
    protected float lastAngle = Float.NaN;
    static float originOffset = 1 / 16f;

    public PumpjackCrankInstance(MaterialManager modelManager, PumpjackCrankBlockEntity tile) {
        super(modelManager, tile);


        hammer = getTransformMaterial().getModel(blockState)
                .createInstance();

        animate(tile.angle);


    }

    @Override
    public void beginFrame() {



        float angle = blockEntity.angle;


        animate(angle);

        lastAngle = angle;
    }

    private void animate(float angle) {
        PoseStack ms = new PoseStack();
        TransformStack msr = TransformStack.cast(ms);

        msr.translate(getInstancePosition());



      //  msr.centre()
        //        .rotateCentered(Direction.EAST, AngleHelper.rad(angle))
          //      .unCentre();
        if(blockEntity.direction==Direction.EAST) {
            msr.translateY(-0.5);
            msr
                    .centre()
                    .translate(0, .25, 0)
                    .rotate(Direction.SOUTH, AngleHelper.rad(angle))
                    .translateBack(0, -.25, 0)
                    .unCentre();
        }
        if(blockEntity.direction==Direction.WEST) {
            msr.translateY(-0.5);
            msr
                    .centre()
                    .translate(0, .25, 0)
                    .rotate(Direction.NORTH, AngleHelper.rad(angle))
                    .translateBack(0, -.25, 0)
                    .unCentre();
        }
        if(blockEntity.direction==Direction.NORTH) {
            msr.translateY(-0.5);
            msr
                    .centre()
                    .translate(0, .25, 0)
                    .rotate(Direction.EAST, AngleHelper.rad(angle))
                    .translateBack(0, -.25, 0)
                    .unCentre();
        }
        if(blockEntity.direction==Direction.SOUTH) {
            msr.translateY(-0.5);
            msr
                    .centre()
                    .translate(0, .25, 0)
                    .rotate(Direction.WEST, AngleHelper.rad(angle))
                    .translateBack(0, -.25, 0)
                    .unCentre();
        }




        hammer.setTransform(ms);
    }



    @Override
    public void updateLight() {
        relight(pos, hammer);
    }

    @Override
    public void remove() {
        hammer.delete();
    }

}
