package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.datagen.TFMGDamageSources;
import com.drmangotea.tfmg.recipes.IndustrialBlastingRecipe;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.drmangotea.tfmg.registry.TFMGTags;
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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class BlastFurnaceOutputBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public SmartInventory inputInventory;
    public SmartInventory fluxInventory;
    public SmartInventory fuelInventory;
    public FluidTank primaryTank;
    public FluidTank secondaryTank;
    protected LazyOptional<IFluidHandler> fluidCapability;
    public LazyOptional<IItemHandlerModifiable> itemCapability;
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

        fuelInventory = new SmartInventory(1, this)
                .forbidInsertion()
                .withMaxStackSize(64).whenContentsChanged(i -> this.onContentsChanged());

        primaryTank = new SmartFluidTank(4000, this::onFluidChanged);

        secondaryTank = new SmartFluidTank(4000, this::onFluidChanged);


        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(inputInventory, fluxInventory, fuelInventory));
        fluidCapability = LazyOptional.of(() -> new CombinedTankWrapper(primaryTank, secondaryTank));
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

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private int calculateProcessingTime(IndustrialBlastingRecipe recipe) {
        int baseDuration = recipe.getProcessingDuration() * 20;
        double timeModifier = TFMGConfigs.common().machines.blastFurnaceMaxHeight.get() /
                ((baseDuration / 2) * TFMGConfigs.common().machines.blastFurnaceHeightSpeedModifier.get());

        int actualTicks = (int)(baseDuration - (getSize() / timeModifier));
        return isReinforced ? actualTicks / 2 : actualTicks;
    }

    private int[] findCleanRatio(IndustrialBlastingRecipe recipe) {
        // Get all values from config
        int ticksPerFuel = TFMGConfigs.common().machines.blastFurnaceFuelConsumption.get();
        int actualTicks = calculateProcessingTime(recipe);

        // Calculate the exact decimal ratio
        float exactRatio = (float)ticksPerFuel / actualTicks;

        // Find the smallest integer ratio that approximates this
        int maxIterations = 1000; // Prevents infinite loops
        float tolerance = 0.0001f; // How close we need to be

        int a = 1, b = 1;
        float currentError = Math.abs(exactRatio - ((float)a/b));

        // Farey sequence approximation
        for (int i = 0; i < maxIterations && currentError > tolerance; i++) {
            if ((float)a/b < exactRatio) {
                a++;
            } else {
                b++;
            }
            currentError = Math.abs(exactRatio - ((float)a/b));
        }

        // Check if recipe needs flux
        boolean needsFlux = recipe.getIngredients().size() > 1;

        // Return as [fuel, ore, flux]
        return new int[]{b, a, needsFlux ? a : 0};
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

        // Dynamic batch ratio calculation for current input
        if (!inputInventory.isEmpty()) {
            Optional<IndustrialBlastingRecipe> recipeOpt = TFMGRecipeTypes.INDUSTRIAL_BLASTING
                    .find(new RecipeWrapper(inputInventory), level);

            if (recipeOpt.isPresent()) {
                IndustrialBlastingRecipe recipe = recipeOpt.get();
                int[] batch = findCleanRatio(recipe); // Uses the corrected method

                CreateLang.translate("goggles.blast_furnace.batch_header")
                        .style(ChatFormatting.GRAY)
                        .forGoggles(tooltip);

                CreateLang.translate("goggles.blast_furnace.batch_ratio",
                                batch[0], batch[1], batch[2])
                        .style(ChatFormatting.AQUA)
                        .forGoggles(tooltip);

                if (batch[1] > 64) {
                    CreateLang.translate("goggles.blast_furnace.batch_warning")
                            .style(ChatFormatting.YELLOW)
                            .forGoggles(tooltip);
                }
            }
        }

        TFMGUtils.createFluidTooltip(this, tooltip);
        TFMGUtils.createItemTooltip(this, tooltip);

        return true;

    }

    public void executeRecipe() {

        RecipeWrapper inventoryIn = new RecipeWrapper(inputInventory);
        Optional<IndustrialBlastingRecipe> optional = TFMGRecipeTypes.INDUSTRIAL_BLASTING.find(inventoryIn, level);

        if (optional.isEmpty())
            return;

        IndustrialBlastingRecipe recipe = optional.get();
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

        if (fuel <= 0 && !fuelInventory.getItem(0).isEmpty()) {
            ItemStack fuelStack = fuelInventory.getItem(0);
            if (fuelStack.is(TFMGTags.TFMGItemTags.BLAST_FURNACE_FUEL.tag)) {
                int fuelValue = 1;
                int toConsume = Math.min(fuelStack.getCount(), STORAGE_SPACE - fuel);
                fuel += toConsume * fuelValue;
                fuelStack.shrink(toConsume);
                setChanged();
            }
        }

        if (fuelConsumeTimer >= TFMGConfigs.common().machines.blastFurnaceFuelConsumption.get() && fuel > 0) {
            fuelConsumeTimer = 0;
            fuel--;
        }

        if (timer > -1) {
            RecipeWrapper inventoryIn = new RecipeWrapper(inputInventory);
            Optional<IndustrialBlastingRecipe> optional = TFMGRecipeTypes.INDUSTRIAL_BLASTING.find(inventoryIn, level);

            if (optional.isEmpty()) {
                timer = -1;
                return;
            }

            IndustrialBlastingRecipe recipe = optional.get();

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
                if (tuyereBE == null && tuyerePos != null)
                    tuyereBE = (BlastFurnaceHatchBlockEntity) level.getBlockEntity(tuyerePos);

                if (tuyereBE.tank.getFluidAmount() < recipe.hotAirUsage || !tuyereBE.tank.getFluid().getFluid().isSame(TFMGFluids.HOT_AIR.getSource()))
                    return;

                tuyereBE.tank.getFluidInTank(0).setAmount(Math.max(tuyereBE.tank.getFluidInTank(0).getAmount() - recipe.hotAirUsage, 0));

                if (!recipe.getGasByproduct().isEmpty()) {
                    if (level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(FACING).getOpposite()).above(getSize())) instanceof BlastFurnaceHatchBlockEntity chargeHatchBE) {
                        chargeHatchBE.tank.fill(recipe.getGasByproduct(), IFluidHandler.FluidAction.EXECUTE);
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
        //if (fuel == 0)
        //    return false;

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
                entity.setSecondsOnFire(15);
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

            if (itemStack.is(TFMGTags.TFMGItemTags.BLAST_FURNACE_FUEL.tag)) {
                // If fuel storage is full, try to add to fuel inventory slot
                if (fuel >= STORAGE_SPACE) {
                    if (fuelInventory.getItem(0).isEmpty() ||
                            (fuelInventory.getItem(0).is(itemStack.getItem()) &&
                                    fuelInventory.getItem(0).getCount() < fuelInventory.getItem(0).getMaxStackSize())) {

                        int added = Math.min(itemStack.getCount(),
                                fuelInventory.getItem(0).getMaxStackSize() - fuelInventory.getItem(0).getCount());
                        if (fuelInventory.getItem(0).isEmpty()) {
                            fuelInventory.setItem(0, new ItemStack(itemStack.getItem(), added));
                        } else {
                            fuelInventory.getItem(0).grow(added);
                        }
                        itemStack.shrink(added);
                        continue;
                    }
                } else {
                    // Add to fuel storage if not full
                    int toAdd = Math.min(itemStack.getCount(), STORAGE_SPACE - fuel);
                    fuel += toAdd;
                    itemStack.shrink(toAdd);
                    continue;
                }
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
                }
            }
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        isReinforced = compound.getBoolean("IsReinforce");
        inputInventory.deserializeNBT(compound.getCompound("InputItems"));
        fluxInventory.deserializeNBT(compound.getCompound("Flux"));
        fuelInventory.deserializeNBT(compound.getCompound("FuelItems"));
        timer = compound.getInt("Timer");
        fuel = compound.getInt("Fuel");
        fuelConsumeTimer = compound.getInt("FuelConsumeTimer");
        primaryTank.readFromNBT(compound.getCompound("PrimaryTankContent"));
        secondaryTank.readFromNBT(compound.getCompound("SecondaryTankContent"));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putBoolean("IsReinforce", isReinforced);
        compound.put("InputItems", inputInventory.serializeNBT());
        compound.put("Flux", fluxInventory.serializeNBT());
        compound.put("FuelItems", fuelInventory.serializeNBT());
        compound.putInt("Timer", timer);
        compound.putInt("Fuel", fuel);
        compound.putInt("FuelConsumeTimer", fuelConsumeTimer);
        compound.put("PrimaryTankContent", primaryTank.writeToNBT(new CompoundTag()));
        compound.put("SecondaryTankContent", secondaryTank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInventory);
        ItemHelper.dropContents(level, worldPosition, fluxInventory);
        ItemHelper.dropContents(level, worldPosition, fuelInventory);
    }

    public int getSize() {

        BlockPos middlePos = getBlockPos().relative(getBlockState().getValue(FACING).getOpposite());

        tuyerePos = null;

        if ((isValidWall(middlePos) == FurnaceBlockType.NONE))
            return 0;

        int size = 0;

        int normalAmount = 0;
        int reinforcedAmount = 0;

        for (int i = 0; i <= TFMGConfigs.common().machines.blastFurnaceMaxHeight.get(); i++) {

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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }

    enum FurnaceBlockType {
        NONE,
        REGULAR,
        REINFORCED

    }
}
