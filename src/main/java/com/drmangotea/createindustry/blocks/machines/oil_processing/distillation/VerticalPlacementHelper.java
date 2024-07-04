package com.drmangotea.createindustry.blocks.machines.oil_processing.distillation;

import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Predicate;

@MethodsReturnNonnullByDefault
public abstract class VerticalPlacementHelper implements IPlacementHelper {

    protected final Predicate<BlockState> statePredicate;



    public VerticalPlacementHelper(Predicate<BlockState> statePredicate) {
        this.statePredicate = statePredicate;


    }

    public boolean matchesAxis(BlockState state, Direction.Axis axis) {

        return axis == Direction.Axis.Y;
    }

    public int attachedPoles(Level world, BlockPos pos, Direction direction) {
        BlockPos checkPos = pos.relative(direction);
        BlockState state = world.getBlockState(checkPos);
        int count = 0;
        while (matchesAxis(state, direction.getAxis())) {
            count++;
            checkPos = checkPos.relative(direction);
            state = world.getBlockState(checkPos);
        }
        return count;
    }

    @Override
    public Predicate<BlockState> getStatePredicate() {
        return this.statePredicate;
    }

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {




        BlockPos posToPlace = pos;
        for(int i = 0;i<15;i++){
            posToPlace = posToPlace.below();

            if(world.getBlockState(posToPlace).is(TFMGBlocks.INDUSTRIAL_PIPE.get()))
                continue;

            if(!world.getBlockState(posToPlace).isAir()) {
                return PlacementOffset.fail();

            }else break;

        }
        if(posToPlace!=pos)
            return PlacementOffset.success(posToPlace, bState -> bState);


        return PlacementOffset.fail();
    }
}