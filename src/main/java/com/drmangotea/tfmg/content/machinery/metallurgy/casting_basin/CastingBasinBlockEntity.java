package com.drmangotea.tfmg.content.machinery.metallurgy.casting_basin;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.recipes.CastingRecipe;
import com.drmangotea.tfmg.recipes.input.CastingRecipeInput;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CastingBasinBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, Clearable {

    int flowTimer = 0;
    public SmartInventory inventory = new SmartInventory(1, this, 1, false);

    public FluidTank tank = new SmartFluidTank(90, this::onFluidChanged);
    public IFluidHandler fluidCapability;
    public IItemHandlerModifiable itemCapability;

    private final RecipeManager.CachedCheck<CastingRecipeInput, CastingRecipe> quickCheck;
    protected int recipeDuration;
    int timer = 0;
    public CastingRecipe currentRecipe;

    protected boolean updateCapability;

    LerpedFloat fluidLevel = LerpedFloat.linear();

    public CastingBasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        fluidCapability = tank;
        itemCapability = inventory;
        this.quickCheck = RecipeManager.createCheck(TFMGRecipeTypes.CASTING.getType());
        updateCapability = false;
        refreshCapability();
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

    public @Nullable CastingRecipe findRecipe() {
        if (level == null)
            return null;
        if (!inventory.isEmpty())
            return null;
        RecipeHolder<CastingRecipe> recipeholder;
        if (!tank.isEmpty()) {
            recipeholder = quickCheck.getRecipeFor(new CastingRecipeInput(tank.getFluid()), level).orElse(null);
        } else {
            return null;
        }
        if(recipeholder == null) {
            return null;
        }
        recipeDuration = recipeholder.value().getProcessingDuration();
        return recipeholder.value();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (currentRecipe == null) {
            currentRecipe = findRecipe();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null) return;
        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
        manageRecipe();

        if(level.isClientSide){
            if(flowTimer>0)
                flowTimer--;
            fluidLevel.chase(tank.getFluidAmount(), 0.3f, LerpedFloat.Chaser.EXP);
            fluidLevel.tickChaser();
        }
    }

    public void manageRecipe() {
        if (level == null || currentRecipe == null || level.isClientSide && !isVirtual())
            return;
        if (timer >= currentRecipe.getProcessingDuration()) {
            CastingRecipe activeRecipe = currentRecipe;
            tank.drain(currentRecipe.getFluidIngredients().getFirst().amount(), IFluidHandler.FluidAction.EXECUTE);
            inventory.setStackInSlot(0, activeRecipe.getRollableResults().getFirst().rollOutput(level.random));
            currentRecipe = null;
            timer = 0;
            recipeDuration = -1;
        } else {
            timer++;
            sendData();
        }
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
        TFMGTexts.progress(timer, recipeDuration).forGoggles(tooltip, 1);
        TFMGUtils.createFluidTooltip(tooltip, fluidCapability);
        TFMGUtils.createItemTooltip(tooltip, itemCapability);
        return true;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.put("Inventory", inventory.serializeNBT(registries));
        compound.put("Tank", tank.writeToNBT(registries,new CompoundTag()));
        compound.putInt("Timer", timer);
        compound.putInt("RecipeDuration", recipeDuration);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        inventory.deserializeNBT(registries,compound.getCompound("Inventory"));
        tank.readFromNBT(registries,compound.getCompound("Tank"));
        updateCapability = true;
        timer = compound.getInt("Timer");
        recipeDuration = compound.getInt("RecipeDuration");
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    public void refreshCapability() {
        fluidCapability = tank;
        invalidateCapabilities();
    }

    @Override
    public void clearContent() {
        this.inventory.clearContent();
    }
}
