package com.drmangotea.tfmg.content.electricity.lights.neon_tube;

import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.connection.cables.SimplePos;
import com.drmangotea.tfmg.content.electricity.lights.LightBulbBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class NeonTubeBlock extends PipeBlock implements IBE<NeonTubeBlockEntity>, IWrenchable {

    public static final IntegerProperty LIGHT = LightBulbBlock.LIGHT;

    public NeonTubeBlock(Properties p_55160_) {
        super(0.3125F/2.5f, p_55160_);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIGHT, 0).setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)).setValue(UP, Boolean.valueOf(true)).setValue(DOWN, Boolean.valueOf(true)));
    }
    public BlockState getStateForPlacement(BlockPlaceContext p_51709_) {
        return this.getStateForPlacement(p_51709_.getLevel(), p_51709_.getClickedPos());
    }
    public BlockState getStateForPlacement(BlockGetter p_51711_, BlockPos p_51712_) {
        BlockState blockstate = p_51711_.getBlockState(p_51712_.below());
        BlockState blockstate1 = p_51711_.getBlockState(p_51712_.above());
        BlockState blockstate2 = p_51711_.getBlockState(p_51712_.north());
        BlockState blockstate3 = p_51711_.getBlockState(p_51712_.east());
        BlockState blockstate4 = p_51711_.getBlockState(p_51712_.south());
        BlockState blockstate5 = p_51711_.getBlockState(p_51712_.west());
        return this.defaultBlockState().setValue(DOWN, Boolean.valueOf(blockstate.is(this) || blockstate.is(Blocks.CHORUS_FLOWER) || blockstate.is(Blocks.END_STONE))).setValue(UP, Boolean.valueOf(blockstate1.is(this) || blockstate1.is(Blocks.CHORUS_FLOWER))).setValue(NORTH, Boolean.valueOf(blockstate2.is(this) || blockstate2.is(Blocks.CHORUS_FLOWER))).setValue(EAST, Boolean.valueOf(blockstate3.is(this) || blockstate3.is(Blocks.CHORUS_FLOWER))).setValue(SOUTH, Boolean.valueOf(blockstate4.is(this) || blockstate4.is(Blocks.CHORUS_FLOWER))).setValue(WEST, Boolean.valueOf(blockstate5.is(this) || blockstate5.is(Blocks.CHORUS_FLOWER)));
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_51735_) {
        p_51735_.add(NORTH, EAST, SOUTH, WEST, UP, DOWN,LIGHT);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();
        Vec3 position = context.getClickLocation();

        SimplePos clickPosition = new SimplePos(position.x()-pos.getX(),position.y()-pos.getY(),position.z()-pos.getZ());

        Direction dirToToggle = Direction.NORTH;

        if(clickPosition.x()>=0.375&&clickPosition.x()<=0.625&&clickPosition.y()>=0.375&&clickPosition.y()<=0.625&&clickPosition.z()>=0.375&&clickPosition.z()<=0.625){
            dirToToggle = facing;
        }

        if(clickPosition.x()>0.625)
            dirToToggle = Direction.EAST;
        if(clickPosition.x()<0.375)
            dirToToggle = Direction.WEST;
        if(clickPosition.y()>0.625)
            dirToToggle = Direction.UP;
        if(clickPosition.y()<0.375)
            dirToToggle = Direction.DOWN;
        if(clickPosition.z()>0.625)
            dirToToggle = Direction.SOUTH;
        if(clickPosition.z()<0.375)
            dirToToggle = Direction.NORTH;



        switch (dirToToggle){
            case UP -> level.setBlockAndUpdate(pos, state.setValue(UP,!state.getValue(UP)));
            case DOWN -> level.setBlockAndUpdate(pos, state.setValue(DOWN,!state.getValue(DOWN)));
            case WEST -> level.setBlockAndUpdate(pos, state.setValue(WEST,!state.getValue(WEST)));
            case EAST -> level.setBlockAndUpdate(pos, state.setValue(EAST,!state.getValue(EAST)));
            case NORTH -> level.setBlockAndUpdate(pos, state.setValue(NORTH,!state.getValue(NORTH)));
            case SOUTH -> level.setBlockAndUpdate(pos, state.setValue(SOUTH,!state.getValue(SOUTH)));
        }

        withBlockEntityDo(level,pos, IElectric::onPlaced);

        for(Direction direction : Direction.values()){
            if(level.getBlockEntity(pos.relative(direction)) instanceof IElectric be){
             be.onPlaced();
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player player, InteractionHand pHand,
                                 BlockHitResult pHit) {
        if (player.isShiftKeyDown())
            return InteractionResult.PASS;
        ItemStack heldItem = player.getItemInHand(pHand);
        NeonTubeBlockEntity be = getBlockEntity(pLevel, pPos);
        DyeColor dye = DyeColor.getColor(heldItem);
        if (be != null) {
            if (dye != null) {
                pLevel.playSound(null, pPos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                be.setColor(dye);
                return InteractionResult.SUCCESS;
            }
        }


        return InteractionResult.PASS;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState p_60569_, boolean p_60570_) {


    if(!p_60569_.is(TFMGBlocks.NEON_TUBE.get()))
        for(Direction facing : Direction.values()) {
            BlockState neighbourState = level.getBlockState(pos.relative(facing));
             if (neighbourState.is(TFMGBlocks.NEON_TUBE.get())) {
                 level.setBlockAndUpdate(pos.relative(facing), neighbourState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(facing.getOpposite()), true));
             }
        }
        withBlockEntityDo(level,pos, IElectric::onPlaced);
        super.onPlace(state, level, pos, p_60569_, p_60570_);

    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }



    @Override
    public Class<NeonTubeBlockEntity> getBlockEntityClass() {
        return NeonTubeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends NeonTubeBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.NEON_TUBE.get();
    }
}
