package com.drmangotea.createindustry.blocks.electricity.polarizer;

import com.drmangotea.createindustry.base.util.TFMGUtils;
import com.drmangotea.createindustry.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.createindustry.recipes.polarizing.PolarizingRecipe;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class PolarizerBlockEntity extends ElectricBlockEntity implements IHaveGoggleInformation {

    public LazyOptional<IItemHandlerModifiable> itemCapability;

    public SmartInventory inventory;
    Optional<PolarizingRecipe> recipe;

    public int timer = 0;
    public PolarizerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        this.inventory = new SmartInventory(1, this).forbidInsertion().withMaxStackSize(1);

        this.itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(this.inventory));
    }
    

    @Override
    public void tick() {
        super.tick();

        if(voltage<200) {
            timer = -1;
            return;
        }
        if (inventory.getItem(0).isEmpty()) {
            timer = -1;
            return;
        }
        
        recipe = this.getRecipe(inventory.getItem(0));
        
        if (recipe.isEmpty()) {
            timer = -1;
            return;
        }
        
        
        if(!recipe.get().getIngredients().get(0).test(inventory.getItem(0))) {
            timer = -1;
            return;
        }
        
        if(energy.getEnergyStored() != 0) {
            useEnergy(recipe.get().getEnergy());
            if (!inventory.isEmpty()) {
                if (timer < recipe.get().getProcessingDuration()) {
                    timer++;
                } else {
                    timer = -1;
                    inventory.setStackInSlot(0, recipe.get().getResultItem());
                    TFMGUtils.spawnElectricParticles(level,getBlockPos());
                    sendStuff();
                }
            }
        }
        else timer = -1;


    }
    
    public Optional<PolarizingRecipe> getRecipe(ItemStack item) {
        Optional<PolarizingRecipe> assemblyRecipe = SequencedAssemblyRecipe.getRecipe(this.level, item, TFMGRecipeTypes.POLARIZING.getType(), PolarizingRecipe.class);
        if (assemblyRecipe.isPresent()) {
            return assemblyRecipe;
        } else {
            inventory.setItem(0, item);
            return TFMGRecipeTypes.POLARIZING.find(inventory, this.level);
        }
    }




    @Override
    public void destroy() {
        super.destroy();
        Containers.dropItemStack(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), inventory.getStackInSlot(0));
    }
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.itemCapability.cast() : super.getCapability(cap, side);
    }

    public boolean playerInteract(Player player, InteractionHand hand){
        if(inventory.isEmpty()) {
            ItemStack stack = player.getItemInHand(hand).copy();
            stack.setCount(1);
            inventory.setStackInSlot(0, stack);
            sendStuff();
            player.getItemInHand(hand).shrink(1);
            return true;
        } else {
            if (player.getItemInHand(hand).isEmpty()) {
                player.setItemInHand(hand,inventory.getItem(0));
                inventory.setStackInSlot(0, ItemStack.EMPTY);
                sendStuff();
                return true;
            }
        }
     return false;
    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if(energy.getEnergyStored()==0){
            Lang.translate("goggles.electric_machine.no_power")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip, 1);
            return true;
        }
        if(voltage < 200) {
            Lang.translate("goggles.electric_machine.insufficient_voltage")
                    .style(ChatFormatting.RED)
                    .forGoggles(tooltip, 1);
            return true;
        }
        if(timer>0 && recipe.isPresent()) {
            Lang.translate("goggles.blast_furnace.status.running")
                    .style(ChatFormatting.GOLD)
                    .forGoggles(tooltip, 1);
            tooltip.add(percent(timer, recipe.get()).withStyle(ChatFormatting.GREEN));

        }
        else
            Lang.translate("goggles.blast_furnace.status.off")
                    .style(ChatFormatting.GOLD)
                    .forGoggles(tooltip, 1);

        return true;
    }
    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        tag.put("Inventory", this.inventory.serializeNBT());
        //tag.put("Item", heldItem.serializeNBT());
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {

        this.inventory.deserializeNBT(tag.getCompound("Inventory"));
        //heldItem = ItemStack.of(tag.getCompound("Item"));
        super.read(tag, clientPacket);


    }
    
    
    private MutableComponent percent(int timer, PolarizingRecipe recipe) {
        int percent = Math.round((float) (timer / recipe.getProcessingDuration()) * 100);
        return Lang.builder().text(" ")
                .text(percent + "%")
                .component();
    }


    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getCounterClockWise();
    }

}
