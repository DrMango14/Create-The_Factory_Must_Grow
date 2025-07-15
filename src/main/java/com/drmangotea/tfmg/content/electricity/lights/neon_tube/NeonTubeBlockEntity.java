package com.drmangotea.tfmg.content.electricity.lights.neon_tube;

import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.tfmg.content.electricity.lights.LightBulbBlock.LIGHT;

public class NeonTubeBlockEntity extends ElectricBlockEntity {

    public DyeColor color= DyeColor.WHITE;

    public LerpedFloat glow = LerpedFloat.linear();

    public NeonTubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public boolean canBeInGroups() {
        return true;
    }
    @Override
    public void tick() {
        super.tick();


            glow.chase(getPowerUsage()*1.5, 0.4, LerpedFloat.Chaser.EXP);
            glow.tickChaser();
            if (Math.min(getData().getVoltage() / 10, 15) != getBlockState().getValue(LIGHT))
                level.setBlock(getBlockPos(), getBlockState().setValue(LIGHT, (int) Math.min(getData().getVoltage() / 10, 15)), 2);

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
    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return getBlockState().getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction));
    }

    @Override
    public float resistance() {
        return 200;
    }
}
