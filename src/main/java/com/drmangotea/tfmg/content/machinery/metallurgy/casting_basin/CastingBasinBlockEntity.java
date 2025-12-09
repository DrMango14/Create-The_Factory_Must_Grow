package com.drmangotea.tfmg.content.machinery.metallurgy.casting_basin;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.recipes.CastingRecipe;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class CastingBasinBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    int flowTimer = 0;
    public SmartInventory inventory = new SmartInventory(1, this, 1, false);

    public FluidTank tank = new SmartFluidTank(144, this::onFluidChanged);
    public LazyOptional<IFluidHandler> fluidCapability;
    public LazyOptional<IItemHandlerModifiable> itemCapability;
    public CastingRecipe recipe = null;
    public int timer = 0;
    private static final Object castingRecipeKey = new Object();

    LerpedFloat fluidLevel = LerpedFloat.linear();

    public CastingBasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        fluidCapability = LazyOptional.of(() -> tank);
        itemCapability = LazyOptional.of(() -> inventory);
    }



    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (side == getBlockState().getValue(FACING).getOpposite() && cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();
        return super.getCapability(cap, side);
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap);
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
                        inventory.setStackInSlot(0, recipe.getRollableResults().get(0).rollOutput());
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
        List<Recipe<?>> list = RecipeFinder.get(getRecipeCacheKey(), level, r -> r instanceof CastingRecipe);
        for (Recipe<?> recipe1 : list) {
            CastingRecipe testedRecipe = (CastingRecipe) recipe1;
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
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("Tank", tank.writeToNBT(new CompoundTag()));
        compound.putInt("Timer",timer);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        tank.readFromNBT(compound.getCompound("Tank"));
        timer = compound.getInt("Timer");
    }
}
