package com.drmangotea.tfmg.content.machines.oil_processing.distillation;


import com.drmangotea.tfmg.recipes.distillation.DistillationRecipe;
import com.drmangotea.tfmg.recipes.distillation.ItemlessRecipe;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.simple.DeferralBehaviour;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class FluidProcessingBlockEntity extends KineticBlockEntity {

    public DeferralBehaviour basinChecker;
    public boolean basinRemoved;
    protected Recipe<?> currentRecipe;

    public FluidProcessingBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        basinChecker = new DeferralBehaviour(this, this::updateController);
        behaviours.add(basinChecker);
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        if (getSpeed() == 0)
            basinRemoved = true;
        basinRemoved = false;
        basinChecker.scheduleUpdate();
    }

    @Override
    public void tick() {
        if (basinRemoved) {
            basinRemoved = false;
            onBasinRemoved();
            sendData();
            return;
        }

        super.tick();
    }

    protected boolean updateController() {


        if (isRunning())
            return true;
        if (level == null || level.isClientSide)
            return true;

        List<Recipe<?>> recipes = getMatchingRecipes();
        if (recipes.isEmpty())
            return true;
        currentRecipe = recipes.get(0);
     // if(currentRecipe instanceof DistillationRecipe) {
     //     if (((DistillationRecipe) currentRecipe).getInputFluid().getMatchingFluidStacks().get(0).getFluid() != TFMGFluids.HEAVY_OIL.get()) {
     //         return true;
     //     }
     // }
        startProcessing();
        sendData();
        return true;
    }

    protected abstract boolean isRunning();

    public void startProcessing() {}

    public boolean continueWithPreviousRecipe() {
        return true;
    }

    protected <C extends Container> boolean matchItemlessRecipe(Recipe<C> recipe) {
        if (recipe == null)
            return false;
        Optional<DistillationControllerBlockEntity> basin = getController();
        if (!basin.isPresent())
            return false;
        return ItemlessRecipe.match(basin.get(), recipe);
    }

    protected void applyItemlessRecipe() {
        if (currentRecipe == null)
            return;

        Optional<DistillationControllerBlockEntity> optionalBasin = getController();
        if (!optionalBasin.isPresent())
            return;
        DistillationControllerBlockEntity basin = optionalBasin.get();
        if (!ItemlessRecipe.apply(basin, currentRecipe))
            return;
        getProcessedRecipeTrigger().ifPresent(this::award);
        basin.inputTank.sendDataImmediately();



        basin.notifyChangeOfContents();
    }

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

    protected abstract void onBasinRemoved();

    protected Optional<DistillationControllerBlockEntity> getController() {
        if (level == null)
            return Optional.empty();
        BlockEntity basinTE = level.getBlockEntity(worldPosition.below(1));
        if (!(basinTE instanceof DistillationControllerBlockEntity))
            return Optional.empty();
        return Optional.of((DistillationControllerBlockEntity) basinTE);
    }

    protected Optional<CreateAdvancement> getProcessedRecipeTrigger() {
        return Optional.empty();
    }

    protected abstract <C extends Container> boolean matchStaticFilters(Recipe<C> recipe);

    protected abstract Object getRecipeCacheKey();
}
