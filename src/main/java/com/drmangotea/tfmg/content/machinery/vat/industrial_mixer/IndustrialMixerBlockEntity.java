package com.drmangotea.tfmg.content.machinery.vat.industrial_mixer;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.machinery.vat.base.IVatMachine;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlock;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlockEntity;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlock;
import com.simibubi.create.infrastructure.config.AllConfigs;
import dev.engine_room.flywheel.api.visual.BlockEntityVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Objects;


public class IndustrialMixerBlockEntity extends KineticBlockEntity implements IVatMachine {


    public MixerMode mixerMode = MixerMode.NONE;
    public int vatSize = 1;
    public int vatHeight = 1;
    public BlockPos vatPos = null;

    LerpedFloat visualSpeed = LerpedFloat.linear();
    float angle;

    public IndustrialMixerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void vatUpdated(VatBlockEntity be) {


        vatSize = be.getWidth();
        vatHeight = be.getHeight();
        vatPos = be.getBlockPos();


    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide)
            return;

        float targetSpeed = getSpeed();
        visualSpeed.updateChaseTarget(targetSpeed);
        visualSpeed.tickChaser();


    }
    public void destroy() {
        ItemStack mixerItem = mixerMode.item;
        Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), mixerItem);
    }




    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        for (MixerMode mode : MixerMode.values()) {
            if (mode == mixerMode) {
                compound.putString("MixerMode", mode.name);
            }
        }
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {

        setMixerMode(compound.getString("MixerMode"), false);

        if (clientPacket)
            visualSpeed.chase(getGeneratedSpeed(), (double) 1 / 32, LerpedFloat.Chaser.EXP);
        super.read(compound, clientPacket);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(getBlockPos()).inflate(3);
    }

    @Override
    public String getOperationId() {
        return switch (mixerMode) {

            case NONE -> "";
            case MIXING -> "tfmg:mixing";
            case CENTRIFUGE -> "tfmg:centrifuge";
        };
    }

    @Override
    public boolean canOperate(VatBlockEntity vat) {
        return getSpeed() >= IRotate.SpeedLevel.MEDIUM.getSpeedValue() || getSpeed() <= -IRotate.SpeedLevel.MEDIUM.getSpeedValue();
    }

    public boolean setMixerMode(ItemStack modeItem, boolean simulate) {
        for (MixerMode mode : MixerMode.values()) {
            if (mode.item.is(modeItem.getItem())) {
                if (!simulate) {
                    mixerMode = mode;
                } else return true;
            }
        }
        if (!simulate && hasLevel())
            VatBlock.updateVatState(getBlockState(), getLevel(), getBlockPos().relative(Direction.DOWN));
        sendData();
        return false;
    }

    public boolean setMixerMode(String name, boolean simulate) {
        for (MixerMode mode : MixerMode.values()) {
            if (Objects.equals(mode.name, name)) {
                if (!simulate) {
                    mixerMode = mode;

                } else return true;
            }
        }
        if (!simulate && hasLevel())
            VatBlock.updateVatState(getBlockState(), getLevel(), getBlockPos().relative(Direction.DOWN));
        sendData();
        return false;
    }

    @Override
    public int getWorkPercentage() {
        return (int) ((getSpeed()/255f)*100);
    }

    @Override
    public PositionRequirement getPositionRequirement() {
        return PositionRequirement.TOP_CENTER;
    }

    @Override
    public String[] doesntWorkWith() {
        return new String[]{"electrodes"};
    }

    enum MixerMode {
        NONE("none", ItemStack.EMPTY),
        MIXING("mixing", TFMGItems.MIXER_BLADE.asStack()),
        CENTRIFUGE("centrifuge", TFMGItems.CENTRIFUGE.asStack());
        public final String name;
        public final ItemStack item;

        MixerMode(String name, ItemStack stack) {
            this.name = name;
            this.item = stack;
        }
    }
}
