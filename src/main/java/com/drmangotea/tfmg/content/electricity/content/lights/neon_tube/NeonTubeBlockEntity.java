package com.drmangotea.tfmg.content.electricity.content.lights.neon_tube;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.drmangotea.tfmg.content.electricity.content.lights.neon_tube.NeonTubeBlock.LIGHT;


public class NeonTubeBlockEntity extends SmartBlockEntity {
    public DyeColor color = DyeColor.WHITE;
    public LerpedFloat glow = LerpedFloat.linear();

    public NeonTubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void tick() {
        super.tick();
		glow.chase(0, 0.4, LerpedFloat.Chaser.EXP);
		glow.tickChaser();
		int light = 0;//Math.min(getData().getVoltage() / 10, 15);
		if (light != getBlockState().getValue(LIGHT) && level != null)
			level.setBlock(getBlockPos(), getBlockState().setValue(LIGHT, light), 2);
    }
	
    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        NBTHelper.writeEnum(compound,"color",color);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        color = NBTHelper.readEnum(compound,"color",DyeColor.class);
    }
    public void setColor(DyeColor color) {
        if(color==DyeColor.BLACK||color == DyeColor.LIGHT_GRAY|| color == DyeColor.GRAY)
            return;

        this.color = color;
        notifyUpdate();
    }

}
