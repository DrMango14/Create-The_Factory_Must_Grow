package com.drmangotea.tfmg.content.decoration.pipes.block;

import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipes;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import net.createmod.catnip.data.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

@MethodsReturnNonnullByDefault
public class TFMGPipeBlock extends FluidPipeBlock {

    public final TFMGPipes.PipeMaterial material;

    public TFMGPipeBlock(Properties properties, TFMGPipes.PipeMaterial material) {
        super(properties);
        this.material = material;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource r) {
        super.tick(state, world, pos, r);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (tryRemoveBracket(context))
            return InteractionResult.SUCCESS;
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();

        Direction.Axis axis = getAxis(state);
        if (axis == null) {
            Vec3 clickLocation = context.getClickLocation()
                    .subtract(pos.getX(), pos.getY(), pos.getZ());
            double closest = Float.MAX_VALUE;
            Direction argClosest = Direction.UP;
            for (Direction direction : Iterate.directions) {
                if (clickedFace.getAxis() == direction.getAxis())
                    continue;
                Vec3 centerOf = Vec3.atCenterOf(direction.getNormal());
                double distance = centerOf.distanceToSqr(clickLocation);
                if (distance < closest) {
                    closest = distance;
                    argClosest = direction;
                }
            }
            axis = argClosest.getAxis();
        }

        if (clickedFace.getAxis() == axis)
            return InteractionResult.PASS;
        if (!world.isClientSide) {
            withBlockEntityDo(world, pos, be -> be.getBehaviour(FluidTransportBehaviour.TYPE).interfaces.values()
                    .stream()
                    .filter(pc -> pc != null && pc.hasFlow())
                    .findAny()
                    .ifPresent($ -> AllAdvancements.GLASS_PIPE.awardTo(context.getPlayer())));

            FluidTransportBehaviour.cacheFlows(world, pos);
            world.setBlockAndUpdate(pos, TFMGPipes.PIPES.get(material).getGlass().getDefaultState()
                    .setValue(GlassFluidPipeBlock.AXIS, axis)
                    .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)));
            FluidTransportBehaviour.loadFlows(world, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    private Direction.Axis getAxis(BlockState state) {
        return FluidPropagator.getStraightPipeAxis(state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!AllBlocks.COPPER_CASING.isIn(player.getItemInHand(hand)))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (world.isClientSide)
            return ItemInteractionResult.SUCCESS;

        FluidTransportBehaviour.cacheFlows(world, pos);
        world.setBlockAndUpdate(pos,
                EncasedPipeBlock.transferSixWayProperties(state, TFMGPipes.PIPES.get(material).getEncased().getDefaultState()));
        FluidTransportBehaviour.loadFlows(world, pos);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public BlockEntityType<? extends FluidPipeBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.TFMG_PIPE.get();
    }

}

