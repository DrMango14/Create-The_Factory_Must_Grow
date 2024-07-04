package com.drmangotea.createindustry.registry;


import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiFunction;

import static net.minecraft.core.Direction.SOUTH;
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
                .forDirectional(),




            CABLE_CONNECTOR = shape(6, 0, 6, 10, 9, 10)
            .forDirectional(),
            CABLE_CONNECTOR_MIDDLE = shape(6, 0, 6, 10, 16, 10)
            .forDirectional(),
            GALVANIC_CELL = shape(5, 10, 5, 11, 16, 16).add(1, 4, 6, 15, 10, 16)
            .forDirectional(),
            GENERATOR = shape(3, 0, 3, 13, 14, 13).add(0, 4, 0, 16, 10, 16)
            .forDirectional(),
            LIGHT_BULB = shape(5, 0, 5, 11, 9, 11)
            .forDirectional(),

            RESISTOR = shape(3, 0, 3, 13, 16, 13).add(1, 1, 13, 15, 15, 16)
            .forDirectional(),
            ROTOR = shape(3, 3, 2, 13, 13, 14)
            .forAxis(),
            VOLTMETER = shape(0, 0, 2, 16, 3, 14)
            .forDirectional(),

            DIAGONAL_CABLE_BLOCK_DOWN = shape(3, 3, 11, 13, 13, 16)
                    .add(3, 11, 3, 13, 16, 13)
                    .add(4, 4, 5, 12, 11, 12)
                    .forDirectional(),
            DIAGONAL_CABLE_BLOCK_UP = shape(3, 3, 0, 13, 13, 5)
                    .add(3, 11, 3, 13, 16, 13)
                    .add(4, 4, 5, 12, 11, 12)
                    .forDirectional(),

            CABLE_TUBE = shape(6, 0, 6, 10, 16, 10)
                    .forDirectional(),


            ELECTRICAL_SWITCH = shape(5, 0, 3, 11, 3, 13)
            .forHorizontalAxis(),
            ELECTRICAL_SWITCH_CEILING = shape(5, 13, 3, 11, 16, 13)
                    .forHorizontalAxis(),
            ELECTRICAL_SWITCH_WALL = shape(5, 3, 0, 11, 13, 3)
                    .forHorizontal(SOUTH)













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

    FULL = shape(0, 0, 0, 16, 16, 16).build(),

    SLAB = shape(0, 0, 0, 16, 8, 16).build();
;

    private static TFMGShapes.Builder shape(VoxelShape shape) {
        return new TFMGShapes.Builder(shape);
    }

    private static TFMGShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
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

        public TFMGShapes.Builder add(VoxelShape shape) {
            this.shape = Shapes.or(this.shape, shape);
            return this;
        }

        public TFMGShapes.Builder add(double x1, double y1, double z1, double x2, double y2, double z2) {
            return add(cuboid(x1, y1, z1, x2, y2, z2));
        }

        public TFMGShapes.Builder erase(double x1, double y1, double z1, double x2, double y2, double z2) {
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
