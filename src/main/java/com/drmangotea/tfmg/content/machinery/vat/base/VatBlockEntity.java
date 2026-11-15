package com.drmangotea.tfmg.content.machinery.vat.base;

import com.drmangotea.tfmg.base.TFMGBlockConnectivityHandler;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.capabilities.TFMGCapabilities;
import com.drmangotea.tfmg.base.capabilities.pressure.IPressureHandler;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.base.pressure.Pressure;
import com.drmangotea.tfmg.base.pressure.behaviour.SmartPressureTankBehaviour;
import com.drmangotea.tfmg.base.pressure.tank.SmartPressureTank;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperation;
import com.drmangotea.tfmg.content.machinery.vat.compressor.CompressorBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.freezer.FreezerBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.IndustrialMixerBlockEntity;
import com.drmangotea.tfmg.mixin.accessor.TankSegmentAccessor;
import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.drmangotea.tfmg.registry.TFMGVatOperations;
import com.drmangotea.tfmg.registry.TFMGVatTypes;
import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeConditions;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import joptsimple.internal.Strings;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.platform.CatnipServices;
import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

import static java.lang.Math.abs;

public class VatBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IMultiBlockEntityContainer.Fluid, Clearable {
    private static final int MAX_SIZE = 3;
    private static final int MAX_PRESSURE = 30;

    //item inventory
    public VatInventory inputInventory;
    public VatInventory outputInventory;
    //fluid inventory
    public SmartFluidTankBehaviour inputTank;
    public SmartFluidTankBehaviour outputTank;
    private final Couple<SmartFluidTankBehaviour> tanks;
    //pressure
    private SmartPressureTankBehaviour pressureTank;
    //capabilities
    protected IFluidHandler fluidCapability;
    protected IItemHandlerModifiable itemCapability;
    protected IPressureHandler pressureCapability;
    // comment here
    protected FilteringBehaviour filteringBehaviour;
    //rendering
    protected boolean forceFluidLevelUpdate;
    public LerpedFloat visualSpeed = LerpedFloat.linear();
    public float spinningAngle;
    protected int luminosity;
    //visual state data
    protected boolean window;
    protected int width;
    protected int height;
    //updating and technical stuff
    protected int recipeDuration;
    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    protected boolean updateConnectivity;
    protected boolean updateCapability;
    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;
    boolean evaluateNextTick = true;
    int timer = 0;
    public VatMachineRecipe recipe;
    //machines
    public Map<BlockPos, VatOperation> machineMap = new HashMap<>();
    public Map<BlockPos, Boolean> operationalMachinesMap = new HashMap<>();
    public boolean areMachinesValid = true;
    //processing data
    float efficiency = 1;
    int heatLevel = 0;
    Pressure pressure = Pressure.EMPTY;
    private static final Object vatRecipeKey = new Object();

    public VatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
        window = false;
        inputInventory = new VatInventory(4, this);
        outputInventory = new VatInventory(4, this);
        tanks = Couple.create(inputTank, outputTank);
        itemCapability = new CombinedInvWrapper(inputInventory, outputInventory);
        forceFluidLevelUpdate = true;
        updateConnectivity = false;
        updateCapability = false;
        height = 1;
        width = 1;
        refreshCapability();
    }

    public Couple<SmartFluidTankBehaviour> getTanks() {
        return tanks;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.CHEMICAL_VAT.get(),
                (be, context) -> be.getNewFluidCapability()
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TFMGBlockEntities.CHEMICAL_VAT.get(),
                (be, context) -> be.getNewItemCapability()
        );
        event.registerBlockEntity(
                TFMGCapabilities.PressureStorage.BLOCK,
                TFMGBlockEntities.CHEMICAL_VAT.get(),
                (be, context) -> be.getNewPressureCapability()
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 4, 4000, true)
                .whenFluidUpdates(this::onInventoryChanged)
                .forbidExtraction();
        outputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 4, 4000, true)
                .whenFluidUpdates(this::onInventoryChanged)
                .forbidInsertion();
        pressureTank = new SmartPressureTankBehaviour(SmartPressureTankBehaviour.INPUT, this, 1, MAX_PRESSURE)
                .whenPressureUpdates(this::onInventoryChanged)
                .forbidExtraction();
        filteringBehaviour = new FilteringBehaviour(this, new VatValueBox()).forRecipes();
        behaviours.add(inputTank);
        behaviours.add(outputTank);
        behaviours.add(pressureTank);
        behaviours.add(filteringBehaviour);

        pressureCapability = pressureTank.getCapability();
        fluidCapability = new CombinedTankWrapper(inputTank.getCapability(), outputTank.getCapability());
    }

    protected Object getRecipeCacheKey() {
        return vatRecipeKey;
    }

    protected void updateConnectivity() {
        updateConnectivity = false;
        if (level == null || level.isClientSide || !isController())
            return;
        TFMGBlockConnectivityHandler.formMulti(this);
    }

    public int getRecipeDuration() {
        return recipeDuration;
    }

    //goggle stuff
    public MutableComponent getHeatComponent(boolean forGoggles) {
        return componentHelper("heat", heatLevel, forGoggles);
    }

    public MutableComponent getPressureComponent(boolean forGoggles) {
        return componentHelper("pressure", pressure.getValue(), forGoggles);
    }

    private MutableComponent componentHelper(String label, int level, boolean forGoggles, ChatFormatting... styles) {
        MutableComponent base = barComponent(level);
        if (!forGoggles)
            return base;
        ChatFormatting style1 = styles.length >= 1 ? styles[0] : ChatFormatting.GRAY;
        ChatFormatting style2 = styles.length >= 2 ? styles[1] : ChatFormatting.DARK_GRAY;

        return CreateLang.translateDirect("vat." + label)
                .withStyle(style1)
                .append(CreateLang.translateDirect("vat." + label + "_dots").withStyle(style2))
                .append(base)
                .append("(" + level + ")");
    }

    private MutableComponent barComponent(int level) {
        // display
        int minValue = 0;
        ChatFormatting color = getColor(level, minValue);

        int maxValue = 0;
        return Component.empty()
                .append(bars(0, ChatFormatting.RED))
                .append(bars(0, ChatFormatting.GOLD))
                .append(bars(Math.max(0, level - minValue), color))
                .append(bars(Math.max(0, maxValue - level), ChatFormatting.BLUE))
                .append(bars(Math.min(18 - maxValue, 5 - maxValue), ChatFormatting.DARK_GRAY));
    }

    private static @NotNull ChatFormatting getColor(int level, int minValue) {
        ChatFormatting color = level - minValue > 19 ? ChatFormatting.DARK_RED : ChatFormatting.DARK_GREEN;
        color = switch (level - minValue) {
            case 1 -> ChatFormatting.BLUE;
            case 2, 3 -> ChatFormatting.DARK_AQUA;
            case 4, 5, 6, 8, 7 -> ChatFormatting.AQUA;
            case 11, 12, 13, 14, 15 -> ChatFormatting.YELLOW;
            case 16 -> ChatFormatting.GOLD;
            case 17, 18 -> ChatFormatting.RED;
            case 19 -> ChatFormatting.DARK_RED;
            default -> color;
        };
        return color;
    }

    private MutableComponent bars(int level, ChatFormatting format) {
        return Component.literal(Strings.repeat('|', level)).withStyle(format);
    }

    /// //////

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (recipe == null && isController()) {
            recipe = getMatchingRecipe();
        }
        if (isController())
            evaluateNextTick = true;

        revalidateMachines();
        updateTemperature();
        if (level != null && level.isClientSide && !(level instanceof PonderLevel)) {
            updateFluidLevel();

            float targetSpeed = 0;
            for (BlockPos pos : machineMap.keySet()) {
                if (!operationalMachinesMap.getOrDefault(pos, true))
                    continue;
                if (level.getBlockEntity(pos) instanceof IndustrialMixerBlockEntity mixer) {
                    if (mixer.getOperationId().is(TFMGVatOperations.MIXER.get()) || mixer.getOperationId().is(TFMGVatOperations.CENTRIFUGE.get())) {
                        targetSpeed += Math.abs(mixer.getSpeed());
                    }
                }
            }
            // Cap speed at 64 rpm so people can actually see items
            visualSpeed.chase(Math.min(targetSpeed, 64f) * .09f, .2f, LerpedFloat.Chaser.EXP);
        }
    }

    public void updateFluidLevel () {
        for (SmartFluidTankBehaviour tank : tanks) { tank.forEach(s -> {
            SmartFluidTank ft = ((TankSegmentAccessor) s).tfmg$tank();
            double percentage = (double) ft.getFluidAmount() / ft.getCapacity();
            s.getFluidLevel().chase(percentage, .5f, LerpedFloat.Chaser.EXP);
        }); }
    }

    public void updateTemperature() {
        if (!isController())
            return;

        int prevHeat = heatLevel;
        int prevPressure = pressure.getValue();
        heatLevel = 0;
        int newPressure = 0;
        BlockPos pos1 = controller == null ? getBlockPos() : controller;
        VatBlockEntity be = getControllerBE() == null ? this : getControllerBE();

        for (int xOffset = 0; xOffset < be.width; xOffset++) {
            for (int zOffset = 0; zOffset < be.width; zOffset++) {
                BlockPos pos = pos1.offset(xOffset, -1, zOffset);
                if (level != null) {
                    BlockState blockState = level.getBlockState(pos);
                    float heat = BoilerHeater.findHeat(level, pos, blockState);

                    if (heat > 0) {
                        heatLevel += (int) heat;
                    }
                }
            }
        }

        for (BlockPos machinePos : machineMap.keySet()) {
            if (!operationalMachinesMap.getOrDefault(machinePos, true))
                continue;
            if (level == null)
                continue;
            BlockEntity machineBe = level.getBlockEntity(machinePos);
            if (machineBe instanceof FreezerBlockEntity freezer && freezer.isOperational()) {
                heatLevel--;
            }
            if (machineBe instanceof CompressorBlockEntity compressor && compressor.getState() != CompressorBlockEntity.CompressorState.NOT_OPERATIONAL) {
                if (compressor.getState() == CompressorBlockEntity.CompressorState.PRESSURIZING)
                    newPressure++;
                if (compressor.getState() == CompressorBlockEntity.CompressorState.DEPRESSURIZING)
                    newPressure--;
            }
        }

        pressure = Pressure.of(newPressure);

        if (heatLevel != prevHeat || newPressure != prevPressure)
            notifyUpdate();
    }

    private void revalidateMachines() {
        if (!isController())
            return;
        for (BlockPos machinePos : machineMap.keySet()) {
            if (level == null || !level.isLoaded(machinePos))
                continue;
            BlockEntity blockEntity = level.getBlockEntity(machinePos);
            if (blockEntity instanceof IVatMachine vatMachine) {
                VatOperation operationId = vatMachine.getOperationId();
                if (operationId.isNone()) {
                    operationalMachinesMap.remove(machinePos);
                    continue;
                }
                if (!operationId.is(machineMap.get(machinePos))) {
                    machineMap.put(machinePos, operationId);
                    recipe = null;
                    notifyUpdate();
                }
                operationalMachinesMap.put(machinePos, vatMachine.canOperate(this));
            } else {
                operationalMachinesMap.remove(machinePos);
            }
            boolean allValid = true;
            for (boolean op : operationalMachinesMap.values()) {
                if (!op) {
                    allValid = false;
                    break;
                }
            }
            areMachinesValid = allValid;
        }
    }

    /**
     * finds a recipe with matching inputs and machines connected
     */
    public VatMachineRecipe getMatchingRecipe() {
        List<RecipeHolder<? extends Recipe<?>>> list = RecipeFinder.get(getRecipeCacheKey(), level, RecipeConditions.isOfType(TFMGRecipeTypes.VAT.getType()));

        for (RecipeHolder<? extends Recipe<?>> recipe1 : list) {
            VatMachineRecipe testedRecipe = (VatMachineRecipe) recipe1.value();
            if (getTotalTankSize() < testedRecipe.minSize || !matchesFilter(testedRecipe)) {
                continue;
            }

            boolean doesntMatch = false;

            List<VatOperation> activeMachines = new ArrayList<>(machineMap.values());
            boolean machinesOk = true;
            for (VatOperation requiredMachine : testedRecipe.machines) {
                if (!activeMachines.remove(requiredMachine)) {
                    machinesOk = false;
                    break;
                }
            }
            if (!machinesOk
                    || !areMachinesValid
                    || (!testedRecipe.allowedVatTypes.isEmpty() && !testedRecipe.allowedVatTypes.contains(((VatBlock) getBlockState().getBlock()).getVatType()))) {
                continue;
            }

            IFluidHandler fluidHandler = inputTank.getCapability();

            //checks if vat contains needed fluids
            Map<Integer, Integer> isFluidFound = new HashMap<>();
            for (int i = 0; i < testedRecipe.getFluidIngredients().size(); i++) {
                SizedFluidIngredient ingredient = testedRecipe.getFluidIngredients().get(i);
                Integer foundAt = null;
                if (ingredient.getFluids().length == 0)
                    break;

                for (int y = 0; y < fluidHandler.getTanks(); y++) {
                    if (isFluidFound.containsValue(y))
                        continue;
                    FluidStack stack = fluidHandler.getFluidInTank(y);
                    if (ingredient.test(stack)) {
                        foundAt = y;
                        break;
                    }
                }
                if (foundAt != null) {
                    isFluidFound.put(i, foundAt);
                } else doesntMatch = true;
            }

            //same but with items
            SmartInventory testInventory = new SmartInventory(4, this);
            for (int i = 0; i < 4; i++) {
                testInventory.setStackInSlot(i, inputInventory.getStackInSlot(i).copy());
            }

            for (int i = 0; i < testedRecipe.getIngredients().size(); i++) {
                Ingredient ingredient = testedRecipe.getIngredients().get(i);
                boolean found = false;
                for (int y = 0; y < 4; y++) {
                    ItemStack stack = testInventory.getStackInSlot(y).copy();
                    if (ingredient.test(stack)) {
                        found = true;
                        testInventory.getItem(y).shrink(1);
                        break;
                    }
                }
                if (!found) {
                    doesntMatch = true;
                    break;
                }
            }


            //////////////////////////////////////////
            if (doesntMatch || !canFitAllOutputs(testedRecipe))
                continue;
            ///////////////////////////////////////

            return testedRecipe;
        }

        return null;
    }

    private boolean matchesFilter(VatMachineRecipe recipe) {
        if (filteringBehaviour == null || filteringBehaviour.getFilter().isEmpty())
            return true;

        for (ProcessingOutput output : recipe.getRollableResults()) {
            if (filteringBehaviour.test(output.getStack())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void tick() {
        super.tick();
        handleRecipe();

        if (isController() && level != null) {
            Iterator<BlockPos> iter = machineMap.keySet().iterator();
            while (iter.hasNext()) {
                BlockPos machinePos = iter.next();
                BlockEntity blockEntity = level.getBlockEntity(machinePos);
                if (blockEntity != null) {
                    if (blockEntity instanceof IVatMachine vatMachine) {
                        boolean operational = vatMachine.canOperate(this);
                        operationalMachinesMap.put(machinePos, operational);
                    } else {
                        iter.remove();
                        operationalMachinesMap.remove(machinePos);
                    }
                } else {
                    iter.remove();
                    operationalMachinesMap.remove(machinePos);
                }
            }
        }

        areMachinesValid = operationalMachinesMap.values().stream().allMatch((op) -> op == true);

        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }
        if (evaluateNextTick) {
            if (level instanceof ServerLevel serverLevel)
                CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(worldPosition), new VatEvaluationPacket(this.getBlockPos()));
            evaluate();
            sendData();
            evaluateNextTick = false;
        }

        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition)) {
            onPositionChanged();
            return;
        }

        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
        if (updateConnectivity)
            updateConnectivity();
        if (forceFluidLevelUpdate) updateFluidLevel();
        visualSpeed.tickChaser();

    }

    /**
     * performs recipe,
     * ticks the processing timer
     */
    public void handleRecipe() {
        if (level == null || recipe == null || !isController() ||
                level.isClientSide && !isVirtual() ||
                recipe.heatLevel * (recipe.heatLevel - heatLevel) > 0 ||
                recipe.pressure.getValue() * (recipe.pressure.getValue() - pressure.getValue()) > 0)
            return;

        if (timer >= recipe.getProcessingDuration()) {
            if (!canFitAllOutputs(recipe)) {
                return;
            }

            VatMachineRecipe activeRecipe = recipe;

            SmartFluidTankBehaviour.TankSegment[] inputs = inputTank.getTanks();
            for (SizedFluidIngredient ingredient : activeRecipe.getFluidIngredients()) {
                int remaining = ingredient.amount();
                for (SmartFluidTankBehaviour.TankSegment segment : inputs) {
                    if (remaining <= 0)
                        break;
                    SmartFluidTank tank = ((TankSegmentAccessor) segment).tfmg$tank();
                    FluidStack fluidInTank = tank.getFluid();
                    if (fluidInTank.isEmpty())
                        continue;
                    if (!ingredient.test(fluidInTank))
                        continue;
                    FluidStack drained = tank.drain(new FluidStack(fluidInTank.getFluidHolder(), remaining), IFluidHandler.FluidAction.EXECUTE);
                    remaining -= drained.getAmount();
                }
            }

            //item output
            List<ItemStack> recovered = new ArrayList<>();
            for (ProcessingOutput output : activeRecipe.getRollableResults()) {
                ItemStack itemStack = output.rollOutput(level.random);

                if (isItemAlsoAnIngredient(activeRecipe, itemStack)) {
                    recovered.add(itemStack);
                    continue;
                }

                boolean handled = false;
                for (int i = 0; i < outputInventory.getSlots(); i++) {
                    ItemStack stackInSlot = outputInventory.getStackInSlot(i);
                    if (stackInSlot.isEmpty())
                        continue;
                    if (ItemStack.isSameItemSameComponents(stackInSlot, itemStack) && stackInSlot.getCount() + itemStack.getCount() <= stackInSlot.getMaxStackSize()) {
                        stackInSlot.setCount(stackInSlot.getCount() + itemStack.getCount());
                        handled = true;
                        break;
                    }
                }
                if (handled)
                    continue;
                for (int i = 0; i < outputInventory.getSlots(); i++) {
                    ItemStack itemInSlot = outputInventory.getStackInSlot(i);
                    if (itemInSlot.isEmpty()) {
                        outputInventory.setStackInSlot(i, itemStack);
                        break;
                    }
                }
            }

            //item input
            for (Ingredient ingredient : activeRecipe.getIngredients()) {
                int needed = ingredient.getItems().length > 0 ? ingredient.getItems()[0].getCount() : 1;
                for (int i = 0; i < inputInventory.getSlots(); i++) {
                    ItemStack stackInInv = inputInventory.getStackInSlot(i);
                    if (stackInInv.isEmpty())
                        continue;
                    if (ingredient.test(stackInInv) && stackInInv.getCount() >= needed) {
                        inputInventory.extractItem(i, needed, false);
                        break;
                    }
                }
            }

            for (ItemStack back : recovered) {
                if (returnToInput(back))
                    continue;
                for (int i = 0; i < outputInventory.getSlots(); i++) {
                    ItemStack inSlot = outputInventory.getStackInSlot(i);
                    if (!inSlot.isEmpty() && inSlot.is(back.getItem())
                            && inSlot.getCount() + back.getCount() <= inSlot.getMaxStackSize()) {
                        inSlot.setCount(inSlot.getCount() + back.getCount());
                        back = ItemStack.EMPTY;
                        break;
                    }
                }
                if (back.isEmpty())
                    continue;
                for (int i = 0; i < outputInventory.getSlots(); i++) {
                    if (outputInventory.getStackInSlot(i).isEmpty()) {
                        outputInventory.setStackInSlot(i, back);
                        break;
                    }
                }
            }

            //fluid output
            SmartFluidTankBehaviour.TankSegment[] outputs = outputTank.getTanks();
            for (FluidStack fluidStack : activeRecipe.getFluidResults()) {
                if (fluidStack.isEmpty())
                    continue;
                int remaining = fluidStack.getAmount();
                for (SmartFluidTankBehaviour.TankSegment segment : outputs) {
                    if (remaining <= 0)
                        break;
                    SmartFluidTank tank = ((TankSegmentAccessor) segment).tfmg$tank();
                    FluidStack fluidInTank = tank.getFluid();
                    if (fluidInTank.isEmpty() || !fluidInTank.getFluid().isSame(fluidStack.getFluid()))
                        continue;
                    int filled = tank.fill(new FluidStack(fluidStack.getFluid(), remaining), IFluidHandler.FluidAction.EXECUTE);
                    remaining -= filled;
                }
                for (SmartFluidTankBehaviour.TankSegment segment : outputs) {
                    if (remaining <= 0)
                        break;
                    SmartFluidTank tank = ((TankSegmentAccessor) segment).tfmg$tank();
                    if (!tank.getFluid().isEmpty())
                        continue;
                    int filled = tank.fill(new FluidStack(fluidStack.getFluid(), remaining), IFluidHandler.FluidAction.EXECUTE);
                    remaining -= filled;
                }
            }
            recipe = null;
            timer = 0;
        } else {
            timer++;
        }
    }

    private static boolean isItemAlsoAnIngredient(VatMachineRecipe recipe, ItemStack resultStack) {
        if (resultStack.isEmpty())
            return false;
        for (Ingredient ingredient : recipe.getIngredients())
            if (ingredient.test(resultStack))
                return true;
        return false;
    }

    private boolean returnToInput(ItemStack stack) {
        for (int i = 0; i < inputInventory.getSlots(); i++) {
            ItemStack inSlot = inputInventory.getStackInSlot(i);
            if (inSlot.isEmpty())
                continue;
            if (!ItemStack.isSameItemSameComponents(inSlot, stack))
                continue;
            if (inSlot.getCount() + stack.getCount() > inSlot.getMaxStackSize())
                continue;
            inSlot.setCount(inSlot.getCount() + stack.getCount());
            return true;
        }
        for (int i = 0; i < inputInventory.getSlots(); i++) {
            if (inputInventory.getStackInSlot(i).isEmpty()) {
                inputInventory.setStackInSlot(i, stack);
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean canFitAllOutputs(VatMachineRecipe recipe) {
        int slots = outputInventory.getSlots();
        int[] simCount = new int[slots];
        ItemStack[] simStack = new ItemStack[slots];
        for (int i = 0; i < slots; i++) {
            ItemStack s = outputInventory.getStackInSlot(i);
            simStack[i] = s.copy();
            simCount[i] = s.getCount();
        }
        for (ProcessingOutput out : recipe.getRollableResults()) {
            ItemStack stack = out.getStack();
            if (stack.isEmpty())
                continue;
            int needed = stack.getCount();
            int placed = -1;
            for (int i = 0; i < slots; i++) {
                if (simCount[i] == 0 || !ItemStack.isSameItemSameComponents(simStack[i], stack))
                    continue;
                int max = simStack[i].getMaxStackSize();
                if (simCount[i] + needed > max)
                    continue;
                simCount[i] += needed;
                placed = i;
                break;
            }
            if (placed >= 0)
                continue;
            for (int i = 0; i < slots; i++) {
                if (simCount[i] == 0) {
                    simStack[i] = stack.copy();
                    simCount[i] = needed;
                    placed = i;
                    break;
                }
            }
            if (placed < 0)
                return false;
        }
        SmartFluidTankBehaviour.TankSegment[] outputTanks = outputTank.getTanks();
        FluidStack[] simFluid = new FluidStack[outputTanks.length];
        int[] simFluidCap = new int[outputTanks.length];
        for (int i = 0; i < outputTanks.length; i++) {
            SmartFluidTank output = ((TankSegmentAccessor) outputTanks[i]).tfmg$tank();
            simFluid[i] = output.getFluid().copy();
            simFluidCap[i] = output.getCapacity() - output.getFluidAmount();
        }
        for (FluidStack fluidResult : recipe.getFluidResults()) {
            if (fluidResult.isEmpty())
                continue;
            int remaining = fluidResult.getAmount();
            for (int i = 0; i < outputTanks.length && remaining > 0; i++) {
                if (simFluid[i].isEmpty())
                    continue;
                if (!simFluid[i].getFluid().isSame(fluidResult.getFluid()))
                    continue;
                int take = Math.min(simFluidCap[i], remaining);
                if (take <= 0)
                    continue;
                simFluidCap[i] -= take;
                remaining -= take;
            }
            for (int i = 0; i < outputTanks.length && remaining > 0; i++) {
                if (!simFluid[i].isEmpty())
                    continue;
                int take = Math.min(simFluidCap[i], remaining);
                if (take <= 0)
                    continue;
                simFluid[i] = new FluidStack(fluidResult.getFluid(), take);
                simFluidCap[i] -= take;
                remaining -= take;
            }
            if (remaining > 0)
                return false;
        }
        return true;
    }

    @Override
    public BlockPos getLastKnownPos() {
        return lastKnownPos;
    }

    @Override
    public boolean isController() {
        return controller == null || worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY() && worldPosition.getZ() == controller.getZ();
    }

    @Override
    public void initialize() {
        super.initialize();
        sendData();
        if (level == null)
            return;
        if (level.isClientSide)
            invalidateRenderBoundingBox();
        if (!level.isClientSide)
            evaluateNextTick = true;
    }

    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
    }

    public void notifyItemContentsChanged() {
        if (level == null)
            return;

        VatBlockEntity controller = getControllerBE();
        if (controller != null && controller != this) {
            controller.notifyItemContentsChanged();
            return;
        }

        recipe = getMatchingRecipe();
        if (!level.isClientSide) {
            setChanged();
            sendData();
        }
    }

    protected void onInventoryChanged() {
        if (level == null)
            return;

        recipe = getMatchingRecipe();
        FluidStack newFluidStack = inputTank.getPrimaryHandler().getFluid();
        FluidType attributes = newFluidStack.getFluid().getFluidType();
        int luminosity = (int) (attributes.getLightLevel(newFluidStack) / 1.2f);
        boolean reversed = attributes.isLighterThanAir();
        int maxY = (int) ((getFillState() * height) + 1);

        for (int yOffset = 0; yOffset < height; yOffset++) {
            boolean isBright = reversed ? (height - yOffset <= maxY) : (yOffset < maxY);
            int actualLuminosity = isBright ? luminosity : luminosity > 0 ? 1 : 0;

            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {
                    BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
                    VatBlockEntity vatAt = TFMGBlockConnectivityHandler.partAt(getType(), level, pos);
                    if (vatAt == null)
                        continue;
                    level.updateNeighbourForOutputSignal(pos, vatAt.getBlockState()
                            .getBlock());
                    if (vatAt.luminosity == actualLuminosity)
                        continue;
                    vatAt.setLuminosity(actualLuminosity);
                }
            }
        }
        setChanged();
        sendData();
    }

    protected void setLuminosity(int luminosity) {
        if (level == null || level.isClientSide)
            return;
        if (this.luminosity == luminosity)
            return;
        this.luminosity = luminosity;
        sendData();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public VatBlockEntity getControllerBE() {
        if (level == null || isController()) return this;
        return level.getBlockEntity(controller) instanceof VatBlockEntity blockEntity ? blockEntity : null;
    }

    public void evaluate() {
        if (!isController()) {
            if (getControllerBE() == null) {
                return;
            }
            getControllerBE().evaluate();
            return;
        }
        if (level == null)
            return;

        Map<BlockPos, VatOperation> oldMachineMap = machineMap;
        machineMap = new HashMap<>();
        efficiency = 1;

        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                for (int yOffset = 0; yOffset < getHeight() + 2; yOffset++) {
                    BlockPos pos = getBlockPos().below().offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (VatBlock.isVat(blockState))
                        continue;
                    BlockEntity blockEntity = level.getBlockEntity(pos);

                    if (blockEntity instanceof IVatMachine be) {
                        if (be.getOperationId().equals(TFMGVatOperations.NONE.get()))
                            continue;

                        if (!isAtValidLocation(be.getPositionRequirement(), pos))
                            continue;

                        be.vatUpdated(this);
                        machineMap.put(pos, be.getOperationId());
                        efficiency *= ((float) be.getWorkPercentage() / 100);
                    }
                }
            }
        }
        if (!oldMachineMap.equals(machineMap))
            recipe = null;

        notifyUpdate();
    }

    public int getTotalCapacity() {
        int totalCapacity = 0;
        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                totalCapacity += ((TankSegmentAccessor) tankSegment).tfmg$tank().getCapacity();
            }
        }
        return totalCapacity;
    }

    public float getTotalFluidUnits(float partialTicks) {
        int renderedFluids = 0;
        float totalUnits = 0;

        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.getRenderedFluid()
                        .isEmpty())
                    continue;
                float units = tankSegment.getTotalUnits(partialTicks);
                if (units < 1)
                    continue;
                totalUnits += units;
                renderedFluids++;
            }
        }

        if (renderedFluids == 0)
            return 0;
        if (totalUnits < 1)
            return 0;
        return totalUnits;
    }

    public boolean isAtValidLocation(IVatMachine.PositionRequirement requirement, BlockPos pos) {
        return switch (requirement) {
            case ANY -> true;
            case BOTTOM -> pos.getY() == getController().getY() - 1;
            case TOP -> pos.getY() == getController().getY() + height;
            case ANY_CENTER -> isAtCenter(pos);
            case BOTTOM_CENTER -> isAtCenter(pos) && pos.getY() == getController().getY() - 1;
            case TOP_CENTER -> isAtCenter(pos) && pos.getY() == getController().getY() + height;
        };
    }


    public boolean isAtCenter(BlockPos pos) {
        return width < 3 || (pos.getX() == getController().getX() + 1 && pos.getZ() == getController().getZ() + 1);
    }

    public void applyVatSize(int blocks) {
        inputTank.forEach(s -> {
            SmartFluidTank tank = ((TankSegmentAccessor) s).tfmg$tank();
            tank.setCapacity(blocks * getCapacityMultiplier());
            int overflow = tank.getFluidAmount() - tank.getCapacity();
            if (overflow > 0)
                tank.drain(overflow, IFluidHandler.FluidAction.EXECUTE);
        });

        outputTank.forEach(s -> {
            SmartFluidTank tank = ((TankSegmentAccessor) s).tfmg$tank();
            tank.setCapacity(blocks * getCapacityMultiplier());
            int overflow = tank.getFluidAmount() - tank.getCapacity();
            if (overflow > 0)
                tank.drain(overflow, IFluidHandler.FluidAction.EXECUTE);
        });

        pressureTank.forEach(s -> {
            SmartPressureTank tank = s.getTank();
            tank.setCapacity(blocks * getCapacityMultiplier());
            int overflow = tank.getPressure().getValue() - tank.getCapacity();
            if (overflow > 0)
                tank.drain(overflow, false);
        });

        forceFluidLevelUpdate = true;
        evaluateNextTick = true;
    }

    public void removeController(boolean keepFluids) {
        if (level == null) return;

        controller = null;
        width = 1;
        height = 1;

        BlockState state = getBlockState();
        if (VatBlock.isVat(state)) {
            state = state.setValue(VatBlock.BOTTOM, true);
            state = state.setValue(VatBlock.TOP, true);
            state = state.setValue(VatBlock.SHAPE, window ? VatBlock.Shape.WINDOW : VatBlock.Shape.PLAIN);
            level.setBlock(getBlockPos(), state, 22);
        }

        if (level.isClientSide) {
            return;
        }

        updateConnectivity = true;
        if (!keepFluids) {
            applyVatSize(1);
        }
        onInventoryChanged();

        evaluateNextTick = true;

        refreshCapability();
        setChanged();
        sendData();
    }

    public void toggleWindows() {
        VatBlockEntity be = getControllerBE();
        if (be == null)
            return;

        if (((VatBlock) getBlockState().getBlock()).getVatType().equals(TFMGVatTypes.FIREPROOF.get()))
            return;

        be.setWindows(!be.window);
    }

    @SuppressWarnings("unused")
    public void sendDataImmediately() {
        syncCooldown = 0;
        queuedSync = false;
        sendData();
    }

    @Override
    public void sendData() {
        if (syncCooldown > 0) {
            queuedSync = true;
            return;
        }
        super.sendData();
        queuedSync = false;
        syncCooldown = SYNC_RATE;
    }

    public void setWindows(boolean window) {
        if (level == null)
            return;
        this.window = window;
        for (int yOffset = 0; yOffset < height; yOffset++) {
            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {

                    BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (!VatBlock.isVat(blockState))
                        continue;

                    VatBlock.Shape shape = getShape(window, xOffset, zOffset);

                    level.setBlock(pos, blockState.setValue(VatBlock.SHAPE, shape), 22);
                    level.getChunkSource()
                            .getLightEngine()
                            .checkBlock(pos);
                }
            }
        }
    }

    private VatBlock.@NotNull Shape getShape(boolean window, int xOffset, int zOffset) {
        VatBlock.Shape shape = VatBlock.Shape.PLAIN;
        if (window) {
            // SIZE 1: Every tank has a window
            if (width == 1)
                shape = VatBlock.Shape.WINDOW;
            // SIZE 2: Every tank has a corner window
            if (width == 2)
                shape = xOffset == 0 ? zOffset == 0 ? VatBlock.Shape.WINDOW_NW : VatBlock.Shape.WINDOW_SW
                        : zOffset == 0 ? VatBlock.Shape.WINDOW_NE : VatBlock.Shape.WINDOW_SE;
            // SIZE 3: Tanks in the centre have a window
            if (width == 3 && abs(abs(xOffset) - abs(zOffset)) == 1)
                shape = VatBlock.Shape.WINDOW;
        }
        return shape;
    }

    @SuppressWarnings("unused")
    public void updateState() {
        if (level == null || !isController())
            return;
        for (int yOffset = 0; yOffset < height; yOffset++)
            for (int xOffset = 0; xOffset < width; xOffset++)
                for (int zOffset = 0; zOffset < width; zOffset++)
                    if (level.getBlockEntity(
                            worldPosition.offset(xOffset, yOffset, zOffset)) instanceof VatBlockEntity fbe)
                        fbe.refreshCapability();
    }

    @Override
    public void setController(BlockPos controller) {
        if (level == null || (level.isClientSide && !isVirtual()))
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;
        refreshCapability();
        setChanged();
        sendData();
    }

    /**
     * Used when the vat changes size
     * sets capabilities of all vat blocks to controller's inventory
     */
    private void refreshCapability() {
        fluidCapability = getNewFluidCapability();
        itemCapability = getNewItemCapability();
        pressureCapability = getNewPressureCapability();
        invalidateCapabilities();
    }

    /**
     * finds the new fitting fluid capability
     *
     * @return fluid capability of the vat's controller
     */
    private IFluidHandler getNewFluidCapability() {
        IFluidHandler outputHandler = outputTank.getCapability();
        IFluidHandler inputHandler = inputTank.getCapability();

        if (inputHandler == null || outputHandler == null)
            return fluidCapability;

        return isController() ? new CombinedTankWrapper(inputHandler, outputHandler)
                : getControllerBE() != null ? getControllerBE().getNewFluidCapability() : fluidCapability;
    }

    /**
     * finds the new fitting item capability
     *
     * @return item capability of the vat's controller
     */
    private IItemHandlerModifiable getNewItemCapability() {
        return isController() ? new CombinedInvWrapper(inputInventory, outputInventory)
                : getControllerBE() != null ? getControllerBE().getNewItemCapability() : itemCapability;
    }

    private IPressureHandler getNewPressureCapability() {
        return isController() ? pressureTank.getCapability()
                : getControllerBE() != null ? getControllerBE().getNewPressureCapability() : pressureCapability;
    }

    /**
     * @return vat's controller
     */
    @Override
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (isController() || controller == null) {
            ItemHelper.dropContents(level, worldPosition, itemCapability);
        }
    }

    @Override
    protected AABB createRenderBoundingBox() {
        if (isController())
            return super.createRenderBoundingBox().expandTowards(width - 1, height - 1, width - 1);
        else
            return super.createRenderBoundingBox();
    }

    public void addMachineTooltip(Map<VatOperation, Couple<Integer>> countedMachines, List<Component> tooltip) {
        for (Map.Entry<VatOperation, Couple<Integer>> entry : countedMachines.entrySet()) {
            VatOperation operationId = entry.getKey();
            LangBuilder operation = TFMGTexts.Vat.operation(operationId);
            Couple<Integer> counts = entry.getValue();
            boolean isOperational = counts.getSecond() > 0;
            if (!isOperational) {
                operation.add(TFMGTexts.Vat.notOperational());
            } else {
                operation.add(TFMGTexts.Vat.operational());
            }
            operation.space().add(TFMGTexts.Vat.count(counts.getFirst(), counts.getSecond()));
            operation.forGoggles(tooltip);
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (getControllerBE() == null)
            return false;
        if (!isController())
            return getControllerBE().addToGoggleTooltip(tooltip, isPlayerSneaking);

        TFMGTexts.header("vat").style(ChatFormatting.GRAY).forGoggles(tooltip);

        TFMGTexts.progress(timer, recipeDuration).forGoggles(tooltip, 1);

        CreateLang.builder().add(getPressureComponent(true)).forGoggles(tooltip, 1);
        CreateLang.builder().add(getHeatComponent(true)).forGoggles(tooltip, 1);

        TFMGTexts.Vat.attachments()
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        Map<VatOperation, Couple<Integer>> countedMachines = new HashMap<>();
        for (Map.Entry<BlockPos, VatOperation> machines : machineMap.entrySet()) {
            boolean operational = operationalMachinesMap.getOrDefault(machines.getKey(), true);
            countedMachines.compute(machines.getValue(), (k, v) -> v == null ? Couple.create(1, operational ? 1 : 0) : Couple.create(v.getFirst() + 1, v.getSecond() + (operational ? 1 : 0)));
            //addMachineTooltip(machines.getValue(), operational, tooltip);
        }
        addMachineTooltip(countedMachines, tooltip);

        TFMGUtils.createFluidTooltip(tooltip, fluidCapability);
        TFMGUtils.createItemTooltip(tooltip, itemCapability);
        return true;
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);

        BlockPos controllerBefore = controller;
        int prevSize = width;
        int prevHeight = height;
        int prevLum = luminosity;

        updateConnectivity = compound.contains("Uninitialized");
        luminosity = compound.getInt("Luminosity");
        controller = null;
        lastKnownPos = null;

        if (NbtUtils.readBlockPos(compound, "LastKnownPos").isPresent())
            lastKnownPos = NbtUtils.readBlockPos(compound, "LastKnownPos").get();
        if (NbtUtils.readBlockPos(compound, "Controller").isPresent())
            controller = NbtUtils.readBlockPos(compound, "Controller").get();

        if (isController()) {
            window = compound.getBoolean("Window");
            width = compound.getInt("Size");
            height = compound.getInt("Height");
            inputTank.forEach(s -> {
                SmartFluidTank tank = ((TankSegmentAccessor) s).tfmg$tank();
                tank.setCapacity(getTotalTankSize() * getCapacityMultiplier());
                if (tank.getSpace() < 0)
                    tank.drain(-tank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
            });
            outputTank.forEach(s -> {
                SmartFluidTank tank = ((TankSegmentAccessor) s).tfmg$tank();
                tank.setCapacity(getTotalTankSize() * getCapacityMultiplier());
                if (tank.getSpace() < 0)
                    tank.drain(-tank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
            });
            inputInventory.deserializeNBT(registries, compound.getCompound("InputItems"));
            outputInventory.deserializeNBT(registries, compound.getCompound("OutputItems"));
            timer = compound.getInt("Timer");
            heatLevel = compound.getInt("HeatLevel");
            recipeDuration = compound.getInt("RecipeDuration");
            pressure = Pressure.from(compound);
        }

        updateCapability = true;

        if (!clientPacket)
            return;

        boolean changeOfController = !Objects.equals(controllerBefore, controller);
        if (changeOfController || prevSize != width || prevHeight != height) {
            if (level != null)
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            if (isController()) {
                inputTank.forEach(s -> ((TankSegmentAccessor) s).tfmg$tank().setCapacity(getCapacityMultiplier() * getTotalTankSize()));
                outputTank.forEach(s -> ((TankSegmentAccessor) s).tfmg$tank().setCapacity(getCapacityMultiplier() * getTotalTankSize()));
            }
            invalidateRenderBoundingBox();
        }
        if (luminosity != prevLum && level != null)
            level.getChunkSource()
                    .getLightEngine()
                    .checkBlock(worldPosition);
    }

    public float getFillState() {
        int tVol = 0, tCap = 0;
        for (SmartFluidTankBehaviour behaviour : tanks) {
            for (SmartFluidTankBehaviour.TankSegment segment : behaviour.getTanks()) {
                SmartFluidTank tank = ((TankSegmentAccessor) segment).tfmg$tank();
                tVol += tank.getFluidAmount();
                tCap += tank.getCapacity();
            }
        }
        return (float) tVol / tCap;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if (updateConnectivity)
            compound.putBoolean("Uninitialized", true);

        if (lastKnownPos != null)
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
        if (isController()) {
            compound.putBoolean("Window", window);
            compound.putInt("Size", width);
            compound.putInt("Height", height);
            compound.put("InputItems", inputInventory.serializeNBT(registries));
            compound.put("OutputItems", outputInventory.serializeNBT(registries));
            compound.putInt("Timer", timer);
            compound.putInt("HeatLevel", heatLevel);
            compound.putInt("RecipeDuration", recipe != null ? recipe.getProcessingDuration() : 0);
            pressure.save(compound);
        } else {
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        }
        compound.putInt("Luminosity", luminosity);
        super.write(compound, registries, clientPacket);
    }

    public int getTotalTankSize() {
        return width * width * height;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }


    public static int getCapacityMultiplier() {
        return AllConfigs.server().fluids.fluidTankCapacity.get() * 1000;
    }

    public static int getMaxHeight() {
        return 10;
    }

    @Override
    public void preventConnectivityUpdate() {
        updateConnectivity = false;
    }

    @Override
    public void notifyMultiUpdated() {
        BlockState state = this.getBlockState();
        if (VatBlock.isVat(state)) {
            state = state.setValue(VatBlock.BOTTOM, getController().getY() == getBlockPos().getY());
            state = state.setValue(VatBlock.TOP, getController().getY() + height - 1 == getBlockPos().getY());
            if (level != null) {
                level.setBlock(getBlockPos(), state, 6);
            }
        }
        if (isController()) {
            setWindows(window);
        }
        evaluateNextTick = true;
        onInventoryChanged();
        setChanged();
    }

    @Override
    public void setExtraData(@Nullable Object data) {
        if (data instanceof Boolean bool)
            window = bool;
    }

    @Override
    @Nullable
    public Object getExtraData() {
        return window;
    }

    @Override
    public Object modifyExtraData(Object data) {
        if (data instanceof Boolean) {
            return window;
        }
        return data;
    }

    @Override
    public Direction.Axis getMainConnectionAxis() {
        return Direction.Axis.Y;
    }

    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        return longAxis == Direction.Axis.Y ? getMaxHeight() : getMaxWidth();
    }

    @Override
    public int getMaxWidth() {
        return MAX_SIZE;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public boolean hasTank() {
        return true;
    }

    @Override
    public int getTankSize(int tank) {
        return getCapacityMultiplier();
    }

    @Override
    public void setTankSize(int tank, int blocks) {
        applyVatSize(blocks);
    }

    @Override
    public IFluidTank getTank(int tank) {
        return new FluidTank(1);
    }

    @Override
    public FluidStack getFluid(int tank) {
        return inputTank.getPrimaryHandler().getFluid();
    }

    @Override
    public void clearContent() {
        inputInventory.clearContent();
        outputInventory.clearContent();
        pressure.setPressure(0);

        filteringBehaviour.setFilter(ItemStack.EMPTY);
    }

    class VatValueBox extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
			Direction facing = getSide();
			return VecHelper.voxelSpace(8, 12, 16.05)
				.add(
					0.5 * (facing.get2DDataValue() > 1 ? 1 - width : width - 1),
					0,
					facing.getAxisDirection() == Direction.AxisDirection.POSITIVE? width - 1 : 0
				);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return isController() && direction.getAxis().isHorizontal();
        }
    }
}
