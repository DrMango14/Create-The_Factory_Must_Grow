package com.drmangotea.createindustry.blocks.engines.intake;

import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;


import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class AirIntakeInstance extends KineticBlockEntityInstance<com.drmangotea.createindustry.blocks.engines.intake.AirIntakeBlockEntity> implements DynamicInstance {


    private final ModelData frame;

    private final ModelData frameClosed;

    private  ModelData chassisMedium;

    private  ModelData chassisLarge;

    protected Optional<RotatingData> shaft;
    protected final RotatingData fan;
    protected final ModelData fan_medium;
    protected final ModelData fan_large;

    final Direction direction;
    private final Direction opposite;

    public AirIntakeInstance(MaterialManager materialManager, com.drmangotea.createindustry.blocks.engines.intake.AirIntakeBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        direction = blockState.getValue(FACING);

        opposite = direction.getOpposite();

            fan = materialManager.defaultCutout()
                    .material(AllMaterialSpecs.ROTATING)
                    .getModel(TFMGPartialModels.AIR_INTAKE_FAN, blockState, opposite)
                    .createInstance();
        fan_medium = getTransformMaterial()
                //.defaultCutout()
                //.material(AllMaterialSpecs.ROTATING)
                .getModel(TFMGPartialModels.AIR_INTAKE_FAN_MEDIUM, blockState, opposite)
                .createInstance();
        fan_large = getTransformMaterial()
                //.defaultCutout()
                //.material(AllMaterialSpecs.ROTATING)
                .getModel(TFMGPartialModels.AIR_INTAKE_FAN_LARGE, blockState, opposite)
                .createInstance();




            setup(fan, getFanSpeed());

            //setup(fan_large, getFanSpeed());



        frame = getTransformMaterial()
                .getModel(TFMGPartialModels.AIR_INTAKE_FRAME,blockState,blockEntity.getBlockState().getValue(FACING))
                .createInstance();
        frameClosed = getTransformMaterial()
                .getModel(TFMGPartialModels.AIR_INTAKE_FRAME_CLOSED,blockState,blockEntity.getBlockState().getValue(FACING))
                .createInstance();

        chassisMedium = getTransformMaterial()
                .getModel(TFMGPartialModels.AIR_INTAKE_MEDIUM,blockState,blockEntity.getBlockState().getValue(FACING))
                .createInstance();
        chassisLarge = getTransformMaterial()
                .getModel(TFMGPartialModels.AIR_INTAKE_LARGE,blockState,blockEntity.getBlockState().getValue(FACING))
                .createInstance();







      //  if(direction.getAxis().isHorizontal()) {
      //      float x = 0;
      //      float z = 0;
      //      if(direction == Direction.NORTH)
      //          x=0.5f;
      //      if(direction == Direction.SOUTH)
      //          x=-0.5f;
      //      if(direction == Direction.WEST)
      //          z=-0.5f;
      //      if(direction == Direction.EAST)
      //          z=0.5f;
//
//
      //     // fan_large.translate(x*2,1,z*2);
      //      fan_medium.setPosition(getInstancePosition()).nudge(x,0.5f,z);
      //  }else {
      //      fan_medium.setPosition(getInstancePosition()).nudge(0.5f, 0, 0.5f);
      //      //fan_large.translate(1,0,1);
      //  }
    }
    @Override
    public void init() {




        RotatingData data = setup(getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, blockState, blockState.getValue(FACING).getOpposite())
                .createInstance());


            shaft = Optional.of(data);

    }

    private float getFanSpeed() {
        float speed = blockEntity.getSpeed();
        if (speed > 0)
            speed = Mth.clamp(speed, 80, 64 * 20);
        if (speed < 0)
            speed = Mth.clamp(speed, -64 * 20, -80);
        return speed;
    }

    @Override
    public void update() {
        shaft.ifPresent(this::updateRotation);



    }


    @Override
    public void beginFrame() {



        PoseStack msFan = new PoseStack();
        TransformStack msrFan = TransformStack.cast(msFan);
        msrFan.translate(getInstancePosition());

        PoseStack msFanMedium = new PoseStack();
        TransformStack msrFanMedium = TransformStack.cast(msFanMedium);
        msrFanMedium.translate(getInstancePosition());
        //msrFan.translate(0,0,0);




        if(direction.getAxis().isHorizontal()) {
            float x = 0;
            float z = 0;
            if(direction == Direction.NORTH)
                x=1f;
            if(direction == Direction.SOUTH)
                x=-1f;
            if(direction == Direction.WEST)
                z=-1f;
            if(direction == Direction.EAST)
                z=1f;


            msrFan.translate(x,1,z);
            msrFanMedium.translate(x/2,0.5,z/2);
        }else {
            msrFanMedium.translate(0.5,0,0.5);
            msrFan.translate(1,0,1);
        }
        //
        float time = AnimationTickHolder.getRenderTime(world);
        float speed = blockEntity.maxShaftSpeed * 2;
        if (speed > 0)
            speed = Mth.clamp(speed, 80, 64 * 20);
        if (speed < 0)
            speed = Mth.clamp(speed, -64 * 20, -80);
        float angle = (time * speed * 3 / 10f) % 360;
        //
        angle = angle / 180f * (float) Math.PI;
        msrFan.centre();
        msrFanMedium.centre();
        msrFan.rotate(direction,angle);
        msrFanMedium.rotate(direction,angle);
        msrFan.unCentre();
        msrFanMedium.unCentre();




        if(blockEntity.isUsedByController) {
            fan_medium.setEmptyTransform();
            fan.delete();
            fan_large.setEmptyTransform();

        }
        if(!blockEntity.isController) {
            chassisMedium.setEmptyTransform();
            fan_medium.setEmptyTransform();
            fan_large.setEmptyTransform();

        }
        if(!blockEntity.isUsedByController) {
            if(blockEntity.diameter ==1) {
                  updateRotation(fan, getFanSpeed());
                fan_medium.setEmptyTransform();
                fan_large.setEmptyTransform();
            }
            if(blockEntity.diameter ==2) {
                //updateRotation(fan_medium, getFanSpeed());
                fan_medium.setTransform(msFanMedium);
                fan.delete();
                fan_large.setEmptyTransform();

            }

            if(blockEntity.diameter ==3) {

                fan_large.setTransform(msFan);

                fan.delete();
                fan_medium.setEmptyTransform();
            }

        }else {
            fan.delete();
            fan_medium.setEmptyTransform();
            fan_large.setEmptyTransform();
        }
        /////////////
        PoseStack ms = new PoseStack();
        TransformStack msr = TransformStack.cast(ms);

        msr.translate(getInstancePosition());


        if(direction==Direction.NORTH)
            msr.translateZ(0.001);
        if(direction==Direction.SOUTH)
            msr.translateZ(-0.001);
        if(direction==Direction.WEST)
            msr.translateX(0.001);
        if(direction==Direction.EAST)
            msr.translateX(-0.001);
        if(direction==Direction.UP)
            msr.translateY(-0.001);
        if(direction==Direction.DOWN)
            msr.translateY(0.001);

         if(blockEntity.hasShaft) {
             frame.setTransform(ms);
              frameClosed.setEmptyTransform();

          } else {
              frameClosed.setTransform(ms);
              frame.setEmptyTransform();
          }
        PoseStack ms1 = new PoseStack();
        TransformStack msr1 = TransformStack.cast(ms1);

        msr1.translate(getInstancePosition());

        PoseStack ms2 = new PoseStack();
        TransformStack msr2 = TransformStack.cast(ms2);

        msr2.translate(getInstancePosition());

        if(direction.getAxis().isHorizontal()){
            msr1.translateY(1);
            if(direction == Direction.NORTH)
                msr1.translateX(1);
            if(direction == Direction.SOUTH)
                msr1.translateX(-1);
            if(direction == Direction.EAST)
                msr1.translateZ(1);
            if(direction == Direction.WEST)
                msr1.translateZ(-1);
        }else {

                msr1.translateZ(1);
                msr1.translateX(1);
                if(direction!=Direction.DOWN)
                    msr2.translateZ(1);
                msr2.translateX(1);


        }

         if(blockEntity.diameter ==2){
             chassisMedium.setTransform(ms2);
             chassisLarge.setEmptyTransform();
         }

        if(blockEntity.diameter ==3){
            chassisLarge.setTransform(ms1);
            chassisMedium.setEmptyTransform();
        }





    }

    @Override
    public void updateLight() {
        BlockPos behind = pos.relative(opposite);

        //AABB a = new AABB(pos);
        //a.inflate()
        //blockEntity.getLevel().getBlockStates(a);


        shaft.ifPresent(d -> relight(pos, d));
        relight(pos,frame);
        relight(pos,frameClosed);

        relight(pos,chassisLarge);
        relight(pos,chassisMedium);

        BlockPos inFront = pos.relative(direction);
        relight(pos, fan);
        relight(pos, fan_medium);
        relight(pos, fan_large);



    }

    @Override
    public void remove() {
        shaft.ifPresent(InstanceData::delete);
        fan.delete();
        fan_medium.delete();
        fan_large.delete();
        frame.delete();
        frameClosed.delete();
        chassisMedium.delete();
        chassisLarge.delete();
    }
}