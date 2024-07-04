package com.drmangotea.createindustry.blocks.machines.metal_processing.blast_furnace;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.blocks.machines.TFMGMachineBlockEntity;
import com.drmangotea.createindustry.recipes.industrial_blasting.IndustrialBlastingRecipe;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class BlastFurnaceOutputBlockEntity extends TFMGMachineBlockEntity implements IHaveGoggleInformation {



    public BlastFurnaceType type;
    public Direction outputFacing = getBlockState().getValue(FACING);
    public BlockPos mainFloor;
    public int height=2;
    public int reinforcementHeight = 0;

    public int validHeight =0;

    public float blastFurnaceLevel =0;

    public float fuelEfficiency =1000;

    public float speedModifier = 1;

    public int timer = -1;

    public IndustrialBlastingRecipe recipe;


    public LerpedFloat coalCokeHeight = LerpedFloat.linear();

    //item storage
    public LazyOptional<IItemHandlerModifiable> itemCapability;

    public SmartInventory inputInventory;

    public SmartInventory fuelInventory;

    int debug = 0;





    public BlastFurnaceOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {


        super(type, pos, state);
        inputInventory = new SmartInventory(1, this).forbidInsertion().forbidExtraction()
                .withMaxStackSize(64);
        fuelInventory = new SmartInventory(1, this).forbidInsertion().forbidExtraction()
                .withMaxStackSize(64);

        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(inputInventory,fuelInventory));


        tank1.getPrimaryHandler().setCapacity(8000);
        tank2.getPrimaryHandler().setCapacity(8000);
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInventory);
        ItemHelper.dropContents(level, worldPosition, fuelInventory);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    }
    public void tick(){
        super.tick();


        manageMoltenMetal();


        if(type== BlastFurnaceType.SMALL) {
            blastFurnaceLevel = ((float) validHeight / 2) - 1;
            blastFurnaceLevel = (float) Math.min(blastFurnaceLevel,2.5);
        }


        if(speedModifier!=0) {
            speedModifier = (blastFurnaceLevel/2);
            fuelEfficiency = 400 * (speedModifier);

        }else {
            fuelEfficiency = 400;
            speedModifier = 1;

        }



        outputFacing = getBlockState().getValue(FACING);
        coalCokeHeight.chase(fuelInventory.getStackInSlot(0).getCount()+inputInventory.getStackInSlot(0).getCount(), 0.1f, LerpedFloat.Chaser.EXP);
        coalCokeHeight.tickChaser();

        checkType();

        if(reinforcementHeight<2) {
            type = BlastFurnaceType.INVALID;
            return;
        }


        if(type == BlastFurnaceType.INVALID)
            return;




        int maxLevelByReinforcements = reinforcementHeight*2;
        validHeight = Math.min(maxLevelByReinforcements,height);




        if(height<4) {
            type = BlastFurnaceType.INVALID;
            return;
        }

        if(recipe !=null)
            if(timer == -1&&
                    (tank1.getPrimaryHandler().getFluidAmount()+recipe.getFluidResults().get(0).getAmount())<=tank1.getPrimaryHandler().getCapacity()&&
                    (tank2.getPrimaryHandler().getFluidAmount()+recipe.getFluidResults().get(1).getAmount())<=tank2.getPrimaryHandler().getCapacity()&&
                     type != BlastFurnaceType.INVALID&&
                    !fuelInventory.isEmpty()
            ) {

                timer= (int) (recipe.getProcessingDuration()/speedModifier);
                    inputInventory.getStackInSlot(0).setCount(inputInventory.getStackInSlot(0).getCount()-1);
            }


        RecipeWrapper inventoryIn = new RecipeWrapper(inputInventory);
        if (recipe == null || !recipe.matches(inventoryIn, level)) {
            Optional<IndustrialBlastingRecipe> recipe = TFMGRecipeTypes.INDUSTRIAL_BLASTING.find(inventoryIn, level);
            if (!recipe.isPresent()) {
                timer = -1;
                sendData();
            } else {
                this.recipe = recipe.get();
                sendData();
            }
        }

        acceptInsertedItems();

       // if(recipe!=null)
       //     if(!fuelInventory.isEmpty()){
       //         timer= (int) (recipe.getProcessingDuration()/speedModifier);
       //     }


        if(type != BlastFurnaceType.INVALID&&timer>0&&
                (tank1.getPrimaryHandler().getFluidAmount()+recipe.getFluidResults().get(0).getAmount())<=tank1.getPrimaryHandler().getCapacity()&&
                (tank2.getPrimaryHandler().getFluidAmount()+recipe.getFluidResults().get(1).getAmount())<=tank2.getPrimaryHandler().getCapacity()) {
            timer--;
            int random = Create.RANDOM.nextInt((int) Math.abs(fuelEfficiency)+1);
            if(random == 69)
                fuelInventory.getStackInSlot(0).shrink(1);

        }


        if(timer == 0){
            process();
            timer = -1;
        }

    }



    public void manageMoltenMetal(){
        BlockPos posToSpawn;


        if (timer>0) {
            posToSpawn = this.getBlockPos().relative(outputFacing.getOpposite()).above();


                if(!level.getBlockState(posToSpawn).is(TFMGBlocks.MOLTEN_METAL.get())) {
                    level.setBlock(posToSpawn, TFMGBlocks.MOLTEN_METAL.getDefaultState(), 3);
                    level.setBlock(posToSpawn.above(), TFMGBlocks.MOLTEN_METAL.getDefaultState(), 3);

                }else {

                    ((MoltenMetalBlockEntity) level.getBlockEntity(posToSpawn)).discardTimer = 40;
                    ((MoltenMetalBlockEntity) level.getBlockEntity(posToSpawn.above())).discardTimer = 40;

                }
        }



    }
    @Override
    protected AABB createRenderBoundingBox() {
        int x = getBlockPos().getX();
        int y = getBlockPos().getY();
        int z = getBlockPos().getZ();

        return new AABB(x-10,y-10,z-10,x+10,y+10,z+10);
    }

    public void process() {


        if (level.isClientSide)
            return;

        tank1.getPrimaryHandler().setFluid(new FluidStack(recipe.getFluidResults().get(0).getFluid(), tank1.getPrimaryHandler().getFluidAmount()+recipe.getFluidResults().get(0).getAmount()));
        tank2.getPrimaryHandler().setFluid(new FluidStack(recipe.getFluidResults().get(1).getFluid(), tank2.getPrimaryHandler().getFluidAmount()+recipe.getFluidResults().get(1).getAmount()));

    }

    public void acceptInsertedItems(){
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


            } else {



                if(itemStack.getCount()==1) {
                    ItemStack stack2 = itemStack;
                    stack2.setCount(2);
                    itemEntity.setItem(stack2);
                }

                int freeSpace2 = inputInventory.getStackInSlot(0).getMaxStackSize()-inputInventory.getStackInSlot(0).getCount();

                int count = itemStack.getCount();
                if(!inputInventory.isEmpty())
                    if(!inputInventory.getItem(0).is(itemStack.getItem()))
                        continue;

                if(count>freeSpace2){
                    itemStack.setCount(itemStack.getCount()-freeSpace2);
                    inputInventory.setItem(0 ,new ItemStack (itemStack.getItem(),inputInventory.getStackInSlot(0).getCount()+freeSpace2));
                }else {
                    inputInventory.setItem(0 ,new ItemStack (itemStack.getItem(),inputInventory.getStackInSlot(0).getCount()+itemStack.getCount()));
                    itemEntity.discard();
                }


            }
        }
    }



    @Override
    @SuppressWarnings("removal")
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
       // Lang.translate("goggles.blast_furnace.height", timer)
       //         .style(ChatFormatting.LIGHT_PURPLE)
       //         .forGoggles(tooltip, 1);
        if(type == BlastFurnaceType.INVALID){
            Lang.translate("goggles.blast_furnace.invalid")
                    .style(ChatFormatting.RED)
                    .forGoggles(tooltip,1);
        } else {


            Lang.translate("goggles.blast_furnace.stats")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip,1);


            if(timer > 0) {
                Lang.translate("goggles.blast_furnace.status.running")
                        .style(ChatFormatting.YELLOW)
                        .forGoggles(tooltip, 1);
            }else
                Lang.translate("goggles.blast_furnace.status.off")
                        .style(ChatFormatting.YELLOW)
                        .forGoggles(tooltip, 1);



            Lang.translate("goggles.blast_furnace.height", validHeight)
                    .style(ChatFormatting.GOLD)
                    .forGoggles(tooltip, 1);




            Lang.translate("goggles.misc.storage_info")
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip, 1);
            Lang.translate("goggles.blast_furnace.item_count", inputInventory.getStackInSlot(0).getCount())
                    .style(ChatFormatting.AQUA)
                    .forGoggles(tooltip, 1);
            Lang.translate("goggles.blast_furnace.fuel_amount", fuelInventory.getStackInSlot(0).getCount())
                    .style(ChatFormatting.AQUA)
                    .forGoggles(tooltip, 1);
            Lang.translate("goggles.blast_furnace.nothing_lol")
                    .style(ChatFormatting.AQUA)
                    .forGoggles(tooltip, 1);


        }



        containedFluidTooltip(tooltip, isPlayerSneaking,
                getCapability(ForgeCapabilities.FLUID_HANDLER));


        return true;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inputInventory.deserializeNBT(compound.getCompound("InputItems"));
        fuelInventory.deserializeNBT(compound.getCompound("Fuel"));
        timer = compound.getInt("Timer");
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.put("InputItems", inputInventory.serializeNBT());
        compound.put("Fuel", fuelInventory.serializeNBT());
        compound.putInt("Timer", timer);

    }
    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
    }
    @Nonnull
    @Override

    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }
    public void checkType(){

        mainFloor = this.getBlockPos().relative(outputFacing.getOpposite());
        if(!isValidBlock(mainFloor)) {
            type = BlastFurnaceType.INVALID;
            return;
        }
        if(reinforcementHeight<2)
            type = BlastFurnaceType.INVALID;
        if(!isAboveClear(mainFloor)){
            type = BlastFurnaceType.INVALID;
        return;
    }


        if(canBeSmall())
            type = BlastFurnaceType.SMALL;


        if(!canBeSmall())
            type = BlastFurnaceType.INVALID;








    }




    public boolean isAboveClear(BlockPos pos){
        pos = pos.above();
        return level.getBlockState(pos).is(Blocks.AIR)||level.getBlockState(pos).is(TFMGBlocks.MOLTEN_METAL.get());
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

        checkedPos = mainPos.relative(outputFacing.getOpposite());

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

    public List<ItemEntity> getItemsToPick(BlastFurnaceType type) {

        return level.getEntitiesOfClass(ItemEntity.class, new AABB(this.getBlockPos().relative(outputFacing.getOpposite()).above()));
    }



    public enum BlastFurnaceType{
        SMALL,
        INVALID
    }
}
