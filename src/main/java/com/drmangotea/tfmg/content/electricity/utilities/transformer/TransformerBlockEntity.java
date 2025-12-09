package com.drmangotea.tfmg.content.electricity.utilities.transformer;

import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.UpdateInFrontPacket;
import com.drmangotea.tfmg.content.electricity.base.VoltageAlteringBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.drmangotea.tfmg.registry.TFMGSoundEvents;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class TransformerBlockEntity extends VoltageAlteringBlockEntity {
    boolean updateInFront = false;

    public ItemStack primaryCoil = ItemStack.EMPTY;
    public ItemStack secondaryCoil = ItemStack.EMPTY;

    public float coilRatio = 0;

    public TransformerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getOutputVoltage() {
        return (int) (getData().getVoltage()*coilRatio);
    }

    @Override
    public int getOutputPower() {
        return coilRatio == 0 ? 0 : getPowerUsage();
    }

    @Override
    public IElectric getControlledBlock() {
        Direction facing = getBlockState().hasProperty(DirectionalBlock.FACING) ? getBlockState().getValue(DirectionalBlock.FACING) : getBlockState().getValue(HorizontalDirectionalBlock.FACING).getCounterClockWise();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            return be;
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        if(updateInFront) {
            updateInFront();
            updateInFront = false;
        }

        if (this.getPowerUsage() > 0 && this.level.isClientSide())
            TFMGSoundEvents.ELECTRIC_HUM.playAt(level, worldPosition, 1.0f, 1.0f, false);
    }

    @Override
    public void destroy() {
        super.destroy();
        BlockPos pos = this.getBlockPos();
        if(!primaryCoil.isEmpty()){
            ItemEntity item = new ItemEntity(level, pos.getX()+.5f,pos.getY()+.5f,pos.getZ()+.5f,primaryCoil);
            level.addFreshEntity(item);
        }
        if(!secondaryCoil.isEmpty()){
            ItemEntity item = new ItemEntity(level, pos.getX()+.5f,pos.getY()+.5f,pos.getZ()+.5f,secondaryCoil);
            level.addFreshEntity(item);
        }
    }

    @Override
    public int getPowerUsage() {
        Direction facing = getDirection();

        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            if (be.hasElectricitySlot(facing.getOpposite())) {

                    return Math.max(be.getNetworkPowerUsage(this), 0);


            }
        }

        return 0;

    }



    public void updateCoils(){

        int primaryTurns = primaryCoil.getOrCreateTag().getInt("Turns");
        int secondaryTurns = secondaryCoil.getOrCreateTag().getInt("Turns");

        if(primaryCoil.isEmpty()||secondaryCoil.isEmpty()||primaryTurns<50||secondaryTurns<50){
            coilRatio = 0;
            updateNextTick();
            updateInFront();
            return;
        }

        coilRatio = (float) (float)secondaryTurns/(float) primaryTurns;

        updateNextTick();
        updateInFront();

    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.makeMultimeterTooltip(tooltip, isPlayerSneaking);

        if(coilRatio!=0) {
            CreateLang.text("----------------------------")
                    .style(ChatFormatting.WHITE)
                    .forGoggles(tooltip);
            CreateLang.translate("multimeter.transformer_ratio")
                    .add(CreateLang.number(coilRatio))
                    .color(0xc6e82c)
                    .forGoggles(tooltip, 1);
        }
        return true;
    }

    @Override
    public float resistance() {
        Direction facing = getBlockState().getValue(FACING).getCounterClockWise();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            if (be.hasElectricitySlot(facing.getOpposite()))
                return Math.max(be.getNetworkResistance(), 0);
        }
        return 0;
    }
    public Direction getDirection(){
        if(!getBlockState().hasProperty(DirectionalBlock.FACING)){
            return getBlockState().getValue(TFMGHorizontalDirectionalBlock.FACING).getCounterClockWise();
        }

        return getBlockState().getValue(DirectionalBlock.FACING);
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getClockWise();
    }
    @Override
    public void onNetworkChanged(int oldVoltage, int oldPower) {
        super.onNetworkChanged(oldVoltage, oldPower);
        if (oldVoltage != getData().getVoltage() || oldPower != getPowerUsage()) {
            updateInFront = true;
        }
        sendStuff();
        setChanged();
    }

    @Override
    public void updateNetwork() {
        super.updateNetwork();
        updateInFront();
    }

    @Override
    public void remove() {

        super.remove();
        updateInFront();
    }

    @Override
    public void onPlaced() {

        super.onPlaced();
        updateInFront = true;
    }

    public void updateInFront() {

        if(!level.isClientSide)
            TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new UpdateInFrontPacket(BlockPos.of(getPos())));
        Direction facing = getBlockState().hasProperty(DirectionalBlock.FACING) ? getBlockState().getValue(DirectionalBlock.FACING) : getBlockState().getValue(HorizontalDirectionalBlock.FACING).getCounterClockWise();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            if (be.hasElectricitySlot(facing.getOpposite())) {
                be.updateNextTick();

            }
        }
        sendStuff();
        setChanged();
    }
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.put("PrimaryCoil", primaryCoil.serializeNBT());
        compound.put("SecondaryCoil", secondaryCoil.serializeNBT());

        compound.putFloat("CoilRation", coilRatio);

    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        primaryCoil = ItemStack.of(compound.getCompound("PrimaryCoil"));
        secondaryCoil = ItemStack.of(compound.getCompound("SecondaryCoil"));

        coilRatio = compound.getFloat("CoilRation");
    }
}
