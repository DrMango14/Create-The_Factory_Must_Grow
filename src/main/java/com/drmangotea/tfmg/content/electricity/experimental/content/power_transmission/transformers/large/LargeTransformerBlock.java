package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.transformers.large;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.switches.large_switch.LargeSwitchBlock;

import com.drmangotea.tfmg.content.electricity.experimental.packets.AddElectricalComponentPacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LargeTransformerBlock extends TFMGHorizontalDirectionalBlock implements IBE<LargeTransformerBlockEntity> {

    public static final BooleanProperty IS_MAIN_PART = LargeSwitchBlock.IS_MAIN_PART;


    public LargeTransformerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(IS_MAIN_PART);
    }



    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockPos blockpos = pos.relative(state.getValue(FACING));
            level.setBlock(blockpos, state.setValue(IS_MAIN_PART, false), 3);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }



    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(!state.getValue(IS_MAIN_PART)) {
            return TFMGShapes.LARGE_TRANSFORMER.get(state.getValue(FACING).getOpposite());
        }else return TFMGShapes.LARGE_TRANSFORMER.get(state.getValue(FACING));
        }

    private static Direction getNeighbourDirection(boolean mainPart, Direction direction) {
        return mainPart ? direction : direction.getOpposite();
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {

        if(!state.getValue(IS_MAIN_PART))
            return new ArrayList<>();

        return super.getDrops(state, params);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
         if (facing == getNeighbourDirection(state.getValue(IS_MAIN_PART), state.getValue(FACING))) {
             if(!(facingState.is(this) && facingState.getValue(IS_MAIN_PART) != state.getValue(IS_MAIN_PART)))

                     return Blocks.AIR.defaultBlockState();
         }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);

    }


    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if(level.getBlockEntity(pos) instanceof IRealisticElectric be&&state.getValue(IS_MAIN_PART)){
            be.onPlace();
            if (level instanceof ServerLevel serverLevel)
                CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(pos), new AddElectricalComponentPacket(BlockPos.of(pos.asLong())));


        }
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
        super.onRemove(state,level,pos,newState,isMoving);
    }

    @Override
    public Class<LargeTransformerBlockEntity> getBlockEntityClass() {
        return LargeTransformerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LargeTransformerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.LARGE_TRANSFORMER.get();
    }
}
