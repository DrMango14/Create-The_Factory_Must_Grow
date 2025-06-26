package com.drmangotea.tfmg.content.machinery.misc.winding_machine;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.engines.base.AbstractEngineBlockEntity;
import com.drmangotea.tfmg.recipes.PolarizingRecipe;
import com.drmangotea.tfmg.recipes.WindingRecipe;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;

import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineBlock.POWERED;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class WindingMachineBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

    LerpedFloat spoolSpeed = LerpedFloat.linear();
    float angle;
    public SmartInventory inventory;
    public LazyOptional<IItemHandlerModifiable> itemCapability;
    public ItemStack spool = ItemStack.EMPTY;
    public WindingRecipe recipe;
    public int amountWinded = 0;
    public boolean update = false;

    protected ScrollValueBehaviour turnPercentage;

    public WindingMachineBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        setLazyTickRate(10);
        inventory = new SmartInventory(1, this)
                .withMaxStackSize(1)
                .whenContentsChanged(i -> this.onContentsChanged());

        itemCapability = LazyOptional.of(() -> inventory);
    }





    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = 100;
        turnPercentage = new ScrollValueBehaviour(CreateLang.translateDirect("winding_machine.turn_percentage"),
                this, new WindingMachineValueBox());
        turnPercentage.between(1, max);
        turnPercentage.value = 20;
        behaviours.add(turnPercentage);

    }

    public void onContentsChanged() {

        findRecipe();
        if (inventory.isEmpty())
            amountWinded = 0;
    }

    public void findRecipe() {
        Optional<WindingRecipe> optional = TFMGRecipeTypes.WINDING.find(new RecipeWrapper(inventory), level);
        Optional<WindingRecipe> assemblyRecipe = SequencedAssemblyRecipe.getRecipe(this.level, inventory, TFMGRecipeTypes.WINDING.getType(), WindingRecipe.class);

        if (assemblyRecipe.isPresent()) {
            recipe = assemblyRecipe.get();
            return;
        }
        if (optional.isEmpty()) {
            recipe = null;
            return;
        }
        WindingRecipe windingRecipe = optional.get();

        if (windingRecipe.getIngredient().test(inventory.getItem(0))) {
            recipe = windingRecipe;
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        onContentsChanged();

        if (spool.is(TFMGItems.EMPTY_SPOOL.get()) && !getBlockState().getValue(POWERED)) {
            level.setBlock(getBlockPos(), getBlockState().setValue(POWERED, true), 2);
            update = true;
        }
        if (!spool.is(TFMGItems.EMPTY_SPOOL.get()) && getBlockState().getValue(POWERED)) {
            level.setBlock(getBlockPos(), getBlockState().setValue(POWERED, false), 2);
            update = true;
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CreateLang.translate("goggles.winding_machine.header")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip, 1);
        if (!spool.isEmpty()) {
            CreateLang.text(spool.getDisplayName().getString().replace("[","").replace("]",""))
                    .color(spool.getBarColor())
                    .forGoggles(tooltip);
            CreateLang.translate("goggles.winding_machine.turns")
                    .add(CreateLang.number(spool.getOrCreateTag().getInt("Amount")))
                    .color(spool.getBarColor())
                    .forGoggles(tooltip);

        if (recipe != null)
            CreateLang.text("")
                    .add(CreateLang.translate("goggles.winding_machine.progress"))
                    .add(CreateLang.number(amountWinded))
                    .add(Component.literal("/" + recipe.getProcessingDuration()))
                    .color(spool.getBarColor())
                    .forGoggles(tooltip);
        }
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        performRecipe();
        if (update) {
            level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
            update = false;
        }

        if (level.isClientSide)
            manageRotation();
    }

    public void performRecipe() {

        if (getSpeed() == 0)
            return;
        if (inventory.getItem(0).is(TFMGItems.ELECTROMAGNETIC_COIL.get()) && spool.is(TFMGItems.COPPER_SPOOL.get()) && spool.getOrCreateTag().getInt("Amount") > 0 && inventory.getItem(0).getOrCreateTag().getInt("Turns") < turnPercentage.getValue() * 10) {
            if(inventory.getItem(0).getOrCreateTag().getInt("Turns")< turnPercentage.getValue()*10){
                spool.getOrCreateTag().putInt("Amount", spool.getOrCreateTag().getInt("Amount") - 1);
                inventory.getItem(0).getOrCreateTag().putInt("Turns", inventory.getItem(0).getOrCreateTag().getInt("Turns") + 1);
                return;
            }
        }
        if (inventory.getItem(0).is(TFMGBlocks.RESISTOR.asItem()) && spool.is(TFMGItems.CONSTANTAN_SPOOL.get()) && spool.getOrCreateTag().getInt("Amount") > 0 && inventory.getItem(0).getOrCreateTag().getInt("Resistance") < turnPercentage.getValue() * 10) {
            if(inventory.getItem(0).getOrCreateTag().getInt("Resistance")< turnPercentage.getValue()*10) {
                spool.getOrCreateTag().putInt("Amount", spool.getOrCreateTag().getInt("Amount") - 1);
                inventory.getItem(0).getOrCreateTag().putInt("Resistance", inventory.getItem(0).getOrCreateTag().getInt("Resistance") + 1);
                return;
            }
        }


        if (spool.getOrCreateTag().getInt("Amount") == 0 && !spool.is(TFMGItems.EMPTY_SPOOL.get()) && spool.getItem() instanceof SpoolItem)
            spool = TFMGItems.EMPTY_SPOOL.asStack();

        if (recipe == null) {
            return;
        }



        if (amountWinded >= recipe.getProcessingDuration()) {
            inventory.setStackInSlot(0, recipe.rollResults().get(0));
            recipe = null;
            amountWinded = 0;

            sendData();
            setChanged();

        } else {
            if (spool.isEmpty() || spool.is(TFMGItems.EMPTY_SPOOL.get())) {
                return;
            }
            if (spool.getOrCreateTag().getInt("Amount") > 0) {
                if (recipe.getSpool().test(spool)) {
                    spool.getOrCreateTag().putInt("Amount", spool.getOrCreateTag().getInt("Amount") - 1);
                    amountWinded++;
                }
            } else {
                inventory.setStackInSlot(0, recipe.rollResults().get(0));
                sendData();
                setChanged();
            }

        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();

        return super.getCapability(cap, side);
    }

    public void manageRotation() {
        float targetSpeed = (float) Math.min(Math.abs(getSpeed() * 1.5), 30);
        spoolSpeed.updateChaseTarget(targetSpeed);
        spoolSpeed.tickChaser();

    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Inventory", inventory.serializeNBT());

        compound.put("Spool", spool.serializeNBT());
        compound.putInt("AmountWinded", amountWinded);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inventory.deserializeNBT(compound.getCompound("Inventory"));

        spool = ItemStack.of(compound.getCompound("Spool"));

        amountWinded = compound.getInt("AmountWinded");
        if (clientPacket)
            spoolSpeed.chase(getGeneratedSpeed(), 1 / 16f, LerpedFloat.Chaser.EXP);
    }

    public static class WindingMachineValueBox extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 4, 16.05);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction == state.getValue(HORIZONTAL_FACING);
        }
    }

    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inventory);
        Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), spool);
    }
}
