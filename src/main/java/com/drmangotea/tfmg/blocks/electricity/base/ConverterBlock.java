package com.drmangotea.tfmg.blocks.electricity.base;

import com.drmangotea.tfmg.blocks.electricity.base.cables.ConnectNeightborsPacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.network.PacketDistributor;

public class ConverterBlock extends Block implements IBE<ConverterBlockEntity>, IWrenchable, IHaveCables {



    public ConverterBlock(Properties p_49795_) {
        super(p_49795_);

    }



    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new ConnectNeightborsPacket(pos));
        withBlockEntityDo(level,pos, ConverterBlockEntity::onPlaced);

    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

    }

    @Override
    public Class<ConverterBlockEntity> getBlockEntityClass() {
        return ConverterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ConverterBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.CONVERTER.get();
    }

}
