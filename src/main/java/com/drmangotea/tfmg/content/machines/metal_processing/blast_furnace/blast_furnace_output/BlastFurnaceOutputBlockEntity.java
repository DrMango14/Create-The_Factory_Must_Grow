package com.drmangotea.tfmg.content.machines.metal_processing.blast_furnace.blast_furnace_output;

import com.drmangotea.tfmg.content.machines.TFMGMachineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class BlastFurnaceOutputBlockEntity extends TFMGMachineBlockEntity implements IHaveGoggleInformation {



    public BlastFurnaceType type;
    public Direction outputFacing = getBlockState().getValue(FACING);
    public BlockPos mainFloor;
    public int height=2;
    public int reinforcementHeight = 0;


    public LerpedFloat coalCokeHeight = LerpedFloat.linear();

    //item storage
    public LazyOptional<IItemHandlerModifiable> itemCapability;

    public SmartInventory inputInventory;

    public SmartInventory fuelInventory;





    public BlastFurnaceOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInventory = new SmartInventory(1, this).forbidInsertion()
                .withMaxStackSize(64);
        fuelInventory = new SmartInventory(1, this).forbidInsertion()
                .withMaxStackSize(64);

        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(inputInventory,fuelInventory));
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    }
    public void tick(){
        super.tick();
        outputFacing = getBlockState().getValue(FACING);
        coalCokeHeight.chase(fuelInventory.getStackInSlot(0).getCount(), 0.1f, LerpedFloat.Chaser.EXP);
        coalCokeHeight.tickChaser();

        checkType();

        if(type == BlastFurnaceType.INVALID)
            return;

        List<ItemEntity> itemsToPick = getItemsToPick(type);
        for(ItemEntity itemEntity : itemsToPick){
            ItemStack itemStack = itemEntity.getItem();
            if(itemStack.is(TFMGItems.COAL_COKE_DUST.get())){

                int freeSpace = fuelInventory.getStackInSlot(0).getMaxStackSize()-fuelInventory.getStackInSlot(0).getCount();

                int coalCokeCount = itemStack.getCount();
               //if(itemStack == ItemStack.EMPTY)
               //    continue;

                if(coalCokeCount>freeSpace){
                    itemStack.setCount(itemStack.getCount()-freeSpace);
                    fuelInventory.setItem(0 ,new ItemStack (TFMGItems.COAL_COKE_DUST.get(),fuelInventory.getStackInSlot(0).getCount()+freeSpace));
                }else {
                    fuelInventory.setItem(0 ,new ItemStack (TFMGItems.COAL_COKE_DUST.get(),fuelInventory.getStackInSlot(0).getCount()+itemStack.getCount()));
                    itemEntity.discard();
                }
                continue;

            }

            itemEntity.discard();
        }
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

      //  Lang.translate("goggles.surface_scanner.distance", fuelInventory.getItem(0).getCount())
      //          .style(ChatFormatting.GREEN)
      //          .forGoggles(tooltip,1);
        Lang.translate("goggles.surface_scanner.distance", reinforcementHeight)
                .style(ChatFormatting.GREEN)
                .forGoggles(tooltip,1);
            return true;

    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inputInventory.deserializeNBT(compound.getCompound("InputItems"));
        fuelInventory.deserializeNBT(compound.getCompound("Fuel"));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.put("InputItems", inputInventory.serializeNBT());
        compound.put("Fuel", fuelInventory.serializeNBT());

    }
    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
    }
    @Nonnull
    @Override
    @SuppressWarnings("'net.minecraftforge.items.CapabilityItemHandler' is deprecated and marked for removal ")
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemCapability.cast();
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }
    public void checkType(){

        mainFloor = this.getBlockPos().relative(outputFacing.getOpposite());
        if(!isValidBlock(mainFloor)) {
            type = BlastFurnaceType.INVALID;
            return;
        }

        if(!isAboveClear(mainFloor)){
            type = BlastFurnaceType.INVALID;
        return;
    }


        if(canBeSmall())
            type = BlastFurnaceType.SMALL;
        if(canBeBigLeft())
            type = BlastFurnaceType.BIG_LEFT;
        if(canBeBigRight())
            type = BlastFurnaceType.BIG_RIGHT;


        if(
                !canBeBigLeft()&&
                        !canBeBigRight()&&
                        !canBeSmall()
        )type = BlastFurnaceType.INVALID;








    }




    public boolean isAboveClear(BlockPos pos){
        pos = pos.above();
        return level.getBlockState(pos).is(Blocks.AIR);
    }
    public boolean isAboveValid(BlockPos pos){
        return isAboveValid(pos,false);
    }
    public boolean isAboveValid(BlockPos pos,boolean firstCheck){
        pos = pos.above();
        boolean isValid = level.getBlockState(pos).is(TFMGBlocks.FIREPROOF_BRICKS.get());

        BlockPos pos1 = pos.above();
        int height=2;
        for(int i=0; i <8; i++){

            if(level.getBlockState(pos1).is(TFMGBlocks.FIREPROOF_BRICKS.get())){
                height++;
            } else {
                break;
            }

            pos1 = pos1.above();
        }
        if(height<=this.height||firstCheck)
            this.height=height;


        return isValid;
    }
    //
    public void isReinforcement(BlockPos pos){
        this.isReinforcement(pos,false);
    }
    public void isReinforcement(BlockPos pos,boolean firstCheck){

        BlockPos pos1 = pos;
        int height=0;
        for(int i=0; i <8; i++){

            if(level.getBlockState(pos1).is(TFMGBlocks.FIREPROOF_BRICK_REINFORCEMENT.get())){
                height++;
            } else {
                break;
            }

            pos1 = pos1.above();
        }
        if(height<=this.reinforcementHeight||firstCheck)
            this.reinforcementHeight=height;

    }
    public boolean isValidBlock(BlockPos pos){
        return level.getBlockState(pos).is(TFMGBlocks.FIREPROOF_BRICKS.get());
    }
    //its average :/
    public boolean canBeSmall(){
        BlockPos checkedPos=this.getBlockPos().relative(outputFacing.getOpposite());
        BlockPos mainPos = checkedPos;

        if(!isValidBlock(mainPos))
            return false;
       if(!isAboveClear(mainPos))
           return false;
        checkedPos = mainPos.relative(outputFacing.getClockWise());

        if(!isValidBlock(checkedPos))
            return false;
        if(!isAboveValid(checkedPos,true))
            return false;

          checkedPos = mainPos.relative(outputFacing.getCounterClockWise());

          if(!isValidBlock(checkedPos))
              return false;
          if(!isAboveValid(checkedPos))
              return false;

          checkedPos = mainPos.relative(outputFacing);

        if(!isAboveValid(checkedPos))
            return false;

        isReinforcement(checkedPos.relative(outputFacing.getCounterClockWise()),true);
        isReinforcement(checkedPos.relative(outputFacing.getClockWise()));
        isReinforcement(mainPos.relative(outputFacing.getOpposite()).relative(outputFacing.getCounterClockWise()));
        isReinforcement(mainPos.relative(outputFacing.getOpposite()).relative(outputFacing.getClockWise()));


       // level.setBlock(checkedPos.above(5), Blocks.GOLD_BLOCK.defaultBlockState(), 3);
        return true;
    }

    public boolean canBeBigRight(){
        mainFloor = this.getBlockPos().relative(outputFacing.getOpposite());

        BlockPos checkedPos = this.getBlockPos().relative(outputFacing.getCounterClockWise());

        for(int i = 0; i<4; i++){


            for(int y = 0; y<4; y++){




              if(
                      !(i ==0 && y ==0)&&
                      !(i ==3 && y ==3)&&
                      !(i ==0 && y ==3)&&
                      !(i ==3 && y ==0)

              ) {
                  if(       i ==1 && y ==0){
                      if(isAboveValid(checkedPos)) {
                          checkedPos = checkedPos.relative(outputFacing.getOpposite());
                          continue;
                      } else return false;
                  }

                  if(!isValidBlock(checkedPos)) {
                      return false;
                  }
                        if(i==0&&y==1){
                            if(!isAboveValid(checkedPos,true)) {
                                return false;
                            }
                        }


                    if(
                            i ==0 || i ==3 ||
                            y == 0 || y == 3

                    ){
                        if(!isAboveValid(checkedPos)) {
                            return false;
                        }
                    }
                    else
                        if(!isAboveClear(checkedPos)) {
                            return false;

                        }

              } else{
                  //reinforcements
                    if(i ==0 && y ==0)
                        isReinforcement(checkedPos,true);

                  isReinforcement(checkedPos);



              }


                    checkedPos = checkedPos.relative(outputFacing.getOpposite());

            }
            checkedPos= checkedPos.relative(outputFacing,4);
            checkedPos=  checkedPos.relative(outputFacing.getClockWise());
        }
       // level.setBlock(checkedPos.above(5), Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
        return true;
    }
    public boolean canBeBigLeft(){
        mainFloor = this.getBlockPos().relative(outputFacing.getOpposite());

        BlockPos checkedPos = this.getBlockPos().relative(outputFacing.getClockWise());

        for(int i = 0; i<4; i++){


            for(int y = 0; y<4; y++){




                if(
                        !(i ==0 && y ==0)&&
                                !(i ==3 && y ==3)&&
                                !(i ==0 && y ==3)&&
                                !(i ==3 && y ==0)

                ) {
                    if(       i ==1 && y ==0){
                        if(isAboveValid(checkedPos)) {
                            checkedPos = checkedPos.relative(outputFacing.getOpposite());
                            continue;
                        } else return false;
                    }

                    if(!isValidBlock(checkedPos)) {
                        return false;
                    }

                    if(i==0&&y==1){
                        if(!isAboveValid(checkedPos,true)) {
                            return false;
                        }
                    }

                    if(
                            i ==0 || i ==3 ||
                                    y == 0 || y == 3

                    ){
                        if(!isAboveValid(checkedPos)) {
                            return false;
                        }
                    }
                    else
                    if(!isAboveClear(checkedPos)) {
                        return false;

                    }

                }else{
                    //reinforcements
                    if(i ==0 && y ==0)
                        isReinforcement(checkedPos,true);

                    isReinforcement(checkedPos);



                }


                checkedPos = checkedPos.relative(outputFacing.getOpposite());

            }
            checkedPos= checkedPos.relative(outputFacing,4);
            checkedPos=  checkedPos.relative(outputFacing.getCounterClockWise());
        }
   //     level.setBlock(checkedPos.above(5), Blocks.EMERALD_BLOCK.defaultBlockState(), 3);

        return true;
    }

    public List<ItemEntity> getItemsToPick(BlastFurnaceType type) {
        AABB searchArea=null;
        AABB searchArea1=null;
        AABB searchArea2=null;
        AABB searchArea3=null;

        if(type == BlastFurnaceType.SMALL)
            searchArea = new AABB(this.getBlockPos().relative(outputFacing.getOpposite()).above());

        if(type == BlastFurnaceType.BIG_RIGHT) {
            searchArea = new AABB(this.getBlockPos().relative(outputFacing.getOpposite()).above());

            searchArea1 = new AABB(this.getBlockPos().relative(outputFacing.getOpposite(), 2).above());
            searchArea2 = new AABB(this.getBlockPos().relative(outputFacing.getOpposite(), 2).above().relative(outputFacing.getClockWise()));
            searchArea3 = new AABB(this.getBlockPos().relative(outputFacing.getOpposite()).above().relative(outputFacing.getClockWise()));

        }
        if(type == BlastFurnaceType.BIG_LEFT) {
            searchArea = new AABB(this.getBlockPos().relative(outputFacing.getOpposite()).above());

            searchArea1 = new AABB(this.getBlockPos().relative(outputFacing.getOpposite(), 2).above());
            searchArea2 = new AABB(this.getBlockPos().relative(outputFacing.getOpposite(), 2).above().relative(outputFacing.getCounterClockWise()));
            searchArea3 = new AABB(this.getBlockPos().relative(outputFacing.getOpposite()).above().relative(outputFacing.getCounterClockWise()));


        }


        if(searchArea1!=null){
            List<ItemEntity> itemList= new ArrayList<>();
            itemList.addAll(level.getEntitiesOfClass(ItemEntity.class, searchArea));
            itemList.addAll(level.getEntitiesOfClass(ItemEntity.class, searchArea1));
            itemList.addAll(level.getEntitiesOfClass(ItemEntity.class, searchArea2));
            itemList.addAll(level.getEntitiesOfClass(ItemEntity.class, searchArea3));
            return itemList;
        }

        return level.getEntitiesOfClass(ItemEntity.class, searchArea);
    }



    public enum BlastFurnaceType{
        SMALL,
        BIG_LEFT,
        BIG_RIGHT,
        INVALID
    }
}
