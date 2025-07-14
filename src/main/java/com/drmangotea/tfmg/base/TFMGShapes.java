package com.drmangotea.tfmg.base;


import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiFunction;

import static net.minecraft.core.Direction.*;

public class TFMGShapes {
    public static final VoxelShaper
            ENGINE = shape(0, 0, 0, 16, 7, 16).add(3, 7, 0, 13, 12, 16)
            .forDirectional(SOUTH),
            ENGINE_GEARBOX = shape(0, 0, -1, 3, 5, 15)
                    .add(13, 0, -1, 16, 5, 15)
                    .add(3, 0, -1, 13, 12, 11)
                    .add(3, 0, 11, 13, 11, 15)
                    .add(13, 5, 3, 16, 13, 13)
                    .add(0, 5, 3, 3, 13, 13)
                    .add(13, 5, -1, 16, 7, 1)
                    .add(0, 5, -1, 3, 7, 1)
                    .add(15, 5, 9, 15, 11, 9)
                    .forDirectional(NORTH),
            TURBINE_ENGINE_FRONT = shape(2, 2, 0, 14, 14, 16)
                    .add(3, 0, 8, 13, 2, 16)
                    .forHorizontal(SOUTH),
            TURBINE_ENGINE_MIDDLE = shape(11, 0, 2, 16, 5, 14)
                    .add(1, 1, 0, 15, 15, 16)
                    .add(0, 0, 2, 5, 5, 14)
                    .forHorizontal(SOUTH),
            TURBINE_ENGINE_BACK = shape(5.3, 0, 5, 11.3, 3, 14)
                    .add(3, 3, 2, 13, 13, 16)
                    .forHorizontal(SOUTH),
            ENGINE_CONTROLLER = shape(0, 0, 0, 4, 5, 16)
                    .add(2, 5, 5, 14, 12, 14)
                    .add(4, 0, 5, 15, 5, 16)
                    .forHorizontal(SOUTH),
            ENGINE_FRONT = shape(0, 0, 1, 16, 7, 16).add(3, 7, 1, 13, 12, 16)
                    .forDirectional(SOUTH),
            PUMPJACK_HAMMER_PART = shape(0, 2, 0, 16, 14, 16)
                    .forDirectional(),
            RADIAL_ENGINE_SINGLE = shape(1, 4, 1, 15, 12, 15)
                    .forDirectional(),
            RADIAL_ENGINE_SIDE = shape(1, 4, 1, 15, 16, 15)
                    .forDirectional(),
            RADIAL_ENGINE_MIDDLE = shape(1, 0, 1, 15, 16, 15)
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
            RESISTOR = shape(6, 0, 3, 10, 4, 13)
                    .forDirectional(),
            GENERATOR = shape(3, 0, 3, 13, 14, 13).add(0, 4, 0, 16, 10, 16)
                    .forDirectional(),
            LIGHT_BULB = shape(5, 0, 5, 11, 9, 11)
                    .forDirectional(),
            MODERN_LIGHT = shape(0, 0, 0, 16, 3, 16)
                    .forDirectional(),
            CIRCULAR_LIGHT = shape(3, 0, 3, 13, 10, 13)
                    .forDirectional(),
            ALUMINUM_LAMP = shape(3, 0, 3, 13, 2, 13).add(4, 2, 4, 12, 3, 12)
                    .forDirectional(),
            POTENTIOMETER = shape(3, 0, 3, 13, 16, 13).add(1, 1, 13, 15, 15, 16)
                    .forDirectional(),
            WINDING_MACHINE = shape(0, 0, 0, 16, 8, 16).add(0, 8, 4, 10, 12, 12)
                    .forHorizontal(NORTH),
            RESISTOR_VERTICAL = shape(3, 0, 3, 13, 16, 13)
                    .forDirectional(),
            BLAST_FURNACE_REINFORCEMENT_WALL = shape(0, 0, 0, 16, 6, 16)
                    .forDirectional(),

    ROTOR = shape(4, 5, 4, 12, 11, 12).add(5, 0, 5, 11, 16, 11)
            .forDirectional(),
            STATOR = shape(5, 1, 0, 16, 15.2, 5).add(5, 1, 5, 10, 15.2, 10).add(0, 1, 0, 5, 15.2, 16)
                    .forDirectional(),
            STATOR_ROTATED = shape(5, 1, 11, 16, 15, 16).add(5, 1, 6, 10, 15, 11).add(0, 1, 0, 5, 15, 16)
                    .forDirectional(),
            STATOR_VERTICAL = shape(5, 0, 1, 16, 5, 15).add(5, 5, 1, 10, 10, 15).add(0, 0, 1, 5, 16, 15)
                    .forDirectional(),
            VOLTMETER = shape(0, 0, 2, 16, 3, 14)
                    .forDirectional(),
            ELECTRIC_PUMP = shape(2, 6, 2, 14, 10, 14)
                    .add(4, 3, 4, 12, 11, 12)
                    .add(3, 11, 3, 13, 16, 13)
                    .add(3, 0, 3, 13, 5, 13)
                    .forDirectional(),
            DIAGONAL_CABLE_BLOCK_DOWN = shape(3, 3, 11, 13, 13, 16)
                    .add(3, 11, 3, 13, 16, 13)
                    .add(4, 4, 5, 12, 11, 12)
                    .forDirectional(),
            DIAGONAL_CABLE_BLOCK_UP = shape(3, 3, 0, 13, 13, 5)
                    .add(3, 11, 3, 13, 16, 13)
                    .add(4, 4, 5, 12, 11, 12)
                    .forDirectional(),
            CASTING_BASIN = shape(0, 0, 0, 16, 8, 16)
                    .add(4, 8, 14, 12, 13, 16)
                    .forHorizontal(NORTH),
            TRANSFORMER = shape(0, 0, 0, 16, 6, 16)
                    .add(1, 6, 5, 15, 15, 11)
                    .forHorizontal(NORTH),
            CABLE_TUBE = shape(6, 0, 6, 10, 16, 10)
                    .forDirectional(),
            REBAR_PILLAR = shape(3, 0, 3, 13, 16, 13)
                    .forDirectional(),
            ELECTRICAL_SWITCH = shape(5, 0, 3, 11, 3, 13)
                    .forHorizontalAxis(),
            ELECTRICAL_SWITCH_CEILING = shape(5, 13, 3, 11, 16, 13)
                    .forHorizontalAxis(),
            ELECTRICAL_SWITCH_WALL = shape(5, 3, 0, 11, 13, 3)
                    .forHorizontal(SOUTH),

    POLARIZER = shape(4, 8, 0, 12, 12, 2)
            .add(5, 8, 14, 11, 11, 16)
            .add(11, 8, 4, 15, 12, 11)
            .add(1, 8, 4, 5, 12, 11)
            .add(0, 0, 0, 16, 8, 16)
            .forHorizontal(NORTH);
    public static final VoxelShape

            EMPTY = shape(0, 0, 0, 0, 0, 0).build(),
            PUMPJACK_CRANK = shape(0, 0, 0, 16, 8, 16).build(),
            INDUSTRIAL_PIPE = shape(4, 0, 4, 12, 16, 12).build(),
            FLARESTACK = shape(3, 0, 3, 13, 14, 14).build(),
            PUMPJACK_BASE = shape(3, 0, 3, 13, 16, 13).build(),
            TRAFFIC_LIGHT = shape(3, 0, 3, 13, 16, 13).build(),
            REBAR_FLOOR = shape(0, 4, 0, 16, 12, 16)
                    .build(),
            SURFACE_SCANNER = shape(2, 0, 2, 14, 14, 14).build(),
            FULL = shape(0, 0, 0, 16, 16, 16).build(),
            ELECTRIC_POST = shape(4, 0, 4, 12, 16, 12).build(),
            SLAB = shape(0, 0, 0, 16, 8, 16).build();
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
