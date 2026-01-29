package com.drmangotea.tfmg.content.machinery.vat.base;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.machinery.vat.compressor.CompressorBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.freezer.FreezerBlockEntity;
import com.drmangotea.tfmg.mixin.accessor.TankSegmentAccessor;
import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeConditions;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import joptsimple.internal.Strings;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.abs;

public class VatBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IMultiBlockEntityContainer.Fluid {

    private static final int MAX_SIZE = 3;

    //item inventory
    public VatInventory inputInventory;
    public VatInventory outputInventory;
    //fluid inventory
    public SmartFluidTankBehaviour inputTank;
    public SmartFluidTankBehaviour outputTank;
    private Couple<SmartFluidTankBehaviour> tanks;
    //capabilities
    protected IFluidHandler fluidCapability;
    protected IItemHandlerModifiable itemCapability;
    //rendering
    protected boolean forceFluidLevelUpdate;
    public LerpedFloat[] fluidLevel = new LerpedFloat[8];
    protected int luminosity;
    //visual state data
    protected boolean window;
    protected int width;
    protected int height;
    //updating and technical stuff
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
    public Map<BlockPos, String> machineMap = new HashMap<>();
    public Map<BlockPos, Boolean> operationalMachinesMap = new HashMap<>();
    public boolean areMachinesValid = true;
    //processing data
    float efficiency = 1;
    int heatLevel = 0;
    int pressure = 0;
    HeatCondition heatCondition = HeatCondition.NONE;
    private static final Object vatRecipeKey = new Object();
    // display
    private int minValue = 0;
    private int maxValue = 0;

    public VatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
        for (int i = 0; i < 8; i++) {
            fluidLevel[i] = LerpedFloat.linear();
        }
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
                (be, context) -> {
                    if (be.fluidCapability == null)
                        be.refreshCapability();
                    return be.fluidCapability;
                }
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TFMGBlockEntities.CHEMICAL_VAT.get(),
                (be, context) -> {
                    if (be.itemCapability == null)
                        be.refreshCapability();
                    return be.itemCapability;
                }
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 4, 4000, true)
                .whenFluidUpdates(this::onInventoryChanged);
        outputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 4, 4000, true)
                .whenFluidUpdates(this::onInventoryChanged)
                .forbidInsertion();
        behaviours.add(inputTank);
        behaviours.add(outputTank);

        fluidCapability = new CombinedTankWrapper(inputTank.getCapability(), outputTank.getCapability());
    }

    protected Object getRecipeCacheKey() {
        return vatRecipeKey;
    }

    protected void updateConnectivity() {
        updateConnectivity = false;
        if (level.isClientSide)
            return;
        if (!isController())
            return;
        ConnectivityHandler.formMulti(this);


    }

    //goggle stuff
    public MutableComponent getHeatComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return componentHelper("heat", 10 + heatLevel, forGoggles, useBlocksAsBars, styles);
    }

    public MutableComponent getPressureComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return componentHelper("pressure", 10 + pressure, forGoggles, useBlocksAsBars, styles);
    }

    private MutableComponent componentHelper(String label, int level, boolean forGoggles, boolean useBlocksAsBars,
                                             ChatFormatting... styles) {
        MutableComponent base = useBlocksAsBars ? blockComponent(level) : barComponent(level);

        if (!forGoggles)
            return base;

        ChatFormatting style1 = styles.length >= 1 ? styles[0] : ChatFormatting.GRAY;
        ChatFormatting style2 = styles.length >= 2 ? styles[1] : ChatFormatting.DARK_GRAY;

        return CreateLang.translateDirect("vat." + label)
                .withStyle(style1)
                .append(CreateLang.translateDirect("vat." + label + "_dots")
                        .withStyle(style2))
                .append(base)
                .append("("+level/10f+")");
    }

    private MutableComponent blockComponent(int level) {
        return Component.literal("" + "\u2588".repeat(minValue) + "\u2592".repeat(level - minValue) + "\u2591".repeat(maxValue - level));
    }

    private MutableComponent barComponent(int level) {
        ChatFormatting color = level - minValue >19 ? ChatFormatting.DARK_RED : ChatFormatting.DARK_GREEN;
        switch (level - minValue) {
            case 1: color = ChatFormatting.BLUE; break;
            case 2: color = ChatFormatting.DARK_AQUA; break;
            case 3: color = ChatFormatting.DARK_AQUA; break;
            case 4: color = ChatFormatting.AQUA; break;
            case 5: color = ChatFormatting.AQUA; break;
            case 6: color = ChatFormatting.AQUA; break;
            case 7: color = ChatFormatting.AQUA; break;
            case 8: color = ChatFormatting.AQUA; break;
            case 11: color = ChatFormatting.YELLOW; break;
            case 12: color = ChatFormatting.YELLOW; break;
            case 13: color = ChatFormatting.YELLOW; break;
            case 14: color = ChatFormatting.YELLOW; break;
            case 15: color = ChatFormatting.YELLOW; break;
            case 16: color = ChatFormatting.GOLD; break;
            case 17: color = ChatFormatting.RED; break;
            case 18: color = ChatFormatting.RED; break;
            case 19: color = ChatFormatting.DARK_RED; break;

        }


        return Component.empty()
                .append(bars(Math.max(0, minValue - 1), ChatFormatting.RED))
                .append(bars(minValue > 0 ? 1 : 0, ChatFormatting.GOLD))
                .append(bars(Math.max(0, level - minValue), color))
                .append(bars(Math.max(0, maxValue - level), ChatFormatting.BLUE))
                .append(bars(Math.max(0, Math.min(18 - maxValue, ((maxValue / 5 + 1) * 5) - maxValue)),
                        ChatFormatting.DARK_GRAY));


    }

    private MutableComponent bars(int level, ChatFormatting format) {
        return Component.literal(Strings.repeat('|', level))
                .withStyle(format);
    }

    /// //////

    @Override
    public void lazyTick() {
        super.lazyTick();

        if (recipe == null && isController()) {
            recipe = getMatchingRecipe();
        }

        updateTemperature();
        if (level.isClientSide && !(level instanceof PonderLevel)) {
            int tankNumber = 0;
            for (int i = 0; i < 8; i++) {
                IFluidHandler fluidHandler = fluidCapability;
                if (fluidHandler != null) {
                    fluidLevel[i].chase((double) (fluidHandler.getFluidInTank(tankNumber).getAmount()) / inputTank.getPrimaryHandler().getCapacity(), .5f, LerpedFloat.Chaser.EXP);
                    getFillState();
                    tankNumber++;
                }
            }
        }
    }

    public void updateTemperature() {

        if (!isController())
            return;

        int prevHeat = heatLevel;
        heatLevel = 0;
        pressure = 0;
        heatCondition = HeatCondition.NONE;
        BlockPos pos1 = controller == null ? getBlockPos() : controller;
        VatBlockEntity be = getControllerBE() == null ? this : getControllerBE();

        for (int xOffset = 0; xOffset < be.width; xOffset++) {
            for (int zOffset = 0; zOffset < be.width; zOffset++) {
                BlockPos pos = pos1.offset(xOffset, -1, zOffset);
                BlockState blockState = level.getBlockState(pos);
                float heat = BoilerHeater.findHeat(level, pos, blockState);

                if (heat > 0) {
                    heatLevel += (int) heat;
                }
                if (level.getBlockEntity(pos) instanceof FreezerBlockEntity freezer && freezer.isOperational()) {
                    heatLevel--;
                }
                if (level.getBlockEntity(pos) instanceof CompressorBlockEntity compressor && compressor.getState() != CompressorBlockEntity.CompressorState.NON_OPERATIONAL) {
                    if (compressor.getState() == CompressorBlockEntity.CompressorState.PRESSURIZING)
                        pressure++;
                    if (compressor.getState() == CompressorBlockEntity.CompressorState.DEPRESSURIZING)
                        pressure--;
                }
            }
        }
        if (heatLevel >= 2) {
            heatCondition = HeatCondition.HEATED;
        }
        if (heatLevel >= 4) {
            heatCondition = HeatCondition.SUPERHEATED;
        }
        if (heatLevel != prevHeat)
            notifyUpdate();
    }

    /**
     * finds a recipe with matching inputs and machines connected
     */
    public VatMachineRecipe getMatchingRecipe() {
        List<RecipeHolder<? extends Recipe<?>>> list = RecipeFinder.get(getRecipeCacheKey(), level, RecipeConditions.isOfType(TFMGRecipeTypes.VAT_MACHINE_RECIPE.getType()));

        for (RecipeHolder<? extends Recipe<?>> recipe1 : list) {
            VatMachineRecipe testedRecipe = (VatMachineRecipe) recipe1.value();
            if (getTotalTankSize() < testedRecipe.minSize)
                continue;
            boolean doesntMatch = false;

            if (!Objects.equals(testedRecipe.machines, machineMap.values().stream().toList())) {
                continue;
            }

            if (!areMachinesValid) {
                continue;
            }

            if (!testedRecipe.allowedVatTypes.contains(((VatBlock) getBlockState().getBlock()).vatType)) {
                continue;
            }

            IFluidHandler fluidHandler = fluidCapability;
            IItemHandler itemHandler = itemCapability;

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
            SmartInventory testInventory = new SmartInventory(8, this);

            for (int i = 0; i < 4; i++) {
                testInventory.setStackInSlot(i, inputInventory.getStackInSlot(i).copy());
            }
            for (int i = 0; i < 4; i++) {
                testInventory.setStackInSlot(i + 4, outputInventory.getStackInSlot(i).copy());
            }

            for (int i = 0; i < testedRecipe.getIngredients().size(); i++) {
                Ingredient ingredient = testedRecipe.getIngredients().get(i);
                boolean found = false;
                for (int y = 0; y < 8; y++) {
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
            if (doesntMatch)
                continue;
            //checks if there's enough space for a recipe to happen
            Map<net.minecraft.world.level.material.Fluid, Integer> fluids = new HashMap<>();

            List<FluidStack> totalFluidStacks = new ArrayList<>();

            for (int i = 0; i < outputTank.getPrimaryHandler().getTanks(); i++) {
                totalFluidStacks.add(outputTank.getPrimaryHandler().getFluidInTank(i));
            }
            totalFluidStacks.addAll(testedRecipe.getFluidResults());

            for (FluidStack stack : totalFluidStacks) {
                if (stack.isEmpty())
                    continue;
                if (fluids.containsKey(stack.getFluid())) {
                    fluids.replace(stack.getFluid(), fluids.get(stack.getFluid()) + stack.getAmount());
                } else fluids.put(stack.getFluid(), stack.getAmount());

            }
            AtomicBoolean cantOutput = new AtomicBoolean(false);
            fluids.forEach((f, a) -> {
                if (a > 4000)
                    cantOutput.set(true);
            });
            //
            Map<Item, Integer> items = new HashMap<>();

            List<ItemStack> totalItemStacks = new ArrayList<>();

            for (int i = 0; i < outputInventory.getSlots(); i++) {
                totalItemStacks.add(outputInventory.getStackInSlot(i));
            }
            totalItemStacks.addAll(testedRecipe.getRollableResultsAsItemStacks());

            for (ItemStack stack : totalItemStacks) {
                if (stack.isEmpty())
                    continue;
                if (items.containsKey(stack.getItem())) {
                    items.replace(stack.getItem(), items.get(stack.getItem()) + stack.getCount());
                } else items.put(stack.getItem(), stack.getCount());
//
            }
            items.forEach((f, a) -> {
                if (a > 64)
                    cantOutput.set(true);
            });
            if (cantOutput.get())
                continue;
            ///////////////////////////////////////

            return testedRecipe;
        }

        return null;
    }

    @Override
    public void tick() {
        super.tick();


        handleRecipe();

        if (isController()) {
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
            //  if (level instanceof ServerLevel serverLevel)
            //      CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(worldPosition),new VatEvaluationPacket(this.getBlockPos()));
            evaluate();
            sendData();
            evaluateNextTick = false;
        }

        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition) && worldPosition != null) {
            onPositionChanged();
            return;
        }

        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
        if (updateConnectivity)
            updateConnectivity();
        for (int i = 0; i < 8; i++) {
            fluidLevel[i].tickChaser();
        }

    }

    /**
     * performs recipe,
     * ticks the processing timer
     */
    public void handleRecipe() {

        if (recipe == null)
            return;
        if (!isController())
            return;
        if (heatLevel < recipe.heatLevel)
            return;
        if (recipe.getRequiredHeat() == HeatCondition.HEATED && heatCondition == HeatCondition.NONE)
            return;
        if (recipe.getRequiredHeat() == HeatCondition.SUPERHEATED && heatCondition != HeatCondition.SUPERHEATED)
            return;

        if (timer >= recipe.getProcessingDuration()) {


            SmartFluidTank outputFluidHandler = outputTank.getPrimaryHandler();
            IFluidHandler fluidHandler = fluidCapability;
            IItemHandler itemHandler = itemCapability;


            //fluid input
            for (SizedFluidIngredient ingredient : recipe.getFluidIngredients()) {
                for (int i = 0; i < fluidHandler.getTanks(); i++) {
                    FluidStack fluidInTank = fluidHandler.getFluidInTank(i);
                    if (ingredient.test(new FluidStack(fluidInTank.getFluidHolder(), 4000))) {
                        fluidHandler.getFluidInTank(i).setAmount(fluidInTank.getAmount() - ingredient.amount());
                        break;
                    }
                }
            }
            //item output

            for (ProcessingOutput output : recipe.getRollableResults()) {

                ItemStack itemStack = output.rollOutput(level.random);


                boolean handled = false;
                for (int i = 0; i < outputInventory.getSlots(); i++) {
                    ItemStack stackInSlot = outputInventory.getStackInSlot(i);
                    if (stackInSlot.isEmpty())
                        continue;

                    if (stackInSlot.is(itemStack.getItem())) {
                        outputInventory.getStackInSlot(i).setCount(stackInSlot.getCount() + (itemStack.getCount()));

                        handled = true;
                        break;
                    }
                }
                if (handled)
                    break;
                for (int i = 0; i < outputInventory.getSlots(); i++) {
                    ItemStack itemInSlot = outputInventory.getStackInSlot(i);
                    if (itemInSlot.isEmpty()) {
                        outputInventory.setStackInSlot(i, itemStack);
                        break;
                    }
                }
            }
            //item input
            if (recipe != null)
                for (Ingredient ingredient : recipe.getIngredients()) {
                    for (int i = 0; i < fluidHandler.getTanks(); i++) {
                        ItemStack stackInInv = itemHandler.getStackInSlot(i);
                        if (ingredient.test(new ItemStack(stackInInv.getItem(), 64))) {
                            stackInInv.setCount(stackInInv.getCount() - ingredient.getItems()[0].getCount());
                            break;
                        }
                    }
                }
            //fluid output


            List<FluidStack> handledFluidStacks = new ArrayList<>();
            List<SmartFluidTankBehaviour.TankSegment> tankSegments = List.of(outputTank.getTanks());
            if (recipe != null)
                for (FluidStack fluidStack : recipe.getFluidResults()) {
                    for (SmartFluidTankBehaviour.TankSegment tankSegment : tankSegments) {
                        SmartFluidTank tank = ((TankSegmentAccessor) tankSegment).tfmg$tank();
                        FluidStack fluidInTank = tank.getFluid();
                        if (handledFluidStacks.contains(fluidStack)) break;

                        if (fluidInTank.getFluid().isSame(fluidStack.getFluid())) {
                            tank.fill(new FluidStack(fluidStack.getFluid(), fluidStack.getAmount()), IFluidHandler.FluidAction.EXECUTE);
                            handledFluidStacks.add(fluidStack);
                            break;
                        }
                        if (!handledFluidStacks.contains(fluidStack) && fluidInTank.isEmpty()) {
                            tank.fill(new FluidStack(fluidStack.getFluid(), fluidStack.getAmount()), IFluidHandler.FluidAction.EXECUTE);
                            break;
                        }
                    }
                }
            recipe = null;
            timer = 0;
        } else {
            timer++;
        }
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
        if (level.isClientSide)
            invalidateRenderBoundingBox();
    }

    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
    }

    protected void onInventoryChanged() {


        if (!hasLevel())
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
                    VatBlockEntity vatAt = ConnectivityHandler.partAt(getType(), level, pos);
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

        if (!level.isClientSide) {
            setChanged();
            sendData();
        }

    }

    protected void setLuminosity(int luminosity) {
        if (level.isClientSide)
            return;
        if (this.luminosity == luminosity)
            return;
        this.luminosity = luminosity;
        sendData();
    }

    @SuppressWarnings("unchecked")
    @Override
    public VatBlockEntity getControllerBE() {
        if (isController())
            return this;
        BlockEntity blockEntity = level.getBlockEntity(controller);
        if (blockEntity instanceof VatBlockEntity)
            return (VatBlockEntity) blockEntity;
        return null;
    }

    public void evaluate() {

        if (!isController()) {
            if (getControllerBE() == null) {
                return;
            }
            getControllerBE().evaluate();
            return;
        }

        Map<BlockPos, String> oldMachineMap = machineMap;
        machineMap = new HashMap<>();
        heatLevel = 0;
        heatCondition = HeatCondition.NONE;


        float speed = 1;

        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                for (int yOffset = 0; yOffset < getHeight() + 2; yOffset++) {
                    BlockPos pos = getBlockPos().below().offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);

                    if (VatBlock.isVat(blockState))
                        continue;

                    BlockEntity blockEntity = level.getBlockEntity(pos);

                    if (blockEntity instanceof IVatMachine be) {
                        if (be.getOperationId().isEmpty())
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
        efficiency = speed;
        if (oldMachineMap != machineMap)
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

        forceFluidLevelUpdate = true;

        evaluateNextTick = true;
    }

    public void removeController(boolean keepFluids) {
        if (level.isClientSide)
            return;
        updateConnectivity = true;
        if (!keepFluids)
            applyVatSize(1);
        controller = null;
        width = 1;
        height = 1;
        onInventoryChanged();

        BlockState state = getBlockState();
        if (VatBlock.isVat(state)) {
            state = state.setValue(VatBlock.BOTTOM, true);
            state = state.setValue(VatBlock.TOP, true);
            state = state.setValue(VatBlock.SHAPE, window ? VatBlock.Shape.WINDOW : VatBlock.Shape.PLAIN);
            getLevel().setBlock(worldPosition, state, 22);
        }

        evaluateNextTick = true;

        refreshCapability();
        setChanged();
        sendData();
    }

    public void toggleWindows() {
        VatBlockEntity be = getControllerBE();
        if (be == null)
            return;

        if (((VatBlock) getBlockState().getBlock()).vatType == "tfmg:firebrick_lined_vat")
            return;

        be.setWindows(!be.window);
    }

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
        this.window = window;
        for (int yOffset = 0; yOffset < height; yOffset++) {
            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {

                    BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (!VatBlock.isVat(blockState))
                        continue;

                    VatBlock.Shape shape = VatBlock.Shape.PLAIN;
                    if (window) {
                        // SIZE 1: Every tank has a window
                        if (width == 1)
                            shape = VatBlock.Shape.WINDOW;
                        // SIZE 2: Every tank has a corner window
                        if (width == 2)
                            shape = xOffset == 0 ? zOffset == 0 ? VatBlock.Shape.WINDOW_NW : VatBlock.Shape.WINDOW_SW
                                    : zOffset == 0 ? VatBlock.Shape.WINDOW_NE : VatBlock.Shape.WINDOW_SE;
                        // SIZE 3: Tanks in the center have a window
                        if (width == 3 && abs(abs(xOffset) - abs(zOffset)) == 1)
                            shape = VatBlock.Shape.WINDOW;
                    }

                    level.setBlock(pos, blockState.setValue(VatBlock.SHAPE, shape), 22);
                    level.getChunkSource()
                            .getLightEngine()
                            .checkBlock(pos);
                }
            }
        }
    }

    public void updateState() {
        if (!isController())
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
        if (level.isClientSide && !isVirtual())
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

    /**
     * @return vat's controller
     */
    @Override
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        if (isController())
            return super.createRenderBoundingBox().expandTowards(width - 1, height - 1, width - 1);
        else
            return super.createRenderBoundingBox();
    }


    public void addMachineTooltip(String operationId, boolean isOperational, List<Component> tooltip) {
        LangBuilder operation = TFMGTexts.Vat.operation(operationId);
        if (!isOperational) {
            operation.add(TFMGTexts.Vat.notOperational());
        }
        operation.forGoggles(tooltip);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        if (getControllerBE() == null)
            return false;

        if (!isController())
            return getControllerBE().addToGoggleTooltip(tooltip, isPlayerSneaking);
        TFMGTexts.header("vat").style(ChatFormatting.GRAY)
                .forGoggles(tooltip);


        ;
        CreateLang.builder().add(getPressureComponent(true, false)).forGoggles(tooltip, 1);
        CreateLang.builder().add(getHeatComponent(true, false)).forGoggles(tooltip, 1);

        TFMGTexts.Vat.contents().forGoggles(tooltip);

        TFMGTexts.Vat.attachments()
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        for (Map.Entry<BlockPos, String> machines : machineMap.entrySet()) {
            boolean operational = operationalMachinesMap.getOrDefault(machines.getKey(), true);
            addMachineTooltip(machines.getValue(), operational, tooltip);
        }




        TFMGTexts.Vat.contents().forGoggles(tooltip);

        ///

        IItemHandlerModifiable items = itemCapability;
        IFluidHandler fluids = fluidCapability;
        boolean isEmpty = true;

        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stackInSlot = items.getStackInSlot(i);
            if (stackInSlot.isEmpty())
                continue;
            TFMGLang.text("")
                    .add(Component.translatable(stackInSlot.getDescriptionId())
                            .withStyle(ChatFormatting.GRAY))
                    .add(TFMGLang.text(" x" + stackInSlot.getCount())
                            .style(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);
            isEmpty = false;
        }

        LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");
        for (int i = 0; i < fluids.getTanks(); i++) {
            FluidStack fluidStack = fluids.getFluidInTank(i);
            if (fluidStack.isEmpty())
                continue;
            TFMGLang.text("")
                    .add(TFMGLang.fluidName(fluidStack)
                            .add(TFMGLang.text(" "))
                            .style(ChatFormatting.GRAY)
                            .add(TFMGLang.number(fluidStack.getAmount())
                                    .add(mb)
                                    .style(ChatFormatting.BLUE)))
                    .forGoggles(tooltip, 1);
            isEmpty = false;
        }

        if (isEmpty)
            tooltip.remove(0);

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
        }


        updateCapability = true;

        if (!clientPacket)
            return;

        boolean changeOfController = !Objects.equals(controllerBefore, controller);
        if (changeOfController || prevSize != width || prevHeight != height) {
            if (hasLevel())
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            if (isController()) {
                inputTank.forEach(s -> ((TankSegmentAccessor) s).tfmg$tank().setCapacity(getCapacityMultiplier() * getTotalTankSize()));
                outputTank.forEach(s -> ((TankSegmentAccessor) s).tfmg$tank().setCapacity(getCapacityMultiplier() * getTotalTankSize()));
            }
            invalidateRenderBoundingBox();
        }
        if (luminosity != prevLum && hasLevel())
            level.getChunkSource()
                    .getLightEngine()
                    .checkBlock(worldPosition);
    }

    public float getFillState() {
        IFluidHandler fluidHandler = fluidCapability;
        for (int i = 0; i < fluidHandler.getTanks(); i++)
            if (!fluidHandler.getFluidInTank(i).isEmpty())
                return (float) fluidHandler.getFluidInTank(i).getAmount() / fluidHandler.getTankCapacity(0);

        return 0;

    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if (updateConnectivity)
            compound.putBoolean("Uninitialized", true);

        if (lastKnownPos != null)
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
        if (!isController())
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        if (isController()) {
            compound.putBoolean("Window", window);
            compound.putInt("Size", width);
            compound.putInt("Height", height);
            compound.put("InputItems", inputInventory.serializeNBT(registries));
            compound.put("OutputItems", outputInventory.serializeNBT(registries));

        }
        compound.putInt("Luminosity", luminosity);
        super.write(compound, registries, clientPacket);

        if (!clientPacket)
            return;
        if (forceFluidLevelUpdate)
            compound.putBoolean("ForceFluidLevel", true);
        if (queuedSync)
            compound.putBoolean("LazySync", true);
        forceFluidLevelUpdate = false;
    }

    public int getTotalTankSize() {
        return width * width * height;
    }

    @Override
    public void invalidate() {
        super.invalidate();
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
            level.setBlock(getBlockPos(), state, 6);
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
        if (data instanceof Boolean)
            window = (boolean) data;
    }

    @Override
    @Nullable
    public Object getExtraData() {
        return window;
    }

    @Override
    public Object modifyExtraData(Object data) {
        if (data instanceof Boolean windows) {
            windows |= window;
            return windows;
        }
        return data;
    }

    @Override
    public Direction.Axis getMainConnectionAxis() {
        return Direction.Axis.Y;
    }

    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        if (longAxis == Direction.Axis.Y)
            return getMaxHeight();
        return getMaxWidth();
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

}
