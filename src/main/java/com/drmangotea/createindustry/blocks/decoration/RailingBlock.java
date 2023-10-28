package com.drmangotea.createindustry.blocks.decoration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RailingBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final VoxelShape SHAPE_WEST = Block.box(0, 0, 0, 2, 16, 16);
    public static final VoxelShape SHAPE_NORTH = Block.box(0, 0, 0, 16, 16, 2);
    public static final VoxelShape SHAPE_EAST = Block.box(14, 0, 0, 16, 16, 16);
    public static final VoxelShape SHAPE_SOUTH = Block.box(0, 0, 14, 16, 16, 16);


    public RailingBlock(Properties p_54479_) {
        super(p_54479_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public RenderShape getRenderShape(BlockState p_54559_) {
        return RenderShape.MODEL;
    }

    public VoxelShape getOcclusionShape(BlockState p_54584_, BlockGetter p_54585_, BlockPos p_54586_) {
        switch((Direction)p_54584_.getValue(FACING)) {
            case NORTH:
                return SHAPE_SOUTH;
            case SOUTH:
                return SHAPE_NORTH;
            case EAST:
                return SHAPE_WEST;
            case WEST:
                return SHAPE_EAST;
            default:
                return SHAPE_NORTH;
        }
    }

    public boolean useShapeForLightOcclusion(BlockState p_54582_) {
        return true;
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_54481_) {
        Level level = p_54481_.getLevel();
        ItemStack itemstack = p_54481_.getItemInHand();
        Player player = p_54481_.getPlayer();
        boolean flag = false;
        if (!level.isClientSide && player != null && player.canUseGameMasterBlocks()) {
            CompoundTag compoundtag = BlockItem.getBlockEntityData(itemstack);
            if (compoundtag != null && compoundtag.contains("Book")) {
                flag = true;
            }
        }

        return this.defaultBlockState().setValue(FACING, p_54481_.getHorizontalDirection().getOpposite());
    }

    public VoxelShape getCollisionShape(BlockState p_54577_, BlockGetter p_54578_, BlockPos p_54579_, CollisionContext p_54580_) {
        switch((Direction)p_54577_.getValue(FACING)) {
            case NORTH:
                return SHAPE_SOUTH;
            case SOUTH:
                return SHAPE_NORTH;
            case EAST:
                return SHAPE_WEST;
            case WEST:
                return SHAPE_EAST;
            default:
                return SHAPE_NORTH;
        }
    }

    public VoxelShape getShape(BlockState p_54561_, BlockGetter p_54562_, BlockPos p_54563_, CollisionContext p_54564_) {
        switch((Direction)p_54561_.getValue(FACING)) {
            case NORTH:
                return SHAPE_SOUTH;
            case SOUTH:
                return SHAPE_NORTH;
            case EAST:
                return SHAPE_WEST;
            case WEST:
                return SHAPE_EAST;
            default:
                return SHAPE_NORTH;
        }
    }

    public BlockState rotate(BlockState p_54540_, Rotation p_54541_) {
        return p_54540_.setValue(FACING, p_54541_.rotate(p_54540_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_54537_, Mirror p_54538_) {
        return p_54537_.rotate(p_54538_.getRotation(p_54537_.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54543_) {
        p_54543_.add(FACING);
    }


    public boolean isPathfindable(BlockState p_54510_, BlockGetter p_54511_, BlockPos p_54512_, PathComputationType p_54513_) {
        return false;
    }
}