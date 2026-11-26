package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.content.devices.polarizer.PolarizerBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.SpoolItem;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class TFMGArmInteractionPoints {
    public static final Holder.Reference<ArmInteractionPointType>
        WINDING_MACHINE = register("winding_machine", WindingMachineType::new),
        POLARIZER = register("polarizer", PolarizerType::new),
        CASTING_BASIN = register("casting_basin", CastingBasinType::new);

    private static Holder.Reference<ArmInteractionPointType> register(String name, Supplier<? extends ArmInteractionPointType> factory) {
        return Registry.registerForHolder(
            CreateBuiltInRegistries.ARM_INTERACTION_POINT_TYPE,
            TFMG.asResource(name),
            factory.get()
        );
    }

    public static void prepare() {}

    public static class CastingBasinType extends ArmInteractionPointType {
        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return TFMGBlocks.CASTING_BASIN.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new CastingBasinPoint(this, level, pos, state);
        }
    }

    public static class CastingBasinPoint extends ArmInteractionPoint {
        public CastingBasinPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
            mode = Mode.TAKE;
        }

        @Override
        public void cycleMode() {}

        @Override
        protected Vec3 getInteractionPositionVector() {
            Direction facing = cachedState.getOptionalValue(HORIZONTAL_FACING).orElse(Direction.SOUTH);
            Vec3 offset = Vec3.atLowerCornerOf(facing.getNormal()).with(Direction.Axis.Y, -1).scale(0.125);
            return Vec3.atCenterOf(pos).add(offset);
        }

        @Override
        public void updateCachedState() {
            BlockState oldState = cachedState;
            super.updateCachedState();
            if (oldState != cachedState)
                cachedAngles = null;
        }

        // Insertion never happens because this interaction point is locked to extraction mode.
        // For extraction, the default behavior inherited from ArmInteractionPoint works fine.
    }

    public static class PolarizerType extends ArmInteractionPointType {
        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return TFMGBlocks.POLARIZER.has(state);
        }

        @Override
        public @Nullable ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new PolarizerPoint(this, level, pos, state);
        }
    }

    public static class PolarizerPoint extends ArmInteractionPoint {
        public PolarizerPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        public int getSlotCount(ArmBlockEntity armBlockEntity) {
            return 1;
        }

       @Override
       protected Vec3 getInteractionPositionVector() {
           return Vec3.atBottomCenterOf(pos).add(0, 1f, 0);
       }

        @Override
        public ItemStack insert(ArmBlockEntity armBlockEntity, ItemStack stack, boolean simulate) {
            if (!(level.getBlockEntity(pos) instanceof PolarizerBlockEntity machine))
                return stack;

            if (!machine.inventory.isEmpty() || !machine.outputInventory.isEmpty())
                return stack;

            return machine.inventory.insertItem(0, stack, simulate);
        }

        @Override
        public ItemStack extract(ArmBlockEntity armBlockEntity, int slot, int amount, boolean simulate) {
            if (!(level.getBlockEntity(pos) instanceof PolarizerBlockEntity machine))
                return ItemStack.EMPTY;

            if (machine.outputInventory.isEmpty())
                return ItemStack.EMPTY;

            return machine.outputInventory.extractItem(0, amount, simulate);
        }
    }

    public static class WindingMachineType extends ArmInteractionPointType {
        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return TFMGBlocks.WINDING_MACHINE.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new WindingMachinePoint(this, level, pos, state);
        }
    }

    public static class WindingMachinePoint extends ArmInteractionPoint {
        public WindingMachinePoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        protected Vec3 getInteractionPositionVector() {
            Direction facing = cachedState.getOptionalValue(HORIZONTAL_FACING).orElse(Direction.SOUTH);
            Vec3 offset = Vec3.atLowerCornerOf(facing.getNormal()).with(Direction.Axis.Y, 1).scale(0.5f);
            return Vec3.atCenterOf(pos).add(offset);
        }

        @Override
        public void updateCachedState() {
            BlockState oldState = cachedState;
            super.updateCachedState();
            if (oldState != cachedState)
                cachedAngles = null;
        }

        @Override
        public ItemStack insert(ArmBlockEntity armBE, ItemStack stack, boolean simulate) {
            if (!(stack.getItem() instanceof SpoolItem) || stack.is(TFMGItems.EMPTY_SPOOL))
                return stack;

            if (!(level.getBlockEntity(pos) instanceof WindingMachineBlockEntity machineBE))
                return stack;

            if (machineBE.hasAnySpool())
                return stack;

            if (!simulate) {
                machineBE.setSpool(stack);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack extract(ArmBlockEntity armBE, int slot, int amount, boolean simulate) {
            if (!(level.getBlockEntity(pos) instanceof WindingMachineBlockEntity machineBE))
                return ItemStack.EMPTY;

            ItemStack spool = machineBE.getSpool();

            if (!spool.is(TFMGItems.EMPTY_SPOOL))
                return ItemStack.EMPTY;

            if (!simulate) {
                machineBE.setSpool(ItemStack.EMPTY);
            }
            return spool;
        }
    }
}
