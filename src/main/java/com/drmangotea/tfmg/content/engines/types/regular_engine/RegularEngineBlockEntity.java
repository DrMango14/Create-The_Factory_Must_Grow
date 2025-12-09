package com.drmangotea.tfmg.content.engines.types.regular_engine;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.turbine_engine.TurbineEngineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGSoundEvents;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

import static com.drmangotea.tfmg.content.engines.base.EngineProperties.pistonsBoxer;
import static com.drmangotea.tfmg.content.engines.base.EngineProperties.pistonsI;
import static com.drmangotea.tfmg.content.engines.base.EngineProperties.pistonsRadial;
import static com.drmangotea.tfmg.content.engines.base.EngineProperties.pistonsTurbine;
import static com.drmangotea.tfmg.content.engines.base.EngineProperties.pistonsU;
import static com.drmangotea.tfmg.content.engines.base.EngineProperties.pistonsV;
import static com.drmangotea.tfmg.content.engines.base.EngineProperties.pistonsW;
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

        CompoundTag fuelsToAllow = pistonInventory.getItem(0).getOrCreateTag().getCompound("Fuels");

        List<TagKey<Fluid>> fuelsFound = new ArrayList<>();
        for (String key : fuelsToAllow.getAllKeys()) {

            String id = fuelsToAllow.getString(key);

            TagKey<Fluid> tag = FluidTags.create(ResourceLocation.fromNamespaceAndPath("forge", id.replace("forge:", "")));

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
    public float calculateAddedStressCapacity() {
        float stress = super.calculateAddedStressCapacity() + (torque);

        stress *= pistonInventory.getSlots()/2f;

        return hasTwoShafts() ? stress / 2 : stress;
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

    @Override
    public void remove() {
        super.remove();
        for(int i =0;i<pistonInventory.getSlots();i++){
            ItemStack stack = pistonInventory.getItem(i);
            dropItem(stack);

        }
    }

    public boolean isCorrectCylinder(ItemStack itemStack) {
        return itemStack.is(TFMGItems.ENGINE_CYLINDER.get())||itemStack.is(TFMGItems.SIMPLE_ENGINE_CYLINDER.get())||itemStack.is(TFMGItems.DIESEL_ENGINE_CYLINDER.get());
    }

    public boolean isCylinderSame(ItemStack stack) {

        if(stack.is(TFMGItems.TURBINE_BLADE.get()))
            return true;

        CompoundTag tag = stack.getOrCreateTag().getCompound("Fuels");


        if (level.getBlockEntity(controller) instanceof RegularEngineBlockEntity controller) {

            List<Long> engines = new ArrayList<>(controller.engines);
            engines.add(this.controller.asLong());


            for (int i = 0; i < controller.engineLength() + 1; i++) {
                BlockPos pos = BlockPos.of(engines.get(i));
                if (level.getBlockEntity(pos) instanceof RegularEngineBlockEntity be) {
                    for (int y = 0; y < be.pistonInventory.getSlots(); y++) {
                        if (!be.pistonInventory.getItem(y).is(TFMGItems.ENGINE_CYLINDER.get()))
                            continue;

                        CompoundTag tagInside = be.pistonInventory.getItem(y).getOrCreateTag().getCompound("Fuels");
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
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putString("Type", type.name);
        compound.put("Cylinders", pistonInventory.serializeNBT());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        for (EngineType engineType : EngineType.values()) {
            if (engineType.name.matches(compound.getString("Type"))) {
                type = engineType;
                break;
            }
        }
        pistonInventory.deserializeNBT(compound.getCompound("Cylinders"));
    }

    @Override
    public float efficiencyModifier() {
        return type.effeciencyModifier * getFuelType().getEfficiency() * getUpgradeEfficiencyModifier();
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



        CreateLang.translate("goggles.engine.header")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        if(nextComponent()!= Ingredient.EMPTY){
            CreateLang.translate("goggles.engine.unfinished")
                    .color(0xde5050)
                    .forGoggles(tooltip);
            CreateLang.translate("goggles.engine.next_component")
                    .add(Component.empty().append(nextComponent().getItems()[0].getHoverName()))
                    .color(0xfff240)
                    .forGoggles(tooltip);
            CreateLang.translate("goggles.engine.type")
                    .add(CreateLang.text(type.name()))
                    .color(0xfcad03)
                    .forGoggles(tooltip);
            return true;
        }
        if(!hasAllPistons()){

            CreateLang.translate(this instanceof TurbineEngineBlockEntity ? "goggles.engine.turbines_missing" : "goggles.engine.pistons_missing")
                    .color(0xde5050)
                    .forGoggles(tooltip);

            return true;
        }


        CreateLang.translate("goggles.engine.type")
                .add(CreateLang.text(type.name()))
                .color(0xfcad03)
                .forGoggles(tooltip);
        CreateLang.translate("goggles.engine.rpm")
                .add(CreateLang.text((int)rpm+" rpm"))
                .color(0xa36f00)
                .forGoggles(tooltip);
        CreateLang.translate("goggles.engine.signal")
                .add(CreateLang.text(String.valueOf(highestSignal)))
                .color(0xfcad03)
                .forGoggles(tooltip);
        CreateLang.translate("goggles.engine.torque")
                .add(CreateLang.text(String.valueOf((int)torque)))
                .color(0xa36f00)
                .forGoggles(tooltip);
        CreateLang.translate("goggles.engine.fuel_consumption")
                .add(CreateLang.text(getFuelConsumption()/1.5f+" mb/s"))
                .color(0xfcad03)
                .forGoggles(tooltip);
        if(oil>0){
            CreateLang.translate("goggles.engine.oil")
                    .add(CreateLang.number(oil))
                    .color(0xf5dd42)
                    .forGoggles(tooltip);
        }
        if(coolingFluid>0){
            CreateLang.translate("goggles.engine.cooling_fluid")
                    .add(CreateLang.number(coolingFluid))
                    .color(0x51bdb9)
                    .forGoggles(tooltip);
        }

        TFMGUtils.createFluidTooltip(this,tooltip);

        return true;
    }

    @Override
    public int getFuelConsumption() {
        return super.getFuelConsumption()*(pistonInventory.getSlots()/2);
    }

    @Override
    public String engineId() {
        return type.name;
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

        }


    }

}
