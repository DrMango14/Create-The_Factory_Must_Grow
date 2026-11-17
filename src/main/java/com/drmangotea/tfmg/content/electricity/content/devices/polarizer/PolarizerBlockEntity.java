package com.drmangotea.tfmg.content.electricity.content.devices.polarizer;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.content.devices.base.ResistiveLoadBlockEntity;
import com.drmangotea.tfmg.recipes.PolarizingRecipe;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.item.SmartInventory;

import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.Optional;


public class PolarizerBlockEntity extends ResistiveLoadBlockEntity {

    public SmartInventory inventory = new SmartInventory(1, this, 1, false)
            .whenContentsChanged(this::onInventoryChanged);
    public IItemHandlerModifiable itemCapability;

    LerpedFloat angle = LerpedFloat.angular();

    public boolean chargeCapacitors = false;
    public int capacitorPercentage = 0;

    public PolarizerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        itemCapability = inventory;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TFMGBlockEntities.POLARIZER.get(),
                (be, context) -> be.itemCapability
        );
    }


    public void onInventoryChanged(int count) {
        sendData();
        setChanged();

        ElectricalNetwork network = ElectricNetworkManager.getNetwork(level);


        if (inventory.isEmpty()) {
            chargeCapacitors = false;
            network.setResistance(this,0, (int) 1E16);
            return;
        }
        ItemStack itemStack = inventory.getItem(0);

        if (getRecipe(itemStack).isPresent()) {



            chargeCapacitors = true;
            network.setResistance(this,0, (int) 30);
            if (capacitorPercentage >= 200) {
                performRecipe(getRecipe(itemStack).get().value());
            }
        } else {
            chargeCapacitors = false;
            network.setResistance(this,0, (int) 1E16);
        }

    }




    @Override
    public double resistance() {
        return chargeCapacitors ? 30 : 1E16;
    }




    @Override
    public void tick() {
        super.tick();


        if (level.isClientSide) {
            angle.chase(180 * (capacitorPercentage / 200f), 0.2f, LerpedFloat.Chaser.EXP);
            angle.tickChaser();
        }


            if (current*voltage >= 1000) {
                if (chargeCapacitors) {
                    if (capacitorPercentage < 200) {
                        capacitorPercentage++;
                    } else onInventoryChanged(inventory.getStackInSlot(0).getCount());
                }
            }

    }

    public void performRecipe(PolarizingRecipe recipe) {
        ItemStack stack = recipe.getRollableResults().get(0).rollOutput(level.random);
        inventory.setStackInSlot(0, stack);
        TFMGUtils.spawnElectricParticles(level, getBlockPos());
        capacitorPercentage = 0;
    }

    public Optional<RecipeHolder<PolarizingRecipe>> getRecipe(ItemStack item) {
        if (!hasLevel())
            return Optional.empty();
        Optional<RecipeHolder<PolarizingRecipe>> assemblyRecipe = SequencedAssemblyRecipe.getRecipe(this.level, item, TFMGRecipeTypes.POLARIZING.getType(), PolarizingRecipe.class);
        if (assemblyRecipe.isPresent()) {
            return assemblyRecipe;
        } else {
            //  inventory.setItem(0, item);
            return TFMGRecipeTypes.POLARIZING.find(new RecipeWrapper(inventory), this.level);
        }
    }

    public int getItemChargingRate() {
        return TFMGConfigs.common().machines.polarizerItemChargingRate.get();
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.put("Inventory", inventory.serializeNBT(registries));
        compound.putInt("CapacitorPercentage", capacitorPercentage);
        compound.putBoolean("ChargeCapacitors", chargeCapacitors);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        inventory.deserializeNBT(registries,compound.getCompound("Inventory"));
        capacitorPercentage = compound.getInt("CapacitorPercentage");
        chargeCapacitors = compound.getBoolean("ChargeCapacitors");
    }

}
