package com.drmangotea.tfmg.blocks.electricity.polarizer;


import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
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

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class PolarizerBlockEntity extends ElectricBlockEntity implements IHaveGoggleInformation {

    public LazyOptional<IItemHandlerModifiable> itemCapability;

    public SmartInventory inventory;

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
            timer = 0;
            return;
        }

        if(!inventory.getItem(0).is(TFMGItems.STEEL_INGOT.get())) {
            timer = 0;
            return;
        }


        if(energy.getEnergyStored()!=0) {
            useEnergy(250);
            if (!inventory.isEmpty()) {
                if (timer < 100) {
                    timer++;
                } else {

                    timer = 0;
                    inventory.setStackInSlot(0, TFMGItems.MAGNETIC_INGOT.asStack());
                    TFMGUtils.spawnElectricParticles(level,getBlockPos());
                    sendStuff();
                }
            }
        }else timer =0;


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

        ;

        if(inventory.isEmpty()) {


            if (player.getItemInHand(hand).is(TFMGItems.STEEL_INGOT.get())) {


                inventory.setStackInSlot(0, TFMGItems.STEEL_INGOT.asStack());
                sendStuff();

                player.getItemInHand(hand).shrink(1);

                return true;
            }


        } else {

            if (player.getItemInHand(hand).isEmpty()) {

                player.setItemInHand(hand,inventory.getItem(0));
                inventory.setStackInSlot(0, Blocks.AIR.asItem().getDefaultInstance());
                sendStuff();



                return true;
            }
        }



     return false;
    }
    @Override
    @SuppressWarnings("removal")
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {


        if(energy.getEnergyStored()==0){
            Lang.translate("goggles.electric_machine.no_power")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip, 1);
        return true;
    }
        if(energy.getEnergyStored()==0) {
            Lang.translate("goggles.electric_machine.insufficient_voltage")
                    .style(ChatFormatting.RED)
                    .forGoggles(tooltip, 1);
            return true;
        }
        if(timer>0) {
            Lang.translate("goggles.blast_furnace.status.running")
                    .style(ChatFormatting.GOLD)
                    .forGoggles(tooltip, 1);
            Lang.translate("goggles.coke_oven.progress", timer)
                    .style(ChatFormatting.GREEN)
                    .add(Lang.translate("goggles.misc.percent_symbol")
                            .style(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);

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



    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getCounterClockWise();
    }

}
