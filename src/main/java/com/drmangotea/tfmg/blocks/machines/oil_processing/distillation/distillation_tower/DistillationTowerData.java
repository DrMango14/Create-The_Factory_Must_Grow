package com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillation_tower;


import com.drmangotea.tfmg.blocks.tanks.SteelTankBlock;
import com.drmangotea.tfmg.blocks.tanks.SteelTankBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.BoilerHeaters;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.foundation.utility.animation.LerpedFloat.Chaser;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.simibubi.create.foundation.fluid.FluidHelper.convertToStill;

public class DistillationTowerData {

    static final int SAMPLE_RATE = 1;

    private static final int waterSupplyPerLevel = 10;
    private static final float passiveEngineEfficiency = 0;

    private SteelTankBlockEntity tank;

    // pooled water supply
    int gatheredSupply;
    float[] supplyOverTime = new float[10];
    int ticksUntilNextSample;
    int currentIndex;

    // heat score
    public boolean needsHeatLevelUpdate;
    public boolean passiveHeat;
    public int activeHeat;

    public float oilSupply;
    public int attachedControllers;
    public int attachedWhistles;

    // display
    public int maxHeatForSize = 0;
    private int maxHeatForOil = 0;
    private int minValue = 0;
    private int maxValue = 0;

    public int towerLevel = Math.min(activeHeat,  maxHeatForSize);

    public LerpedFloat gauge = LerpedFloat.linear();

    public void tick(SteelTankBlockEntity controller) {
        towerLevel = Math.min(activeHeat,  maxHeatForSize);
        tank=controller;
       // tank.tankInventory.drain(2, IFluidHandler.FluidAction.EXECUTE);

        if (controller.getLevel().isClientSide) {
            gauge.tickChaser();
            float current = gauge.getValue(1);
            if (current > 1 && Create.RANDOM.nextFloat() < 1 / 2f)
                gauge.setValueNoUpdate(current + Math.min(-(current - 1) * Create.RANDOM.nextFloat(), 0));
            return;
        }

        if (needsHeatLevelUpdate && updateTemperature(controller))
            controller.notifyUpdate();
        ticksUntilNextSample--;
        if (ticksUntilNextSample > 0)
            return;
        int capacity = controller.tankInventory.getCapacity();
        if (capacity == 0)
            return;


        ticksUntilNextSample = SAMPLE_RATE;
        supplyOverTime[currentIndex] = gatheredSupply / (float) SAMPLE_RATE;
        oilSupply = Math.max(oilSupply, supplyOverTime[currentIndex]);
        currentIndex = (currentIndex + 1) % supplyOverTime.length;
        gatheredSupply = 0;


        if (currentIndex == 0) {
            oilSupply = 0;
            for (float i : supplyOverTime)
                oilSupply = Math.max(i, oilSupply);
        }

/*
        if (getActualHeat(controller.getTotalTankSize()) == 18)
            controller.award(AllAdvancements.STEAM_ENGINE_MAXED);
*/
        controller.notifyUpdate();

    }

    public int getTheoreticalHeatLevel() {
        return activeHeat;
    }

    public int getMaxHeatLevelForBoilerSize(int towerSize) {
        return (int) Math.min(12, towerSize / 4);
    }

    public int getMaxHeatLevelForOilSupply() {
        return (int) Math.min(12, Mth.ceil(oilSupply) / waterSupplyPerLevel);
    }

    public boolean isPassive() {
        return passiveHeat && maxHeatForSize > 0 && maxHeatForOil > 0;
    }

    public boolean isPassive(int towerSize) {
        calcMinMaxForSize(towerSize);
        return isPassive();
    }

    public float getTowerEfficiency(int towerSize) {
        if (isPassive(towerSize))
            return 0;
        if (activeHeat == 0)
            return 0;
        int actualHeat = getActualHeat(towerSize);
        return this.towerLevel;
    }

    public int getActualHeat(int towerSize) {
        int forBoilerSize = getMaxHeatLevelForBoilerSize(towerSize);
        int forWaterSupply = getMaxHeatLevelForOilSupply();
        int actualHeat = Math.min(activeHeat, Math.min(forWaterSupply, forBoilerSize));
        return actualHeat;
    }

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, int towerSize) {
        if (!isActive())
            return false;

        Component indent = Components.literal(IHaveGoggleInformation.spacing);
        Component indent2 = Components.literal(IHaveGoggleInformation.spacing + " ");

        calcMinMaxForSize(towerSize);

        tooltip.add(indent.plainCopy()
                .append(
                        Lang.translateDirect("distillation_tower.status", getHeatLevelTextComponent().withStyle(ChatFormatting.AQUA))));
        tooltip.add(indent2.plainCopy()
                .append(getSizeComponent(true, false)));
       // tooltip.add(indent2.plainCopy()
                //.append(getOilComponent(true, false)));
        tooltip.add(indent2.plainCopy()
                .append(getHeatComponent(true, false)));

        if (attachedControllers == 0)
            return true;

        int boilerLevel = Math.min(activeHeat,  maxHeatForSize);


        tooltip.add(Components.immutableEmpty());

        return true;
    }

    public void calcMinMaxForSize(int towerSize) {
        maxHeatForSize = getMaxHeatLevelForBoilerSize(towerSize);
        maxHeatForOil = getMaxHeatLevelForOilSupply();

        minValue = Math.min(passiveHeat ? 1 : activeHeat, Math.min(maxHeatForOil, maxHeatForSize));
        maxValue = Math.max(passiveHeat ? 1 : activeHeat, Math.max(maxHeatForOil, maxHeatForSize));
    }

    @NotNull
    public MutableComponent getHeatLevelTextComponent() {
        int boilerLevel = Math.min(activeHeat, Math.min(maxHeatForOil, maxHeatForSize));

        return isPassive() ? Lang.translateDirect("boiler.passive")
                : (boilerLevel == 0 ? Lang.translateDirect("boiler.idle")
                : boilerLevel == 18 ? Lang.translateDirect("boiler.max_lvl")
                : Lang.translateDirect("boiler.lvl", String.valueOf(boilerLevel)));
    }

    public MutableComponent getSizeComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return componentHelper("size", maxHeatForSize, forGoggles, useBlocksAsBars, styles);
    }
/*
    public MutableComponent getOilComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return componentHelper("crude_oil", maxHeatForOil, forGoggles, useBlocksAsBars, styles);
    }

 */

    public MutableComponent getHeatComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return componentHelper("heat", passiveHeat ? 1 : activeHeat, forGoggles, useBlocksAsBars, styles);
    }

    private MutableComponent componentHelper(String label, int level, boolean forGoggles, boolean useBlocksAsBars,
                                             ChatFormatting... styles) {
        MutableComponent base = useBlocksAsBars ? blockComponent(level) : barComponent(level);

        if (!forGoggles)
            return base;

        ChatFormatting style1 = styles.length >= 1 ? styles[0] : ChatFormatting.GRAY;
        ChatFormatting style2 = styles.length >= 2 ? styles[1] : ChatFormatting.DARK_GRAY;

        return Lang.translateDirect("distillation_tower." + label)
                .withStyle(style1)
                .append(Lang.translateDirect("boiler." + label + "_dots")
                        .withStyle(style2))
                .append(base);
    }

    private MutableComponent blockComponent(int level) {
        return Components.literal(
                "" + "\u2588".repeat(minValue) + "\u2592".repeat(level - minValue) + "\u2591".repeat(maxValue - level));
    }

    private MutableComponent barComponent(int level) {
        return Components.empty()
                .append(bars(Math.max(0, minValue - 1), ChatFormatting.DARK_GREEN))
                .append(bars(minValue > 0 ? 1 : 0, ChatFormatting.GREEN))
                .append(bars(Math.max(0, level - minValue), ChatFormatting.DARK_GREEN))
                .append(bars(Math.max(0, maxValue - level), ChatFormatting.DARK_RED))
                .append(bars(Math.max(0, Math.min(18 - maxValue, ((maxValue / 5 + 1) * 5) - maxValue)),
                        ChatFormatting.DARK_GRAY));

    }

    private MutableComponent bars(int level, ChatFormatting format) {
        return Components.literal(Strings.repeat('|', level)).withStyle(format);
    }

    public boolean evaluate(SteelTankBlockEntity controller) {
        BlockPos controllerPos = controller.getBlockPos();
        Level level = controller.getLevel();
        int prevEngines = attachedControllers;
        int prevWhistles = attachedWhistles;
        attachedControllers = 0;
        attachedWhistles = 0;

        for (int yOffset = 0; yOffset < controller.height; yOffset++) {
            for (int xOffset = 0; xOffset < controller.width; xOffset++) {
                for (int zOffset = 0; zOffset < controller.width; zOffset++) {

                    BlockPos pos = controllerPos.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (!SteelTankBlock.isTank(blockState))
                        continue;
                    for (Direction d : Iterate.directions) {
                        BlockPos attachedPos = pos.relative(d);
                        BlockState attachedState = level.getBlockState(attachedPos);
                       if (TFMGBlocks.STEEL_DISTILLATION_CONTROLLER.has(attachedState) && DistillationControllerBlock.getFacing(attachedState) == d)
                           attachedControllers++;

                    }
                }
            }
        }

        needsHeatLevelUpdate = true;
        return prevEngines != attachedControllers || prevWhistles != attachedWhistles;
    }



    public boolean updateTemperature(SteelTankBlockEntity controller) {
        BlockPos controllerPos = controller.getBlockPos();
        Level level = controller.getLevel();
        needsHeatLevelUpdate = false;

        boolean prevPassive = passiveHeat;
        int prevActive = activeHeat;
        passiveHeat = false;
        activeHeat = 0;

        for (int xOffset = 0; xOffset < controller.width; xOffset++) {
            for (int zOffset = 0; zOffset < controller.width; zOffset++) {
                BlockPos pos = controllerPos.offset(xOffset, -1, zOffset);
                BlockState blockState = level.getBlockState(pos);
                float heat = BoilerHeaters.getActiveHeat(level, pos, blockState);
                if (heat == 0) {
                    passiveHeat = true;
                } else if (heat > 0) {
                    activeHeat += heat;
                }
            }
        }

        passiveHeat &= activeHeat == 0;

        return prevActive != activeHeat || prevPassive != passiveHeat;
    }

    public boolean isActive() {
        return attachedControllers == 1;
    }

    public void clear() {
        oilSupply = 0;
        activeHeat = 0;
        passiveHeat = false;
        attachedControllers = 0;
        Arrays.fill(supplyOverTime, 0);
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("Supply", oilSupply);
        nbt.putInt("ActiveHeat", activeHeat);
        nbt.putBoolean("PassiveHeat", passiveHeat);
        nbt.putInt("Engines", attachedControllers);
        nbt.putInt("Whistles", attachedWhistles);
        nbt.putBoolean("Update", needsHeatLevelUpdate);
        return nbt;
    }

    public void read(CompoundTag nbt, int towerSize) {
        oilSupply = nbt.getFloat("Supply");
        activeHeat = nbt.getInt("ActiveHeat");
        passiveHeat = nbt.getBoolean("PassiveHeat");
        attachedControllers = nbt.getInt("Engines");
        attachedWhistles = nbt.getInt("Whistles");
        needsHeatLevelUpdate = nbt.getBoolean("Update");
        Arrays.fill(supplyOverTime, (int) oilSupply);

        int forBoilerSize = getMaxHeatLevelForBoilerSize(towerSize);
        int forOilSupply = getMaxHeatLevelForOilSupply();
        int actualHeat = Math.min(activeHeat, Math.min(forOilSupply, forBoilerSize));
        float target = isPassive(towerSize) ? 1 / 8f : forBoilerSize == 0 ? 0 : actualHeat / (forBoilerSize * 1f);
        gauge.chase(target, 0.125f, Chaser.EXP);
    }

    public DistillationFluidHandler createHandler() {
        return new DistillationFluidHandler();
    }

    public class DistillationFluidHandler implements IFluidHandler {

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return 10000;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return false;
        }
        public static boolean isOil(Fluid fluid) {
            return convertToStill(fluid) == TFMGFluids.CRUDE_OIL.get();
        }
        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (!isFluidValid(0, resource))
                return 0;
            int amount = resource.getAmount();
            if (action.execute())
                gatheredSupply += amount;
            return amount;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY;
        }

    }

}
