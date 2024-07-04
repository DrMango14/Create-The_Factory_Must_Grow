package com.drmangotea.createindustry.mixins;


import com.drmangotea.createindustry.base.TFMGPipes;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.AxisPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.VanillaFluidTargets;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(FluidPropagator.class)
public class FluidPropagatorMixin {
    @Shadow
    public static CreateAdvancement[] getSharedTriggers() {
        return new CreateAdvancement[] { AllAdvancements.WATER_SUPPLY, AllAdvancements.CROSS_STREAMS,
                AllAdvancements.HONEY_DRAIN };
    }


    /**
     * @author DrMangoTea
     * @reason felt like it
     */
    @Overwrite( remap = false)
    public static void propagateChangedPipe(LevelAccessor world, BlockPos pipePos, BlockState pipeState) {
        List<Pair<Integer, BlockPos>> frontier = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Set<Pair<PumpBlockEntity, Direction>> discoveredPumps = new HashSet<>();

        frontier.add(Pair.of(0, pipePos));


        // Visit all connected pumps to update their network
        while (!frontier.isEmpty()) {

            Pair<Integer, BlockPos> pair = frontier.remove(0);
            BlockPos currentPos = pair.getSecond();
            if (visited.contains(currentPos))
                continue;
            visited.add(currentPos);
            BlockState currentState = currentPos.equals(pipePos) ? pipeState : world.getBlockState(currentPos);
            FluidTransportBehaviour pipe = getPipe(world, currentPos);
            if (pipe == null)
                continue;
            pipe.wipePressure();

            for (Direction direction : getPipeConnections(currentState, pipe)) {
                BlockPos target = currentPos.relative(direction);
                if (world instanceof Level l && !l.isLoaded(target))
                    continue;

                BlockEntity tileEntity = world.getBlockEntity(target);
                BlockState targetState = world.getBlockState(target);
                if (tileEntity instanceof PumpBlockEntity) {
                    if (
                            !TFMGPipes.STEEL_MECHANICAL_PUMP.has(targetState)&&
                                    !AllBlocks.MECHANICAL_PUMP.has(targetState)&&
                    !TFMGPipes.BRASS_MECHANICAL_PUMP.has(targetState)&&
                    !TFMGPipes.CAST_IRON_MECHANICAL_PUMP.has(targetState)&&
                    !TFMGPipes.ALUMINUM_MECHANICAL_PUMP.has(targetState)&&
                    !TFMGPipes.PLASTIC_MECHANICAL_PUMP.has(targetState)
                                    || targetState.getValue(PumpBlock.FACING)
                            .getAxis() != direction.getAxis())
                        continue;
                    discoveredPumps.add(Pair.of((PumpBlockEntity) tileEntity, direction.getOpposite()));
                    continue;
                }
                if (visited.contains(target))
                    continue;
                FluidTransportBehaviour targetPipe = getPipe(world, target);
                if (targetPipe == null)
                    continue;
                Integer distance = pair.getFirst();
                if (distance >= getPumpRange() && !targetPipe.hasAnyPressure())
                    continue;
                if (targetPipe.canHaveFlowToward(targetState, direction.getOpposite()))
                    frontier.add(Pair.of(distance + 1, target));
            }
        }

        discoveredPumps.forEach(pair -> pair.getFirst()
                .updatePipesOnSide(pair.getSecond()));
    }

    @Shadow
    public static FluidTransportBehaviour getPipe(BlockGetter reader, BlockPos pos) {
        return BlockEntityBehaviour.get(reader, pos, FluidTransportBehaviour.TYPE);
    }
    @Shadow
    public static boolean isOpenEnd(BlockGetter reader, BlockPos pos, Direction side) {
        BlockPos connectedPos = pos.relative(side);
        BlockState connectedState = reader.getBlockState(connectedPos);
        FluidTransportBehaviour pipe = FluidPropagator.getPipe(reader, connectedPos);
        if (pipe != null && pipe.canHaveFlowToward(connectedState, side.getOpposite()))
            return false;
        if (PumpBlock.isPump(connectedState) && connectedState.getValue(PumpBlock.FACING)
                .getAxis() == side.getAxis())
            return false;
        if (VanillaFluidTargets.shouldPipesConnectTo(connectedState))
            return true;
        if (BlockHelper.hasBlockSolidSide(connectedState, reader, connectedPos, side.getOpposite())
                && !AllTags.AllBlockTags.FAN_TRANSPARENT.matches(connectedState))
            return false;
        if (hasFluidCapability(reader, connectedPos, side.getOpposite()))
            return false;
        if (!(connectedState.getMaterial()
                .isReplaceable() && connectedState.getDestroySpeed(reader, connectedPos) != -1)
                && !connectedState.hasProperty(BlockStateProperties.WATERLOGGED))
            return false;
        return true;
    }
    @Shadow
    public static List<Direction> getPipeConnections(BlockState state, FluidTransportBehaviour pipe) {
        List<Direction> list = new ArrayList<>();
        for (Direction d : Iterate.directions)
            if (pipe.canHaveFlowToward(state, d))
                list.add(d);
        return list;
    }
    @Shadow
    public static int getPumpRange() {
        return AllConfigs.server().fluids.mechanicalPumpRange.get();
    }
    @Shadow
    public static boolean hasFluidCapability(BlockGetter world, BlockPos pos, Direction side) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity == null)
            return false;
        LazyOptional<IFluidHandler> capability =
                tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
        return capability.isPresent();
    }
    @Shadow
    @Nullable
    public static Direction.Axis getStraightPipeAxis(BlockState state) {
        if (state.getBlock() instanceof PumpBlock)
            return state.getValue(PumpBlock.FACING)
                    .getAxis();
        if (state.getBlock() instanceof AxisPipeBlock)
            return state.getValue(AxisPipeBlock.AXIS);
        if (!FluidPipeBlock.isPipe(state))
            return null;
        Direction.Axis axisFound = null;
        int connections = 0;
        for (Direction.Axis axis : Iterate.axes) {
            Direction d1 = Direction.get(Direction.AxisDirection.NEGATIVE, axis);
            Direction d2 = Direction.get(Direction.AxisDirection.POSITIVE, axis);
            boolean openAt1 = FluidPipeBlock.isOpenAt(state, d1);
            boolean openAt2 = FluidPipeBlock.isOpenAt(state, d2);
            if (openAt1)
                connections++;
            if (openAt2)
                connections++;
            if (openAt1 && openAt2)
                if (axisFound != null)
                    return null;
                else
                    axisFound = axis;
        }
        return connections == 2 ? axisFound : null;
    }

}
