package com.drmangotea.tfmg.content.engines.types.regular_engine;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.data_storage.CylinderFuels;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.EngineType;
import com.drmangotea.tfmg.content.engines.types.turbine_engine.TurbineEngineBlockEntity;
import com.drmangotea.tfmg.registry.*;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlock.EXTENDED;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class RegularEngineBlockEntity extends AbstractSmallEngineBlockEntity {

    public EngineType type = getDefaultEngineType();
    public SmartInventory pistonInventory;
    Predicate<FluidStack> supportedFuels = fs -> false;

    protected int soundTimer = 0;
    boolean updateFuel = true;

    public RegularEngineBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        pistonInventory = createInventory();
    }

    public EngineType getDefaultEngineType() {
        return TFMGEngineTypes.I.get();
    }

    public void updateInventory() {
        pistonInventory = createInventory();
    }

    public SmartInventory createInventory() {
        return new SmartInventory(type.pistons.size(), this)
                .withMaxStackSize(1)
                .whenContentsChanged(this::onInventoryChanged);
    }

    private void onInventoryChanged(int integer) {
        refreshFuels();
        updateRotation();
        sendData();
        setChanged();
    }

    @Override
    public CombinedInvWrapper itemHandler() {
        return new CombinedInvWrapper(componentsInventory, upgradeInventory, pistonInventory);
    }

    public void refreshFuels() {
        CylinderFuels cylinderFuels = pistonInventory.getItem(0).getOrDefault(TFMGDataComponents.ENGINE_CYLINDER, CylinderFuels.EMPTY);

        if(cylinderFuels.isEmpty())
            return;

        Predicate<FluidStack> isFuelValid = (fs) -> {
            if (level == null)
                return false;
            return cylinderFuels.testFuel(fs, level.registryAccess());
        };

        if (level == null) return;
        if (level.getBlockEntity(controller) instanceof RegularEngineBlockEntity ctrl) {
            ctrl.supportedFuels = isFuelValid;
            for (BlockPos pos : ctrl.engines) {
                if (level.getBlockEntity(pos) instanceof RegularEngineBlockEntity engine) {
                    engine.supportedFuels = isFuelValid;
                }
            }
        }
    }

    @Override
    public Predicate<FluidStack> validFuels() {
        return supportedFuels;
    }

    @Override
    public boolean canWork() {
        if (level == null) return false;
        if (level.getBlockEntity(controller) instanceof RegularEngineBlockEntity controllerBlock) {
            for (BlockPos pos : controllerBlock.getAllEngines()) {
                if (level.getBlockEntity(pos) instanceof RegularEngineBlockEntity be) {
                    for (int i = 0; i < be.pistonInventory.getSlots(); i++) {
                        if (be.pistonInventory.getItem(i).isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return super.canWork();
        }
        return false;
    }

    public boolean hasAllPistons() {
        if (level == null) return false;
        for (BlockPos pos : getControllerBE().getAllEngines()) {
            if (level.getBlockEntity(pos) instanceof RegularEngineBlockEntity be) {
                for (int i = 0; i < be.pistonInventory.getSlots(); i++) {
                    if (be.pistonInventory.getItem(i).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean insertItem(ItemStack itemStack, boolean shifting, Player player, InteractionHand hand) {
        if (level == null) return false;
        if (itemStack.is(AllItems.EMPTY_SCHEMATIC.get())) {
            if (type.is(TFMGTags.Engines.SCHEMATIC_CYCLE_BLACKLIST.tag))
                return false;
            boolean next = false;
            if (type.is(TFMGEngineTypes.BOXER)) {
                if (level.getBlockEntity(controller) instanceof RegularEngineBlockEntity be)
                    be.updateEngineType(TFMGEngineTypes.I.get());
                AllSoundEvents.CONFIRM.play(level, null, getBlockPos(), 1, 1);
                return true;
            }
            for (EngineType engineType : TFMGRegistries.ENGINE_TYPE_REGISTRY) {
                if (next) {
                    if (level.getBlockEntity(controller) instanceof RegularEngineBlockEntity be)
                        be.updateEngineType(engineType);
                    AllSoundEvents.CONFIRM.play(level, null, getBlockPos(), 1, 1);
                    return true;
                }
                if (engineType == type) {
                    next = true;
                }
            }
        }

        if (itemStack.is(TFMGItems.SCREWDRIVER.get())) {
            if (!pistonInventory.isEmpty()) {
                for (int i = 0; i < pistonInventory.getSlots(); i++) {
                    if (!pistonInventory.getItem(i).isEmpty()) {
                        dropItem(pistonInventory.getItem(i));
                        pistonInventory.setItem(i, ItemStack.EMPTY);
                        playRemovalSound();
                        updateRotation();
                        setChanged();
                        sendData();
                        return true;
                    }
                }
            }
            for (int i = componentsInventory.components.size() - 1; i >= 0; i--) {
                if (!componentsInventory.getItem(i).isEmpty()) {
                    dropItem(componentsInventory.getItem(i));
                    componentsInventory.setItem(i, ItemStack.EMPTY);
                    playRemovalSound();
                    updateRotation();
                    setChanged();
                    sendData();
                    return true;
                }
            }

        }
        if (hasAllComponents())
            if (isCorrectCylinder(itemStack))
                if (isCylinderSame(itemStack)) {
                    for (int i = pistonInventory.getSlots() - 1; i >= 0; i--) {
                        if (pistonInventory.getItem(i).isEmpty()) {
                            ItemStack toInsert = itemStack.copy();
                            toInsert.setCount(1);
                            pistonInventory.setItem(i, toInsert);
                            itemStack.shrink(1);
                            playInsertionSound();
                            updateRotation();
                            setChanged();
                            sendData();
                            return true;
                        }
                    }
                }
        if (nextComponent().test(itemStack) && !isController()) {

            if (level.getBlockEntity(controller) instanceof AbstractSmallEngineBlockEntity be) {
                return be.insertItem(itemStack, shifting, player, hand);
            }

        }

        return super.insertItem(itemStack, shifting, player, hand);
    }

    public boolean isCorrectCylinder(ItemStack itemStack) {
        if (!itemStack.has(TFMGDataComponents.ENGINE_CYLINDER))
            return false;
        return !itemStack.is(TFMGTags.Items.ENGINE_TURBINE.tag);
    }

    public boolean isCylinderSame(ItemStack stack) {
        if (level == null)
            return false;
        if (stack.is(TFMGItems.TURBINE_BLADE.get()))
            return true;

        CylinderFuels cylinderFuels = stack.getOrDefault(TFMGDataComponents.ENGINE_CYLINDER, CylinderFuels.EMPTY);

        if (level.getBlockEntity(controller) instanceof RegularEngineBlockEntity ctrl) {
            List<BlockPos> engines = new ArrayList<>(ctrl.engines);
            engines.add(this.controller);
            for (int i = 0; i < ctrl.engineLength() + 1; i++) {
                BlockPos pos = engines.get(i);
                if (level.getBlockEntity(pos) instanceof RegularEngineBlockEntity be) {
                    for (int y = 0; y < be.pistonInventory.getSlots(); y++) {
                        if (!be.pistonInventory.getItem(y).has(TFMGDataComponents.ENGINE_CYLINDER))
                            continue;
                        CylinderFuels fuelsInside = be.pistonInventory.getItem(y).getOrDefault(TFMGDataComponents.ENGINE_CYLINDER, CylinderFuels.EMPTY);
                        if (!fuelsInside.isEmpty() && !fuelsInside.isSame(cylinderFuels))
                            return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null) return;
        if (level.isClientSide)
            makeSound();
        if (updateFuel) {
            refreshFuels();
            updateFuel = false;
        }

    }

    @OnlyIn(Dist.CLIENT)
    private void makeSound() {
        soundTimer++;
        if (!isController())
            return;

        if (soundTimer > 1 / Math.min(6000, (rpm * 0.0002) * pistonInventory.getSlots())) {
            if (level == null) return;
            soundTimer = 0;
            float randomPitch = (level.getRandom().nextFloat() - .5f) * 0.05f;

            if (this instanceof TurbineEngineBlockEntity) {
                TFMGSoundEvents.ENGINE.playAt(level, worldPosition, 0.06f * TFMGConfigs.common().machines.engineLoudness.getF(), 1.5f, false);
            } else

                TFMGSoundEvents.ENGINE.playAt(level, worldPosition, 0.1f * TFMGConfigs.common().machines.engineLoudness.getF(), 0.7f + randomPitch, false);
        }

    }

    public boolean updateEngineType(EngineType newType) {
        if (level == null) return false;
        Direction updateDirection = getBlockState().getValue(HORIZONTAL_FACING);
        if (level.getBlockEntity(getBlockPos().relative(updateDirection)) instanceof RegularEngineBlockEntity be) {
            return be.updateEngineType(newType);
        }
        for (int i = 0; i <= engineLength(); i++) {
            BlockPos pos = getBlockPos().relative(updateDirection.getOpposite(), i);
            if (level.getBlockEntity(pos) instanceof RegularEngineBlockEntity be) {
                if (!be.pistonInventory.isEmpty())
                    return false;
            }
        }
        for (int i = 0; i <= engineLength(); i++) {
            BlockPos pos = getBlockPos().relative(updateDirection.getOpposite(), i);
            if (level.getBlockEntity(pos) instanceof RegularEngineBlockEntity be) {
                be.type = newType;
                be.updateInventory();
                level.setBlockAndUpdate(pos, be.getBlockState().setValue(EXTENDED, newType == TFMGEngineTypes.I.get() || newType == TFMGEngineTypes.U.get()));
            }
        }

        return true;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putString("Type", type.getKey().toString());
        compound.put("Cylinders", pistonInventory.serializeNBT(registries));
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        this.type = TFMGEngineTypes.decodeType(compound.getString("Type"), getDefaultEngineType());
        pistonInventory.deserializeNBT(registries, compound.getCompound("Cylinders"));
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, pistonInventory);
    }

    @Override
    public float efficiencyModifier() {
        return type.efficiencyModifier * super.efficiencyModifier() * getUpgradeEfficiencyModifier()*(TFMGConfigs.common().machines.engineFuelConsumption.getF()/100f);
    }

    @Override
    public float speedModifier() {
        return type.speedModifier * super.speedModifier() * getUpgradeSpeedModifier();
    }

    @Override
    public float torqueModifier() {
        return type.torqueModifier * super.torqueModifier() * getUpgradeTorqueModifier();
    }

    @Override
    public boolean canConnect(AbstractSmallEngineBlockEntity candidate) {
        if (candidate instanceof RegularEngineBlockEntity regularEngine) {
            return regularEngine.type == this.type;
        }
        return false;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!isController())
            return getControllerBE().addToGoggleTooltip(tooltip, isPlayerSneaking);

        TFMGTexts.header("engine")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        if (nextComponent() != Ingredient.EMPTY) {
            TFMGTexts.Engine.unfinished().forGoggles(tooltip);
            TFMGTexts.Engine.nextComponent(nextComponent().getItems()[0]).forGoggles(tooltip);
            TFMGTexts.Engine.type(type).forGoggles(tooltip, 1);
            return true;
        }
        if (!hasAllPistons()) {
            TFMGTexts.Engine.lastRequirement(type.lastRequirement).forGoggles(tooltip);
            TFMGTexts.Engine.type(type).forGoggles(tooltip, 1);
            return true;
        }

        TFMGTexts.Engine.type(type).forGoggles(tooltip, 1);
        TFMGTexts.Engine.rpm(rpm).forGoggles(tooltip, 1);
        TFMGTexts.Engine.signal((int) (highestSignal * 15)).forGoggles(tooltip, 1);
        TFMGTexts.Engine.torque(torque).forGoggles(tooltip, 1);
        TFMGTexts.Engine.fuelConsumption(getFuelConsumption()).forGoggles(tooltip, 1);
        if (oil > 0) {
            TFMGTexts.Engine.oil(oil).forGoggles(tooltip);
        }
        if (coolingFluid > 0) {
            TFMGTexts.Engine.coolingFluid(coolingFluid).forGoggles(tooltip);
        }
        TFMGUtils.createFluidTooltip(tooltip, fluidCapability);

        return true;
    }

    @Override
    public void clearContent() {
        super.clearContent();
        this.pistonInventory.clearContent();
    }
}
