package com.drmangotea.tfmg.content.decoration.pipes.block;

import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipes;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class TFMGEncasedPipeBlock extends EncasedPipeBlock {

    public final TFMGPipes.PipeMaterial material;
    public TFMGEncasedPipeBlock(Properties p_i48339_1_, Supplier<Block> casing, TFMGPipes.PipeMaterial material) {
        super(p_i48339_1_, casing);
        this.material = material;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (world.isClientSide)
            return InteractionResult.SUCCESS;

        context.getLevel()
                .levelEvent(2001, context.getClickedPos(), Block.getId(state));
        BlockState equivalentPipe = transferSixWayProperties(state, TFMGPipes.PIPES.get(material).getPipe().getDefaultState());

        Direction firstFound = Direction.UP;
        for (Direction d : Iterate.directions)
            if (state.getValue(FACING_TO_PROPERTY_MAP.get(d))) {
                firstFound = d;
                break;
            }
        FluidTransportBehaviour.cacheFlows(world, pos);
        world.setBlockAndUpdate(pos, TFMGPipes.PIPES.get(material).getPipe().get()
                .updateBlockState(equivalentPipe, firstFound, null, world, pos));
        FluidTransportBehaviour.loadFlows(world, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return TFMGPipes.PIPES.get(material).getPipe().asStack();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        return ItemRequirement.of(TFMGPipes.PIPES.get(material).getPipe().getDefaultState(), be);
    }

    @Override
    public BlockEntityType<? extends FluidPipeBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.ENCASED_TFMG_PIPE.get();
    }
}
