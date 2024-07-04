package com.drmangotea.createindustry.blocks.machines.metal_processing.coke_oven;



import com.drmangotea.createindustry.blocks.machines.TFMGMachineBlockEntity;
import com.drmangotea.createindustry.recipes.coking.CokingRecipe;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;

import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;


import static com.drmangotea.createindustry.blocks.machines.metal_processing.coke_oven.CokeOvenBlock.CONTROLLER_TYPE;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class CokeOvenBlockEntity extends TFMGMachineBlockEntity implements IWrenchable {

    public boolean isController = false;

    public CokeOvenBlockEntity controller;

    public CokingRecipe lastRecipe;

    int progress = 0;

    public LerpedFloat visualDoorAngle = LerpedFloat.angular();
    public int doorAngle = 0;


    public int timer = -1;

    public SmartInventory inputInventory;
    public LazyOptional<IItemHandlerModifiable> itemCapability;

    public final int CARBON_DIOXIDE_PRODUCTION = 9;

    public CokeOvenBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInventory = new SmartInventory(1, this)
                .withMaxStackSize(64);


        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(inputInventory));

        tank2.forbidInsertion();
        tank1.forbidInsertion();
    }

    public void tick(){
        super.tick();


       // if(isController)
       //     level.setBlock(getBlockPos().above(5), Blocks.DIAMOND_BLOCK.defaultBlockState(),3);




        if(controller==null){
            controller = this;
            inputInventory.forbidInsertion();
        } else {
            inputInventory.allowInsertion();
        }

       //if(controller!=this)
       //    level.setBlock(this.getBlockPos().above(5), Blocks.GOLD_BLOCK.defaultBlockState(),3);

        visualDoorAngle.chase(doorAngle, 0.2f, LerpedFloat.Chaser.EXP);
        visualDoorAngle.tickChaser();



        // if(controller != null)
        //     refreshCapability();
        if(isController){
            controller = this;
        }
        setControllers();

        if(controller!=null)
            if(!controller.isController)
                controller=this;

        if(controller!=null)
            if(!(level.getBlockEntity(controller.getBlockPos()) instanceof CokeOvenBlockEntity))
                controller = this;


        setBlockState();

        setTimer();

        if(lastRecipe!=null) {
            if(timer == -1){
                progress = 0;
            }else {

                progress = 100-(timer/(lastRecipe.getProcessingDuration()/100));
            }
        }


        if(timer>=0&&timer<44){
            doorAngle = 90;
        }else doorAngle = 0;



        if(timer==0) {
            timer=-1;
            process();
        }

        /////
        RecipeWrapper inventoryIn = new RecipeWrapper(inputInventory);
        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)&&timer==-1) {
            Optional<CokingRecipe> recipe = TFMGRecipeTypes.COKING.find(inventoryIn, level);
            if (!recipe.isPresent()) {
                timer = -1;
                sendData();
            } else {
                lastRecipe = recipe.get();
                sendData();
            }
            return;
        }





    }


    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInventory);
    }


    public void setBlockState(){

        if(controller == this){
            level.setBlock(getBlockPos(),this.getBlockState().setValue(CONTROLLER_TYPE, CokeOvenBlock.ControllerType.CASUAL),2);

        }


        if(!this.isController)
            return;


        if(timer==-1) {
            level.setBlock(getBlockPos(),this.getBlockState().setValue(CONTROLLER_TYPE, CokeOvenBlock.ControllerType.MIDDLE_OFF),2);
            if(level.getBlockEntity(getBlockPos().below())instanceof CokeOvenBlockEntity)
                level.setBlock(getBlockPos().below(),level.getBlockState(getBlockPos().below()).setValue(CONTROLLER_TYPE, CokeOvenBlock.ControllerType.BOTTOM_OFF).setValue(FACING,this.getBlockState().getValue(FACING)),2);
            if(level.getBlockEntity(getBlockPos().above())instanceof CokeOvenBlockEntity)
                level.setBlock(getBlockPos().above(),level.getBlockState(getBlockPos().above()).setValue(CONTROLLER_TYPE, CokeOvenBlock.ControllerType.TOP_OFF).setValue(FACING,this.getBlockState().getValue(FACING)),2);
        }else {
            level.setBlock(getBlockPos(),this.getBlockState().setValue(CONTROLLER_TYPE, CokeOvenBlock.ControllerType.MIDDLE_ON),2);
            if(level.getBlockEntity(getBlockPos().below())instanceof CokeOvenBlockEntity)
                level.setBlock(getBlockPos().below(),level.getBlockState(getBlockPos().below()).setValue(CONTROLLER_TYPE, CokeOvenBlock.ControllerType.BOTTOM_ON).setValue(FACING,this.getBlockState().getValue(FACING)),2);
            if(level.getBlockEntity(getBlockPos().above())instanceof CokeOvenBlockEntity)
                level.setBlock(getBlockPos().above(),level.getBlockState(getBlockPos().above()).setValue(CONTROLLER_TYPE, CokeOvenBlock.ControllerType.TOP_ON).setValue(FACING,this.getBlockState().getValue(FACING)),2);
        }







    }

    private void setTimer() {



        if(lastRecipe!=null
                &&isController
                &&timer==-1
                &&inputInventory.getItem(0).getCount()>=1&&
                (tank2.getPrimaryHandler().getFluidAmount()+CARBON_DIOXIDE_PRODUCTION)<=tank2.getPrimaryHandler().getCapacity()&&
                (tank1.getPrimaryHandler().getFluidAmount()+lastRecipe.getFluidResults().get(0).getAmount())<=tank1.getPrimaryHandler().getCapacity(


                )){
            timer = lastRecipe.getProcessingDuration();
            inputInventory.setItem(0,new ItemStack(inputInventory.getItem(0).getItem(),inputInventory.getItem(0).getCount()-1));

        }
        //  if(lastRecipe != null)
        //      if((tank1.getPrimaryHandler().getFluidAmount()+lastRecipe.getFluidResults().get(0).getAmount())>tank1.getPrimaryHandler().getCapacity())
        //          timer = -1;
        //  if(lastRecipe != null)
        //      if((tank2.getPrimaryHandler().getFluidAmount()+CARBON_DIOXIDE_PRODUCTION)>tank2.getPrimaryHandler().getCapacity())
        //          timer = -1;


        if(lastRecipe!=null
                &&timer>0
                &&isController&&
                (tank2.getPrimaryHandler().getFluidAmount()+CARBON_DIOXIDE_PRODUCTION)<=tank2.getPrimaryHandler().getCapacity()&&
                (tank1.getPrimaryHandler().getFluidAmount()+lastRecipe.getFluidResults().get(0).getAmount())<=tank1.getPrimaryHandler().getCapacity()
        ) {
            timer--;
            tank1.getPrimaryHandler().setFluid(new FluidStack(lastRecipe.getFluidResults().get(0), tank1.getPrimaryHandler().getFluidAmount()+lastRecipe.getFluidResults().get(0).getAmount()));
            //tank2.getPrimaryHandler().setFluid(new FluidStack(TFMGFluids.CARBON_DIOXIDE.getSource(),tank2.getPrimaryHandler().getFluidAmount()+CARBON_DIOXIDE_PRODUCTION));
        }

    }

    public void process(){



        if(!isController)
            return;
        //RecipeWrapper inventoryIn = new RecipeWrapper(inputInventory);
        //if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
        //    Optional<CokingRecipe> recipe = TFMGRecipeTypes.COKING.find(inventoryIn, level);
        //    if (!recipe.isPresent())
        //        return;
        //    lastRecipe = recipe.get();
        //})
        BlockPos toSpawn = getBlockPos().below().relative(this.getBlockState().getValue(FACING));

        if(lastRecipe == null)
            return;

        ItemEntity itemToSpawn = new ItemEntity(level, toSpawn.getX() + 0.5f, toSpawn.getY() + 0.5f, toSpawn.getZ() + 0.5f, lastRecipe.getResultItem().copy());
        level.addFreshEntity(itemToSpawn);

        // }

    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().expandTowards(0, -2, 0).expandTowards(0, 2, 0);
    }

    private void refreshCapability() {




        if(controller!=null)
            if (controller.tank1 != null)
                if (controller.tank2 != null)
                    if (controller.inputInventory != null){
                        fluidCapability = LazyOptional.of(() -> new CombinedTankWrapper(controller.tank1.getPrimaryHandler(), controller.tank2.getPrimaryHandler()));
                        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(controller.inputInventory));
                    }


    }
    public void setControllers(){
        if(!isValid())
            return;
        if(!isController)
            return;

        Direction facing = this.getBlockState().getValue(FACING);

        BlockPos checkedPos=this.getBlockPos().above();


        for(int i = 0; i<3;i++){
            for(int y = 0; y<3;y++){



                CokeOvenBlockEntity checkedBE = (CokeOvenBlockEntity) level.getBlockEntity(checkedPos);
                checkedBE.controller = this;

                if(checkedBE.getBlockState().getValue(FACING)!=getBlockState().getValue(FACING))
                    level.setBlock(checkedPos,checkedBE.getBlockState().setValue(FACING,getBlockState().getValue(FACING)),2);

                checkedPos = checkedPos.below();
            }

            checkedPos = checkedPos.above(3);
            checkedPos = checkedPos.relative(facing.getOpposite());
        }

    }

    public boolean isValid(){
        Direction facing = this.getBlockState().getValue(FACING);

        BlockPos checkedPos=this.getBlockPos().above();

        if(controller!=this){
            isController = false;
            return false;
        }


        for(int i = 0; i<3;i++){
            for(int y = 0; y<3;y++){



                if(checkedPos == this.getBlockPos()){
                    if(!isCokeOvenBlock(checkedPos,true)) {
                        isController = false;
                        return false;
                    }
                }
                else
//
                    if(!isCokeOvenBlock(checkedPos)) {
                        isController = false;
                        return false;
                    }
//
                if(occupiedByOtherController(checkedPos)) {
                    isController = false;
                    return false;
                }



                checkedPos = checkedPos.below();
            }

            checkedPos = checkedPos.above(3);
            checkedPos = checkedPos.relative(facing.getOpposite());
        }
        return true ;
    }

    public boolean isCokeOvenBlock(BlockPos pos){return isCokeOvenBlock(pos,false);}
    public boolean isCokeOvenBlock(BlockPos pos, boolean controllerSensitive){
        if(controllerSensitive)
            if(level.getBlockState(pos).is(TFMGBlocks.COKE_OVEN.get()))
                if(isController)
                    return false;

        return level.getBlockState(pos).is(TFMGBlocks.COKE_OVEN.get());
    }
    public boolean occupiedByOtherController(BlockPos pos){

        if(controller == null)
            controller = this;

        if(level.getBlockEntity(pos).getBlockState().is(TFMGBlocks.COKE_OVEN.get()))
            if(((CokeOvenBlockEntity)level.getBlockEntity(pos)).controller == ((CokeOvenBlockEntity)level.getBlockEntity(pos)).controller||((CokeOvenBlockEntity)level.getBlockEntity(pos)).controller == this)
                //          if(()
                return false;

        return true;
    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        //if(controller !=null)
        //    Lang.translate("goggles.surface_scanner.distance",controller.getBlockPos().getX())
        //            .style(ChatFormatting.DARK_BLUE)
        //            .forGoggles(tooltip,1);
        //if(controller !=null)
        //    Lang.translate("goggles.surface_scanner.distance",controller.getBlockPos().getY())
        //            .style(ChatFormatting.DARK_BLUE)
        //            .forGoggles(tooltip,1);
        // if(controller !=null)
        //     Lang.translate("goggles.surface_scanner.distance",controller.timer)
        //             .style(ChatFormatting.DARK_BLUE)
        //             .forGoggles(tooltip,1);
//
        //if(controller==null){
        //    Lang.translate("aaaaaaaaaaaaaaaaaaaaaaaaaaa")
        //            .style(ChatFormatting.DARK_RED)
        //            .forGoggles(tooltip,1);
//

        //     return true;
        // }
        if(controller!=null)
            if(controller.getBlockPos() == getBlockPos()&&!isValid()){
                Lang.translate("goggles.coke_oven.invalid")
                        .style(ChatFormatting.DARK_RED)
                        .forGoggles(tooltip,1);


                return true;
            }



        if(!isController)
            return false;




        if(lastRecipe != null)
            if((tank1.getPrimaryHandler().getFluidAmount()+lastRecipe.getFluidResults().get(0).getAmount())>tank1.getPrimaryHandler().getCapacity()
                    &&(tank2.getPrimaryHandler().getFluidAmount()+CARBON_DIOXIDE_PRODUCTION)>tank2.getPrimaryHandler().getCapacity()) {
                Lang.translate("goggles.coke_oven.tank_full")
                        .style(ChatFormatting.DARK_RED)
                        .forGoggles(tooltip,1);
                return true;
            }



        Lang.translate("goggles.coke_oven.status")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip,1);

        //

        Lang.translate("goggles.coke_oven.progress", progress)
                .add(Lang.translate("goggles.misc.percent_symbol"))
                .style(ChatFormatting.DARK_PURPLE)
                .forGoggles(tooltip,1);


        //
        Lang.translate("goggles.misc.storage_info")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip,1);



        //   Lang.translate("goggles.coke_oven.fluid_amount_output",tank1.getPrimaryHandler().getCapacity())
        //           .style(ChatFormatting.DARK_AQUA)
        //           .forGoggles(tooltip,1);
        //   Lang.translate("goggles.coke_oven.fluid_amount_exhaust",tank2.getPrimaryHandler().getCapacity())
        //           .style(ChatFormatting.DARK_AQUA)
        //           .forGoggles(tooltip,1);
        Lang.translate("goggles.coke_oven.item_count",inputInventory.getItem(0).getCount())
                .style(ChatFormatting.GOLD)
                .forGoggles(tooltip,1);




        return true;

    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        inputInventory.deserializeNBT(compound.getCompound("InputItems"));

        isController = compound.getBoolean("Is Controller");

        timer = compound.getInt("Timer");


        // controller = (CokeOvenBlockEntity) level.getBlockEntity(new BlockPos(
        //         compound.getInt("controllerX"),
        //         compound.getInt("controllerY"),
        //         compound.getInt("controllerZ")
//
        // ));


    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.put("InputItems", inputInventory.serializeNBT());
        compound.putBoolean("Is Controller",isController);
        compound.putInt("Timer", timer);


        // compound.putInt("controllerX", controller.getBlockPos().getX());
        // compound.putInt("controllerY", controller.getBlockPos().getY());
        // compound.putInt("controllerZ", controller.getBlockPos().getZ());





    }

    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
    }
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if(controller!=null)
            refreshCapability();
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    }
}