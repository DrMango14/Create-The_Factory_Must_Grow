package com.drmangotea.tfmg.blocks.electricity.generation.generator;


import com.drmangotea.tfmg.blocks.electricity.base.IHaveCables;
import com.drmangotea.tfmg.blocks.electricity.base.cables.ConnectNeightborsPacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.drmangotea.tfmg.registry.TFMGShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;

public class  GeneratorBlock extends DirectionalKineticBlock implements IBE<GeneratorBlockEntity>, IHaveCables {
    public GeneratorBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.GENERATOR.get(pState.getValue(FACING));
    }
    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new ConnectNeightborsPacket(pos));
        withBlockEntityDo(level,pos, GeneratorBlockEntity::onPlaced);

    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }
    @Override
    public Class<GeneratorBlockEntity> getBlockEntityClass() {
        return GeneratorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GeneratorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.GENERATOR.get();
    }
    public Direction getPreferredFacing(BlockPlaceContext context) {

        if(super.getPreferredFacing(context)==null)
            return null;

        return super.getPreferredFacing(context).getOpposite();
    }
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);

    }
}
