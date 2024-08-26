package com.drmangotea.tfmg.blocks.electricity.cable_blocks;


import com.drmangotea.tfmg.blocks.electricity.base.IHaveCables;
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
import net.minecraftforge.network.PacketDistributor;

public class CableHubBlock extends Block implements IBE<CableHubBlockEntity>, IWrenchable, IHaveCables {
    public CableHubBlock(Properties p_49795_) {
        super(p_49795_);
    }
    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new ConnectNeightborsPacket(pos));
        withBlockEntityDo(level,pos, CableHubBlockEntity::onPlaced);

    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {

        IBE.onRemove(state, level, pos, newState);



    }
    @Override
    public Class<CableHubBlockEntity> getBlockEntityClass() {
        return CableHubBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CableHubBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.CABLE_HUB.get();
    }
}
