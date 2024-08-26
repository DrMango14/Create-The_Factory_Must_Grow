package com.drmangotea.tfmg.blocks.electricity.polarizer;


import com.drmangotea.tfmg.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.blocks.electricity.base.IHaveCables;
import com.drmangotea.tfmg.blocks.electricity.base.cables.ConnectNeightborsPacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.drmangotea.tfmg.registry.TFMGShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;

public class PolarizerBlock extends TFMGHorizontalDirectionalBlock implements IBE<PolarizerBlockEntity>, IWrenchable, IHaveCables {
    public PolarizerBlock(Properties p_54120_) {
        super(p_54120_);
    }


    @Override
    public InteractionResult use(BlockState pState, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack inHand = player.getItemInHand(pHand);


        if (level.getBlockEntity(pos) instanceof PolarizerBlockEntity be)
            if (be.playerInteract(player, pHand))
                return InteractionResult.SUCCESS;

        return InteractionResult.PASS;
    }


    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new ConnectNeightborsPacket(pos));
        withBlockEntityDo(level,pos, PolarizerBlockEntity::onPlaced);

    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return TFMGShapes.SLAB;
    }

    @Override
    public Class<PolarizerBlockEntity> getBlockEntityClass() {
        return PolarizerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PolarizerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.POLARIZER.get();
    }
}
