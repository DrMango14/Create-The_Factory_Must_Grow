package com.drmangotea.createindustry.base.multiblock;

import net.minecraft.core.BlockPos;

public class StackableMultiblockSegment {
    private final int maxSegmentHeight = 1;
    public MultiblockStructure segmentStructure;
    private int maxStackHeight = 4;
    
    public StackableMultiblockSegment(MultiblockStructure segmentStructure) {
        this.segmentStructure = segmentStructure;
    }
    
    public StackableMultiblockSegment maxStackHeight(int pMaxStackHeight) {
        maxStackHeight = pMaxStackHeight;
        return this;
    }
    
    public int getMaxStackHeight() {
        return maxStackHeight;
    }
    
    public int getMaxSegmentHeight() {
        return maxSegmentHeight;
    }
    
    public boolean isSegmentValid() {
        return segmentStructure.isStructureCorrect();
    }
    
    private String createSegmentIdentifier(BlockPos pos, String multiblockIdentifier) {
        return "(multiblockSegment:" + multiblockIdentifier + ":" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + ")";
    }
    
    public String getSegmentIdentifier(BlockPos pos, String multiblockIdentifier) {
        return createSegmentIdentifier(pos, multiblockIdentifier);
    }
    
    
}
