package com.drmangotea.tfmg.content.machinery.metallurgy.casting_basin;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.recipes.CastingRecipe;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeConditions;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.util.List;

public class CastingBasinBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    int flowTimer = 0;
    public SmartInventory inventory = new SmartInventory(1, this, 1, false);

    public FluidTank tank = new SmartFluidTank(144, this::onFluidChanged);
    public IFluidHandler fluidCapability;
    public IItemHandlerModifiable itemCapability;
    public CastingRecipe recipe = null;
    public int timer = 0;
    private static final Object castingRecipeKey = new Object();

    LerpedFloat fluidLevel = LerpedFloat.linear();

    public CastingBasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        fluidCapability = tank;
        itemCapability = inventory;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.CASTING_BASIN.get(),
                (be, context) -> be.fluidCapability
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TFMGBlockEntities.CASTING_BASIN.get(),
                (be, context) -> be.itemCapability
        );
    }

    @Override
    public void tick() {
        super.tick();
        if (tank.getSpace() == 0) {
            if (recipe == null)
                findRecipe();
            if (recipe != null) {
                if(recipe.getIngrenient().test(tank.getFluid())) {
                    if (timer >= recipe.getProcessingDuration()) {
                        tank.setFluid(FluidStack.EMPTY);
                        inventory.setStackInSlot(0, recipe.getRollableResults().get(0).rollOutput(level.random));
                        recipe = null;
                        timer = 0;
                    } else timer++;
                } else findRecipe();
            } else timer = 0;
        }

        if(level.isClientSide){

            if(flowTimer>0)
                flowTimer--;

            fluidLevel.chase(tank.getFluidAmount(), 0.3f, LerpedFloat.Chaser.EXP);
            fluidLevel.tickChaser();
        }
    }

    public void findRecipe() {
        recipe = null;
        List<RecipeHolder<? extends Recipe<?>>> list = RecipeFinder.get(getRecipeCacheKey(), level, RecipeConditions.isOfType(TFMGRecipeTypes.CASTING.getType()));
        for (RecipeHolder<? extends Recipe<?>> recipe1 : list) {
            CastingRecipe testedRecipe = (CastingRecipe) recipe1.value();
            if (testedRecipe.getIngrenient().test(tank.getFluid()) && inventory.isEmpty()) {
                recipe = testedRecipe;
                return;
            }
        }
    }

    protected Object getRecipeCacheKey() {
        return castingRecipeKey;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    private void onFluidChanged(FluidStack stack) {
        flowTimer = 10;
        sendData();
        setChanged();
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inventory);
    }



    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        TFMGUtils.createFluidTooltip(this, tooltip);
        TFMGUtils.createItemTooltip(this, tooltip);
        return true;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.put("Inventory", inventory.serializeNBT(registries));
        compound.put("Tank", tank.writeToNBT(registries,new CompoundTag()));
        compound.putInt("Timer",timer);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        inventory.deserializeNBT(registries,compound.getCompound("Inventory"));
        tank.readFromNBT(registries,compound.getCompound("Tank"));
        timer = compound.getInt("Timer");
    }
}
