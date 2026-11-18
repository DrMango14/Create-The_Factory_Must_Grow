package com.drmangotea.tfmg.content.engines.types;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.engines.base.AbstractEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.base.EngineComponentsInventory;
import com.drmangotea.tfmg.content.engines.base.EngineProperties;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineGenerator;
import com.drmangotea.tfmg.content.engines.upgrades.EnginePipingUpgrade;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgrade;
import com.drmangotea.tfmg.registry.*;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.drmangotea.tfmg.content.engines.base.EngineBlock.ENGINE_STATE;
import static com.drmangotea.tfmg.content.engines.base.EngineBlock.EngineState.NORMAL;
import static com.drmangotea.tfmg.content.engines.base.EngineBlock.EngineState.SHAFT;
import static com.drmangotea.tfmg.content.engines.base.EngineBlock.SHAFT_FACING;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class AbstractSmallEngineBlockEntity extends AbstractEngineBlockEntity implements Clearable, IElectric {
    public Optional<? extends EngineUpgrade> upgrade = Optional.empty();
    private List<Component> cachedUpgradeLines = null;
    public int oil = 0;
    public int coolingFluid = 0;

    public SmartInventory upgradeInventory = new SmartInventory(1, this, 1, false).whenContentsChanged(this::upgradesChanged);
    public EngineComponentsInventory componentsInventory;

    public BlockPos controller = getBlockPos();
    public boolean connectNextTick = true;
    public boolean delayedConnect = false;

    public List<BlockPos> engines = new ArrayList<>();
    public int engineNumber = 0;


    public AbstractSmallEngineBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        componentsInventory = new EngineComponentsInventory(this, EngineProperties.commonRegularComponents());
    }

    public void upgradesChanged(int slot) {
        sendData();
        setChanged();
        if (upgradeInventory.isEmpty()) {
            this.upgrade = Optional.empty();
            return;
        }
        ItemStack itemStack = upgradeInventory.getItem(slot);
        Optional<Holder<EngineUpgrade>> upgradeHolder = EngineUpgrade.getUpgradeFromItem(itemStack);
        this.upgrade = upgradeHolder.map(Holder::value);
        if (upgrade.isPresent()) {
            playInsertionSound();
        }
		upgrade.ifPresent(u -> u.updateUpgrade(this));
        updateRotation();
    }

    public CombinedInvWrapper itemHandler() {
        return new CombinedInvWrapper(componentsInventory, upgradeInventory);
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, componentsInventory);
        ItemHelper.dropContents(level, worldPosition, upgradeInventory);
    }
	
	@Override
	public Level getWorld() {
		return level;
	}
	
	@Override
	public BlockPos getPos() {
		return getBlockPos();
	}

    public int getFuelConsumption() {
        if (rpm == 0)
            return 0;

        float oilModifier = oil > 0 ? 0.7f : 1f;
        float coolingFluidModifier = coolingFluid > 0 ? 0.7f : 1f;

		return (int) ((5f/6f * (1 / efficiencyModifier()) * getSpeedEfficiency() * highestSignal * oilModifier * coolingFluidModifier * (engineLength() + 1)));
    }

    public void detachEngines() {}

    private List<Component> getUpgradeLines() {
        if (cachedUpgradeLines != null)
            return cachedUpgradeLines;

        List<Component> lines = getUpgrades().map(upgrade -> (Component) (
			Component.literal("- ").withStyle(ChatFormatting.DARK_GRAY)
				.append(upgrade.getDisplayName().copy().withStyle(ChatFormatting.AQUA))
		)).toList();
        cachedUpgradeLines = lines;
        return lines;
    }

    public void invalidateUpgradeCache() {
        cachedUpgradeLines = null;
        if (level != null && !isController() && level.getBlockEntity(controller) instanceof AbstractSmallEngineBlockEntity c) {
            c.cachedUpgradeLines = null;
        }
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        List<Component> lines = getControllerBE().getUpgradeLines();
        if (lines.isEmpty()) return false;
        tooltip.add(Component.translatable("block.tfmg.engine.tooltip.upgrades").withStyle(ChatFormatting.GOLD));
        tooltip.addAll(lines);
        return true;
    }

    public void setBlockStates(AbstractSmallEngineBlockEntity be, BlockPos last) {
        if (level == null || be.isController()) return;
        BlockPos pos = be.getBlockPos();
		Direction facing = getBlockState().getValue(SHAFT_FACING).getOpposite();
		level.setBlock(pos, level.getBlockState(pos).setValue(SHAFT_FACING, facing), 2);
    }

    public boolean hasAllComponents() {
        if (level == null) return false;
        if (level.getBlockEntity(controller) instanceof AbstractSmallEngineBlockEntity be) {
            return be.nextComponent() == Ingredient.EMPTY;
        }
        return false;
    }

    public boolean hasUpgrade() {
        return upgrade.isPresent();
    }

    public int voltageGeneration() {
        if (upgrade.isPresent() && upgrade.get() instanceof EngineGenerator generator)
            return generator.getVoltageGeneration(this);
        return 0;
    }

    public float powerGeneration() {
        if (upgrade.isPresent() && upgrade.get() instanceof EngineGenerator generator)
            return generator.getPowerGeneration(this);
        return 0;
    }

	public ElectricalProperties getProperties() {
		if (upgrade.isPresent() && upgrade.get() instanceof EngineGenerator generator)
			return generator.getProperties(this);
		return null;
	}

    @Override
    public void lazyTick() {
        super.lazyTick();
        upgrade.ifPresent(engineUpgrade -> engineUpgrade.lazyTickUpgrade(this));

        if (!canWork()) return;
        if(rpm==0) return;
        if (level == null) return;
        if (level.random.nextInt(45) == 0) {
            if (oil > 0)
                oil--;
        }
        if (level.random.nextInt(45) == 0) {
            if (coolingFluid > 0)
                coolingFluid--;
        }
    }

    @Override
    public float calculateAddedStressCapacity() {
        float stress = (int)(super.calculateAddedStressCapacity() + (torque))*(TFMGConfigs.common().machines.enginePower.getF() /100)*(Math.max(1,engines.size()))*0.7f;
        return hasTwoShafts() ? stress / 2 : stress;
    }

    public boolean hasTwoShafts() {
        if (!isController())
            return getControllerBE().hasTwoShafts();
        if (level == null) return false;
        if (this.getBlockState().hasProperty(ENGINE_STATE) && this.getBlockState().getValue(ENGINE_STATE) == SHAFT) {
            BlockPos pos = getBlockPos().relative(this.getBlockState().getValue(SHAFT_FACING).getOpposite(), engineLength() );
            return level.getBlockState(pos).getValue(ENGINE_STATE) == SHAFT;
        }
        return false;
    }

    @Override
    public void neighbourChanged() {
        if (controller == null)
            return;
        super.neighbourChanged();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.upgrade.isPresent() && this.upgrade.get() instanceof EnginePipingUpgrade pipe) {
            pipe.findTank(this);
        }
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.put("Controller", NbtUtils.writeBlockPos(controller));
        compound.put("Components", componentsInventory.serializeNBT(registries));
        compound.put("Upgrades", upgradeInventory.serializeNBT(registries));
        this.upgrade.ifPresent(upgrade -> upgrade.write(compound, "Upgrade"));
        compound.putInt("Oil", oil);
        compound.putInt("CoolingFluid", coolingFluid);
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        oil = compound.getInt("Oil");
        coolingFluid = compound.getInt("CoolingFluid");
        componentsInventory.deserializeNBT(registries, compound.getCompound("Components"));
        upgradeInventory.deserializeNBT(registries,compound.getCompound("Upgrades"));
        remapUpgrade(compound, registries);
        if (compound.contains("Upgrade")) {
			this.upgrade = EngineUpgrade.read(compound.get("Upgrade"));
        }
        super.read(compound, registries, clientPacket);
        controller = NbtUtils.readBlockPos(compound, "Controller").orElse(getBlockPos());
        invalidateUpgradeCache();
    }

    private void remapUpgrade(CompoundTag compound, HolderLookup.Provider registries) {
        if (compound.contains("UpgradeItem")) {
            ItemStack upgradeItem = ItemStack.parse(registries, compound.getCompound("UpgradeItem")).orElse(ItemStack.EMPTY);
            if (!upgradeItem.isEmpty()) {
                upgradeInventory.setStackInSlot(0, upgradeItem);
            }
            compound.remove("UpgradeItem");
            upgradesChanged(0);
        }
    }

    public int engineLength() {
        return engines.size();
    }

    @Override
    public boolean canWork() {
        if (!nextComponent().isEmpty())
            return false;
        return super.canWork();
    }

    public Ingredient nextComponent() {
        if (!isController())
            return Ingredient.EMPTY;
        for (int i = 0; i < componentsInventory.getSlots(); i++) {
            if (componentsInventory.getStackInSlot(i).isEmpty()) {
                return componentsInventory.components.get(i);
            }
        }

        return Ingredient.EMPTY;
    }

    protected void analogSignalChanged() {
        getControllerBE().updateRotation();
        if (level == null) return;

        int newSignal = level.getBestNeighborSignal(getBlockPos());
        signal = newSignal;
        if (!isController() && level.getBlockEntity(controller) instanceof AbstractSmallEngineBlockEntity be) {
			be.analogSignalChanged();
			return;
		}

        for (BlockPos pos : engines) {
            newSignal = Math.max(level.getBestNeighborSignal(pos), newSignal);
        }
        newSignal = Math.max(level.getBestNeighborSignal(controller), newSignal);
        highestSignal = newSignal/15f;
        updateRotation();
    }

    @Override
    public IFluidHandler handlerForCapability() {
        return isController() || getControllerBE() == this ? new CombinedTankWrapper(fuelTank, exhaustTank)
                : getControllerBE().handlerForCapability();
    }


    public void updateRotation() {
        if (level == null) return;
        if (!isController()) {
            if (level.getBlockEntity(controller) instanceof AbstractSmallEngineBlockEntity be)
                be.updateRotation();
            return;
        }

        if (fuelTank.isEmpty()) {
            rpm = 0;
            torque = 0;
        }

        List<BlockPos> allEngines = new ArrayList<>(engines);
        allEngines.add(controller);
        if (validFuels().test(fuelTank.getFluid())) {
            if (!canWork()) {
                allEngines.forEach(pos -> {
                    if (level == null) return;
                    if (level.getBlockEntity(pos) instanceof AbstractEngineBlockEntity be) {
                        be.rpm = 0;
                        be.torque = 0;
                        be.updateGeneratedRotation();
                    }
                });
                return;
            }
            allEngines.forEach(pos -> {
                if (level == null) return;
                if (level.getBlockEntity(pos) instanceof AbstractEngineBlockEntity be) {
                    be.rpm = 4000 * speedModifier() * highestSignal;
                    be.torque = 15 * torqueModifier() * highestSignal;
                    be.updateGeneratedRotation();
                }
            });
            return;
        }
        updateGeneratedRotation();
        getAllEngines().forEach(pos -> {
            if (level == null) return;
            if (level.getBlockEntity(pos) instanceof AbstractEngineBlockEntity be) {
                be.updateGeneratedRotation();
            }
        });
    }

    public boolean canGenerateSpeed() {
        return getBlockState().getValue(ENGINE_STATE) == SHAFT;
    }


    @Override
    public float getGeneratedSpeed() {
        if (!canGenerateSpeed())
            return 0;
        float speed;

        if (hasLevel()) {
            if (getControllerBE().fuelTank.isEmpty())
                return 0;
            if (!getControllerBE().canWork())
                return 0;
            speed = rpm / 15;
            if (reverse)
                speed = speed * -1;
            return convertToDirection(Math.min((int) speed, 256), getBlockState().getValue(HORIZONTAL_FACING));
        }
        return 0;
    }

    @Override
    public void tankUpdated(FluidStack stack, boolean fuel) {
        if (stack.getFluid().isSame(TFMGFluids.CARBON_DIOXIDE.get()) && stack.getAmount() >= exhaustTank.getSpace())
            updateRotation();
        super.tankUpdated(stack, fuel);
    }

    public boolean insertItem(ItemStack itemStack, boolean shifting, Player player, InteractionHand hand) {
        if (level == null || (itemStack.isEmpty() && !player.isCreative())) return false;
        Direction shaft_facing = getBlockState().getValue(SHAFT_FACING);

        if (itemStack.is(AllBlocks.SHAFT.asItem()) && getBlockState().getValue(ENGINE_STATE) == NORMAL && !(level.getBlockEntity(getBlockPos().relative(shaft_facing)) instanceof AbstractEngineBlockEntity)) {
            playInsertionSound();
            level.setBlock(getBlockPos(), getBlockState().setValue(ENGINE_STATE, SHAFT), 2);
            itemStack.shrink(1);
            updateRotation();
            setChanged();
            sendData();
            return true;
        }
        if (itemStack.is(TFMGItems.SCREWDRIVER.get())) {
            for (int i = componentsInventory.components.size() - 1; i >= 0; i--) {
                if (!componentsInventory.getItem(i).isEmpty()) {
                    dropItem(componentsInventory.getItem(i));
                    componentsInventory.setStackInSlot(i, ItemStack.EMPTY);
                    playRemovalSound();
                    updateRotation();
                    setChanged();
                    sendData();
                    return true;
                }
            }
        }
        if (itemStack.is(TFMGItems.COOLING_FLUID_BOTTLE.get())) {
            if (level.getBlockEntity(controller) instanceof AbstractSmallEngineBlockEntity be) {
                Integer amount = itemStack.get(TFMGDataComponents.AMOUNT);
                if (amount == null)
                    return false;
                int toDrain = Math.min(2000 - be.coolingFluid, amount);
                itemStack.set(TFMGDataComponents.AMOUNT, amount - toDrain);
                be.coolingFluid += toDrain;
                level.playSound(null, getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1f, 1f);
                return true;
            }
        }
        if (itemStack.is(TFMGItems.OIL_CAN.get())) {
            if (level.getBlockEntity(controller) instanceof AbstractSmallEngineBlockEntity be) {
                Integer amount = itemStack.get(TFMGDataComponents.AMOUNT);
                if (amount == null)
                    return false;

                int toDrain = Math.min(2000 - be.oil, amount);
                itemStack.set(TFMGDataComponents.AMOUNT, amount - toDrain);
                be.oil += toDrain;
                level.playSound(null, getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1f, 1f);
                updateRotation();
                return true;
            }
        }
        if (itemStack.is(TFMGFluids.COOLING_FLUID.getBucket().orElseThrow())) {
            if (level.getBlockEntity(controller) instanceof AbstractSmallEngineBlockEntity be) {
                if (be.coolingFluid > 1000)
                    return false;
                be.coolingFluid += 1000;
                player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
                level.playSound(null, getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1f, 1f);
                updateRotation();
                return true;
            }
        }
        if (itemStack.is(TFMGFluids.LUBRICATION_OIL.getBucket().orElseThrow())) {
            if (level.getBlockEntity(controller) instanceof AbstractSmallEngineBlockEntity be) {
                if (be.oil > 1000) 
                    return false;
                be.oil += 1000;
                player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
                level.playSound(null, getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1f, 1f);
                updateRotation();
                return true;
            }
        }
        if (upgradeInventory.isEmpty()) {
            Optional<Holder<EngineUpgrade>> upgradeHolder = EngineUpgrade.getUpgradeFromItem(itemStack);
            if (upgradeHolder.isPresent()) {
                upgradeInventory.insertItem(0, itemStack.copyWithCount(1), false);
                upgradesChanged(0);
                itemStack.shrink(1);
                return true;
            }
        }

        if (!isController())
            return false;

        if(player.isCreative()){
            while (!hasAllComponents()) {
                    componentsInventory.insertItem(nextComponent().getItems()[0]);
            }
        }

        if (nextComponent().test(itemStack)) {
            if (componentsInventory.insertItem(itemStack)) {
                if (!itemStack.is(TFMGItems.SCREWDRIVER.get()))
                    itemStack.shrink(1);
                playInsertionSound();
                updateRotation();
                setChanged();
                sendData();
                return true;
            }
        }
        return false;
    }

    public List<AbstractSmallEngineBlockEntity> getEngines() {
		return getEngineStream().toList();
	}
	
	public Stream<AbstractSmallEngineBlockEntity> getEngineStream() {
		if (level == null) return Stream.of();
		return getAllEngines().stream().mapMulti((pos, c) -> {
			if (level.getBlockEntity(pos) instanceof AbstractSmallEngineBlockEntity be) c.accept(be);
		});
	}

    public boolean isController() {
        if (controller == null)
            controller = getBlockPos();
        if (engineNumber == 0)
            controller = getBlockPos();
        return controller.equals(getBlockPos());
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (controller == getBlockPos())
            TFMGTexts.header("engine_controller").forGoggles(tooltip);
        TFMGTexts.Engine.speedEfficiency(getSpeedEfficiency()).forGoggles(tooltip);
        TFMGTexts.Engine.efficiency(efficiencyModifier()).forGoggles(tooltip);
        TFMGTexts.Engine.fuelConsumption(getFuelConsumption()).forGoggles(tooltip);
        TFMGTexts.Engine.rpm(rpm).forGoggles(tooltip);
        TFMGTexts.Engine.length(engineLength()).forGoggles(tooltip);
        TFMGTexts.Engine.torque(torque).forGoggles(tooltip);
        TFMGTexts.Engine.signal((int) (highestSignal*15)).forGoggles(tooltip);
        TFMGLang.number(engineNumber).style(ChatFormatting.DARK_GREEN).forGoggles(tooltip);
        if (isController() && !nextComponent().isEmpty())
            TFMGLang.text(nextComponent().getItems()[0].getDisplayName().getString()).forGoggles(tooltip);

        TFMGUtils.createFluidTooltip(tooltip, fluidCapability);
		
		//don't display electricity without a generator attached
		if (upgrade.isPresent() && upgrade.get() instanceof EngineGenerator)
			makeMultimeterTooltip(tooltip, isPlayerSneaking);

        return true;
    }

    public List<BlockPos> getAllEngines() {
        List<BlockPos> list = new ArrayList<>(engines);
        list.add(controller);
        return list;
    }

    public AbstractSmallEngineBlockEntity getControllerBE() {
        if (isController())
            return this;
        if (level != null && level.getBlockEntity(controller) instanceof AbstractSmallEngineBlockEntity be)
            return be;
        return this;
    }


    @Override
    public void tick() {
        upgrade.ifPresent(engineUpgrade -> engineUpgrade.tickUpgrade(this));
        if (connectNextTick) {
            if (isController()) {
                connect();
                connectNextTick = false;
            }
        }
        super.tick();
    }

    public boolean canConnect(AbstractSmallEngineBlockEntity candidate) {
        return candidate.getBlockState().getBlock() == this.getBlockState().getBlock();
    }

    public void connect() {
        if (level == null) return;
        try {
            Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
            Direction updateDirection = facing.getOpposite();

            BlockEntity candidate = level.getBlockEntity(getBlockPos().relative(facing));
            if (candidate instanceof AbstractSmallEngineBlockEntity smallEngine) {
                if (canConnect(smallEngine)) {
                    smallEngine.connect();
                    return;
                }
            }

            engines = new ArrayList<>();

            for (int i = 0; i < getMaxLength(); i++) {
                BlockPos pos = getBlockPos().relative(updateDirection, i);
                if (level.getBlockEntity(pos) instanceof AbstractSmallEngineBlockEntity be) {
                    if(!canConnect(be))
                        return;
                    if (be.getBlockState().getValue(HORIZONTAL_FACING) != facing) {
                        return;
                    }
                    level.setBlock(be.getBlockPos(), be.getBlockState().setValue(SHAFT_FACING, be.getBlockPos() == this.getBlockPos() ? facing : updateDirection), 2);
                    be.detachEngines();
                    engines.add(pos);

                    be.engineNumber = i;
                    be.engines = new ArrayList<>();
                    be.controller = getBlockPos();
                    be.refreshCapability();

                    setBlockStates(be, null);
                    updateGeneratedRotation();
                    onUpdated();
                    be.sendData();
                    be.setChanged();

                    if (be.getBlockState().getValue(ENGINE_STATE) != NORMAL && i != 0) {
                        setBlockStates(this, getBlockPos().relative(updateDirection, i - 1));
                        break;
                    }
                    if (i == getMaxLength() - 1)
                        setBlockStates(this, getBlockPos().relative(updateDirection, i));


                } else {
                    setBlockStates(this, getBlockPos().relative(updateDirection, i - 1));
                    return;
                }
            }

            updateGeneratedRotation();
            updateRotation();
            setChanged();
            sendData();

        } catch (StackOverflowError ignored) {

        }

    }

    @Override
    public void remove() {
        super.remove();
        updateOthers();
    }

    public void updateOthers() {

        if (!isController()) {
            getControllerBE().connectNextTick = true;
        }
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis() != facing.getAxis())
                continue;
            if (level == null) return;
            if (level.getBlockEntity(getBlockPos().relative(direction)) instanceof AbstractSmallEngineBlockEntity be) {
                level.setBlockAndUpdate(be.getBlockPos(), be.getBlockState().setValue(SHAFT_FACING, direction.getOpposite()));
                be.delayedConnect = true;
                be.connectNextTick = true;
                be.connect();
            }

        }

    }

	public Stream<? extends EngineUpgrade> getUpgrades() {
		return getEngineStream().mapMulti((e,c) -> e.upgrade.ifPresent(c));
	}

    public float getUpgradeSpeedModifier() {
		return getUpgrades().map(u -> u.getSpeedModifier(this))
			.reduce((f1, f2) -> f1*f2).orElse(1f);
    }

    public float getUpgradeTorqueModifier() {
		return getUpgrades().map(u -> u.getTorqueModifier(this))
			.reduce((f1, f2) -> f1*f2).orElse(1f);
    }

    public float getUpgradeEfficiencyModifier() {
		return getUpgrades().map(u -> u.getEfficiencyModifier(this))
			.reduce((f1, f2) -> f1*f2).orElse(1f);
    }

    @Override
    public void clearContent() {
        this.componentsInventory.clearContent();
        this.upgradeInventory.clearContent();
    }
}
