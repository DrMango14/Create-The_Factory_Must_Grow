package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.datagen.TFMGDamageSources;
import com.drmangotea.tfmg.recipes.IndustrialBlastingRecipe;
import com.drmangotea.tfmg.registry.*;
import com.simibubi.create.Create;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class BlastFurnaceOutputBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public SmartInventory inputInventory;
    public SmartInventory fluxInventory;
    public FluidTank primaryTank;
    public FluidTank secondaryTank;
    protected IFluidHandler fluidCapability;
    public IItemHandlerModifiable itemCapability;
    public int fuel = 0;
    public int fuelConsumeTimer = 0;
    public float duration;
    public int timer = -1;
    public BlockPos tuyerePos;
    public BlastFurnaceHatchBlockEntity tuyereBE = null;
    public static final int STORAGE_SPACE = 64;
    public LerpedFloat coalCokeHeight = LerpedFloat.linear();
    boolean isReinforced = false;


    public BlastFurnaceOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
        inputInventory = new SmartInventory(1, this)
                .forbidInsertion()
                .forbidExtraction()
                .withMaxStackSize(64);
        fluxInventory = new SmartInventory(1, this)
                .forbidInsertion()
                .forbidExtraction()
                .withMaxStackSize(64).whenContentsChanged(i -> this.onContentsChanged());

        primaryTank = new SmartFluidTank(4000, this::onFluidChanged);

        secondaryTank = new SmartFluidTank(4000, this::onFluidChanged);


        itemCapability = new CombinedInvWrapper(inputInventory, fluxInventory);
        fluidCapability = new CombinedTankWrapper(primaryTank, secondaryTank);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.BLAST_FURNACE_OUTPUT.get(),
                (be, context) -> be.fluidCapability
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TFMGBlockEntities.BLAST_FURNACE_OUTPUT.get(),
                (be, context) -> be.itemCapability
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    private void onFluidChanged(FluidStack stack) {
        if (!hasLevel())
            return;
        if (!level.isClientSide) {
            setChanged();
            sendData();
        }
    }

    public void onContentsChanged() {
        if (!inputInventory.isEmpty() && timer == -1) {
            executeRecipe();
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        CreateLang.translate("goggles.blast_furnace.stats", inputInventory.getStackInSlot(0).getCount())
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip, 1);

        CreateLang.translate("goggles.blast_furnace.height", getSize())
                .forGoggles(tooltip, 1);
        CreateLang.translate("goggles.blast_furnace.fuel_amount", fuel)
                .forGoggles(tooltip, 1);

        if (timer != -1)
            CreateLang.translate("goggles.blast_furnace.timer", timer)
                    .style(ChatFormatting.GOLD)
                    .forGoggles(tooltip, 1);


        if (isReinforced)
            CreateLang.translate("goggles.blast_furnace.reinforced")
                    .style(ChatFormatting.GREEN)
                    .forGoggles(tooltip);

        TFMGUtils.createFluidTooltip(this, tooltip);
        TFMGUtils.createItemTooltip(this, tooltip);


        return true;
    }

    public void executeRecipe() {

        RecipeWrapper inventoryIn = new RecipeWrapper(inputInventory);
        Optional<RecipeHolder<IndustrialBlastingRecipe>> optional = TFMGRecipeTypes.INDUSTRIAL_BLASTING.find(inventoryIn, level);

        if (optional.isEmpty())
            return;

        IndustrialBlastingRecipe recipe = optional.get().value();
        if (recipe.getIngredients().size() > 1)
            if (!(recipe.getIngredients().get(1).test(fluxInventory.getItem(0))))
                return;

        if (fluxInventory.getItem(0).getCount() < recipe.getIngredients().size() - 1)
            return;

        int baseDuration = recipe.getProcessingDuration() * 20;
        int heigth = getSize();
        int maxHeigth = TFMGConfigs.common().machines.blastFurnaceMaxHeight.get();
        double maxTimeModifier = TFMGConfigs.common().machines.blastFurnaceHeightSpeedModifier.get();
        double timeModifier = maxHeigth / ((baseDuration / 2) * maxTimeModifier);

        timer = (int) (baseDuration - (heigth / timeModifier));
        if (isReinforced)
            timer /= 2;
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide) {
            coalCokeHeight.chase(Math.min(fuel + inputInventory.getStackInSlot(0).getCount(), 24), 0.1f, LerpedFloat.Chaser.EXP);
            coalCokeHeight.tickChaser();
        }

        if (inputInventory.isEmpty())
            return;
        if (getSize() < 3)
            return;

        if (fuelConsumeTimer >= TFMGConfigs.common().machines.blastFurnaceFuelConsumption.get() && fuel > 0) {
            fuelConsumeTimer = 0;
            fuel--;
        }

        if (timer > -1) {
            RecipeWrapper inventoryIn = new RecipeWrapper(inputInventory);
            Optional<RecipeHolder<IndustrialBlastingRecipe>> optional = TFMGRecipeTypes.INDUSTRIAL_BLASTING.find(inventoryIn, level);

            if (optional.isEmpty()) {
                timer = -1;
                return;
            }

            IndustrialBlastingRecipe recipe = optional.get().value();

            if (timer == 0) {
                if (canProcess(recipe)) {
                    int itemsUsed = 1;
                    int fluxUsed = 1;

                    if (!(primaryTank.getSpace() >= recipe.getPrimaryResult().getAmount()))
                        return;
                    if (recipe.getFluidResults().size() > 1)
                        if (!(secondaryTank.getSpace() >= recipe.getSecondaryResult().getAmount()))
                            return;

                    inputInventory.getItem(0).shrink(1);
                    if (recipe.getIngredients().size() > 1)
                        fluxInventory.getItem(0).shrink(recipe.getIngredients().size() - 1);
                    primaryTank.fill(recipe.getPrimaryResult(), IFluidHandler.FluidAction.EXECUTE);
                    if (recipe.getFluidResults().size() > 1)
                        secondaryTank.fill(recipe.getSecondaryResult(), IFluidHandler.FluidAction.EXECUTE);

                    timer = -1;

                    sendData();
                    setChanged();
                }
            }
            if (timer > 0 && fuel > 0) {
                if (recipe.hotAirUsage > 0 && (tuyerePos == null || !level.getBlockState(tuyerePos).is(TFMGBlocks.BLAST_FURNACE_HATCH.get()))) {
                    tuyereBE = null;
                    return;
                }
                if (tuyereBE == null)
                    tuyereBE = (BlastFurnaceHatchBlockEntity) level.getBlockEntity(tuyerePos);

                if (tuyereBE.tank.getFluidAmount() < recipe.hotAirUsage || !tuyereBE.tank.getFluid().getFluid().isSame(TFMGFluids.HOT_AIR.getSource()))
                    return;

                tuyereBE.tank.getFluidInTank(0).setAmount(Math.max(tuyereBE.tank.getFluidInTank(0).getAmount() - recipe.hotAirUsage, 0));

                if (!recipe.getGasByproduct().isEmpty()) {
                    if (level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(FACING).getOpposite()).above(getSize())) instanceof BlastFurnaceHatchBlockEntity be) {
                        be.tank.fill(recipe.getGasByproduct(), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
                if (level.isClientSide())
                    makeParticles();
                hurtEntities();
                timer--;
                fuelConsumeTimer++;

                if (!level.isClientSide) {
                    setChanged();
                    sendData();
                }
            }
        }
    }

    public void makeParticles() {
        Random random = Create.RANDOM;
        Direction direction = getBlockState().getValue(FACING).getOpposite();
        BlockPos pos = getBlockPos().above().relative(direction);
        int shouldSpawnSmoke = random.nextInt(7);
        if (shouldSpawnSmoke == 0) {
            level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() + random.nextFloat(0.6f) + 0.2, pos.getY() + 1, pos.getZ() + random.nextFloat(0.6f) + 0.2, 0.0D, 0.08D, 0.0D);
        }
    }

    private boolean canProcess(IndustrialBlastingRecipe recipe) {
        if (fuel == 0)
            return false;

        if (!primaryTank.getFluid().isEmpty() && !primaryTank.getFluid().getFluid().isSame(recipe.getPrimaryResult().getFluid()))
            return false;
        if (!secondaryTank.getFluid().isEmpty() && !secondaryTank.getFluid().getFluid().isSame(recipe.getSecondaryResult().getFluid()))
            return false;
        return true;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        onContentsChanged();
        collectItems();
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos()).setMaxY(getBlockPos().getY() + 2);
    }

    public void hurtEntities() {

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(this.getBlockPos().relative(getBlockState().getValue(FACING).getOpposite()).above()));

        for (LivingEntity entity : entities) {
            if (!entity.fireImmune()) {
                entity.setRemainingFireTicks(15);
                if (entity.hurt(TFMGDamageSources.blastFurnace(level), 4.0F)) {
                    entity.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + entity.getRandom().nextFloat() * 0.4F);
                }

            }
        }
    }

    public void collectItems() {

        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(this.getBlockPos().relative(getBlockState().getValue(FACING).getOpposite()).above()));

        if (items.isEmpty())
            return;

        ItemStack itemStack = items.get(0).getItem();

        for (int i = 0; i < 64; i++) {

            if (itemStack.isEmpty())
                return;

            if (itemStack.is(TFMGTags.TFMGItemTags.BLAST_FURNACE_FUEL.tag) && fuel < STORAGE_SPACE) {

                fuel++;
                itemStack.shrink(1);
                continue;
            }
            if (itemStack.is(TFMGTags.TFMGItemTags.FLUX.tag) && fluxInventory.getItem(0).getCount() < itemStack.getMaxStackSize()) {
                if (fluxInventory.isEmpty() || fluxInventory.getItem(0).is(itemStack.getItem())) {
                    fluxInventory.setItem(0, new ItemStack(itemStack.getItem(), fluxInventory.getItem(0).getCount() + 1));
                    itemStack.shrink(1);
                    continue;
                }
            }
            if (inputInventory.getItem(0).getCount() < itemStack.getMaxStackSize()) {
                if (inputInventory.isEmpty() || inputInventory.getItem(0).is(itemStack.getItem())) {
                    inputInventory.setItem(0, new ItemStack(itemStack.getItem(), inputInventory.getItem(0).getCount() + 1));
                    itemStack.shrink(1);
                    continue;
                }
            }
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        isReinforced = compound.getBoolean("IsReinforce");
        inputInventory.deserializeNBT(registries,compound.getCompound("InputItems"));
        fluxInventory.deserializeNBT(registries,compound.getCompound("Flux"));
        timer = compound.getInt("Timer");
        fuel = compound.getInt("Fuel");
        fuelConsumeTimer = compound.getInt("FuelConsumeTimer");
        primaryTank.readFromNBT(registries,compound.getCompound("PrimaryTankContent"));
        secondaryTank.readFromNBT(registries,compound.getCompound("SecondaryTankContent"));
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.putBoolean("IsReinforce", isReinforced);
        compound.put("InputItems", inputInventory.serializeNBT(registries));
        compound.put("Flux", fluxInventory.serializeNBT(registries));
        compound.putInt("Timer", timer);
        compound.putInt("Fuel", fuel);
        compound.putInt("FuelConsumeTimer", fuelConsumeTimer);
        compound.put("PrimaryTankContent", primaryTank.writeToNBT(registries,new CompoundTag()));
        compound.put("SecondaryTankContent", secondaryTank.writeToNBT(registries,new CompoundTag()));
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInventory);
        ItemHelper.dropContents(level, worldPosition, fluxInventory);
    }

    public int getSize() {

        BlockPos middlePos = getBlockPos().relative(getBlockState().getValue(FACING).getOpposite());

        tuyerePos = null;

        if ((isValidWall(middlePos) == FurnaceBlockType.NONE))
            return 0;

        int size = 0;

        int normalAmount = 0;
        int reinforcedAmount = 0;

        for (int i = 0; i < TFMGConfigs.common().machines.blastFurnaceMaxHeight.get(); i++) {

            BlockPos checkedPos = middlePos.above(i).east().south();

            for (int j = 0; j < 3; j++) {
                for (int y = 0; y < 3; y++) {
                    FurnaceBlockType wall = isValidWall(checkedPos);
                    FurnaceBlockType support = isValidSupport(checkedPos);
                    if (checkedPos.getX() == middlePos.getX() ^ checkedPos.getZ() == middlePos.getZ()) {
                        if (!(i == 0 && level.getBlockState(checkedPos).is(TFMGBlocks.BLAST_FURNACE_OUTPUT.get()))) {
                            if (wall == FurnaceBlockType.NONE) {
                                isReinforced = normalAmount == 0 && reinforcedAmount > 0;
                                return size;
                            } else {
                                if (wall == FurnaceBlockType.REGULAR) {
                                    normalAmount++;
                                } else reinforcedAmount++;
                            }
                        }
                    } else if (checkedPos.getX() == middlePos.getX() && checkedPos.getZ() == middlePos.getZ()) {
                        if (!level.getBlockState(checkedPos).isAir() && i != 0) {
                            isReinforced = normalAmount == 0 && reinforcedAmount > 0;

                            return size;
                        }
                    } else if (support == FurnaceBlockType.NONE) {
                        isReinforced = normalAmount == 0 && reinforcedAmount > 0;
                        return size;
                    } else {
                        if (support == FurnaceBlockType.REGULAR) {
                            normalAmount++;
                        } else reinforcedAmount++;
                    }

                    checkedPos = checkedPos.west();
                }
                checkedPos = checkedPos.north();
                checkedPos = checkedPos.east(3);
            }
            size++;
        }
        return size;
    }

    public FurnaceBlockType isValidWall(BlockPos pos) {

        BlockState state = level.getBlockState(pos);

        if (state.is(TFMGBlocks.BLAST_FURNACE_HATCH.get())) {
            if (tuyerePos != null)
                return FurnaceBlockType.NONE;
            tuyerePos = pos;
        }

        if (state.is(TFMGTags.TFMGBlockTags.REINFORCED_BLAST_FURNACE_WALL.tag))
            return FurnaceBlockType.REINFORCED;
        if (state.is(TFMGTags.TFMGBlockTags.BLAST_FURNACE_WALL.tag))
            return FurnaceBlockType.REGULAR;
        return FurnaceBlockType.NONE;
    }

    public FurnaceBlockType isValidSupport(BlockPos pos) {

        BlockState state = level.getBlockState(pos);

        if (state.is(TFMGTags.TFMGBlockTags.REINFORCED_BLAST_FURNACE_SUPPORT.tag))
            return FurnaceBlockType.REINFORCED;
        if (state.is(TFMGTags.TFMGBlockTags.BLAST_FURNACE_SUPPORT.tag))
            return FurnaceBlockType.REGULAR;
        return FurnaceBlockType.NONE;
    }



    enum FurnaceBlockType {
        NONE,
        REGULAR,
        REINFORCED

    }
}