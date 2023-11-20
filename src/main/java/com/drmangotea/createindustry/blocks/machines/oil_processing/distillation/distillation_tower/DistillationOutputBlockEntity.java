package com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.distillation_tower;



import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.distillery.DistilleryOutputBlockEntity;
import com.drmangotea.createindustry.recipes.distillation.DistillationRecipe;
import com.drmangotea.createindustry.recipes.distillation.AbstractDistillationRecipe;
import com.drmangotea.createindustry.recipes.distillation.AdvancedDistillationRecipe;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.Optional;

public class DistillationOutputBlockEntity extends DistilleryOutputBlockEntity implements IHaveGoggleInformation {

    private static final Object AdvancedDistillationRecipesKey = new Object();

    public int speed=3;
    public int processTimer=0;

    public int foundOutputs=0;
    public int recipeOutputs=6;

    public DistillationOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected <C extends Container> boolean matchItemlessRecipe(Recipe<C> recipe) {
        if (recipe == null)
            return false;
        Optional<DistillationControllerBlockEntity> controller = getDistillationController();
        if (!controller.isPresent())
            return false;
        return AbstractDistillationRecipe.match2(controller.get(), recipe);
    }

    @Override
    protected boolean updateController() {


        if (level == null || level.isClientSide)
            return true;

        DistillationControllerBlockEntity be=null;

        if(level.getBlockEntity(getBlockPos().below())instanceof DistillationControllerBlockEntity)
            be = (DistillationControllerBlockEntity) level.getBlockEntity(getBlockPos().below());

        if(be==null)
            return true;

        recipe = getMatchingRecipes(be);

        //List<Recipe<?>> recipes = getMatchingRecipes();
        //if (recipes.isEmpty())
        //    return true;
        //currentRecipe = recipes.get(0);



        startProcessing();
        sendData();
        return true;
    }


    @Override
    public void tick() {
        super.tick();


        if (level != null) {
            if ((!level.isClientSide || isVirtual())) {
                process();
                sendData();
            }
        }
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }
    }
    @Override
    protected void process() {
        updateController();





     //   if((currentRecipe instanceof ShapelessRecipe))
     //       return;

        BlockEntity above1 = level.getBlockEntity(this.getBlockPos().above(2));
        BlockEntity above2 = level.getBlockEntity(this.getBlockPos().above(4));
        BlockEntity above3 = level.getBlockEntity(this.getBlockPos().above(6));
        BlockEntity above4 = level.getBlockEntity(this.getBlockPos().above(8));
        BlockEntity above5 = level.getBlockEntity(this.getBlockPos().above(10));





    Optional<DistillationControllerBlockEntity> optionalController = getDistillationController();
    if (!optionalController.isPresent())
        return;
        foundOutputs = 1;

    if(above1 instanceof DistillationOutputBlockEntity)
        foundOutputs = 2;


    if(above2 instanceof DistillationOutputBlockEntity) {
        if(foundOutputs==2) {
            foundOutputs = 3;
        }else foundOutputs = 1;
    }


    if(above3 instanceof DistillationOutputBlockEntity) {
        if(foundOutputs==3) {
            foundOutputs = 4;
        }else foundOutputs = 2;
    }

        if(above4 instanceof DistillationOutputBlockEntity) {
            if(foundOutputs==4) {
                foundOutputs = 5;
            }else foundOutputs = 3;
        }
        if(above5 instanceof DistillationOutputBlockEntity) {
            if(foundOutputs==5) {
                foundOutputs = 6;
            }else foundOutputs = 4;
        }

        if (recipe == null)
            return;


    if(((AdvancedDistillationRecipe) recipe).getOutputCount((AdvancedDistillationRecipe) recipe)>foundOutputs)
        return;
        FluidStack fluidInRecipe1=null;
        FluidStack fluidInRecipe2=null;
        FluidStack fluidInRecipe3=null;
        FluidStack fluidInRecipe4=null;
        FluidStack fluidInRecipe5=null;
        FluidStack fluidInRecipe6=null;

         fluidInRecipe1 = ((AdvancedDistillationRecipe) recipe).getFirstFluidResult();
        if(((AdvancedDistillationRecipe) recipe).getOutputCount((AdvancedDistillationRecipe) recipe)>=2)
         fluidInRecipe2 = ((AdvancedDistillationRecipe) recipe).getSecondFluidResult();
        if(((AdvancedDistillationRecipe) recipe).getOutputCount((AdvancedDistillationRecipe) recipe)>=3)
         fluidInRecipe3 = ((AdvancedDistillationRecipe) recipe).getThirdFluidResult();
        if(((AdvancedDistillationRecipe) recipe).getOutputCount((AdvancedDistillationRecipe) recipe)>=4)
         fluidInRecipe4 = ((AdvancedDistillationRecipe) recipe).getFourthFluidResult();
        if(((AdvancedDistillationRecipe) recipe).getOutputCount((AdvancedDistillationRecipe) recipe)>=5)
         fluidInRecipe5 = ((AdvancedDistillationRecipe) recipe).getFifthFluidResult();
        if(((AdvancedDistillationRecipe) recipe).getOutputCount((AdvancedDistillationRecipe) recipe)>=6)
         fluidInRecipe6 = ((AdvancedDistillationRecipe) recipe).getSixthFluidResult();



    if(!hasIndustrialPipes(foundOutputs))
        return;

//
    if (fluidInRecipe1.getFluid() != this.tankInventory.getFluid().getFluid()
         && tankInventory.getFluidAmount()!=0
    )
        return;
if(fluidInRecipe2!=null)
        if(above1 instanceof DistillationOutputBlockEntity)
    if(foundOutputs>=2)
  if (fluidInRecipe2.getFluid() != (((DistillationOutputBlockEntity) above1).tankInventory.getFluid().getFluid())
          &&((DistillationOutputBlockEntity) above1).tankInventory.getFluidAmount()!=0
  )
      return;
        if(fluidInRecipe3!=null)
        if(above2 instanceof DistillationOutputBlockEntity)
    if(foundOutputs>=3)
  if (fluidInRecipe3.getFluid() != (((DistillationOutputBlockEntity) above2).tankInventory.getFluid().getFluid())
          &&((DistillationOutputBlockEntity) above2).tankInventory.getFluidAmount()!=0
  )
      return;
        if(fluidInRecipe4!=null)
        if(above3 instanceof DistillationOutputBlockEntity)
    if(foundOutputs>=4)
    if (fluidInRecipe4.getFluid() != (((DistillationOutputBlockEntity) above3).tankInventory.getFluid().getFluid())
            &&((DistillationOutputBlockEntity) above3).tankInventory.getFluidAmount()!=0
    )
        return;
        if(fluidInRecipe5!=null)
        if(above4 instanceof DistillationOutputBlockEntity)
    if(foundOutputs>=5)
    if (fluidInRecipe5.getFluid() != (((DistillationOutputBlockEntity) above4).tankInventory.getFluid().getFluid())
            &&((DistillationOutputBlockEntity) above4).tankInventory.getFluidAmount()!=0
    )
        return;
        if(fluidInRecipe6!=null)
        if(above5 instanceof DistillationOutputBlockEntity)
    if(foundOutputs>=6)
    if (fluidInRecipe6.getFluid() != (((DistillationOutputBlockEntity) above5).tankInventory.getFluid().getFluid())
            &&((DistillationOutputBlockEntity) above5).tankInventory.getFluidAmount()!=0
    )
        return;
//
        if(((AdvancedDistillationRecipe) recipe).getOutputCount((AdvancedDistillationRecipe) recipe)>=1)
            if(tankInventory.getFluidAmount()+((AdvancedDistillationRecipe) recipe).getFirstFluidResult().getAmount()>8000)
                return;
        if(((AdvancedDistillationRecipe) recipe).getOutputCount((AdvancedDistillationRecipe) recipe)>=2)
            if(((DistillationOutputBlockEntity)above1).tankInventory.getFluidAmount()+((AdvancedDistillationRecipe) recipe).getSecondFluidResult().getAmount()>8000)
                return;
        if(((AdvancedDistillationRecipe) recipe).getOutputCount((AdvancedDistillationRecipe) recipe)>=3)
            if(((DistillationOutputBlockEntity)above2).tankInventory.getFluidAmount()+((AdvancedDistillationRecipe) recipe).getThirdFluidResult().getAmount()>8000)
                return;
        if(((AdvancedDistillationRecipe) recipe).getOutputCount((AdvancedDistillationRecipe) recipe)>=4)
            if(((DistillationOutputBlockEntity)above3).tankInventory.getFluidAmount()+((AdvancedDistillationRecipe) recipe).getFourthFluidResult().getAmount()>8000)
                return;
        if(((AdvancedDistillationRecipe) recipe).getOutputCount((AdvancedDistillationRecipe) recipe)>=5)
            if(((DistillationOutputBlockEntity)above4).tankInventory.getFluidAmount()+((AdvancedDistillationRecipe) recipe).getFifthFluidResult().getAmount()>8000)
                return;
        if(((AdvancedDistillationRecipe) recipe).getOutputCount((AdvancedDistillationRecipe) recipe)>=6)
            if(((DistillationOutputBlockEntity)above5).tankInventory.getFluidAmount()+((AdvancedDistillationRecipe) recipe).getSixthFluidResult().getAmount()>8000)
                return;

    if(getDistillationController().get().getTanks().get(true).getPrimaryHandler().getFluid().getFluid() != ((AdvancedDistillationRecipe) recipe).getInputFluid().getMatchingFluidStacks().get(0).getFluid())
            return;

    DistillationControllerBlockEntity controller = optionalController.get();



    if (!controller.getTanks().get(true).isEmpty()) {


           // if(((AdvancedDistillationRecipe)currentRecipe).getInputFluid().getMatchingFluidStacks().get(0).getFluid() != TFMGFluids.HEAVY_OIL.get())
           //   return;


            if(!controller.hasTank)
                return;


            if(controller.towerLevel<4)
                return;

            if(controller.towerLevel>=12) {
                speed = 3;
            } else speed=6;

            if(processTimer<speed){
                processTimer++;
                return;
            }
            processTimer=0;


            int desiredHeight = (foundOutputs*2)+1;
            if(controller.getTank().height<desiredHeight)
                return;

        if(!level.isClientSide) {
            controller.getTanks().get(true).getPrimaryHandler().drain(((AdvancedDistillationRecipe) recipe).getFluidIngredients().get(0).getRequiredAmount(), IFluidHandler.FluidAction.EXECUTE);

       
        tankInventory.setFluid(new FluidStack(((AdvancedDistillationRecipe) recipe).getFirstFluidResult().getFluid(), ((AdvancedDistillationRecipe) recipe).getFirstFluidResult().getAmount() + this.tankInventory.getFluidAmount()));
            if(above1 instanceof DistillationOutputBlockEntity)
            if(foundOutputs>=2)
                if (((AdvancedDistillationRecipe) recipe).getResults().toArray().length>1)
        ((DistillationOutputBlockEntity) above1).tankInventory.setFluid(new FluidStack(((AdvancedDistillationRecipe) recipe).getSecondFluidResult().getFluid(), ((AdvancedDistillationRecipe) recipe).getSecondFluidResult().getAmount() + ((DistillationOutputBlockEntity) above1).tankInventory.getFluidAmount()));
            if(above2 instanceof DistillationOutputBlockEntity)
            if(foundOutputs>=3)
                if (((AdvancedDistillationRecipe) recipe).getResults().toArray().length>2)
        ((DistillationOutputBlockEntity) above2).tankInventory.setFluid(new FluidStack(((AdvancedDistillationRecipe) recipe).getThirdFluidResult().getFluid(), ((AdvancedDistillationRecipe) recipe).getThirdFluidResult().getAmount() + ((DistillationOutputBlockEntity) above2).tankInventory.getFluidAmount()));
            if(above3 instanceof DistillationOutputBlockEntity)
            if(foundOutputs>=4)
                if (((AdvancedDistillationRecipe) recipe).getResults().toArray().length>3)
        ((DistillationOutputBlockEntity) above3).tankInventory.setFluid(new FluidStack(((AdvancedDistillationRecipe) recipe).getFourthFluidResult().getFluid(), ((AdvancedDistillationRecipe) recipe).getFourthFluidResult().getAmount() + ((DistillationOutputBlockEntity) above3).tankInventory.getFluidAmount()));
            if(above4 instanceof DistillationOutputBlockEntity)
            if(foundOutputs>=5)
                if (((AdvancedDistillationRecipe) recipe).getResults().toArray().length>4)
        ((DistillationOutputBlockEntity) above4).tankInventory.setFluid(new FluidStack(((AdvancedDistillationRecipe) recipe).getFifthFluidResult().getFluid(), ((AdvancedDistillationRecipe) recipe).getFifthFluidResult().getAmount() + ((DistillationOutputBlockEntity) above4).tankInventory.getFluidAmount()));
            if(above5 instanceof DistillationOutputBlockEntity)
            if(foundOutputs>=6)
                if (((AdvancedDistillationRecipe) recipe).getResults().toArray().length>5)
        ((DistillationOutputBlockEntity) above5).tankInventory.setFluid(new FluidStack(((AdvancedDistillationRecipe) recipe).getSixthFluidResult().getFluid(), ((AdvancedDistillationRecipe) recipe).getSixthFluidResult().getAmount() + ((DistillationOutputBlockEntity) above5).tankInventory.getFluidAmount()));

}
/*
if(!(((AdvancedDistillationRecipe) currentRecipe).getThirdItemResult().isEmpty()))
    controller.outputInventory.setItem(2,((AdvancedDistillationRecipe) currentRecipe).getFirstItemResult());


 */

    controller.notifyChangeOfContents();
    }



}

   // @Override
   // protected List<Recipe<?>> getMatchingRecipes() {
//
//
   //     List<Recipe<?>> list = RecipeFinder.get(getRecipeCacheKey(), level, this::matchStaticFilters);
   //     return list.stream()
   //             .filter(this::matchItemlessRecipe)
   //             .sorted((r1, r2) -> r2.getIngredients()
   //                     .size()
   //                     - r1.getIngredients()
   //                     .size())
   //             .collect(Collectors.toList());
   // }

    protected AdvancedDistillationRecipe getMatchingRecipes(DistillationControllerBlockEntity be) {


        List<Recipe<?>> list = RecipeFinder.get(getRecipeCacheKey(), level, this::matchStaticFilters);


        for(int i = 0; i < list.toArray().length;i++){
            AdvancedDistillationRecipe recipe = (AdvancedDistillationRecipe) list.get(i);

            if(recipe.getOutputCount(recipe)!=foundOutputs)
                continue;

            for(int y = 0; y < recipe.getFluidIngredients().get(0).getMatchingFluidStacks().toArray().length;y++)
                if(be.inputTank.getPrimaryHandler().getFluid().getFluid()==recipe.getFluidIngredients().get(0).getMatchingFluidStacks().get(y).getFluid())
                    if(be.inputTank.getPrimaryHandler().getFluidAmount()>=recipe.getFluidIngredients().get(0).getRequiredAmount())
                        return recipe;
        }

        return null;
    }

    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return r instanceof AdvancedDistillationRecipe;
    }

    @Override
    public void startProcessing() {
        if (running )
            return;
        super.startProcessing();
        running = true;

    }

    @Override
    public boolean continueWithPreviousRecipe() {
        return true;
    }

    @Override
    protected void onControllerRemoved() {
        if (!running)
            return;

        running = false;
    }

    @Override
    protected Object getRecipeCacheKey() {
        return AdvancedDistillationRecipesKey;
    }

    @Override
    protected boolean isRunning() {
        return running;
    }
/*
    @Override
    protected Optional<CreateAdvancement> getProcessedRecipeTrigger() {
        return Optional.of(AllAdvancements.MIXER);
    }
 */


    public Optional<DistillationControllerBlockEntity> getDistillationController() {
        if (level == null)
            return Optional.empty();
        BlockEntity basinTE = level.getBlockEntity(worldPosition.below(1));
        if (!(basinTE instanceof DistillationControllerBlockEntity))
            return Optional.empty();
        return Optional.of((DistillationControllerBlockEntity) basinTE);
    }

    public boolean hasIndustrialPipes(int outputAmount){
        BlockPos checkedPos = this.getBlockPos().above();
        Block checkedBlock;


        for(int i = 0;i<(outputAmount-1);i++){
            checkedBlock = level.getBlockState(checkedPos).getBlock();
            if(checkedBlock == TFMGBlocks.INDUSTRIAL_PIPE.get()){
                checkedPos=checkedPos.above(2);
                continue;
            }
            return false;
        }
        return true;
    }

}

