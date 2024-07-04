package com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin;


import com.drmangotea.createindustry.blocks.machines.metal_processing.coke_oven.CokeOvenBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CastingBasinBlock extends Block implements IBE<CastingBasinBlockEntity>, IWrenchable {

    public static final EnumProperty<CastingBasinBlockEntity.MoldType> MOLD_TYPE = EnumProperty.create("mold_type", CastingBasinBlockEntity.MoldType.class);

    public CastingBasinBlock(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(defaultBlockState().setValue(MOLD_TYPE, CastingBasinBlockEntity.MoldType.NONE));
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return AllShapes.BASIN_BLOCK_SHAPE;
    }    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        CastingBasinBlockEntity be;



        if(level.getBlockEntity(pos) instanceof CastingBasinBlockEntity) {
            be = (CastingBasinBlockEntity) level.getBlockEntity(pos);
        }else  return InteractionResult.PASS;

        if(be ==null)
            return InteractionResult.PASS;
        ///

        if(!be.tank1.isEmpty())
            return InteractionResult.PASS;

        if(player.getItemInHand(interactionHand).is(Items.AIR)&&!(((CastingBasinBlockEntity) level.getBlockEntity(pos)).moldInventory.isEmpty())){
            player.setItemInHand(interactionHand,new ItemStack(be.moldInventory.getStackInSlot(0).getItem(),1));
            ((CastingBasinBlockEntity) level.getBlockEntity(pos)).moldInventory.getStackInSlot(0).shrink(1);

            level.setBlock(pos,state.setValue(MOLD_TYPE, CastingBasinBlockEntity.MoldType.NONE),2);
            return InteractionResult.SUCCESS;
        }


        ///
        if(player.getItemInHand(interactionHand).getItem() instanceof CastingMoldItem&&((CastingBasinBlockEntity) level.getBlockEntity(pos)).moldInventory.isEmpty()){
            ((CastingBasinBlockEntity) level.getBlockEntity(pos)).moldInventory.setStackInSlot(0, new ItemStack(player.getItemInHand(interactionHand).getItem(),1));
            player.getItemInHand(interactionHand).shrink(1);


         return InteractionResult.SUCCESS;
        }
            return InteractionResult.PASS;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(MOLD_TYPE);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public Class<CastingBasinBlockEntity> getBlockEntityClass() {
        return CastingBasinBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CastingBasinBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.CASTING_BASIN.get();
    }
}
