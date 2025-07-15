package com.drmangotea.tfmg.content.electricity.utilities.fuse_block;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.UpdateInFrontPacket;
import com.drmangotea.tfmg.content.electricity.utilities.diode.ElectricDiodeBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPackets;

import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;


public class FuseBlockEntity extends ElectricDiodeBlockEntity {

    public ItemStack fuse = ItemStack.EMPTY;

    boolean testFuse = false;


    public FuseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }



    public void updateInFront() {

        if (level instanceof ServerLevel serverLevel)
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(worldPosition),new UpdateInFrontPacket(BlockPos.of(getPos())));
        Direction facing = getBlockState().hasProperty(DirectionalBlock.FACING) ? getBlockState().getValue(DirectionalBlock.FACING) : getBlockState().getValue(FACING).getCounterClockWise();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            if (be.hasElectricitySlot(facing.getOpposite())) {
                be.updateNextTick();
            }
        }

        sendStuff();
        setChanged();
    }
    @Override
    public float resistance() {
        if(!hasFuse())
            return 0;

        Direction facing = getBlockState().getValue(FACING).getCounterClockWise();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            if (be.hasElectricitySlot(facing.getOpposite()))
                return Math.max(be.getNetworkResistance(), 0);
        }
        return 0;
    }


    //@Override
    //public void onNetworkChanged(int oldVoltage, int oldPower) {
    //    super.onNetworkChanged(oldVoltage, oldPower);
//
    //    if (hasFuse() && getData().highestCurrent >= (float) fuse.getOrCreateTag().getInt("AmpRating")) {
    //        blowFuse();
    //        updateNetwork();
    //        updateInFront();
//
    //    }
    //    Direction facing = getBlockState().getValue(FACING).getCounterClockWise();
    //    if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be) {
//
    //        if (hasFuse() && be.getData().highestCurrent >= (float) fuse.getOrCreateTag().getInt("AmpRating")) {
    //            blowFuse();
    //            updateNetwork();
    //            updateInFront();
//
    //        }
    //    }
    //}



    //@Override
    //public void setVoltage(int newVoltage) {
    //    super.setVoltage(newVoltage);
    //    Direction facing = getBlockState().getValue(FACING).getCounterClockWise();
    //    if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be) {
//
    //        if (hasFuse() && be.getData().highestCurrent >= (float) fuse.getOrCreateTag().getInt("AmpRating")) {
    //            blowFuse();
    //            updateNetwork();
    //            updateInFront();
//
    //        }
    //    }
    //    if (hasFuse() && getData().highestCurrent >= (float) fuse.getOrCreateTag().getInt("AmpRating")) {
    //        blowFuse();
    //        updateNetwork();
    //        updateInFront();
//
    //    }
    //    testFuse = true;
//
    //}
//
    //public void testFuse(){
    //    Direction facing = getBlockState().getValue(FACING).getCounterClockWise();
    //    if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be) {
//
    //        if (hasFuse() && be.getData().highestCurrent >= (float) fuse.getOrCreateTag().getInt("AmpRating")) {
    //            blowFuse();
    //            updateNetwork();
    //            updateInFront();
//
    //        }
    //    }
    //    if (hasFuse() && getData().highestCurrent >= (float) fuse.getOrCreateTag().getInt("AmpRating")) {
    //        blowFuse();
    //        updateNetwork();
    //        updateInFront();
//
    //    }
    //    testFuse = true;
    //}

    @Override
    public void tick() {
        super.tick();


        //if(testFuse){
        //    testFuse();
        //    testFuse = false;
        //}


        Direction facing = getBlockState().getValue(FACING).getCounterClockWise();


    }

    @Override
    public void updateNextTick() {
        super.updateNextTick();
    }

    @Override
    public int getOutputVoltage() {
        return fuse.isEmpty() ? 0 : super.getOutputVoltage();
    }

    @Override
    public int getOutputPower() {
        return fuse.isEmpty() ? 0 : super.getOutputPower();
    }

    public boolean hasFuse(){
        return !fuse.isEmpty();
    }

    public void blowFuse(){
        level.playSound(null, getBlockPos(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 2.0F,
                level.random.nextFloat() * 0.4F + 0.8F);
        fuse = ItemStack.EMPTY;
        TFMGUtils.spawnElectricParticles(level,getBlockPos());
        sendStuff();

    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getClockWise();
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        super.addToTooltip(tooltip, isPlayerSneaking);
        return true;
    }
//
   // @Override
   // protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
   //     super.write(compound,registries , clientPacket);
   //     compound.put("Fuse", fuse.serializeNBT());
   // }
//
   // @Override
   // protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
   //     super.read(compound,registries , clientPacket);
   //     fuse = ItemStack.of(compound.getCompound("Fuse"));
   // }
}
