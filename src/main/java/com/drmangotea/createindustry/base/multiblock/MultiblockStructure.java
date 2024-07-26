package com.drmangotea.createindustry.base.multiblock;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.util.Triple;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.ghost.GhostBlockParams;
import com.simibubi.create.foundation.utility.ghost.GhostBlockRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.*;
import java.util.stream.Collectors;

public class MultiblockStructure {
    private final MultiblockMasterBlockEntity master;
    private final ArrayList<Map<BlockPos, BlockState>> structure;
    private final ArrayList<Map<BlockPos, String>> fluidOutputs = new ArrayList<>();
    private final ArrayList<Map<String, Integer>> fluidCapacities = new ArrayList<>();
    private final ArrayList<Map<BlockPos, String>> segments = new ArrayList<>();
    
    public MultiblockStructure(MultiblockMasterBlockEntity master, ArrayList<Map<BlockPos, BlockState>> structure) {
        this.master = master;
        this.structure = structure;
    }
    
    public MultiblockStructure addSegment(BlockPos pos, String identifier) {
        segments.add(Map.of(pos, identifier));
        return this;
    }
    
    public MultiblockStructure addFluidOutput(BlockPos pos, String identifier) {
        fluidOutputs.add(Map.of(pos, identifier));
        return this;
    }
    public MultiblockStructure addFluidOutputs(List<Map<BlockPos, String>> fluidOutputs, List<Map<String, Integer>> fluidCapacities) {
        this.fluidOutputs.addAll(fluidOutputs);
        this.fluidCapacities.addAll(fluidCapacities);
        return this;
    }
    
    public static CuboidBuilder cuboidBuilder(MultiblockMasterBlockEntity master) {
        return new CuboidBuilder(master);
    }
    
    public static class CuboidBuilder {
        private final MultiblockMasterBlockEntity master;
        private Direction direction = null;
        private boolean isDirectional = false;
        private final ArrayList<Map<BlockPos, BlockState>> structure = new ArrayList<>();
        private final ArrayList<Map<BlockPos, String>> fluidOutputs = new ArrayList<>();
        private final ArrayList<Map<String, Integer>> fluidCapacities = new ArrayList<>();
        private final ArrayList<Map<BlockPos, String>> segments = new ArrayList<>();
        private int width = 0;
        private int height = 0;
        private int depth = 0;
        
        public CuboidBuilder(MultiblockMasterBlockEntity master) {
            this.master = master;
        }
        
        public MultiblockMasterBlockEntity getMaster() {
            return master;
        }
        
        public BlockPos getMasterPosition() {
            return getMaster().getBlockPos();
        }
        
        public CuboidBuilder cube(int size) {
            return withSize(size, size, size);
        }
        
        public CuboidBuilder withSize(int width, int height) {
            return withSize(width, height, width);
        }
        
        public CuboidBuilder withSize(int width, int height, int depth) {
            this.width = width;
            this.height = height;
            this.depth = depth;
            return this;
        }
        
        public CuboidBuilder directional(Direction direction) {
            this.direction = direction;
            this.isDirectional = true;
            return this;
        }
        
        public Triple<Integer, Integer, Integer> getSize() {
            return Triple.of(width, height, depth);
        }
        
        public int getWidth() {
            return getSize().getFirst();
        }
        
        public int getHeight() {
            return getSize().getSecond();
        }
        
        public int getDepth() {
            return getSize().getThird();
        }
        
        public CuboidBuilder withFluidOutputAt(int x, int y, int z, String identifier, int capacity) {
            BlockPos pos = translateToMaster(x, y, z);
            fluidOutputs.add(Map.of(pos, identifier));
            fluidCapacities.add(Map.of(identifier, capacity));
            return withBlockAt(x, y, z, TFMGBlocks.FLUID_OUTPUT.get().defaultBlockState());
        }
        
        public CuboidBuilder withBlockAt(int x, int z, ScrollValueBehaviour y, BlockState block) {
            return withBlockAt(x, y.getValue(), z, block);
        }
        
        public CuboidBuilder withBlockAt(int x, int y, int z, BlockState block) {
            BlockPos pos = translateToMaster(x, y, z);
            structure.add(Map.of(pos, block));
            return this;
        }
        
        public CuboidBuilder withBlockAt(PositionUtil.PositionRange range, BlockState block) {
            for (Triple<Integer, Integer, Integer> pos : range.getPositions()) {
                structure.add(Map.of(translateToMaster(pos.getFirst(), pos.getSecond(), pos.getThird()), block));
            }
            return this;
        }
        
        public CuboidBuilder withSegmentAt(int x, int y, int z, StackableMultiblockSegment segment) {
            for (Map<BlockPos, BlockState> map : segment.segmentStructure.getStructure()) {
                for (BlockPos pos : map.keySet()) {
                    segments.add(Map.of(translateToMaster(x + pos.getX(), y + pos.getY(), z + pos.getZ()), segment.getSegmentIdentifier(pos, master.getMultiblockIdentifier())));
                    withBlockAt(x + pos.getX(), y + pos.getY(), z + pos.getZ(), map.get(pos));
                }
            }
            return this;
        }
        
        private BlockPos translateToMaster(int x, int y, int z) {
            BlockPos masterPos = getMasterPosition();
            if (isDirectional) {
                return masterPos.relative(direction, x).above(y).relative(direction.getClockWise(), z);
            }
            return masterPos.offset(x, y, z);
        }
        
        public MultiblockStructure build() {
            return new MultiblockStructure(getMaster(), structure).addFluidOutputs(fluidOutputs, fluidCapacities);
        }
    }
    
    private Triple<Integer, Integer, Integer> translateToRelative(BlockPos pos, Direction direction) {
        BlockPos masterPos = getMasterPosition();
        int x = pos.getX() - masterPos.getX();
        int y = pos.getY() - masterPos.getY();
        int z = pos.getZ() - masterPos.getZ();
        if (direction == null) {
            return Triple.of(x, y, z);
        }
        return Triple.of(direction.getAxis() == Direction.Axis.X ? x : direction.getAxis() == Direction.Axis.Y ? y : z, y, direction.getAxis() == Direction.Axis.Z ? x : direction.getAxis() == Direction.Axis.Y ? z : y);
    }
    
    public ArrayList<Map<BlockPos, BlockState>> getStructure() {
        return structure;
    }
    
    public BlockEntity getMaster() {
        return master;
    }
    
    public ArrayList<Map<BlockPos, String>> getFluidOutputs() {
        return fluidOutputs;
    }
    
    public ArrayList<FluidOutputBlockEntity> getFluidOutputBlockEntities() {
        ArrayList<FluidOutputBlockEntity> fluidOutputBlockEntities = new ArrayList<>();
        for (Map<BlockPos, String> map : fluidOutputs) {
            for (BlockPos pos : map.keySet()) {
                BlockEntity blockEntity = master.getLevel().getBlockEntity(pos);
                if (blockEntity instanceof FluidOutputBlockEntity) {
                    fluidOutputBlockEntities.add((FluidOutputBlockEntity) blockEntity);
                }
            }
        }
        return fluidOutputBlockEntities;
    }
    
    public BlockPos getFluidOutputPosition(String identifier) {
        for (Map<BlockPos, String> map : fluidOutputs) {
            for (BlockPos pos : map.keySet()) {
                if (map.get(pos).equals(identifier)) {
                    return pos;
                }
            }
        }
        return null;
    }
    
    public BlockPos getMasterPosition() {
        return getMaster().getBlockPos();
    }
    
    public BlockState getMasterBlockState() {
        return getMaster().getLevel().getBlockState(getMasterPosition());
    }
    
    public Direction getMasterDirection() {
        return getMasterBlockState().getValue(BlockStateProperties.FACING);
    }
    
    public boolean isBlockCorrect(BlockPos pos) {
        if (master.getLevel() == null) return false;
        for (Map<BlockPos, BlockState> map : getStructure()) {
            if (map.containsKey(pos)) {
                return master.getLevel().getBlockState(pos).is(map.get(pos).getBlock());
            }
        }
        return false;
    }
    
    public void renderGhostBlocksByYLevel() {
        if (master.getLevel() == null) {
            CreateTFMG.LOGGER.error("Level is null, cannot render ghost blocks");
            return;
        }
        
        // Group blocks by their y-levels
        Map<Integer, List<BlockPos>> yLevelMap = new HashMap<>();
        for (Map<BlockPos, BlockState> map : getStructure()) {
            for (BlockPos pos : map.keySet()) {
                yLevelMap.computeIfAbsent(pos.getY(), k -> new ArrayList<>()).add(pos);
            }
        }
        
        // Check each y-level for completeness and render ghost blocks for the first incomplete y-level
        for (Integer yLevel : yLevelMap.keySet().stream().sorted().toList()) {
            boolean isComplete = true;
            for (BlockPos pos : yLevelMap.get(yLevel)) {
                if (!isBlockCorrect(pos)) {
                    isComplete = false;
                    break;
                }
            }
            
            if (!isComplete) {
                for (BlockPos pos : yLevelMap.get(yLevel)) {
                    for (Map<BlockPos, BlockState> map : getStructure()) {
                        if (!isBlockCorrect(pos)) {
                            BlockState blockState = map.get(pos);
                            if (blockState != null)
                                CreateClient.GHOST_BLOCKS.showGhost(pos, GhostBlockRenderer.standard(), GhostBlockParams.of(blockState).at(pos).breathingAlpha(), 1);
                        }
                    }
                }
                break; // Stop after rendering the first incomplete y-level
            }
        }
    }
    
    public void renderGhostBlocks() {
        if (master.getLevel() == null) {
            CreateTFMG.LOGGER.error("Level is null, cannot render ghost blocks");
            return;
        }
        
        for (Map<BlockPos, BlockState> map : getStructure()) {
            for (BlockPos pos : map.keySet()) {
                if (!isBlockCorrect(pos)) {
                    CreateClient.GHOST_BLOCKS.showGhost(pos, GhostBlockRenderer.standard(), GhostBlockParams.of(map.get(pos)).at(pos).breathingAlpha(), 1);
                }
            }
        }
    }
    
    public boolean isStructureCorrect() {
        for (Map<BlockPos, BlockState> map : getStructure()) {
            for (BlockPos pos : map.keySet()) {
                if (!isBlockCorrect(pos)) {
                    return false;
                }
            }
        }
        return true;
    }
    public void setFluidOutputCapacities() {
        if (master.getLevel() == null) return;
        for (Map<String, Integer> map : fluidCapacities) {
            for (String identifier : map.keySet()) {
                FluidOutputBlockEntity fluidOutput = master.getLevel().getBlockEntity(getFluidOutputPosition(identifier)) instanceof FluidOutputBlockEntity ? (FluidOutputBlockEntity) master.getLevel().getBlockEntity(getFluidOutputPosition(identifier)) : null;
                setFluidOutputCapacity(fluidOutput, isStructureCorrect() ? map.get(identifier) : 0);
            }
        }
    }
    
    public void setFluidOutputCapacity(FluidOutputBlockEntity fluidOutput, int value) {
        if (fluidOutput == null) return;
        if (fluidOutput.tankInventory.getCapacity() == value) return;
        fluidOutput.tankInventory.setCapacity(value);
    }
    
    public void addFluidTooltip(List<Component> tooltip, Optional<IFluidHandler> resolve, BlockPos fluidOutput) {
            if (!resolve.isPresent()) {
                return;
            } else {
                IFluidHandler tank = resolve.get();
                if (tank.getTanks() == 0) {
                    return;
                } else {
                    LangBuilder tankName = null;
                    for (Map<BlockPos, String> map : getFluidOutputs()) {
                        for (BlockPos pos : map.keySet()) {
                            if (pos.equals(fluidOutput)) {
                                tankName = Lang.translate(map.get(pos));
                            }
                        }
                    }
                    LangBuilder mb = Lang.translate("generic.unit.millibuckets");
                    boolean isEmpty = true;
                    if (tankName != null) {
                        tankName.style(ChatFormatting.WHITE).forGoggles(tooltip, 1);
                    }
                    
                    for(int i = 0; i < tank.getTanks(); ++i) {
                        FluidStack fluidStack = tank.getFluidInTank(i);
                        if (!fluidStack.isEmpty()) {
                            Lang.fluidName(fluidStack).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                            Lang.builder().add(Lang.number(fluidStack.getAmount()).add(mb).style(ChatFormatting.GOLD)).text(ChatFormatting.GRAY, " / ").add(Lang.number((double)tank.getTankCapacity(i)).add(mb).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
                            isEmpty = false;
                        }
                    }
                    
                    if (tank.getTanks() > 1) {
                        if (isEmpty) {
                            tooltip.remove(tooltip.size() - 1);
                        }
                        
                        return;
                    } else if (!isEmpty) {
                        return;
                    } else {
                        Lang.translate("gui.goggles.fluid_container.capacity", new Object[0]).add(Lang.number(tank.getTankCapacity(0)).add(mb).style(ChatFormatting.DARK_GREEN)).style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
                        return;
                    }
                }
            }
    }
    
    public void addToGoggleTooltip(List<Component> tooltip) {
        for (FluidOutputBlockEntity fluidOutput : getFluidOutputBlockEntities()) {
            LazyOptional<IFluidHandler> handler = fluidOutput.getCapability(ForgeCapabilities.FLUID_HANDLER);
            Optional<IFluidHandler> resolve = handler.resolve();
            addFluidTooltip(tooltip, resolve, fluidOutput.getBlockPos());
        }
    }
}
