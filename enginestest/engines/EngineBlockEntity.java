package com.drmangotea.tfmg.content.engines;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.ElectricBlockValues;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.registry.*;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

import static com.drmangotea.tfmg.content.engines.EngineProperties.*;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class EngineBlockEntity extends GeneratingKineticBlockEntity implements IElectric, IHaveGoggleInformation {


    /// analog redstone signal
    public int signal = 0;

    public int highestSignal = 0;

    /// position of a block controlling this multiblock
    public BlockPos controller = getBlockPos();

    public int length = 0;

    public boolean connectEngineNextTick = false;

    /// list of all engine blocks merged with this one
    public List<EngineBlockEntity> engines = new ArrayList<>();

    public int oil = 0;
    public int coolingFluid = 0;

    boolean drainFuel = false;


    /// fuel and exhaust inventory
    public EngineFluidTank fuelTank;
    public EngineFluidTank exhaustTank;
    public IFluidHandler fluidCapability;
    ///
    List<TagKey<Fluid>> supportedFuels = new ArrayList<>();

    public EngineType engineType = EngineType.I;

    public EngineComponentsInventory componentsInventory;
    public SmartInventory cylinderInventory;


    /// stores data related to electricity
    public ElectricBlockValues data = new ElectricBlockValues(getBlockPos().asLong());

    public EngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
        componentsInventory = new EngineComponentsInventory(this, EngineProperties.commonRegularComponents());
        cylinderInventory = createInventory();
        refreshCapability();
    }

    public void updateInventory() {
        cylinderInventory = createInventory();
    }

    public SmartInventory createInventory() {
        return new SmartInventory(engineType.pistons.size(), this)
                .withMaxStackSize(1)
                .whenContentsChanged(this::onInventoryChanged)
                ;
    }

    private void onInventoryChanged(int integer) {
        refreshFuels();
        updateGeneratedRotation();
        sendData();
        setChanged();
    }

    public void refreshCapability() {
        fuelTank = new EngineFluidTank(8000, false, true, f -> tankUpdated(f, true), TFMGTags.TFMGFluidTags.AIR.tag);
        exhaustTank = new EngineFluidTank(8000, true, false, f -> tankUpdated(f, false));
        fluidCapability = new CombinedTankWrapper(fuelTank, exhaustTank);
        invalidateCapabilities();

    }

    public void refreshFuels() {

        CompoundTag fuelsToAllow = cylinderInventory.getItem(0).get(TFMGDataComponents.FUEL_TAGS);

        if (fuelsToAllow == null)
            return;

        List<TagKey<Fluid>> fuelsFound = new ArrayList<>();
        for (String key : fuelsToAllow.getAllKeys()) {

            String id = fuelsToAllow.getString(key);

            TagKey<Fluid> tag = FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", id.replace("c:", "")));

            fuelsFound.add(tag);
        }

        if (level.getBlockEntity(controller) instanceof EngineBlockEntity be) {
            be.supportedFuels = new ArrayList<>(fuelsFound);

            for (EngineBlockEntity engine : be.engines) {
                BlockPos pos = BlockPos.of(engine.getPos());
                if (level.getBlockEntity(pos) instanceof EngineBlockEntity be1) {
                    be1.supportedFuels = new ArrayList<>(fuelsFound);
                }
            }
        }
        updateGeneratedRotation();
    }


    public List<TagKey<Fluid>> getSupportedFuels() {
        return supportedFuels;
    }


    @Override
    public float getGeneratedSpeed() {

        if (!isController())
            return 0;
       // if (!canWork())
       //     return 0;

        return convertToDirection(highestSignal * 10, getBlockState().getValue(HORIZONTAL_FACING));
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.REGULAR_ENGINE.get(),
                (be, context) -> be.fluidCapability
        );
    }

    public boolean insertItem(ItemStack itemStack, boolean shifting, Player player, InteractionHand hand) {


        if (itemStack.is(AllItems.EMPTY_SCHEMATIC.get()) && cylinderInventory.isEmpty()) {

            boolean next = false;
            if (engineType == EngineType.TURBINE) {
                if (level.getBlockEntity(controller) instanceof EngineBlockEntity be) {
                    be.updateEngineType(EngineType.I);

                }
                AllSoundEvents.CONFIRM.play(level, null, getBlockPos(), 1, 1);
                return true;
            }
            for (EngineType engineType : EngineType.values()) {
                if (next) {
                    if (level.getBlockEntity(controller) instanceof EngineBlockEntity be) {
                        be.updateEngineType(engineType);

                    }
                    AllSoundEvents.CONFIRM.play(level, null, getBlockPos(), 1, 1);
                    return true;
                }
                if (this.engineType == engineType) {
                    next = true;
                }
            }
        }

        if (itemStack.is(TFMGItems.SCREWDRIVER.get())) {
            if (!cylinderInventory.isEmpty()) {
                for (int i = 0; i < cylinderInventory.getSlots(); i++) {
                    if (!cylinderInventory.getItem(i).isEmpty()) {
                        dropItem(cylinderInventory.getItem(i));
                        cylinderInventory.setItem(i, ItemStack.EMPTY);
                        playRemovalSound();
                        updateGeneratedRotation();
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
                    updateGeneratedRotation();
                    setChanged();
                    sendData();
                    return true;
                }
            }

        }
        if (hasAllComponents())
            if (isCorrectCylinder(itemStack))
                if (isCylinderSame(itemStack)) {
                    for (int i = cylinderInventory.getSlots() - 1; i >= 0; i--) {
                        if (cylinderInventory.getItem(i).isEmpty()) {
                            ItemStack toInsert = itemStack.copy();
                            toInsert.setCount(1);
                            cylinderInventory.setItem(i, toInsert);
                            itemStack.shrink(1);
                            playInsertionSound();
                            updateGeneratedRotation();
                            setChanged();
                            sendData();
                            return true;
                        }
                    }
                }
        if (nextComponent().test(itemStack) && !isController()) {

            if (level.getBlockEntity(controller) instanceof EngineBlockEntity be) {
                return be.insertItem(itemStack, shifting, player, hand);
            }

        }

        if (itemStack.is(TFMGItems.COOLING_FLUID_BOTTLE.get())) {

            if (level.getBlockEntity(controller) instanceof EngineBlockEntity be) {

                int toDrain = Math.min(2000 - coolingFluid, itemStack.get(TFMGDataComponents.AMOUNT));
                itemStack.set(TFMGDataComponents.AMOUNT, itemStack.get(TFMGDataComponents.AMOUNT) - toDrain);
                be.coolingFluid += toDrain;
                level.playSound(null, getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1f, 1f);
                return true;
            }
        }
        if (itemStack.is(TFMGItems.OIL_CAN.get())) {
            if (level.getBlockEntity(controller) instanceof EngineBlockEntity be) {
                int toDrain = Math.min(2000 - oil, itemStack.get(TFMGDataComponents.AMOUNT));
                itemStack.set(TFMGDataComponents.AMOUNT, itemStack.get(TFMGDataComponents.AMOUNT) - toDrain);
                be.oil += toDrain;
                level.playSound(null, getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1f, 1f);
                updateGeneratedRotation();
                return true;
            }
        }
        if (itemStack.is(TFMGFluids.COOLING_FLUID.getBucket().get())) {
            if (coolingFluid <= 1000) {
                coolingFluid += 1000;
                player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
                level.playSound(null, getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1f, 1f);
                updateGeneratedRotation();
                return true;
            }
        }
        if (itemStack.is(TFMGFluids.LUBRICATION_OIL.getBucket().get())) {
            if (oil <= 1000) {
                oil += 1000;
                player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
                level.playSound(null, getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1f, 1f);
                updateGeneratedRotation();
                return true;
            }
        }

        if (!isController())
            return false;
        if (player.isCreative() && !nextComponent().isEmpty()) {
            if (componentsInventory.insertItem(nextComponent().getItems()[0])) {
                playInsertionSound();
                updateGeneratedRotation();
                setChanged();
                sendData();
                return true;
            }
        }


        if (nextComponent().test(itemStack)) {
            if (componentsInventory.insertItem(itemStack)) {
                if (!itemStack.is(TFMGItems.SCREWDRIVER.get()))
                    itemStack.shrink(1);
                playInsertionSound();
                updateGeneratedRotation();
                setChanged();
                sendData();
                return true;
            }
        }

        return false;
    }

    public boolean isCylinderSame(ItemStack stack) {

        if (stack.is(TFMGItems.TURBINE_BLADE.get()))
            return true;

        CompoundTag tag = stack.get(TFMGDataComponents.FUELS);


        if (level.getBlockEntity(controller) instanceof EngineBlockEntity controller) {

            List<EngineBlockEntity> engines = new ArrayList<>(controller.engines);


            for (int i = 0; i < controller.length; i++) {
                BlockPos pos = engines.get(i).getBlockPos();
                if (level.getBlockEntity(pos) instanceof EngineBlockEntity be) {
                    for (int y = 0; y < be.cylinderInventory.getSlots(); y++) {
                        if (!be.cylinderInventory.getItem(y).is(TFMGItems.ENGINE_CYLINDER.get()))
                            continue;

                        CompoundTag tagInside = be.cylinderInventory.getItem(y).get(TFMGDataComponents.FUELS);

                        if (tagInside == null)
                            continue;

                        if (!tagInside.toString().equals(tag.toString()))
                            return false;

                    }
                }
            }
        }

        return true;
    }

    public boolean isCorrectCylinder(ItemStack itemStack) {
        return itemStack.is(TFMGItems.ENGINE_CYLINDER.get()) || itemStack.is(TFMGItems.SIMPLE_ENGINE_CYLINDER.get()) || itemStack.is(TFMGItems.DIESEL_ENGINE_CYLINDER.get());
    }

    public void playInsertionSound() {
        level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4f, 0.5f);
    }

    public void playRemovalSound() {
        level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4f, 0.5f);
    }

    public void dropItem(ItemStack stack) {
        Vec3 dropVec = VecHelper.getCenterOf(worldPosition).add(0, 0.3f, 0);
        ItemEntity dropped = new ItemEntity(level, dropVec.x, dropVec.y, dropVec.z, stack);
        dropped.setDefaultPickUpDelay();
        dropped.setDeltaMovement(0, 0.15f, 0);
        level.addFreshEntity(dropped);
    }

    public boolean updateEngineType(EngineType newType) {

        Direction updateDirection = getBlockState().getValue(HORIZONTAL_FACING);
        if (level.getBlockEntity(getBlockPos().relative(updateDirection.getOpposite())) instanceof EngineBlockEntity be) {
            return be.updateEngineType(newType);

        }

        for (EngineBlockEntity engine : engines) {
            engine.engineType = newType;
            engine.updateInventory();
        }

        //for (int i = 0; i <= length; i++) {
        //    BlockPos pos = getBlockPos().relative(updateDirection, i);
        //    if (level.getBlockEntity(pos) instanceof EngineBlockEntity be) {
        //        //be.type = EngineType.I;
        //        //  if (!be.pistonInventory.isEmpty())
        //        //      return false;
        //    }
        //}
        //for (int i = 0; i <= length; i++) {
        //    BlockPos pos = getBlockPos().relative(updateDirection, i);
        //    if (level.getBlockEntity(pos) instanceof EngineBlockEntity be) {
        //        be.engineType = newType;
        //        //be.updateInventory();
        //        //level.setBlockAndUpdate(pos, be.getBlockState().setValue(EXTENDED, newType == EngineType.I || newType == EngineType.U));
        //    }
        //}

        return true;
    }

    public void tankUpdated(FluidStack stack, boolean fuelTank) {

        if (fuelTank && stack.isEmpty()) {
            updateGeneratedRotation();
        }
        if (!fuelTank && stack.getAmount() == exhaustTank.getCapacity())
            updateGeneratedRotation();
        sendData();
        setChanged();
    }

    /// checks for all conditions the engines needs to work
    public boolean canWork() {


        boolean hasValidFuel = false;

        for (TagKey<Fluid> fluidTag : getSupportedFuels()) {
            if (fuelTank.getFluid().getFluid().is(fluidTag)) {
                hasValidFuel = true;
            }
        }


        for (EngineBlockEntity engine : engines) {

            if (level.getBlockEntity(engine.getBlockPos()) instanceof EngineBlockEntity be) {
                for (int i = 0; i < be.cylinderInventory.getSlots(); i++) {
                    if (be.cylinderInventory.getItem(i).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        if (!hasAllComponents()) {
            return false;
        }


        if (fuelTank.isEmpty() || exhaustTank.getSpace() == 0)
            return false;


        return true;
    }


    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("Components", componentsInventory.serializeNBT(registries));
        compound.put("FuelTank", fuelTank.writeToNBT(registries, new CompoundTag()));
        compound.put("ExhaustTank", exhaustTank.writeToNBT(registries, new CompoundTag()));
        compound.putInt("Oil", oil);
        compound.putInt("CoolingFluid", coolingFluid);
        compound.putString("Type", engineType.name);
        compound.put("Cylinders", cylinderInventory.serializeNBT(registries));

    }


    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        readElectricity(compound, clientPacket);
        fuelTank.readFromNBT(registries, compound.getCompound("FuelTank"));
        exhaustTank.readFromNBT(registries, compound.getCompound("ExhaustTank"));
        oil = compound.getInt("Oil");
        coolingFluid = compound.getInt("CoolingFluid");
        componentsInventory.deserializeNBT(registries, compound.getCompound("Components"));
        for (EngineType engineType : EngineType.values()) {
            if (engineType.name.matches(compound.getString("Type"))) {
                this.engineType = engineType;
                break;
            }
        }
        cylinderInventory.deserializeNBT(registries, compound.getCompound("Cylinders"));
    }

    public boolean isController() {
        return controller == getBlockPos();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        connectEngineNextTick = true;
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

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        TFMGTexts.header("engine")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        TFMGTexts.Engine.type(engineType.langKey).forGoggles(tooltip, 1);

        EngineBlockEntity controller = getControllerBE();

        if (controller.nextComponent() != Ingredient.EMPTY) {
            TFMGTexts.Engine.unfinished().forGoggles(tooltip);
            TFMGTexts.Engine.nextComponent(controller.nextComponent().getItems()[0]).forGoggles(tooltip);
            return true;
        }

        TFMGTexts.Engine.efficiency(engineType.effeciencyModifier).forGoggles(tooltip);
        TFMGTexts.Engine.fuelConsumption(getFuelConsumption()).forGoggles(tooltip);
        //TFMGTexts.Engine.rpm(rpm).forGoggles(tooltip);
        TFMGTexts.Engine.length(controller.length).forGoggles(tooltip);
        //TFMGTexts.Engine.torque(torque).forGoggles(tooltip);
        TFMGTexts.Engine.signal(controller.highestSignal).forGoggles(tooltip);
        //TFMGLang.number(engineNumber).style(ChatFormatting.DARK_GREEN).forGoggles(tooltip);
        //if (isController() && !nextComponent().isEmpty())
        //    TFMGLang.text(nextComponent().getItems()[0].getDisplayName().getString()).forGoggles(tooltip);

        TFMGUtils.createFluidTooltip(this, tooltip);

        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return false;
    }

    @Override
    public void onPlaced() {
        IElectric.super.onPlaced();
        connectEngineNextTick = true;


    }

    public boolean hasAllComponents() {

        if (level.getBlockEntity(controller) instanceof EngineBlockEntity be) {
            return be.nextComponent() == Ingredient.EMPTY;
        }

        return false;
    }

    @Override
    public void remove() {
        super.remove();
        disconnectEngines();

    }

    public EngineBlockEntity getControllerBE() {
        if (!engines.isEmpty())
            return engines.get(0);

        if (level.getBlockEntity(controller) instanceof EngineBlockEntity be)
            return be;

        return this;
    }


    public void disconnectEngines() {

        for (EngineBlockEntity engine : getControllerBE().engines) {
            TFMG.LOGGER.debug("meow");
            level.destroyBlock(engine.getBlockPos(), true);


        }
    }


    public void connectEngines() {

        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);

        if (level.getBlockEntity(getBlockPos().relative(facing.getOpposite())) instanceof EngineBlockEntity be) {
            be.connectEngines();
            return;
        }
        List<EngineBlockEntity> engines = new ArrayList<>();
        for (int i = 0; i < TFMGConfigs.common().machines.engineMaxLength.get(); i++) {
            if (level.getBlockEntity(getBlockPos().relative(facing, i)) instanceof EngineBlockEntity be && be.getBlockState().getValue(HORIZONTAL_FACING) == facing) {
                engines.add(be);
                be.controller = this.getBlockPos();
                if (i != 0) {
                    be.fluidCapability = fluidCapability;
                    be.invalidateCapabilities();
                }

            } else {
                length = engines.size();
                this.engines = engines;
                break;
            }
        }


    }


    @Override
    public void tick() {
        super.tick();

        if (connectEngineNextTick) {
            connectEngines();
            connectEngineNextTick = false;
        }
        tickElectricity();
    }

    /// removes fuel and creates exhaust when the engines is active
    public void handleRunning() {
        fuelTank.forceDrain(getFuelConsumption(), IFluidHandler.FluidAction.EXECUTE);

        drainFuel = false;
    }

    public int getFuelConsumption() {
        return (int) (cylinderInventory.getSlots() * length * engineType.effeciencyModifier * (TFMGConfigs.common().machines.engineFuelConsumption.getF() / 100f));
    }

    @Override
    public void lazyTick() {
        super.lazyTick();


        if (isController() && Math.abs(getGeneratedSpeed()) > 0 && drainFuel) {
            handleRunning();
        } else drainFuel = true;
        int newSignal = level.getBestNeighborSignal(getBlockPos());
        if (signal != newSignal) {
            signalChanged(newSignal);
        }
    }

    public void signalChanged(int newSignal) {
        signal = newSignal;
        getControllerBE().findHighestSignal();
    }

    public void findHighestSignal() {
        highestSignal = signal;
        engines.forEach(be -> {
            if (be.signal > highestSignal) {
                highestSignal = be.signal;
            }
        });

        updateGeneratedRotation();
    }

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public LevelAccessor getLevelAccessor() {
        return level;
    }

    @Override
    public ElectricBlockValues getData() {
        return data;
    }

    @Override
    public void sendStuff() {
        sendData();
    }

    public enum EngineType {
        I("engine_i", pistonsI(), commonRegularComponents(), 0.6f, 1.0f, 1.2f, true),
        V("engine_v", pistonsV(), commonRegularComponents(), 1.2f, 1.3f, 0.8f),
        W("engine_w", pistonsW(), commonRegularComponents(), 1.5f, 1.1f, 0.5f),
        U("engine_u", pistonsU(), commonRegularComponents(), 1.0f, 1.5f, 0.9f, true),
        BOXER("engine_boxer", pistonsBoxer(), commonRegularComponents(), 1.0f, 0.8f, 1.2f),
        RADIAL("radial", pistonsRadial(), commonRegularComponents(), 1.0f, 0.8f, 1.2f),
        TURBINE("turbine", pistonsTurbine(), commonRegularComponents(), 2f, 1.0f, 0.5f);
        public final float speedModifier;
        public final float torqueModifier;
        public final float effeciencyModifier;
        public final List<PistonPosition> pistons;
        public final List<Fluid> fluidBlacklist;
        public final String name;
        public final boolean upgradesOnSide;
        public final String langKey;
        public final List<Ingredient> ingredients;

        EngineType(String name, List<PistonPosition> positions, List<Ingredient> ingredients, float speedModifier,
                   float torqueModifier, float efficiencyModifier, boolean upgradesOnSide) {
            this(name, positions, ingredients, speedModifier, torqueModifier, efficiencyModifier, upgradesOnSide, new ArrayList<>());
        }

        EngineType(String name, List<PistonPosition> positions, List<Ingredient> ingredients, float speedModifier,
                   float torqueModifier, float efficiencyModifier) {
            this(name, positions, ingredients, speedModifier, torqueModifier, efficiencyModifier, false, new ArrayList<>());
        }

        EngineType(String name, List<PistonPosition> positions, List<Ingredient> ingredients, float speedModifier,
                   float torqueModifier, float efficiencyModifier, boolean upgradesOnSide, List<Fluid> fluidBlacklist) {
            this.name = name;
            this.pistons = positions;
            this.speedModifier = speedModifier;
            this.torqueModifier = torqueModifier;
            this.effeciencyModifier = efficiencyModifier;
            this.fluidBlacklist = fluidBlacklist;
            this.upgradesOnSide = upgradesOnSide;
            this.langKey = "engine.type." + name;
            this.ingredients = ingredients;

        }


    }
}
