package com.drmangotea.createindustry.base.multiblock;

import com.drmangotea.createindustry.base.util.Triple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class PositionUtil {
    
    
    public static BlockPos getLeftSidePos(BlockPos pos, Direction direction) {
        return pos.relative(direction.getCounterClockWise());
    }
    public static BlockPos getLeftSidePos(BlockPos pos, int distance, Direction direction) {
        return pos.relative(direction.getCounterClockWise(), distance);
    }
    public static BlockPos getRightSidePos(BlockPos pos, Direction direction) {
        return pos.relative(direction.getClockWise());
    }
    public static BlockPos getRightSidePos(BlockPos pos, int distance, Direction direction) {
        return pos.relative(direction.getClockWise(), distance);
    }
    
    public static List<Integer> zero() {
        return List.of(0);
    }
    public static List<Integer> pos(int pos) {
        return List.of(pos);
    }
    
    public static List<Integer> generateSequence(int start, int end, int step) {
        List<Integer> sequence = new ArrayList<>();
        for (int i = start; i <= end; i += step) {
            sequence.add(i);
        }
        return sequence;
    }
    
    public static class PositionRange {
        private final List<Integer> xRange;
        private final List<Integer> yRange;
        private final List<Integer> zRange;
        private final List<Triple<Integer, Integer, Integer>> toIgnore = new ArrayList<>();
        
        public PositionRange(List<Integer> xRange, List<Integer> yRange, List<Integer> zRange) {
            this.xRange = xRange;
            this.yRange = yRange;
            this.zRange = zRange;
        }
        
        public PositionRange ignorePosition(int x, int y, int z) {
            toIgnore.add(Triple.of(x, y, z));
            return this;
        }
        
        public List<Integer> getXRange() {
            return xRange;
        }
        
        public List<Integer> getYRange() {
            return yRange;
        }
        
        public List<Integer> getZRange() {
            return zRange;
        }
        
        public List<Triple<Integer, Integer, Integer>> getPositions() {
            List<Triple<Integer, Integer, Integer>> positions = new ArrayList<>();
            for (int x : xRange) {
                for (int y : yRange) {
                    for (int z : zRange) {
                        if (toIgnore.contains(Triple.of(x, y, z))) {
                            continue;
                        }
                        if (x == 0 && y == 0 && z == 0) {
                            continue;
                        }
                        positions.add(Triple.of(x, y, z));
                    }
                }
            }
            return positions;
        }
    }
}
