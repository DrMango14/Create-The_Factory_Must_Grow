package com.drmangotea.tfmg.blocks.electricity.resistors;



import com.drmangotea.tfmg.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.blocks.electricity.base.IHaveCables;
import com.drmangotea.tfmg.blocks.electricity.base.cables.ConnectNeightborsPacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.drmangotea.tfmg.registry.TFMGShapes;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;

public class ResistorBlock extends TFMGHorizontalDirectionalBlock implements IBE<ResistorBlockEntity>, IHaveCables {
    public ResistorBlock(Properties p_54120_) {
        super(p_54120_);
    }
    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new ConnectNeightborsPacket(pos));
        withBlockEntityDo(level,pos, ResistorBlockEntity::onPlaced);

    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }
    @Override
    public Class<ResistorBlockEntity> getBlockEntityClass() {
        return ResistorBlockEntity.class;
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.RESISTOR.get(pState.getValue(FACING));
    }
    @Override
    public BlockEntityType<? extends ResistorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.RESISTOR.get();
    }
}
