package com.drmangotea.tfmg.content.engines.types.regular_engine;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.turbine_engine.TurbineEngineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGSoundEvents;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

import static com.drmangotea.tfmg.content.engines.base.EngineProperties.*;
import static com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlock.EXTENDED;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class RegularEngineBlockEntity extends AbstractSmallEngineBlockEntity {


    public EngineType type = getDefaultEngineType();

    public SmartInventory pistonInventory;

    List<TagKey<Fluid>> supportedFuels = new ArrayList<>();

    protected int soundTimer=0;

    boolean updateFuel = true;

    public RegularEngineBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        pistonInventory = createInventory();

    }

    public EngineType getDefaultEngineType() {
        return EngineType.I;
    }

    public void updateInventory() {
        pistonInventory = createInventory();
    }

    public SmartInventory createInventory() {
        return new SmartInventory(type.pistons.size(), this)
                .withMaxStackSize(1)
                .whenContentsChanged(this::onInventoryChanged)
                ;
    }


    private void onInventoryChanged(int integer) {
        refreshFuels();
        updateRotation();
        sendData();
        setChanged();
    }

    public void refreshFuels() {

        CompoundTag fuelsToAllow = pistonInventory.getItem(0).get(TFMGDataComponents.FUEL_TAGS);

        if(fuelsToAllow == null)
            return;

        List<TagKey<Fluid>> fuelsFound = new ArrayList<>();
        for (String key : fuelsToAllow.getAllKeys()) {

            String id = fuelsToAllow.getString(key);

            TagKey<Fluid> tag = FluidTags.create(ResourceLocation.fromNamespaceAndPath("c",id.replace("c:","")));

            fuelsFound.add(tag);
        }

        if (level.getBlockEntity(controller) instanceof RegularEngineBlockEntity be) {
            be.supportedFuels = new ArrayList<>(fuelsFound);

            for (Long position : be.engines) {
                BlockPos pos = BlockPos.of(position);
                if (level.getBlockEntity(pos) instanceof RegularEngineBlockEntity be1) {
                    be1.supportedFuels = new ArrayList<>(fuelsFound);
                }
            }
        }
    }

    @Override
    public List<TagKey<Fluid>> getSupportedFuels() {
        return supportedFuels;
    }



    @Override
    public boolean canWork() {


        if (level.getBlockEntity(controller) instanceof RegularEngineBlockEntity controller) {

            for (Long position : controller.getAllEngines()) {

                if (level.getBlockEntity(BlockPos.of(position)) instanceof RegularEngineBlockEntity be) {
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

    public boolean hasAllPistons(){
        for (Long position : getControllerBE().getAllEngines()) {

            if (level.getBlockEntity(BlockPos.of(position)) instanceof RegularEngineBlockEntity be) {
                for (int i = 0; i < be.pistonInventory.getSlots(); i++) {
                    if (be.pistonInventory.getItem(i).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return  true;
    }

    @Override
    public boolean insertItem(ItemStack itemStack, boolean shifting, Player player, InteractionHand hand) {


        if (itemStack.is(AllItems.EMPTY_SCHEMATIC.get())) {

            if(type == EngineType.RADIAL||type == EngineType.TURBINE)
                return false;

            boolean next = false;
            if (type == EngineType.BOXER) {
                if (level.getBlockEntity(controller) instanceof RegularEngineBlockEntity be)
                    be.updateEngineType(EngineType.I);
                AllSoundEvents.CONFIRM.play(level, null, getBlockPos(), 1, 1);
                return true;
            }
            for (EngineType engineType : EngineType.values()) {
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
        return itemStack.is(TFMGItems.ENGINE_CYLINDER.get())||itemStack.is(TFMGItems.SIMPLE_ENGINE_CYLINDER.get())||itemStack.is(TFMGItems.DIESEL_ENGINE_CYLINDER.get());
    }

    public boolean isCylinderSame(ItemStack stack) {

        if(stack.is(TFMGItems.TURBINE_BLADE.get()))
            return true;

        CompoundTag tag = stack.get(TFMGDataComponents.FUELS);


        if (level.getBlockEntity(controller) instanceof RegularEngineBlockEntity controller) {

            List<Long> engines = new ArrayList<>(controller.engines);
            engines.add(this.controller.asLong());


            for (int i = 0; i < controller.engineLength() + 1; i++) {
                BlockPos pos = BlockPos.of(engines.get(i));
                if (level.getBlockEntity(pos) instanceof RegularEngineBlockEntity be) {
                    for (int y = 0; y < be.pistonInventory.getSlots(); y++) {
                        if (!be.pistonInventory.getItem(y).is(TFMGItems.ENGINE_CYLINDER.get()))
                            continue;

                        CompoundTag tagInside = be.pistonInventory.getItem(y).get(TFMGDataComponents.FUELS);
                        if (!tagInside.toString().equals(tag.toString()))
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

        if (level.isClientSide)
            makeSound();


        if (updateFuel) {
            refreshFuels();
            updateFuel = false;
        }

    }

    @OnlyIn(Dist.CLIENT)
    private void makeSound(){
        soundTimer++;
        if(!isController())
            return;

        if(soundTimer>1/Math.min(6000,(rpm*0.0002)*pistonInventory.getSlots())) {


            soundTimer = 0;

            float randomPitch = (level.getRandom().nextFloat()-.5f)*0.05f;

            if (this instanceof TurbineEngineBlockEntity) {
                TFMGSoundEvents.ENGINE.playAt(level, worldPosition, 0.06f * TFMGConfigs.common().machines.engineLoudness.getF(), 1.5f, false);
            } else

                TFMGSoundEvents.ENGINE.playAt(level, worldPosition, 0.1f * TFMGConfigs.common().machines.engineLoudness.getF(), 0.7f+ randomPitch, false);
        }

    }

    public boolean updateEngineType(EngineType newType) {

        Direction updateDirection = getBlockState().getValue(HORIZONTAL_FACING);
        if (level.getBlockEntity(getBlockPos().relative(updateDirection)) instanceof RegularEngineBlockEntity be) {
            return be.updateEngineType(newType);
        }
        for (int i = 0; i <= engineLength(); i++) {
            BlockPos pos = getBlockPos().relative(updateDirection.getOpposite(), i);
            if (level.getBlockEntity(pos) instanceof RegularEngineBlockEntity be) {
                //be.type = EngineType.I;
                if (!be.pistonInventory.isEmpty())
                    return false;
            }
        }
        for (int i = 0; i <= engineLength(); i++) {
            BlockPos pos = getBlockPos().relative(updateDirection.getOpposite(), i);
            if (level.getBlockEntity(pos) instanceof RegularEngineBlockEntity be) {
                be.type = newType;
                be.updateInventory();
                level.setBlockAndUpdate(pos, be.getBlockState().setValue(EXTENDED, newType == EngineType.I || newType == EngineType.U));
            }
        }

        return true;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.putString("Type", type.name);
        compound.put("Cylinders", pistonInventory.serializeNBT(registries));
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);

        for (EngineType engineType : EngineType.values()) {
            if (engineType.name.matches(compound.getString("Type"))) {
                type = engineType;
                break;
            }
        }
        pistonInventory.deserializeNBT(registries,compound.getCompound("Cylinders"));
    }

    @Override
    public float efficiencyModifier() {
        return type.effeciencyModifier * getFuelType().getEfficiency() * getUpgradeEfficiencyModifier()*(TFMGConfigs.common().machines.engineFuelConsumption.getF()/100f);
    }

    @Override
    public float speedModifier() {
        return type.speedModifier * getFuelType().getSpeed() * getUpgradeSpeedModifier();
    }

    @Override
    public float torqueModifier() {
        return type.torqueModifier * getFuelType().getStress() * getUpgradeTorqueModifier();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if(!isController())
            return getControllerBE().addToGoggleTooltip(tooltip,isPlayerSneaking);

        TFMGTexts.header("engine")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        if(nextComponent()!= Ingredient.EMPTY){
            TFMGTexts.Engine.unfinished().forGoggles(tooltip);
            TFMGTexts.Engine.nextComponent(nextComponent().getItems()[0]).forGoggles(tooltip);
            TFMGTexts.Engine.type(type.langKey).forGoggles(tooltip, 1);
            return true;
        }
        if(!hasAllPistons()){
            TFMGTexts.Engine.lastRequirement(this instanceof TurbineEngineBlockEntity ? "turbines" : "pistons").forGoggles(tooltip);
            TFMGTexts.Engine.type(type.langKey).forGoggles(tooltip, 1);
            return true;
        }

        TFMGTexts.Engine.type(type.langKey).forGoggles(tooltip, 1);
        TFMGTexts.Engine.rpm(rpm).forGoggles(tooltip, 1);
        TFMGTexts.Engine.signal((int) (highestSignal*15)).forGoggles(tooltip, 1);
        TFMGTexts.Engine.torque(torque).forGoggles(tooltip, 1);
        TFMGTexts.Engine.fuelConsumption(getFuelConsumption()).forGoggles(tooltip, 1);
        if(oil>0){
            TFMGTexts.Engine.oil(oil).forGoggles(tooltip);
        }
        if(coolingFluid>0){
            TFMGTexts.Engine.coolingFluid(coolingFluid).forGoggles(tooltip);
        }

        TFMGUtils.createFluidTooltip(this,tooltip);

        return true;
    }


    public enum EngineType {
        I("engine_i", pistonsI(), 1, 1, 1, true),
        V("engine_v", pistonsV(), 1.2f, 1.3f, 0.8f),
        W("engine_w", pistonsW(), 1.3f, 1.1f, 0.5f),
        U("engine_u", pistonsU(), 1, 1.5f, 0.9f, true),
        BOXER("engine_boxer", pistonsBoxer(), 1, 0.8f, 1.2f),
        RADIAL("radial", pistonsRadial(), 1, 0.8f, 1.2f),
        TURBINE("turbine", pistonsTurbine(), 1.5f, 1.5f, 0.5f);
        public final float speedModifier;
        public final float torqueModifier;
        public final float effeciencyModifier;
        public final List<PistonPosition> pistons;
        public final List<Fluid> fluidBlacklist;
        public final String name;
        public final boolean upgradesOnSide;
        public final String langKey;

        EngineType(String name, List<PistonPosition> positions, float speedModifier,
                   float torqueModifier, float efficiencyModifier, boolean upgradesOnSide) {
            this(name, positions, speedModifier, torqueModifier, efficiencyModifier, upgradesOnSide, new ArrayList<>());
        }

        EngineType(String name, List<PistonPosition> positions, float speedModifier,
                   float torqueModifier, float efficiencyModifier) {
            this(name, positions, speedModifier, torqueModifier, efficiencyModifier, false, new ArrayList<>());
        }

        EngineType(String name, List<PistonPosition> positions, float speedModifier,
                   float torqueModifier, float efficiencyModifier, boolean upgradesOnSide, List<Fluid> fluidBlacklist) {
            this.name = name;
            this.pistons = positions;
            this.speedModifier = speedModifier;
            this.torqueModifier = torqueModifier;
            this.effeciencyModifier = efficiencyModifier;
            this.fluidBlacklist = fluidBlacklist;
            this.upgradesOnSide = upgradesOnSide;
            this.langKey = "engine.type." + name;

        }


    }

}
