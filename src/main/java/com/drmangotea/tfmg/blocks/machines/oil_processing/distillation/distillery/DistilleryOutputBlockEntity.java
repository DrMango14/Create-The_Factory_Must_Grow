package com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillery;


import com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.FluidProcessingBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillation_tower.DistillationOutputBlockEntity;
import com.drmangotea.tfmg.recipes.distillation.DistillationRecipe;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DistilleryOutputBlockEntity extends FluidProcessingBlockEntity implements IHaveGoggleInformation {

    protected LazyOptional<IFluidHandler> fluidCapability;
    public FluidTank tankInventory;
    protected BlockPos lastKnownPos;

    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;


    private static final Object DistillationRecipesKey = new Object();

    public boolean running;

    public DistilleryOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();
        fluidCapability = LazyOptional.of(() -> tankInventory);
        refreshCapability();
    }

    protected <C extends Container> boolean matchItemlessRecipe(Recipe<C> recipe) {
        if (recipe == null)
            return false;
        Optional<DistilleryControllerBlockEntity> controller = getController();
        if (!controller.isPresent())
            return false;
        return DistillationRecipe.match(controller.get(), recipe);
    }

    @Override
    protected boolean updateController() {


        if (level == null || level.isClientSide)
            return true;

        List<Recipe<?>> recipes = getMatchingRecipes();
        if (recipes.isEmpty())
            return true;
        currentRecipe = recipes.get(0);
        startProcessing();
        sendData();
        return true;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).expandTowards(0, -1.5, 0);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        running = compound.getBoolean("Running");

        super.read(compound, clientPacket);
        lastKnownPos = null;

        if (compound.contains("LastKnownPos"))
            lastKnownPos = NbtUtils.readBlockPos(compound.getCompound("LastKnownPos"));


        tankInventory.setCapacity(8000);
        tankInventory.readFromNBT(compound.getCompound("TankContent"));
        if (tankInventory.getSpace() < 0)
            tankInventory.drain(-tankInventory.getSpace(), IFluidHandler.FluidAction.EXECUTE);

    }
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("Running", running);
        if (lastKnownPos != null)
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));

        compound.put("TankContent", tankInventory.writeToNBT(new CompoundTag()));

        super.write(compound, clientPacket);
        super.write(compound, clientPacket);
    }

    @Override
    public void tick() {
        super.tick();
        if(this instanceof DistillationOutputBlockEntity)
            return;


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

    protected void process() {
        updateController();

        if (currentRecipe == null)
            return;

     //   if((currentRecipe instanceof ShapelessRecipe))
     //       return;

        BlockEntity above1 = level.getBlockEntity(this.getBlockPos().above(1));
        BlockEntity above2 = level.getBlockEntity(this.getBlockPos().above(2));
        BlockEntity burner = level.getBlockEntity(this.getBlockPos().below(2));
        if(!(burner instanceof BlazeBurnerBlockEntity))
            return;
        if(((BlazeBurnerBlockEntity) burner).getHeatLevelFromBlock()== BlazeBurnerBlock.HeatLevel.NONE)
            return;
        if(((BlazeBurnerBlockEntity) burner).getHeatLevelFromBlock()== BlazeBurnerBlock.HeatLevel.SMOULDERING)
            return;


if(above1 !=null&& above2 !=null
 //       &&
      /// tankInventory.getFluidAmount()+
      ///         ((DistillationRecipe)currentRecipe)
      ///                 .getFirstFluidResult()
      ///                 .getAmount()
      ///         <8000&&
     //      ((DistillationOutputBlockEntity) above1).tankInventory.getFluidAmount()+((DistillationRecipe)currentRecipe).getFluidResults().get(1).getAmount()<((DistillationOutputBlockEntity) above2).tankInventory.getCapacity()&&
   //     ((DistillationOutputBlockEntity) above2).tankInventory.getFluidAmount()+((DistillationRecipe)currentRecipe).getFluidResults().get(2).getAmount()<((DistillationOutputBlockEntity) above2).tankInventory.getCapacity()
){
    Optional<DistilleryControllerBlockEntity> optionalController = getController();
    if (!optionalController.isPresent())
        return;

    if(!(above1 instanceof DistilleryOutputBlockEntity)&&
           !( above2 instanceof DistilleryOutputBlockEntity)) {
        return;
    }

    if(!(above1 instanceof DistilleryOutputBlockEntity))
        return;
    if(!(above2 instanceof DistilleryOutputBlockEntity))
        return;


    FluidStack fluidInRecipe1 = ((DistillationRecipe) currentRecipe).getFirstFluidResult();
    FluidStack fluidInRecipe2 = ((DistillationRecipe) currentRecipe).getSecondFluidResult();
    FluidStack fluidInRecipe3 = ((DistillationRecipe) currentRecipe).getThirdFluidResult();




    if (fluidInRecipe1.getFluid() != this.tankInventory.getFluid().getFluid()
         && tankInventory.getFluidAmount()!=0
    )
        return;

  if (fluidInRecipe2.getFluid() != (((DistilleryOutputBlockEntity) above1).tankInventory.getFluid().getFluid())
          &&((DistilleryOutputBlockEntity) above1).tankInventory.getFluidAmount()!=0
  )
      return;
  if (fluidInRecipe3.getFluid() != (((DistilleryOutputBlockEntity) above2).tankInventory.getFluid().getFluid())
          &&((DistilleryOutputBlockEntity) above2).tankInventory.getFluidAmount()!=0
  )
      return;

    if(
            tankInventory.getFluidAmount()+((DistillationRecipe) currentRecipe).getFirstFluidResult().getAmount()>8000||
                    ((DistilleryOutputBlockEntity)above1).tankInventory.getFluidAmount()+((DistillationRecipe) currentRecipe).getFirstFluidResult().getAmount()>8000||
                    ((DistilleryOutputBlockEntity)above2).tankInventory.getFluidAmount()+((DistillationRecipe) currentRecipe).getFirstFluidResult().getAmount()>8000
    )
        return;


    if(getController().get().getTanks().get(true).getPrimaryHandler().getFluid().getFluid() != ((DistillationRecipe) currentRecipe).getInputFluid().getMatchingFluidStacks().get(0).getFluid())
            return;

    DistilleryControllerBlockEntity controller = optionalController.get();


    if(controller.outputInventory.getStackInSlot(0).getCount()>=1||
    controller.outputInventory.getStackInSlot(1).getCount()>=1||
    controller.outputInventory.getStackInSlot(2).getCount()>=1)
        return;

    if (!controller.getTanks().get(true).isEmpty()) {
        if(!level.isClientSide) {
           // if(((DistillationRecipe)currentRecipe).getInputFluid().getMatchingFluidStacks().get(0).getFluid() != TFMGFluids.HEAVY_OIL.get())
           //   return;

        controller.getTanks().get(true).getPrimaryHandler().drain(((DistillationRecipe) currentRecipe).getFluidIngredients().get(0).getRequiredAmount(), IFluidHandler.FluidAction.EXECUTE);

    if (!(((DistillationRecipe) currentRecipe).getFluidResults().get(0).isEmpty()))
        tankInventory.setFluid(new FluidStack(((DistillationRecipe) currentRecipe).getFluidResults().get(0).getFluid(), ((DistillationRecipe) currentRecipe).getFluidResults().get(0).getAmount() + this.tankInventory.getFluidAmount()));
    if (!(((DistillationRecipe) currentRecipe).getFluidResults().get(1).isEmpty()))
        ((DistilleryOutputBlockEntity) above1).tankInventory.setFluid(new FluidStack(((DistillationRecipe) currentRecipe).getFluidResults().get(1).getFluid(), ((DistillationRecipe) currentRecipe).getFluidResults().get(1).getAmount() + ((DistilleryOutputBlockEntity) above1).tankInventory.getFluidAmount()));
    if (!(((DistillationRecipe) currentRecipe).getFluidResults().get(2).isEmpty()))
        ((DistilleryOutputBlockEntity) above2).tankInventory.setFluid(new FluidStack(((DistillationRecipe) currentRecipe).getFluidResults().get(2).getFluid(), ((DistillationRecipe) currentRecipe).getFluidResults().get(2).getAmount() + ((DistilleryOutputBlockEntity) above2).tankInventory.getFluidAmount()));


    if (!(((DistillationRecipe) currentRecipe).getFirstItemResult().isEmpty()))
        controller.outputInventory.setItem(0, ((DistillationRecipe) currentRecipe).getFirstItemResult());

    if (!(((DistillationRecipe) currentRecipe).getSecondItemResult().isEmpty()))
        controller.outputInventory.setItem(1, ((DistillationRecipe) currentRecipe).getSecondItemResult());
}
/*
if(!(((DistillationRecipe) currentRecipe).getThirdItemResult().isEmpty()))
    controller.outputInventory.setItem(2,((DistillationRecipe) currentRecipe).getFirstItemResult());


 */

    controller.notifyChangeOfContents();
    }

}

}

    @Override
    protected List<Recipe<?>> getMatchingRecipes() {


        List<Recipe<?>> list = RecipeFinder.get(getRecipeCacheKey(), level, this::matchStaticFilters);
        return list.stream()
                .filter(this::matchItemlessRecipe)
                .sorted((r1, r2) -> r2.getIngredients()
                        .size()
                        - r1.getIngredients()
                        .size())
                .collect(Collectors.toList());
    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return ((r instanceof CraftingRecipe && !(r instanceof IShapedRecipe<?>)
                && r.getIngredients()
                .size() > 1
                && !MechanicalPressBlockEntity.canCompress(r)) && !AllRecipeTypes.shouldIgnoreInAutomation(r)
                || r.getType() == TFMGRecipeTypes.DISTILLATION.getType());
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
    protected void onBasinRemoved() {
        if (!running)
            return;

        running = false;
    }

    @Override
    protected Object getRecipeCacheKey() {
        return DistillationRecipesKey;
    }

    @Override
    protected boolean isRunning() {
        return running;
    }



    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(8000, this::onFluidStackChanged);
    }


    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;


        }


    @Override
    public void sendData() {
        if (syncCooldown > 0) {
            queuedSync = true;
            return;
        }
        super.sendData();
        queuedSync = false;
        syncCooldown = SYNC_RATE;
    }






    private void refreshCapability() {
        LazyOptional<IFluidHandler> oldCap = fluidCapability;
        fluidCapability = LazyOptional.of(() -> handlerForCapability());
        oldCap.invalidate();
    }

    private IFluidHandler handlerForCapability() {

        return tankInventory;
    }





    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        return containedFluidTooltip(tooltip, isPlayerSneaking,
                this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!fluidCapability.isPresent())
            refreshCapability();
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }


    public IFluidTank getTankInventory() {
        return tankInventory;
    }


    public FluidStack getFluid(int tank) {
        return tankInventory.getFluid()
                .copy();
}

}

