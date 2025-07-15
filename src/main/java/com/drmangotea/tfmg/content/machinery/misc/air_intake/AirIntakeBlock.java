package com.drmangotea.tfmg.content.machinery.misc.air_intake;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class AirIntakeBlock extends DirectionalKineticBlock implements IBE<AirIntakeBlockEntity>, IWrenchable {



    public static final BooleanProperty INVISIBLE = BooleanProperty.create("invisible");



    public AirIntakeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(INVISIBLE, false));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }


    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        AirIntakeBlockEntity be = (AirIntakeBlockEntity) world.getBlockEntity(pos);

        if(be.hasShaft) {
            return face == state.getValue(FACING).getOpposite();
        }else return false;
    }
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {

        Direction direction = context.getClickedFace();
        Level level = context.getLevel();
        AirIntakeBlockEntity be = (AirIntakeBlockEntity) level.getBlockEntity(context.getClickedPos());

        if(direction == state.getValue(FACING).getOpposite()) {
            ((AirIntakeBlockEntity) level.getBlockEntity(context.getClickedPos())).hasShaft = !be.hasShaft;
            //////////
            BlockPos pos = context.getClickedPos();

            IWrenchable.playRotateSound(level,pos);

            AirIntakeBlockEntity kineticBlockEntity = (AirIntakeBlockEntity) level.getBlockEntity(pos);
            if(!kineticBlockEntity.hasShaft) {
                if (kineticBlockEntity.hasNetwork())
                    kineticBlockEntity.getOrCreateNetwork()
                            .remove(kineticBlockEntity);
                kineticBlockEntity.detachKinetics();
                kineticBlockEntity.removeSource();
                kineticBlockEntity.setSpeed(0);
                kineticBlockEntity.remove();
                kineticBlockEntity.notifyUpdate();
            } else{
                kineticBlockEntity.attachKinetics();
                kineticBlockEntity.notifyUpdate();
            }
        }



        return super.onWrenched(state,context);
    }

    @Override
    public Class<AirIntakeBlockEntity> getBlockEntityClass() {
        return AirIntakeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends AirIntakeBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.AIR_INTAKE.get();
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55125_) {
        p_55125_.add(FACING, INVISIBLE);
    }
}
