package com.drmangotea.tfmg.blocks.engines.intake;

import com.drmangotea.tfmg.registry.TFMGPartialModels;
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


import com.jozufozu.flywheel.core.materials.FlatLit;


public class AirIntakeInstance extends KineticBlockEntityInstance<AirIntakeBlockEntity> implements DynamicInstance {
    private final ModelData frame;
    private final ModelData frameClosed;
    private ModelData chassisMedium;
    private ModelData chassisLarge;
    protected Optional<RotatingData> shaft;
    protected final RotatingData fan;
    protected final ModelData fan_medium;
    protected final ModelData fan_large;
    final Direction direction;
    private final Direction opposite;

    public AirIntakeInstance(MaterialManager materialManager, AirIntakeBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        this.direction = (Direction)this.blockState.getValue(FACING);
        this.opposite = this.direction.getOpposite();
        this.fan = (RotatingData)materialManager.defaultCutout().material(AllMaterialSpecs.ROTATING).getModel(TFMGPartialModels.AIR_INTAKE_FAN, this.blockState, this.opposite).createInstance();
        this.fan_medium = (ModelData)this.getTransformMaterial().getModel(TFMGPartialModels.AIR_INTAKE_FAN_MEDIUM, this.blockState, this.opposite).createInstance();
        this.fan_large = (ModelData)this.getTransformMaterial().getModel(TFMGPartialModels.AIR_INTAKE_FAN_LARGE, this.blockState, this.opposite).createInstance();
        this.setup(this.fan, this.getFanSpeed());
        this.frame = (ModelData)this.getTransformMaterial().getModel(TFMGPartialModels.AIR_INTAKE_FRAME, this.blockState, (Direction)blockEntity.getBlockState().getValue(FACING)).createInstance();
        this.frameClosed = (ModelData)this.getTransformMaterial().getModel(TFMGPartialModels.AIR_INTAKE_FRAME_CLOSED, this.blockState, (Direction)blockEntity.getBlockState().getValue(FACING)).createInstance();
        this.chassisMedium = (ModelData)this.getTransformMaterial().getModel(TFMGPartialModels.AIR_INTAKE_MEDIUM, this.blockState, (Direction)blockEntity.getBlockState().getValue(FACING)).createInstance();
        this.chassisLarge = (ModelData)this.getTransformMaterial().getModel(TFMGPartialModels.AIR_INTAKE_LARGE, this.blockState, (Direction)blockEntity.getBlockState().getValue(FACING)).createInstance();
    }

    public void init() {
        RotatingData data = this.setup((RotatingData)this.getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, this.blockState, ((Direction)this.blockState.getValue(FACING)).getOpposite()).createInstance());
        this.shaft = Optional.of(data);
    }

    private float getFanSpeed() {
        float speed = ((AirIntakeBlockEntity)this.blockEntity).getSpeed();
        if (speed > 0.0F) {
            speed = Mth.clamp(speed, 80.0F, 1280.0F);
        }

        if (speed < 0.0F) {
            speed = Mth.clamp(speed, -1280.0F, -80.0F);
        }

        return speed;
    }

    public void update() {
        this.shaft.ifPresent((x$0) -> {
            this.updateRotation(x$0);
        });
    }

    public void beginFrame() {
        PoseStack msFan = new PoseStack();
        TransformStack msrFan = TransformStack.cast(msFan);
        msrFan.translate(this.getInstancePosition());
        PoseStack msFanMedium = new PoseStack();
        TransformStack msrFanMedium = TransformStack.cast(msFanMedium);
        msrFanMedium.translate(this.getInstancePosition());
        float x;
        float z;
        if (this.direction.getAxis().isHorizontal()) {
            x = 0.0F;
            z = 0.0F;
            if (this.direction == Direction.NORTH) {
                x = 1.0F;
            }

            if (this.direction == Direction.SOUTH) {
                x = -1.0F;
            }

            if (this.direction == Direction.WEST) {
                z = -1.0F;
            }

            if (this.direction == Direction.EAST) {
                z = 1.0F;
            }

            msrFan.translate((double)x, 1.0, (double)z);
            msrFanMedium.translate((double)(x / 2.0F), 0.5, (double)(z / 2.0F));
        } else {
            msrFanMedium.translate(0.5, 0.0, 0.5);
            msrFan.translate(1.0, 0.0, 1.0);
        }

        x = AnimationTickHolder.getRenderTime(this.world);
        z = ((AirIntakeBlockEntity)this.blockEntity).maxShaftSpeed * 2.0F;
        if (z > 0.0F) {
            z = Mth.clamp(z, 80.0F, 1280.0F);
        }

        if (z < 0.0F) {
            z = Mth.clamp(z, -1280.0F, -80.0F);
        }

        float angle = x * z * 3.0F / 10.0F % 360.0F;
        angle = angle / 180.0F * 3.1415927F;
        msrFan.centre();
        msrFanMedium.centre();
        msrFan.rotate(this.direction, angle);
        msrFanMedium.rotate(this.direction, angle);
        msrFan.unCentre();
        msrFanMedium.unCentre();
        if (((AirIntakeBlockEntity)this.blockEntity).isUsedByController) {
            this.fan_medium.setEmptyTransform();
            this.fan.delete();
            this.fan_large.setEmptyTransform();
        }

        if (!((AirIntakeBlockEntity)this.blockEntity).isController) {
            this.chassisMedium.setEmptyTransform();
            this.fan_medium.setEmptyTransform();
            this.fan_large.setEmptyTransform();
        }

        if (!((AirIntakeBlockEntity)this.blockEntity).isUsedByController) {
            if (((AirIntakeBlockEntity)this.blockEntity).diameter == 1) {
                this.updateRotation(this.fan, this.getFanSpeed());
                this.fan_medium.setEmptyTransform();
                this.fan_large.setEmptyTransform();
            }

            if (((AirIntakeBlockEntity)this.blockEntity).diameter == 2) {
                this.fan_medium.setTransform(msFanMedium);
                this.fan.delete();
                this.fan_large.setEmptyTransform();
            }

            if (((AirIntakeBlockEntity)this.blockEntity).diameter == 3) {
                this.fan_large.setTransform(msFan);
                this.fan.delete();
                this.fan_medium.setEmptyTransform();
            }
        } else {
            this.fan.delete();
            this.fan_medium.setEmptyTransform();
            this.fan_large.setEmptyTransform();
        }

        PoseStack ms = new PoseStack();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(this.getInstancePosition());
        if (this.direction == Direction.NORTH) {
            msr.translateZ(0.001);
        }

        if (this.direction == Direction.SOUTH) {
            msr.translateZ(-0.001);
        }

        if (this.direction == Direction.WEST) {
            msr.translateX(0.001);
        }

        if (this.direction == Direction.EAST) {
            msr.translateX(-0.001);
        }

        if (this.direction == Direction.UP) {
            msr.translateY(-0.001);
        }

        if (this.direction == Direction.DOWN) {
            msr.translateY(0.001);
        }

        if (((AirIntakeBlockEntity)this.blockEntity).hasShaft) {
            this.frame.setTransform(ms);
            this.frameClosed.setEmptyTransform();
        } else {
            this.frameClosed.setTransform(ms);
            this.frame.setEmptyTransform();
        }

        PoseStack ms1 = new PoseStack();
        TransformStack msr1 = TransformStack.cast(ms1);
        msr1.translate(this.getInstancePosition());
        PoseStack ms2 = new PoseStack();
        TransformStack msr2 = TransformStack.cast(ms2);
        msr2.translate(this.getInstancePosition());
        if (this.direction.getAxis().isHorizontal()) {
            msr1.translateY(1.0);
            if (this.direction == Direction.NORTH) {
                msr1.translateX(1.0);
            }

            if (this.direction == Direction.SOUTH) {
                msr1.translateX(-1.0);
            }

            if (this.direction == Direction.EAST) {
                msr1.translateZ(1.0);
            }

            if (this.direction == Direction.WEST) {
                msr1.translateZ(-1.0);
            }
        } else {
            msr1.translateZ(1.0);
            msr1.translateX(1.0);
            if (this.direction != Direction.DOWN) {
                msr2.translateZ(1.0);
            }

            msr2.translateX(1.0);
        }

        if (((AirIntakeBlockEntity)this.blockEntity).diameter == 2) {
            this.chassisMedium.setTransform(ms2);
            this.chassisLarge.setEmptyTransform();
        }

        if (((AirIntakeBlockEntity)this.blockEntity).diameter == 3) {
            this.chassisLarge.setTransform(ms1);
            this.chassisMedium.setEmptyTransform();
        }

    }

    public void updateLight() {
        BlockPos behind = this.pos.relative(this.opposite);
        this.shaft.ifPresent((d) -> {
            this.relight(this.pos, new FlatLit[]{d});
        });
        this.relight(this.pos, new FlatLit[]{this.frame});
        this.relight(this.pos, new FlatLit[]{this.frameClosed});
        this.relight(this.pos, new FlatLit[]{this.chassisLarge});
        this.relight(this.pos, new FlatLit[]{this.chassisMedium});
        //BlockPos inFront = this.pos.m_121945_(this.direction);
        this.relight(this.pos, new FlatLit[]{this.fan});
        this.relight(this.pos, new FlatLit[]{this.fan_medium});
        this.relight(this.pos, new FlatLit[]{this.fan_large});
    }

    public void remove() {
        this.shaft.ifPresent(InstanceData::delete);
        this.fan.delete();
        this.fan_medium.delete();
        this.fan_large.delete();
        this.frame.delete();
        this.frameClosed.delete();
        this.chassisMedium.delete();
        this.chassisLarge.delete();
    }
}