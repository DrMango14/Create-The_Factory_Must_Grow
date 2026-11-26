package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.fluid.ForceableFluidTank;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.datagen.TFMGDamageSources;
import com.drmangotea.tfmg.recipes.IndustrialBlastingRecipe;
import com.drmangotea.tfmg.recipes.input.IndustrialBlastingRecipeInput;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;
import static net.neoforged.neoforge.fluids.FluidStack.isSameFluidSameComponents;

public class BlastFurnaceOutputBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, Clearable {

    public SmartInventory inputInventory;
    public SmartInventory fluxInventory;
    public ForceableFluidTank primaryTank;
    public ForceableFluidTank secondaryTank;
    protected IFluidHandler fluidCapability;
    public IItemHandlerModifiable itemCapability;
    public int fuel = 0;
    public int fuelConsumeTimer = 0;
    public static final int STORAGE_SPACE = 64;
    public LerpedFloat coalCokeHeight = LerpedFloat.linear();
    public final BlastFurnaceMultiblock multiblock;

    private final RecipeManager.CachedCheck<IndustrialBlastingRecipeInput, IndustrialBlastingRecipe> quickCheck;
    protected int recipeDuration;
    int timer = 0;
    public IndustrialBlastingRecipe currentRecipe;


    public BlastFurnaceOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
        multiblock = new BlastFurnaceMultiblock(this);
        inputInventory = new SmartInventory(1, this)
                .forbidInsertion()
                .forbidExtraction()
                .withMaxStackSize(64);
        fluxInventory = new SmartInventory(1, this)
                .forbidInsertion()
                .forbidExtraction()
                .withMaxStackSize(64).whenContentsChanged(i -> this.onContentsChanged());

        primaryTank = new ForceableFluidTank(4000, this::onFluidChanged).blockInsertion();
        secondaryTank = new ForceableFluidTank(4000, this::onFluidChanged).blockInsertion();


        itemCapability = new CombinedInvWrapper(inputInventory, fluxInventory);
        fluidCapability = new CombinedTankWrapper(primaryTank, secondaryTank);

        this.quickCheck = RecipeManager.createCheck(TFMGRecipeTypes.INDUSTRIAL_BLASTING.getType());
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

    public void addFuel(int amount) {
        this.fuel += amount;
        setChanged();
        sendData();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    private void onFluidChanged(FluidStack stack) {
        if (level == null)
            return;
        if (!level.isClientSide) {
            setChanged();
            sendData();
        }
    }

    public void onContentsChanged() {
        if (level == null || level.isClientSide) return;

        setChanged();
        sendData();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        TFMGTexts.BlastFurnace.stats(inputInventory.getStackInSlot(0).getCount()).forGoggles(tooltip, 1);
        TFMGTexts.progress(timer, recipeDuration).forGoggles(tooltip, 1);
        TFMGTexts.BlastFurnace.height(multiblock.getSize()).forGoggles(tooltip, 1);
        TFMGTexts.BlastFurnace.fuelAmount(fuel).forGoggles(tooltip, 1);
        if (multiblock.isReinforced())
            TFMGTexts.BlastFurnace.reinforced().forGoggles(tooltip);
		
		TFMGUtils.createFluidTooltip(tooltip, fluidCapability);
		TFMGUtils.createItemTooltip(tooltip, itemCapability);
        return true;
    }

    boolean extractedAir = false;

    @Override
    public void tick() {
        super.tick();
        if (level == null)
            return;

        if (level.isClientSide) {
            coalCokeHeight.chase(Math.min(fuel + inputInventory.getStackInSlot(0).getCount(), 24), 0.1f, LerpedFloat.Chaser.EXP);
            coalCokeHeight.tickChaser();
        }

        BlockPos behindPos = getBlockPos().above().relative(getBlockState().getValue(FACING).getOpposite());
        if (level.getBlockState(behindPos).is(TFMGTags.Blocks.BLAST_FURNACE_MELTS.tag)) {
            level.removeBlock(behindPos, false);
        }

        manageRecipe();
    }

    public void manageRecipe() {
        if (level == null || fuel <= 0 || inputInventory.isEmpty() || multiblock.getSize() < 3 || currentRecipe == null || level.isClientSide && !isVirtual())
            return;

        IndustrialBlastingRecipe activeRecipe = currentRecipe;
        if (timer >= recipeDuration) {
            if (hasSpace(activeRecipe)) {
                inputInventory.setStackInSlot(0, inputInventory.getStackInSlot(0).copyWithCount(inputInventory.getStackInSlot(0).getCount() - 1));
                if (activeRecipe.getIngredients().size() > 1)
                    fluxInventory.setStackInSlot(0, fluxInventory.getStackInSlot(0).copyWithCount(fluxInventory.getStackInSlot(0).getCount() - 1));
                primaryTank.forceFill(activeRecipe.getPrimaryResult(), IFluidHandler.FluidAction.EXECUTE);
                if (activeRecipe.getFluidResults().size() > 1)
                    secondaryTank.forceFill(activeRecipe.getSecondaryResult(), IFluidHandler.FluidAction.EXECUTE);
                currentRecipe = null;
                timer = 0;
                recipeDuration = -1;
                sendData();
                setChanged();
            }
        } else {
            if (fuelConsumeTimer >= TFMGConfigs.common().machines.blastFurnaceFuelConsumption.get() && fuel > 0) {
                fuelConsumeTimer = 0;
                fuel--;
            }

            BlastFurnaceHatchBlockEntity tuyere = multiblock.getTuyereBlockEntity();
            if (activeRecipe.hotAirUsage > 0) {
                if (tuyere != null && !extractedAir) {
                    int simulated = tuyere.tank.forceDrain(activeRecipe.hotAirUsage, IFluidHandler.FluidAction.SIMULATE).getAmount();
                    if (simulated < activeRecipe.hotAirUsage)
                        return;
                    tuyere.tank.forceDrain(activeRecipe.hotAirUsage, IFluidHandler.FluidAction.EXECUTE);
                    extractedAir = true;
                }
                if (!activeRecipe.getGasByproduct().isEmpty() && multiblock.getTopHatchBlockEntity() != null) {
                    multiblock.getTopHatchBlockEntity().tank.forceFill(activeRecipe.getGasByproduct(), IFluidHandler.FluidAction.EXECUTE);
                }
            }

            if (level.isClientSide())
                makeParticles();
            hurtEntities();

            timer++;
            fuelConsumeTimer++;
            extractedAir = false;
            sendData();
            setChanged();
        }
    }

    private boolean hasSpace(IndustrialBlastingRecipe activeRecipe) {
        var primary = false;
        if (primaryTank.isEmpty() || isSameFluidSameComponents(primaryTank.getFluid(), activeRecipe.getPrimaryResult()))
            primary = primaryTank.getSpace() >= activeRecipe.getPrimaryResult().getAmount();
        var secondary = true;
        if ((activeRecipe.getFluidResults().size() > 1) && (secondaryTank.isEmpty() || isSameFluidSameComponents(secondaryTank.getFluid(), activeRecipe.getSecondaryResult())))
            secondary = secondaryTank.getSpace() >= activeRecipe.getSecondaryResult().getAmount();
        return primary && secondary;
    }

    public @Nullable IndustrialBlastingRecipe findRecipe() {
        if (level == null)
            return null;
        RecipeHolder<IndustrialBlastingRecipe> recipeholder;
        if (!inputInventory.isEmpty()) {
            recipeholder = quickCheck.getRecipeFor(new IndustrialBlastingRecipeInput(inputInventory.getStackInSlot(0), fluxInventory.getStackInSlot(0)), level).orElse(null);
        } else {
            return null;
        }
        if(recipeholder == null) {
            return null;
        }

        int baseDuration = recipeholder.value().getProcessingDuration();
        int height = multiblock.getSize();
        int maxHeight = TFMGConfigs.common().machines.blastFurnaceMaxHeight.get();
        double maxTimeModifier = TFMGConfigs.common().machines.blastFurnaceHeightSpeedModifier.get();
        double timeModifier = maxHeight / (((double) baseDuration / 2) * maxTimeModifier);

        recipeDuration = (int) (baseDuration - (height / timeModifier));
        if (multiblock.isReinforced())
            recipeDuration /= 2;
        return recipeholder.value();
    }

    public void makeParticles() {
        if (level == null || this.multiblock.getTopHatchBlockEntity() != null)
            return;
        BlockPos pos = getBlockPos().above().relative(getBlockState().getValue(FACING).getOpposite());
		TFMGUtils.spawnSmokeParticles(level, Vec3.atLowerCornerWithOffset(pos, 0.2, 0, 0.2), 0.6f);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        this.multiblock.evaluate();
        onContentsChanged();
        collectItems();
        if (currentRecipe == null) {
            currentRecipe = findRecipe();
        }
        var topHatch = this.multiblock.getTopHatchBlockEntity();
        if (topHatch != null) {
            topHatch.fillFurnace(this);
        }
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos()).setMaxY(getBlockPos().getY() + 2);
    }

    public void hurtEntities() {
        if (level == null)
            return;
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
        if (level == null)
            return;
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(this.getBlockPos().relative(getBlockState().getValue(FACING).getOpposite()).above()));
        if (items.isEmpty())
            return;

        ItemStack itemStack = items.getFirst().getItem();

        for (int i = 1; i <= itemStack.getCount(); i++) {
            if (itemStack.isEmpty())
                return;
            if (itemStack.is(TFMGTags.Items.BLAST_FURNACE_FUEL.tag) && fuel < STORAGE_SPACE) {
                fuel++;
                itemStack.shrink(1);
                continue;
            }

            if (itemStack.is(TFMGTags.Items.FLUX.tag)) {
                if (fluxInventory.isEmpty()) {
                    fluxInventory.setItem(0, itemStack.copyWithCount(1));
                    itemStack.shrink(1);
                } else {
                    ItemStack fluxItem = fluxInventory.getItem(0).copy();
                    if (fluxItem.getCount() < itemStack.getMaxStackSize()) {
                        if (ItemStack.isSameItemSameComponents(fluxItem, itemStack)) {
                            fluxInventory.setItem(0, fluxItem.copyWithCount(fluxItem.getCount() + 1));
                            itemStack.shrink(1);
                        }
                    }
                }
            }

            if (inputInventory.isEmpty()) {
                inputInventory.setItem(0, itemStack.copyWithCount(1));
                itemStack.shrink(1);
            } else {
                ItemStack inputItem = inputInventory.getItem(0).copy();
                if (inputItem.getCount() < itemStack.getMaxStackSize()) {
                    if (ItemStack.isSameItemSameComponents(inputItem, itemStack)) {
                        inputInventory.setItem(0, inputItem.copyWithCount(inputItem.getCount() + 1));
                        itemStack.shrink(1);
                    }
                }
            }
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        multiblock.read(compound, "MultiblockData", registries, clientPacket);
        inputInventory.deserializeNBT(registries,compound.getCompound("InputItems"));
        fluxInventory.deserializeNBT(registries,compound.getCompound("Flux"));
        timer = compound.getInt("Timer");
        recipeDuration = compound.getInt("RecipeDuration");
        fuel = compound.getInt("Fuel");
        fuelConsumeTimer = compound.getInt("FuelConsumeTimer");
        primaryTank.readFromNBT(registries,compound.getCompound("PrimaryTankContent"));
        secondaryTank.readFromNBT(registries,compound.getCompound("SecondaryTankContent"));
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("MultiblockData", multiblock.write(registries, clientPacket));
        compound.put("InputItems", inputInventory.serializeNBT(registries));
        compound.put("Flux", fluxInventory.serializeNBT(registries));
        compound.putInt("Timer", timer);
        compound.putInt("RecipeDuration", recipeDuration);
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

    @Override
    public void clearContent() {
        this.inputInventory.clearContent();
        this.fluxInventory.clearContent();
    }
}
