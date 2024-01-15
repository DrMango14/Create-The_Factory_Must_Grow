package com.drmangotea.tfmg.registry;


import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiFunction;

import static net.minecraft.core.Direction.UP;

public class TFMGShapes {

    public static final VoxelShaper
            ENGINE_BACK = shape(3, 0, 3, 13, 16, 16)
                .forDirectional(),
            ENGINE_BACK_VERTICAL = shape(3, 0, 3, 16, 16, 13)
                    .forDirectional(),
            ENGINE_VERTICAL = shape(3, 0, 3, 13, 14, 16)
                .forDirectional(),
            ENGINE = shape(3, 0, 3, 13, 14, 16)
                .forDirectional(),

            PUMPJACK_HAMMER_PART = shape(0, 2, 0, 16, 14, 16)
                .forDirectional(),

            RADIAL_ENGINE = shape(1, 4, 1, 15, 12, 15)
                .forDirectional(),
            LARGE_RADIAL_ENGINE = shape(-3, 4, -3, 19, 12, 19)
                    .forDirectional(),

            PUMPJACK_HEAD = shape(1, 0, -4, 15, 14, 24)
                .forDirectional(),



            COMPACT_ENGINE_VERTICAL = shape(3, 0, 3, 13, 14, 14)
                .forDirectional(),
            COMPACT_ENGINE = shape(3, 0, 3, 13, 14, 14)
                .forDirectional()



    ;
    public static final VoxelShape


    EMPTY = shape(0, 0, 0, 0, 0, 0).build(),
    PUMPJACK_CRANK = shape(0, 0, 0, 16, 8, 16).build(),
    INDUSTRIAL_PIPE = shape(4, 0, 4, 12, 16, 12).build(),
    FLARESTACK = shape(3, 0, 3, 13, 14, 14).build(),
    PUMPJACK_BASE = shape(3, 0, 3, 13, 16, 13).build(),

    CASTING_SPOUT = shape(1, 2, 1, 15, 14, 15)
            .build(),
    SURFACE_SCANNER = shape(2, 0, 2, 14, 14, 14).build(),

    FULL = shape(0, 0, 0, 16, 16, 16).build();
;

    private static Builder shape(VoxelShape shape) {
        return new Builder(shape);
    }

    private static Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }

    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }

    public static class Builder {

        private VoxelShape shape;

        public Builder(VoxelShape shape) {
            this.shape = shape;
        }

        public Builder add(VoxelShape shape) {
            this.shape = Shapes.or(this.shape, shape);
            return this;
        }

        public Builder add(double x1, double y1, double z1, double x2, double y2, double z2) {
            return add(cuboid(x1, y1, z1, x2, y2, z2));
        }

        public Builder erase(double x1, double y1, double z1, double x2, double y2, double z2) {
            this.shape = Shapes.join(shape, cuboid(x1, y1, z1, x2, y2, z2), BooleanOp.ONLY_FIRST);
            return this;
        }

        public VoxelShape build() {
            return shape;
        }

        public VoxelShaper build(BiFunction<VoxelShape, Direction, VoxelShaper> factory, Direction direction) {
            return factory.apply(shape, direction);
        }

        public VoxelShaper build(BiFunction<VoxelShape, Axis, VoxelShaper> factory, Axis axis) {
            return factory.apply(shape, axis);
        }

        public VoxelShaper forDirectional(Direction direction) {
            return build(VoxelShaper::forDirectional, direction);
        }

        public VoxelShaper forAxis() {
            return build(VoxelShaper::forAxis, Axis.Y);
        }

        public VoxelShaper forHorizontalAxis() {
            return build(VoxelShaper::forHorizontalAxis, Axis.Z);
        }

        public VoxelShaper forHorizontal(Direction direction) {
            return build(VoxelShaper::forHorizontal, direction);
        }

        public VoxelShaper forDirectional() {
            return forDirectional(UP);
        }

    }

}
