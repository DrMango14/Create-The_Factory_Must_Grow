package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.accumulator_rack;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.cells.AccumulatorCellItem;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.animatedContainer.AnimatedContainerBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class AccumulatorRackBlockEntity extends SmartBlockEntity implements MenuProvider, IHaveGoggleInformation {

    public AccumulatorRackEnergyStorage energy = createEnergyStorage();

    public SmartInventory inventory = new SmartInventory(4, this, 1, false)
            .whenContentsChanged(this::onInventoryChanged);

    public int length = 1;
    public BlockPos controller = getBlockPos();
    boolean refreshNextTick = true;

    protected AnimatedContainerBehaviour<AccumulatorRackMenu> openTracker;

    private IEnergyStorage energyCapability;


    public AccumulatorRackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        energyCapability = energy;

    }

    public AccumulatorRackEnergyStorage createEnergyStorage() {
        return new AccumulatorRackEnergyStorage(1000000, 2500000, this) {
            @Override
            public void onEnergyChanged(int amount, int oldAmount) {
                sendData();

            }
        };
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        TFMGLang.text("Length " + length).forGoggles(tooltip);
        TFMGLang.text("is controller " + isController()).forGoggles(tooltip);
        if (level.getBlockEntity(controller) instanceof AccumulatorRackBlockEntity be)
            TFMGLang.text("Energy c " + be.energy.getEnergyStored()).forGoggles(tooltip);
        TFMGLang.text("Energy " + energy.getEnergyStored()).forGoggles(tooltip);
        TFMGLang.text("Max Energy " + energy.getMaxEnergyStored()).forGoggles(tooltip);

        return true;
    }

    public List<ItemStack> getAllStorageItems() {
        if (!isController()) {
            if (level.getBlockEntity(controller) instanceof AccumulatorRackBlockEntity controller) {
                return controller.getAllStorageItems();
            } else return new ArrayList<>();
        }
        List<ItemStack> items = new ArrayList<>();
        Direction facing = getBlockState().getValue(FACING);
        for (int i = 0; i < 10; i++) {
            BlockPos pos = getBlockPos().relative(facing.getOpposite(), i);
            if (level.getBlockEntity(pos) instanceof AccumulatorRackBlockEntity be) {
                for (int j = 0; j < 4; j++) {
                    ItemStack stack = be.inventory.getItem(j);
                    if (stack.getItem() instanceof AccumulatorCellItem) {
                        items.add(stack);
                    }
                }
            } else break;
        }


        return items;
    }

    public void onInventoryChanged(int count) {
        updateStorage();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateStorage();
    }

    public boolean isController() {
        return controller == getBlockPos();
    }

    public void updateStorage() {
        if (!isController()) {
            if (level.getBlockEntity(controller) instanceof AccumulatorRackBlockEntity c) {
                c.updateStorage();
            }
            return;
        }

        Direction facing = getBlockState().getValue(FACING);

        int totalStorage = 0;
        int totalMaxStorage = 0;

        for (int i = 0; i < length; i++) {
            BlockPos pos = getBlockPos().relative(facing.getOpposite(), i);
            if (level.getBlockEntity(pos) instanceof AccumulatorRackBlockEntity be) {
                for (int j = 0; j < 4; j++) {
                    ItemStack stack = be.inventory.getItem(j);
                    totalStorage += stack.getOrDefault(TFMGDataComponents.ACCUMULATOR_STORAGE, 0);
                    if (stack.getItem() instanceof AccumulatorCellItem item)
                        totalMaxStorage += item.capacity;

                }
            }
        }
        energy.setMaxEnergyStorage(totalMaxStorage);
        energy.setEnergy(totalStorage);

    }

    @Override
    public void tick() {
        super.tick();


        if (refreshNextTick) {
            refreshMultiblock();
            refreshNextTick = false;
        }


    }


    @Override
    public void remove() {
        super.remove();
        refreshController();

        Direction facing = getBlockState().getValue(FACING);
        for (int i = 0; i < 10; i++) {
            BlockPos pos = getBlockPos().relative(facing, 5).relative(facing.getOpposite(), i);
            if (level.getBlockEntity(pos) instanceof AccumulatorRackBlockEntity be) {
                be.refreshMultiblock();
                be.updateStorage();
            }
        }
    }


    public void onPlaced() {
        refreshController();
    }


    public void refreshController() {
        Direction facing = getBlockState().getValue(FACING);
        for (int i = 0; i < 15; i++) {
            BlockPos pos = getBlockPos().relative(getBlockState().getValue(FACING), i);
            if (level.getBlockEntity(pos) instanceof AccumulatorRackBlockEntity be && be.getBlockState().getValue(FACING) == facing &&
                    !(level.getBlockEntity(pos.relative(facing)) instanceof AccumulatorRackBlockEntity otherBE && otherBE.getBlockState().getValue(FACING) == facing)) {
                be.refreshMultiblock();
                be.sendData();
            }
        }
    }

    public void refreshMultiblock() {
        Direction facing = getBlockState().getValue(FACING);
        refreshCapability();
        if (!(level.getBlockEntity(getBlockPos().relative(facing)) instanceof AccumulatorRackBlockEntity be && be.getBlockState().getValue(FACING) == facing)) {
            int newLength = 1;
            controller = getBlockPos();
            for (int i = 1; i < 15; i++) {
                BlockPos pos = getBlockPos().relative(getBlockState().getValue(FACING).getOpposite(), i);
                if (level.getBlockEntity(pos) instanceof AccumulatorRackBlockEntity otherBe && otherBe.getBlockState().getValue(FACING) == getBlockState().getValue(FACING)) {
                    otherBe.controller = this.getBlockPos();
                    otherBe.refreshCapability();
                    otherBe.length = 0;
                    otherBe.energy.setEnergy(0);
                    sendData();
                    newLength++;
                } else break;
            }
            length = newLength;
            int oldEnergy = energy.getEnergyStored();
            energy = createEnergyStorage();
            energy.setEnergy(Math.min(oldEnergy, energy.getMaxEnergyStored() * length));
            refreshCapability();
            for (int i = 1; i < length; i++) {
                BlockPos pos = getBlockPos().relative(getBlockState().getValue(FACING).getOpposite(), i);
                if (level.getBlockEntity(pos) instanceof AccumulatorRackBlockEntity be) {
                    be.refreshCapability();
                    sendData();
                }
            }
        }
    }

    public void refreshCapability() {
        IEnergyStorage oldCap = energyCapability;
        if (level.getBlockEntity(controller) instanceof AccumulatorRackBlockEntity be) {
            energyCapability = be.energy;
        } else energyCapability = energy;
        invalidateCapabilities();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                TFMGBlockEntities.ACCUMULATOR_RACK.get(),
                (be, context) -> be.energyCapability
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TFMGBlockEntities.ACCUMULATOR_RACK.get(),
                (be, context) -> be.inventory
        );
    }

    public ItemInteractionResult use(Player player) {
        player.openMenu(this, worldPosition);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("Inventory", inventory.serializeNBT(registries));
        compound.putInt("ForgeEnergy", energy.getEnergyStored());
        compound.putInt("Length", length);


    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
        length = compound.getInt("Length");
        energy.setEnergy(compound.getInt("ForgeEnergy"));


    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(openTracker = new AnimatedContainerBehaviour<>(this, AccumulatorRackMenu.class));
    }


    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return AccumulatorRackMenu.create(i, inventory, this);
    }
}
