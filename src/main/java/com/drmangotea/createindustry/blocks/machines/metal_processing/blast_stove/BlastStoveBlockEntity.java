package com.drmangotea.createindustry.blocks.machines.metal_processing.blast_stove;

import com.drmangotea.createindustry.base.multiblock.FluidOutputBlockEntity;
import com.drmangotea.createindustry.base.multiblock.MultiblockMasterBlockEntity;
import com.drmangotea.createindustry.base.multiblock.MultiblockStructure;
import com.drmangotea.createindustry.base.multiblock.PositionUtil;
import com.drmangotea.createindustry.blocks.machines.firebox.FireboxBlock;
import com.drmangotea.createindustry.recipes.gas_blasting.GasBlastingRecipe;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;

import static com.drmangotea.createindustry.base.multiblock.PositionUtil.*;
import static com.drmangotea.createindustry.base.multiblock.PositionUtil.generateSequence;

public class BlastStoveBlockEntity extends MultiblockMasterBlockEntity implements IHaveGoggleInformation {
    private static final Object GasBlastingRecipesKey = new Object();
    public static final int maxSegments = 12;
    public static final int defaultSegments = 2;
    protected ScrollValueBehaviour segments;
    public GasBlastingRecipe recipe;
    public Direction getMasterDirection() {
        return this.getBlockState().getValue(BlastStoveBlock.FACING);
    }
    public BlockState mainWall = TFMGBlocks.FIREPROOF_BRICKS.get().defaultBlockState();
    public BlockState cinderFlourBlock = TFMGBlocks.CINDERFLOUR_BLOCK.get().defaultBlockState();
    public BlockState reinforcement = TFMGBlocks.FIREPROOF_BRICK_REINFORCEMENT.get().defaultBlockState();
    public BlastStoveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 8000, "blast_stove");
        this.timer = -1;
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(this.segments = new ScrollValueBehaviour(Lang.translateDirect("multiblock.blast_stove.segments_height"), this, new SegmentValueBox()));
        this.segments.between(2, 8);
        this.segments.value = defaultSegments;
    }
    
    public void lazyTick() {
        super.lazyTick();
        
    }
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        this.multiblockStructure = MultiblockStructure.cuboidBuilder(this).directional(getMasterDirection())
                .withBlockAt(new PositionUtil.PositionRange(generateSequence(-3, 0, 1), zero(), generateSequence(-1, 1, 1)).ignorePosition(0, 1, 0), mainWall)
                .withBlockAt(new PositionUtil.PositionRange(List.of(-4), zero(), generateSequence(-1, 1, 2)), reinforcement)
                .withFluidOutputAt(-4, 0, 0, "createindustry.blast_stove.tank1", 4000 * segments.getValue())
                .withFluidOutputAt(-4, 1, 0, "createindustry.blast_stove.tank2", 4000 * segments.getValue())
                .withFluidOutputAt(0, 1, 0, "createindustry.blast_stove.tank0", 4000 * segments.getValue())
                .withBlockAt(new PositionUtil.PositionRange(List.of(0, -1, -3), List.of(1), generateSequence(-1, 1, 2)), mainWall)
                .withBlockAt(new PositionUtil.PositionRange(List.of(-2, -4), List.of(1), generateSequence(-1, 1, 2)), reinforcement)
                .withBlockAt(-2, 1, 0, mainWall)
                //.withBlockAt(new PositionUtil.PositionRange(List.of(-1, -2), zero(), generateSequence(-2, 2, 1)), mainWall)
                //.withBlockAt(new PositionUtil.PositionRange(pos(-3), generateSequence(0, 4, 1), generateSequence(-1, 1, 1)), mainWall)
                //.withBlockAt(new PositionUtil.PositionRange(pos(-3), generateSequence(0, 3, 1), generateSequence(-2, 2, 4)), reinforcement)
                .withBlockAt(new PositionUtil.PositionRange(List.of(-1), generateSequence(2, 1 + segments.getValue(), 1), zero()), cinderFlourBlock)
                .withBlockAt(new PositionUtil.PositionRange(List.of(-1, -3), generateSequence(2, 1 + segments.getValue(), 1), generateSequence(-1, 1, 2)), mainWall)
                .withBlockAt(new PositionUtil.PositionRange(zero(), generateSequence(2, 1 + segments.getValue(), 1), generateSequence(-1, 1, 1)), mainWall)
                .withBlockAt(new PositionUtil.PositionRange(List.of(-2, -4), generateSequence(2, 1 + segments.getValue(), 1), generateSequence(-1, 1, 2)), reinforcement)
                .withBlockAt(new PositionUtil.PositionRange(List.of(-2, -4), generateSequence(2, 1 + segments.getValue(), 1), zero()), mainWall)
                .withBlockAt(new PositionUtil.PositionRange(List.of(-3), List.of(2 + segments.getValue()), generateSequence(-1, 1, 2)), mainWall)
                .withBlockAt(new PositionUtil.PositionRange(List.of(-1, -2), List.of(2 + segments.getValue()), generateSequence(-1, 1, 2)), reinforcement)
                .withBlockAt(-4, 2 + segments.getValue(), 0, mainWall)
                .withBlockAt(0, 2 + segments.getValue(), 0, reinforcement)
                .withBlockAt(new PositionUtil.PositionRange(zero(), List.of(2 + segments.getValue()), generateSequence(-1, 1, 2)), mainWall)
                .withBlockAt(new PositionUtil.PositionRange(List.of(0, -1, -2, -3), List.of(3 + segments.getValue()), zero()), mainWall)
                .withBlockAt(new PositionUtil.PositionRange(List.of(-1, -2), List.of(3 + segments.getValue()), generateSequence(-1, 1, 2)), mainWall)
                .build();
        if(!isValid)
            return;
        
        if (tankInventory.getCapacity() != masterTankCapacity * segments.getValue()) {
            this.tankInventory.setCapacity(masterTankCapacity * segments.getValue());
        }
        
        if(recipe !=null) {
            if (timer == -1 &&
                    (getMainFluidOutput().tankInventory.getFluidAmount() + recipe.getFluidResults().get(0).getAmount()) <= getMainFluidOutput().tankInventory.getCapacity() && (getSecondaryFluidOutput().tankInventory.getFluidAmount() + recipe.getFluidResults().get(1).getAmount()) <= getSecondaryFluidOutput().tankInventory.getCapacity()&&
                    canContinue()
            ) {
                timer = recipe.getProcessingDuration();
                tankInventory.drain(recipe.getFluidIngredients().get(0).getRequiredAmount(), IFluidHandler.FluidAction.EXECUTE);
                getSecondaryFluidInput().tankInventory.drain(recipe.getFluidIngredients().get(1).getRequiredAmount(), IFluidHandler.FluidAction.EXECUTE);
            }
        }
        
        findRecipe();
        setRunning(timer > 0);
        
        if (timer > 0 &&
                (getMainFluidOutput().tankInventory.getFluidAmount() + recipe.getFluidResults().get(0).getAmount()) <= getMainFluidOutput().tankInventory.getCapacity() && (getSecondaryFluidOutput().tankInventory.getFluidAmount() + recipe.getFluidResults().get(1).getAmount()) <= getSecondaryFluidOutput().tankInventory.getCapacity()&&canContinue()
        ) {
            timer--;
        }
        
        if (timer == 0) {
            process(getMainFluidOutput().tankInventory, getSecondaryFluidOutput().tankInventory);
            timer = -1;
        }
    }
    
    public boolean canContinue() {
        if (recipe == null)
            return false;
        return recipe.matches(tankInventory, getSecondaryFluidInput().tankInventory);
    }
    
    public void findRecipe(){
        CombinedTankWrapper tankIn = new CombinedTankWrapper(tankInventory,getSecondaryFluidInput().tankInventory);
        if (recipe == null || !recipe.matches(tankIn, level)) {
            GasBlastingRecipe recipe = getMatchingRecipes();
            if (recipe!=null) {
                this.recipe = recipe;
                sendData();
            }
        }
    }
    
    public void process(FluidTank mainOutput, FluidTank secondaryOutput) {
        if (level == null)
            return;
        if (level.isClientSide)
            return;
        
        mainOutput.setFluid(new FluidStack(recipe.getFluidResults().get(0).getFluid(), mainOutput.getFluidAmount()+recipe.getFluidResults().get(0).getAmount()));
        secondaryOutput.setFluid(new FluidStack(recipe.getFluidResults().get(1).getFluid(), secondaryOutput.getFluidAmount()+recipe.getFluidResults().get(1).getAmount()));
        
    }
    
    protected void setRunning(boolean running) {
        if (level == null)
            return;
        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlastStoveBlock.RUNNING, running));
        notifyUpdate();
    }
    
    public void invalidate() {
        super.invalidate();
    }
    
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
    }
    
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
    }
    
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if (recipe == null) {
            tooltip.add(Component.nullToEmpty("No recipe"));
            return true;
        } else {
            tooltip.add(Component.nullToEmpty("Recipe: " + recipe.getId()));
            tooltip.add(percent());
        }
        return true;
    }
    
    class GhostBlockDisplay extends ValueBoxTransform.Sided {
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(12.0, 8.0, 15.5);
        }
        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return !isValid && state.getValue(BlastStoveBlock.FACING) == direction;
        }
    }
    
    class SegmentValueBox extends ValueBoxTransform.Sided {
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(4.0, 8.0, 15.5);
        }
        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return !isValid && state.getValue(BlastStoveBlock.FACING) == direction;
        }
    }
    
    protected FluidOutputBlockEntity getMainFluidOutput() {
        if (level == null || multiblockStructure == null) {
            return null;
        }
        return (FluidOutputBlockEntity) level.getBlockEntity(multiblockStructure.getFluidOutputPosition("createindustry.blast_stove.tank2"));
    }
    
    protected FluidOutputBlockEntity getSecondaryFluidOutput() {
        if (level == null || multiblockStructure == null) {
            return null;
        }
        return (FluidOutputBlockEntity) level.getBlockEntity(multiblockStructure.getFluidOutputPosition("createindustry.blast_stove.tank0"));
    }
    
    protected FluidOutputBlockEntity getSecondaryFluidInput() {
        if (level == null || multiblockStructure == null) {
            return null;
        }
        return (FluidOutputBlockEntity) level.getBlockEntity(multiblockStructure.getFluidOutputPosition("createindustry.blast_stove.tank1"));
    }
    
    protected GasBlastingRecipe getMatchingRecipes() {
        
        
        List<Recipe<?>> list = RecipeFinder.get(getRecipeCacheKey(), level, this::matchStaticFilters);
        
        
        for(int i = 0; i < list.toArray().length;i++){
            GasBlastingRecipe recipe = (GasBlastingRecipe) list.get(i);
            for(int y = 0; y < recipe.getFluidIngredients().get(0).getMatchingFluidStacks().toArray().length;y++)
                if(tankInventory.getFluid().getFluid()==recipe.getFluidIngredients().get(0).getMatchingFluidStacks().get(y).getFluid())
                    if(tankInventory.getFluidAmount()>=recipe.getFluidIngredients().get(0).getRequiredAmount())
                        return recipe;
        }
        
        return null;
    }
    
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        
        return r instanceof GasBlastingRecipe;
        
        
        
    }
    
    protected Object getRecipeCacheKey() {
        return GasBlastingRecipesKey;
    }
    
    private MutableComponent percent() {
        float percent = Math.round((float) timer / recipe.getProcessingDuration() * 100);
        return Lang.builder().text(" ")
                .text(percent + "%")
                .component();
    }
}
