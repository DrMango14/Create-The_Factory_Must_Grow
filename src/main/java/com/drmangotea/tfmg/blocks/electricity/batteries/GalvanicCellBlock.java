package com.drmangotea.tfmg.blocks.electricity.batteries;

import com.drmangotea.tfmg.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.blocks.electricity.base.IHaveCables;
import com.drmangotea.tfmg.blocks.electricity.base.cables.ConnectNeightborsPacket;
import com.drmangotea.tfmg.blocks.electricity.electric_motor.ElectricMotorBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.drmangotea.tfmg.registry.TFMGShapes;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;

public class GalvanicCellBlock extends TFMGHorizontalDirectionalBlock implements IBE<GalvanicCellBlockEntity>, IHaveCables {


    public GalvanicCellBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.GALVANIC_CELL.get(pState.getValue(FACING));
    }
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        withBlockEntityDo(level, pos, be -> be.setCapacity(stack));
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack stack) {

        int capacity = 0;

        if(be !=null)
            if(be instanceof BatteryBlockEntity be1)
                capacity = be1.capacity;


        stack.getOrCreateTag().putInt("Capacity",capacity);

        super.playerDestroy(level,player,pos,state,be,stack);
    }
    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new ConnectNeightborsPacket(pos));
        withBlockEntityDo(level,pos, GalvanicCellBlockEntity::onPlaced);

    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }
    @Override
    public Class<GalvanicCellBlockEntity> getBlockEntityClass() {
        return GalvanicCellBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GalvanicCellBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.GALVANIC_CELL.get();
    }
}
