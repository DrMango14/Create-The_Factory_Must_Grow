package com.drmangotea.tfmg.blocks.machines.metal_processing.blast_furnace;

import com.drmangotea.tfmg.blocks.machines.TFMGMachineBlockEntity;
import com.drmangotea.tfmg.recipes.industrial_blasting.IndustrialBlastingRecipe;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class BlastFurnaceOutputBlockEntity extends TFMGMachineBlockEntity implements IHaveGoggleInformation {


    public BlastFurnaceType type;
    public Direction outputFacing = getBlockState().getValue(FACING);
    public int height = 2;
    public int reinforcementHeight = 0;

    public int validHeight = 0;

    public float blastFurnaceLevel = 0;

    public float fuelEfficiency = 1000;

    public float speedModifier = 1;

    private int timer = -1;

    private static final int HALT_INTERVAL = 5;
    private int lastWorkingTime = -HALT_INTERVAL;

    public IndustrialBlastingRecipe recipe;


    private final LerpedFloat coalCokeHeight = LerpedFloat.linear();

    // item storage
    public LazyOptional<IItemHandlerModifiable> itemCapability;

    public SmartInventory inputInventory;

    public SmartInventory fuelInventory;

    public AABB interiorArea;

    public float projectedAreaSize;

    public BlastFurnaceOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {

        super(type, pos, state);
        inputInventory = new SmartInventory(1, this).forbidInsertion()
                .withMaxStackSize(64);
        fuelInventory = new SmartInventory(1, this).forbidInsertion()
                .withMaxStackSize(64);

        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(inputInventory, fuelInventory));

        inputInventory.forbidExtraction();
        fuelInventory.forbidExtraction();

        tank1.getPrimaryHandler().setCapacity(8000);
        tank2.getPrimaryHandler().setCapacity(8000);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    }

    @Override
    public void tick() {
        super.tick();
        ++currentTick;

        adjustType();
        if (type == BlastFurnaceType.INVALID) return;

        if (type == BlastFurnaceType.SMALL) {
            blastFurnaceLevel = ((float) validHeight / 2) - 1;
            blastFurnaceLevel = (float) Math.min(blastFurnaceLevel, 2.5);
        }
        else if (type == BlastFurnaceType.BIG_LEFT || type == BlastFurnaceType.BIG_RIGHT) {
            blastFurnaceLevel = validHeight;
            blastFurnaceLevel = Math.min(blastFurnaceLevel, 15);
        }

        if (speedModifier != 0) {
            speedModifier = (blastFurnaceLevel / 2);
            fuelEfficiency = 400 * (speedModifier);
        }
        else {
            fuelEfficiency = 400;
            speedModifier = 1;
        }


        outputFacing = getBlockState().getValue(FACING);
        coalCokeHeight.chase(fuelInventory.getStackInSlot(0).getCount() + inputInventory.getStackInSlot(0).getCount(), 0.1f, LerpedFloat.Chaser.EXP);
        coalCokeHeight.tickChaser();


        int maxLevelByReinforcements = reinforcementHeight * 2;
        validHeight = Math.min(maxLevelByReinforcements, height);

        if (recipe != null) {
            if (timer == -1 &&
                    (tank1.getPrimaryHandler().getFluidAmount() + recipe.getFluidResults().get(0).getAmount()) <= tank1.getPrimaryHandler().getCapacity() &&
                    (tank2.getPrimaryHandler().getFluidAmount() + recipe.getFluidResults().get(1).getAmount()) <= tank2.getPrimaryHandler().getCapacity() &&
                    type != BlastFurnaceType.INVALID &&
                    !fuelInventory.isEmpty()
            ) {
                timer = (int) (recipe.getProcessingDuration() / speedModifier);
                inputInventory.getStackInSlot(0).setCount(inputInventory.getStackInSlot(0).getCount() - 1);
            }
        }


        RecipeWrapper inventoryIn = new RecipeWrapper(inputInventory);
        if (recipe == null || !recipe.matches(inventoryIn, level)) {
            Optional<IndustrialBlastingRecipe> recipe = TFMGRecipeTypes.INDUSTRIAL_BLASTING.find(inventoryIn, level);
            if (recipe.isEmpty()) {
                timer = -1;
                sendData();
            }
            else {
                this.recipe = recipe.get();
                sendData();
            }
        }

        var level = contentLevel();
        acceptInsertedItems(level);

        if (isWorking()) {
            damageEntitiesInside(level);
            playWorkingParticle(level);
        }

        if (type != BlastFurnaceType.INVALID && timer > 0 &&
                (tank1.getPrimaryHandler().getFluidAmount() + recipe.getFluidResults().get(0).getAmount()) <= tank1.getPrimaryHandler().getCapacity() &&
                (tank2.getPrimaryHandler().getFluidAmount() + recipe.getFluidResults().get(1).getAmount()) <= tank2.getPrimaryHandler().getCapacity()) {
            timer--;
            int random = Create.RANDOM.nextInt((int) Math.abs(fuelEfficiency) + 1);
            if (random == 69) {
                fuelInventory.getStackInSlot(0).shrink(1);
            }

        }


        if (timer == 0) {
            process();
            timer = -1;
        }

        if (timer >= 0) {
            lastWorkingTime = currentTick;
        }
    }

    private int currentTick = 0;

    public boolean isWorking() {
        return currentTick - lastWorkingTime <= HALT_INTERVAL;
    }

    public boolean isValid() {
        return type != BlastFurnaceType.INVALID;
    }

    public float contentLevel() {
        return coalCokeHeight.getValue() / 64;
    }

    public BoundingBox getAlignedInteriorArea() {
        var i = interiorArea;
        return new BoundingBox((int) Math.round(i.minX), (int) Math.round(i.minY), (int) Math.round(i.minZ), (int) Math.round(i.maxX), (int) Math.round(i.maxY), (int) Math.round(i.maxZ));
    }


    public static final float MIN_CONTENT_DISPLAY_HEIGHT = 0.25f;
    private static final int TYPE_AIR = 0;
    private static final int TYPE_FIREPROOF_BRICKS = 1;
    private static final int TYPE_FIREPROOF_REINFORCEMENT = 2;
    private static final int TYPE_INVALID = -1;

    private int getEncodedType(BlockPos pos) {
        var type = level.getBlockState(pos);
        if (type.is(Blocks.AIR)) {
            return TYPE_AIR;
        }
        else if (type.is(TFMGBlocks.FIREPROOF_BRICKS.get())) {
            return TYPE_FIREPROOF_BRICKS;
        }
        else if (type.is(TFMGBlocks.FIREPROOF_BRICK_REINFORCEMENT.get())) {
            return TYPE_FIREPROOF_REINFORCEMENT;
        }
        else {
            return TYPE_INVALID;
        }
    }


    /**
     * Each int in array represents a pattern of a block, with each bit represents match this block or not.
     * <p>
     * Relative offset to the location looks like:
     * <code>
     * 0  1  2  3  4
     * 5  6  7  8  9
     * 10 11 12 13 14
     * 15 16 17 18 19
     * 20 21 22 23 24
     * </code>
     * <p>
     * Where index 2 is the origin
     */
    private int[] scanPattern(BlockPos origin, Function<Vec3, Vec3> mat) {
        var center = origin.getCenter();
        var types = new int[4];
        int i = 0;
        for (int dz = 0; dz > -5; --dz) {
            for (int dx = -2; dx <= 2; ++dx) {
                var transformed = center.add(mat.apply(new Vec3(dx, 0, dz)));
                var xt = getEncodedType(new BlockPos((int) Math.floor(transformed.x), (int) Math.floor(transformed.y), (int) Math.floor(transformed.z)));
                if (xt >= 0) types[xt] |= 1 << i;

                ++i;
            }
        }
        return types;
    }

    private void damageEntitiesInside(float height) {
        if (height < MIN_CONTENT_DISPLAY_HEIGHT) return;

        for (var entity : level.getEntitiesOfClass(LivingEntity.class, interiorArea.setMaxY(interiorArea.minY + height))) {
            entity.setSecondsOnFire(10);

            entity.makeStuckInBlock(Blocks.LAVA.defaultBlockState(), new Vec3(0.9F, 1.5D, 0.9F));

            int random = Create.RANDOM.nextInt(24);

            if (random == 7) {
                entity.hurt(level.damageSources().inFire(), 4);
            }
        }
    }

    private static AABB makeAABB(BlockPos origin,
                                 int x0,
                                 int y0,
                                 int z0,
                                 int x1,
                                 int y1,
                                 int z1,
                                 Function<Vec3, Vec3> mat) {
        var center = origin.getCenter();
        Vec3 low = center.add(mat.apply(new Vec3(x0, y0, z0))), hi = center.add(mat.apply(new Vec3(x1, y1, z1)));
        double minX = (int) Math.floor(Math.min(low.x, hi.x)), maxX = (int) Math.ceil(Math.max(low.x, hi.x));
        double minY = (int) Math.floor(Math.min(low.y, hi.y)), maxY = (int) Math.ceil(Math.max(low.y, hi.y));
        double minZ = (int) Math.floor(Math.min(low.z, hi.z)), maxZ = (int) Math.ceil(Math.max(low.z, hi.z));
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private void adjustType() {
        Function<Vec3, Vec3> f; // x-z rotation matrix
        switch (outputFacing) {
            case EAST -> f = v -> new Vec3(v.z, v.y, v.x);
            case WEST -> f = v -> new Vec3(-v.z, v.y, -v.x);
            case NORTH -> f = v -> new Vec3(-v.x, v.y, -v.z);
            case SOUTH -> f = v -> v;
            default -> {
                type = BlastFurnaceType.INVALID;
                return;
            }
        }
        var pivot = getBlockPos();

        var floorPattern = scanPattern(pivot, f);
        boolean canBeSmall = (floorPattern[TYPE_FIREPROOF_BRICKS] & 0b1000111000000) == 0b1000111000000;
        boolean canBeLBig = (floorPattern[TYPE_FIREPROOF_BRICKS] & 0b110011110111100010) == 0b110011110111100010;
        boolean canBeRBig = (floorPattern[TYPE_FIREPROOF_BRICKS] & 0b1100111101111001000) == 0b1100111101111001000;

        if (!canBeSmall && !canBeLBig && !canBeRBig) {
            type = BlastFurnaceType.INVALID;
            return;
        }

        var pats = new int[][]{
                floorPattern,
                scanPattern(pivot.above(1), f),
                scanPattern(pivot.above(2), f),
                scanPattern(pivot.above(3), f),
                scanPattern(pivot.above(4), f),
                scanPattern(pivot.above(5), f),
                scanPattern(pivot.above(6), f)
        };

        for (int i = 1; i < 4; ++i) {
            var pat = pats[i];
            var wall = pat[TYPE_FIREPROOF_BRICKS];

            canBeSmall &= (wall & 0b1000101000100) == 0b1000101000100;
            canBeLBig &= (wall & 0b110010010100100110) == 0b110010010100100110;
            canBeRBig &= (wall & 0b1100100101001001100) == 0b1100100101001001100;

            var air = pat[TYPE_AIR];
            canBeSmall &= (air & 0b10000000) == 0b10000000;
            canBeLBig &= (air & 0b1100011000000) == 0b1100011000000;
            canBeRBig &= (air & 0b11000110000000) == 0b11000110000000;
        }

        if (canBeSmall) {
            int refHeight = 0;
            for (; refHeight < 6; ++refHeight) {
                var ref = pats[refHeight][TYPE_FIREPROOF_REINFORCEMENT];
                if ((ref & 0b10100000001010) != 0b10100000001010) {
                    break;
                }
            }
            int actHeight = 1;
            for (; actHeight < 6; ++actHeight) {
                var brk = pats[actHeight][TYPE_FIREPROOF_BRICKS];
                var air = pats[actHeight][TYPE_AIR];
                if ((brk & 0b1000101000100) != 0b1000101000100 || (air & 0b10000000) != 0b10000000) {
                    break;
                }
            }
            reinforcementHeight = refHeight;
            height = actHeight;
            interiorArea = makeAABB(pivot, 0, 1, -1, 0, actHeight, -1, f);
            type = BlastFurnaceType.SMALL;
        }
        else if (canBeLBig) {
            int refHeight = 0;
            for (; refHeight < 6; ++refHeight) {
                var ref = pats[refHeight][TYPE_FIREPROOF_REINFORCEMENT];
                if ((ref & 0b1001000000000001001) != 0b1001000000000001001) {
                    break;
                }

            }
            int actHeight = 1;
            for (; actHeight < 6; ++actHeight) {
                var brk = pats[actHeight][TYPE_FIREPROOF_BRICKS];
                var air = pats[actHeight][TYPE_AIR];
                if ((brk & 0b110010010100100110) != 0b110010010100100110 || (air & 0b1100011000000) != 0b1100011000000) {
                    break;
                }
            }
            reinforcementHeight = refHeight;
            height = actHeight;
            interiorArea = makeAABB(pivot, -1, 1, -1, 0, actHeight, -2, f);
            type = BlastFurnaceType.BIG_LEFT;
        }
        else if (canBeRBig) {
            int refHeight = 0;
            for (; refHeight < 6; ++refHeight) {
                var ref = pats[refHeight][TYPE_FIREPROOF_REINFORCEMENT];
                if ((ref & 0b10010000000000010010) != 0b10010000000000010010) {
                    break;
                }
            }
            int actHeight = 1;
            for (; actHeight < 6; ++actHeight) {
                var brk = pats[actHeight][TYPE_FIREPROOF_BRICKS];
                var air = pats[actHeight][TYPE_AIR];
                if ((brk & 0b1100100101001001100) != 0b1100100101001001100 || (air & 0b11000110000000) != 0b11000110000000) {
                    break;
                }
            }
            reinforcementHeight = refHeight;
            height = actHeight;
            interiorArea = makeAABB(pivot, 0, 1, -1, 1, actHeight, -2, f);
            type = BlastFurnaceType.BIG_RIGHT;
        }
        else {
            reinforcementHeight = 0;
            height = 0;
            type = BlastFurnaceType.INVALID;
        }
        projectedAreaSize = calcProjectedAreaSize();
    }

    @Override
    protected AABB createRenderBoundingBox() {
        int x = getBlockPos().getX();
        int y = getBlockPos().getY();
        int z = getBlockPos().getZ();

        return new AABB(x - 16, y - 16, z - 16, x + 16, y + 16, z + 16);
    }

    private void playWorkingParticle(float height) {
        // 1/8 chance to generate a particle
        if ((Create.RANDOM.nextInt() & 0b111) == 0b111) {
            var center = interiorArea.getCenter();
            var yLoc = interiorArea.minY + height;
            level.addParticle(ParticleTypes.LAVA, false, center.x,yLoc,center.z, 0.2, 0.2, 0.2);
        }
    }

    public float contentFluidHeight() {
        var total = (tank1.getPrimaryHandler().getFluidAmount() + tank2.getPrimaryHandler().getFluidAmount()) / 1000f; // in buckets/blocks
        var percent = total / (tank1.getPrimaryHandler().getCapacity() + tank2.getPrimaryHandler().getCapacity());
        return percent * height;
    }

    private float calcProjectedAreaSize() {
        return (float) ((interiorArea.maxX - interiorArea.minX) * (interiorArea.maxY - interiorArea.minY));
    }

    public void process() {


        if (level.isClientSide) {
            return;
        }

        tank1.getPrimaryHandler().setFluid(new FluidStack(recipe.getFluidResults().get(0).getFluid(), tank1.getPrimaryHandler().getFluidAmount() + recipe.getFluidResults().get(0).getAmount()));
        tank2.getPrimaryHandler().setFluid(new FluidStack(recipe.getFluidResults().get(1).getFluid(), tank2.getPrimaryHandler().getFluidAmount() + recipe.getFluidResults().get(1).getAmount()));

    }

    public void acceptInsertedItems(float height) {
        List<ItemEntity> itemsToPick = getItemsToPick(height);
        for (ItemEntity itemEntity : itemsToPick) {
            ItemStack itemStack = itemEntity.getItem();
            if (itemStack.is(TFMGItems.COAL_COKE_DUST.get())) {

                int freeSpace = fuelInventory.getStackInSlot(0).getMaxStackSize() - fuelInventory.getStackInSlot(0).getCount();

                int coalCokeCount = itemStack.getCount();
                // if(itemStack == ItemStack.EMPTY)
                //    continue;

                if (coalCokeCount > freeSpace) {
                    itemStack.setCount(itemStack.getCount() - freeSpace);
                    fuelInventory.setItem(0, new ItemStack(TFMGItems.COAL_COKE_DUST.get(), fuelInventory.getStackInSlot(0).getCount() + freeSpace));
                }
                else {
                    fuelInventory.setItem(0, new ItemStack(TFMGItems.COAL_COKE_DUST.get(), fuelInventory.getStackInSlot(0).getCount() + itemStack.getCount()));
                    itemEntity.discard();
                }


            }
            else {


                int freeSpace = inputInventory.getStackInSlot(0).getMaxStackSize() - inputInventory.getStackInSlot(0).getCount();

                int count = itemStack.getCount();
                if (!inputInventory.isEmpty()) {
                    if (!inputInventory.getItem(0).is(itemStack.getItem())) {
                        continue;
                    }
                }

                if (count > freeSpace) {
                    itemStack.setCount(itemStack.getCount() - freeSpace);
                    inputInventory.setItem(0, new ItemStack(itemStack.getItem(), inputInventory.getStackInSlot(0).getCount() + freeSpace));
                }
                else {
                    inputInventory.setItem(0, new ItemStack(itemStack.getItem(), inputInventory.getStackInSlot(0).getCount() + itemStack.getCount()));
                    itemEntity.discard();
                }


            }
        }
    }


    @Override
    @SuppressWarnings("removal")
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        // Lang.translate("goggles.blast_furnace.height", timer)
        //         .style(ChatFormatting.LIGHT_PURPLE)
        //         .forGoggles(tooltip, 1);
        if (type == BlastFurnaceType.INVALID) {
            Lang.translate("goggles.blast_furnace.invalid")
                    .style(ChatFormatting.RED)
                    .forGoggles(tooltip, 1);
        }
        else {


            Lang.translate("goggles.blast_furnace.stats")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);


            if (timer > 0) {
                Lang.translate("goggles.blast_furnace.status.running")
                        .style(ChatFormatting.YELLOW)
                        .forGoggles(tooltip, 1);
            }
            else {
                Lang.translate("goggles.blast_furnace.status.off")
                        .style(ChatFormatting.YELLOW)
                        .forGoggles(tooltip, 1);
            }


            Lang.translate("goggles.blast_furnace.size_stats")
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip, 1);
            Lang.translate("goggles.blast_furnace.height", validHeight)
                    .style(ChatFormatting.GOLD)
                    .forGoggles(tooltip, 1);

            if (type == BlastFurnaceType.SMALL) {
                Lang.translate("goggles.blast_furnace.diameter.one")
                        .style(ChatFormatting.GOLD)
                        .forGoggles(tooltip, 1);
            }
            else {
                Lang.translate("goggles.blast_furnace.diameter.two")
                        .style(ChatFormatting.GOLD)
                        .forGoggles(tooltip, 1);
            }


            Lang.translate("goggles.misc.storage_info")
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip, 1);
            Lang.translate("goggles.blast_furnace.item_count", inputInventory.getStackInSlot(0).getCount())
                    .style(ChatFormatting.AQUA)
                    .forGoggles(tooltip, 1);
            Lang.translate("goggles.blast_furnace.fuel_amount", fuelInventory.getStackInSlot(0).getCount())
                    .style(ChatFormatting.AQUA)
                    .forGoggles(tooltip, 1);
            Lang.translate("goggles.blast_furnace.nothing_lol")
                    .style(ChatFormatting.AQUA)
                    .forGoggles(tooltip, 1);


        }


        //--Fluid Info--//
        LazyOptional<IFluidHandler> handler = this.getCapability(ForgeCapabilities.FLUID_HANDLER);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent()) {
            return false;
        }

        IFluidHandler tank = resolve.get();
        if (tank.getTanks() == 0) {
            return false;
        }

        LangBuilder mb = Lang.translate("generic.unit.millibuckets");


        boolean isEmpty = true;
        for (int i = 0; i < tank.getTanks(); i++) {
            FluidStack fluidStack = tank.getFluidInTank(i);
            if (fluidStack.isEmpty()) {
                continue;
            }

            Lang.fluidName(fluidStack)
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);

            Lang.builder()
                    .add(Lang.number(fluidStack.getAmount())
                            .add(mb)
                            .style(ChatFormatting.DARK_GREEN))
                    .text(ChatFormatting.GRAY, " / ")
                    .add(Lang.number(tank.getTankCapacity(i))
                            .add(mb)
                            .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip, 1);

            isEmpty = false;
        }

        if (tank.getTanks() > 1) {
            if (isEmpty) {
                tooltip.remove(tooltip.size() - 1);
            }
            return true;
        }

        if (!isEmpty) {
            return true;
        }

        Lang.translate("gui.goggles.fluid_container.capacity")
                .add(Lang.number(tank.getTankCapacity(0))
                        .add(mb)
                        .style(ChatFormatting.DARK_GREEN))
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip, 1);


        return true;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inputInventory.deserializeNBT(compound.getCompound("InputItems"));
        fuelInventory.deserializeNBT(compound.getCompound("Fuel"));
        timer = compound.getInt("Timer");
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.put("InputItems", inputInventory.serializeNBT());
        compound.put("Fuel", fuelInventory.serializeNBT());
        compound.putInt("Timer", timer);

    }

    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
    }

    @Nonnull
    @Override
    @SuppressWarnings("'net.minecraftforge.items.CapabilityItemHandler' is deprecated and marked for removal ")
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemCapability.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    public List<ItemEntity> getItemsToPick(float height) {
        return level.getEntitiesOfClass(ItemEntity.class, interiorArea.setMaxY(interiorArea.minY + Math.max(1, height)));
    }


    public enum BlastFurnaceType {
        SMALL,
        BIG_LEFT,
        BIG_RIGHT,
        INVALID
    }
}
