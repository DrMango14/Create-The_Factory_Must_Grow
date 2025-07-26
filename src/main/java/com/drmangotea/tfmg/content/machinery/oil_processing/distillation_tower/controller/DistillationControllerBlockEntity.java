package com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.controller;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlockEntity;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.output.DistillationOutputBlockEntity;
import com.drmangotea.tfmg.mixin.accessor.FluidTankBlockEntityAccessor;
import com.drmangotea.tfmg.recipes.DistillationRecipe;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.recipe.RecipeConditions;
import com.simibubi.create.foundation.recipe.RecipeFinder;

import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;


import java.util.ArrayList;
import java.util.List;

import static com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.controller.DistillationControllerBlock.getFacing;

public class DistillationControllerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    private static final Object DistillationRecipesKey = new Object();

    public DistillationRecipe recipe;

    LerpedFloat angle = LerpedFloat.angular();

    protected IFluidHandler fluidCapability;

    public final FluidTank tank = new SmartFluidTank(8000, this::onFluidStackChanged);

    public DistillationControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        fluidCapability = tank;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.DISTILLATION_CONTROLLER.get(),
                (be, context) -> be.fluidCapability
        );
    }

    @Override
    public void remove() {
        super.remove();
        SteelTankBlock.updateTowerState(level, getBlockPos().relative(getFacing(getBlockState()).getOpposite()),false,false);

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    public void manageDialRendering(){
        if (level.isClientSide) {
            angle.chase(180 * ((float) tank.getFluidAmount() / tank.getCapacity()), 0.2f, LerpedFloat.Chaser.EXP);
            angle.tickChaser();
        }
    }

    public void findRecipe(ArrayList<DistillationOutputBlockEntity> outputs){
        if (recipe == null || !recipe.matches(tank, outputs.toArray().length)) {
            DistillationRecipe recipe = getMatchingRecipes();

            if (recipe != null) {
                this.recipe = recipe;
                sendData();
            }
        }
    }

    public void manageRecipe(){
        ArrayList<DistillationOutputBlockEntity> outputs = getOutputs();
        BlockEntity beBehind = level.getBlockEntity(getBlockPos().relative(getFacing(getBlockState()).getOpposite()));
        if (!(beBehind instanceof SteelTankBlockEntity be))
            return;


        if (outputs.isEmpty() || be.activeHeat == 0)
            return;

        findRecipe(outputs);

        if (recipe == null)
            return;

        ///
        float speedModifier = (float) be.activeHeat / 2;
        if (recipe.getInputFluid().getRequiredAmount() * speedModifier > tank.getFluidAmount())
            return;

        if (recipe.getFluidResults().toArray().length != getOutputs().toArray().length)
            return;
        if (be.isController()) {
            if (be.getHeight() < outputs.toArray().length * 2 || (((FluidTankBlockEntityAccessor)be).tfmg$getWidth() < 2 && outputs.toArray().length > 3))
                return;
        }  else {
            if (be.getControllerBE() != null)
                if (be.getControllerBE().getHeight() < outputs.toArray().length * 2 || ((FluidTankBlockEntityAccessor)be.getControllerBE()).tfmg$getWidth() < 2)
                    return;
        }

        for (DistillationOutputBlockEntity be1 : outputs) {
            if (be1.tank.getSpace() == 0&&be1.mode.get() == DistillationOutputBlockEntity.DistillationOutputMode.KEEP_FLUID)
                return;
        }
        int numero = 0;
        for (DistillationOutputBlockEntity output : outputs) {
            FluidStack fluidStack = recipe.getFluidResults().get(numero);
            if (fluidStack.isEmpty())
                break;
            if (output.tank.fill(new FluidStack(fluidStack.getFluidHolder(), (int) (fluidStack.getAmount() * speedModifier)), IFluidHandler.FluidAction.SIMULATE) > output.tank.getCapacity()&&output.mode.get() == DistillationOutputBlockEntity.DistillationOutputMode.KEEP_FLUID)
                break;

            output.tank.fill(new FluidStack(fluidStack.getFluidHolder(), (int) (fluidStack.getAmount() * speedModifier)), IFluidHandler.FluidAction.EXECUTE);
            int consumption = (recipe.getInputFluid().getRequiredAmount() / 6);

            tank.drain((int) (consumption * speedModifier), IFluidHandler.FluidAction.EXECUTE);
            numero++;
        }
    }
    @Override
    public void tick() {
        super.tick();

        manageDialRendering();
        manageRecipe();

    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;

        if (!level.isClientSide) {
            setChanged();
            sendData();
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        BlockEntity beBehind = level.getBlockEntity(getBlockPos().relative(getFacing(getBlockState()).getOpposite()));
        if (beBehind instanceof SteelTankBlockEntity be) {

            CreateLang.translate("goggles.distillation_tower.status")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);

            if (be.getControllerBE().activeHeat > 0) {
                CreateLang.translate("goggles.distillation_tower.level", be.getControllerBE().activeHeat)
                        .style(ChatFormatting.GOLD)
                        .forGoggles(tooltip, 1);
            } else
                CreateLang.translate("goggles.distillation_tower.level", be.getControllerBE().activeHeat)
                        .style(ChatFormatting.RED)
                        .forGoggles(tooltip, 1);
            if (getOutputs().toArray().length > 0) {
                CreateLang.translate("goggles.distillation_tower.found_outputs", getOutputs().toArray().length)
                        .style(ChatFormatting.GOLD)
                        .forGoggles(tooltip, 1);
            } else
                CreateLang.translate("goggles.distillation_tower.found_outputs", getOutputs().toArray().length)
                        .style(ChatFormatting.RED)
                        .forGoggles(tooltip, 1);


        } else
            CreateLang.translate("goggles.distillation_tower.tank_not_found")
                    .style(ChatFormatting.RED)
                    .forGoggles(tooltip, 1);
        TFMGUtils.createFluidTooltip(this,tooltip);

        return true;
    }

    protected DistillationRecipe getMatchingRecipes() {
        List<RecipeHolder<? extends Recipe<?>>> list = RecipeFinder.get(getRecipeCacheKey(), level, RecipeConditions.isOfType(TFMGRecipeTypes.DISTILLATION.getType()));
        for (int i = 0; i < list.toArray().length; i++) {
            DistillationRecipe recipe = (DistillationRecipe) list.get(i).value();
            if (recipe.getFluidResults().toArray().length == getOutputs().toArray().length)
                for (int y = 0; y < recipe.getFluidIngredients().get(0).getMatchingFluidStacks().toArray().length; y++)
                    if (tank.getFluid().getFluid() == recipe.getFluidIngredients().get(0).getMatchingFluidStacks().get(y).getFluid())
                        if (tank.getFluidAmount() >= recipe.getFluidIngredients().get(0).getRequiredAmount())
                            return recipe;
        }
        return null;
    }

    protected Object getRecipeCacheKey() {
        return DistillationRecipesKey;
    }

    //@Nonnull
    //@Override
    //public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    //    if (cap == ForgeCapabilities.FLUID_HANDLER)
    //        return fluidCapability.cast();
    //    return super.getCapability(cap, side);
    //}

    public ArrayList<DistillationOutputBlockEntity> getOutputs() {
        ArrayList<DistillationOutputBlockEntity> outputs = new ArrayList<>();
        BlockPos checkedPos = this.getBlockPos().above();
        for (int i = 0; i < 11; i++) {
            if (i == 0 || i == 2 || i == 4 || i == 6 || i == 8 || i == 10) {
                if (level.getBlockEntity(checkedPos) instanceof DistillationOutputBlockEntity be) {
                    outputs.add(be);
                } else break;
            } else {
                if (!(level.getBlockState(checkedPos).is(TFMGTags.TFMGBlockTags.INDUSTRIAL_PIPE.tag)))
                    break;
            }
            checkedPos = checkedPos.above();
        }
        return outputs;
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        tank.readFromNBT(registries,compound.getCompound("TankContent"));
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.put("TankContent", tank.writeToNBT(registries,new CompoundTag()));
    }
}
