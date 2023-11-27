package com.drmangotea.tfmg.blocks.machines.oil_processing.distillation;


import com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillery.DistilleryControllerBlockEntity;
import com.drmangotea.tfmg.recipes.distillation.AbstractDistillationRecipe;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class FluidProcessingBlockEntity extends KineticBlockEntity {

    public DeferralBehaviour basinChecker;
    public boolean basinRemoved;
    protected AbstractDistillationRecipe recipe;

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
    public void tick() {
        if (basinRemoved) {
            basinRemoved = false;
            onControllerRemoved();
            sendData();
            return;
        }

        super.tick();
    }

    protected boolean updateController() {
        return false;
    }

    protected abstract boolean isRunning();

    public void startProcessing() {}

    public boolean continueWithPreviousRecipe() {
        return true;
    }

    protected <C extends Container> boolean matchItemlessRecipe(Recipe<C> recipe) {
        if (recipe == null)
            return false;
        Optional<DistilleryControllerBlockEntity> controller = getController();

        if (!controller.isPresent())
            return false;
        return AbstractDistillationRecipe.match(controller.get(), recipe);
    }




    protected abstract void onControllerRemoved();

    public Optional<DistilleryControllerBlockEntity> getController() {
        if (level == null)
            return Optional.empty();
        BlockEntity basinTE = level.getBlockEntity(worldPosition.below(1));
        if (!(basinTE instanceof DistilleryControllerBlockEntity))
            return Optional.empty();
        return Optional.of((DistilleryControllerBlockEntity) basinTE);
    }

    protected Optional<CreateAdvancement> getProcessedRecipeTrigger() {
        return Optional.empty();
    }

    protected abstract <C extends Container> boolean matchStaticFilters(Recipe<C> recipe);

    protected abstract Object getRecipeCacheKey();
}
