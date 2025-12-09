package com.drmangotea.tfmg.content.electricity.utilities.polarizer;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.recipes.PolarizingRecipe;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;


public class PolarizerBlockEntity extends ElectricBlockEntity implements IHaveGoggleInformation {

    public SmartInventory inventory = new SmartInventory(1, this, 1, false)
            .whenContentsChanged(this::onInventoryChanged);
    public LazyOptional<IItemHandlerModifiable> itemCapability;

    LerpedFloat angle = LerpedFloat.angular();

    public boolean chargeCapacitors = false;
    public int capacitorPercentage = 0;

    public PolarizerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        itemCapability = LazyOptional.of(() -> inventory);
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite();
    }

    public void onInventoryChanged(int count) {
        sendData();
        setChanged();
        if (inventory.isEmpty()) {
            chargeCapacitors = false;
            updateNextTick();
            return;
        }
        ItemStack itemStack = inventory.getItem(0);

        if (getRecipe(itemStack).isPresent()) {



            chargeCapacitors = true;
            updateNextTick();
            if (capacitorPercentage >= 200) {
                performRecipe(getRecipe(itemStack).get());
            }
        } else {
            chargeCapacitors = false;
            updateNextTick();
        }

    }




    @Override
    public float resistance() {
        return chargeCapacitors ? 30 : 0;
    }

    //
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        if(getPowerUsage()<2000&&!inventory.isEmpty()){
            CreateLang.translate("goggles.polarizer.insufficient_power")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);
            return true;
        }

        CreateLang.translate("goggles.polarizer.header")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip, 1);

        CreateLang.translate("goggles.polarizer.charge")
                .add(CreateLang.text(capacitorPercentage/2f+"%"))
                .style(ChatFormatting.DARK_AQUA)
                .forGoggles(tooltip);

        return true;
    }

    @Override
    public boolean canBeInGroups() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();


        if (level.isClientSide) {
            angle.chase(180 * (capacitorPercentage / 200f), 0.2f, LerpedFloat.Chaser.EXP);
            angle.tickChaser();
        }


            if (getPowerUsage() >= 2000) {
                if (chargeCapacitors) {
                    if (capacitorPercentage < 200) {
                        capacitorPercentage++;
                    } else onInventoryChanged(inventory.getStackInSlot(0).getCount());
                }
            }

    }

    public void performRecipe(PolarizingRecipe recipe) {
        ItemStack stack = recipe.getRollableResults().get(0).rollOutput();
        inventory.setStackInSlot(0, stack);
        TFMGUtils.spawnElectricParticles(level, getBlockPos());
        capacitorPercentage = 0;
    }

    public Optional<PolarizingRecipe> getRecipe(ItemStack item) {
        if (!hasLevel())
            return Optional.empty();
        Optional<PolarizingRecipe> assemblyRecipe = SequencedAssemblyRecipe.getRecipe(this.level, item, TFMGRecipeTypes.POLARIZING.getType(), PolarizingRecipe.class);
        if (assemblyRecipe.isPresent()) {
            return assemblyRecipe;
        } else {
            //  inventory.setItem(0, item);
            return TFMGRecipeTypes.POLARIZING.find(inventory, this.level);
        }
    }

    public int getItemChargingRate() {
        return TFMGConfigs.common().machines.polarizerItemChargingRate.get();
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Inventory", inventory.serializeNBT());
        compound.putInt("CapacitorPercentage", capacitorPercentage);
        compound.putBoolean("ChargeCapacitors", chargeCapacitors);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        capacitorPercentage = compound.getInt("CapacitorPercentage");
        chargeCapacitors = compound.getBoolean("ChargeCapacitors");
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();
        return super.getCapability(cap, side);
    }
}
