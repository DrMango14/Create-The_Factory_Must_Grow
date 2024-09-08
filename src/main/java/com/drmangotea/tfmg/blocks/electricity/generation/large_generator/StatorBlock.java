package com.drmangotea.tfmg.blocks.electricity.generation.large_generator;


import com.drmangotea.tfmg.blocks.electricity.base.IHaveCables;
import com.drmangotea.tfmg.blocks.electricity.base.WallMountBlock;
import com.drmangotea.tfmg.blocks.electricity.base.cables.ConnectNeightborsPacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.network.PacketDistributor;

public class StatorBlock extends DirectionalBlock implements IBE<StatorBlockEntity>, IHaveCables {


    public static final BooleanProperty VALUE = BooleanProperty.create("value");
    public static final EnumProperty<StatorState> STATOR_STATE = EnumProperty.create("stator_state", StatorState.class);

    public StatorBlock(Properties properties) {
        super(properties);
    }





    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new ConnectNeightborsPacket(pos));
        withBlockEntityDo(level,pos, StatorBlockEntity::onPlaced);

    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STATOR_STATE,FACING,VALUE);
        super.createBlockStateDefinition(pBuilder);
    }



    @Override
    public Class<StatorBlockEntity> getBlockEntityClass() {
        return StatorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StatorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.STATOR.get();
    }

    public enum StatorState implements StringRepresentable{


        UNUSED("unused"),
        SIDE("side"),

        CORNER_HORIZONTAL("corner_horizontal"),
        CORNER("corner")


        ;
        final String name;
        StatorState(String name){
            this.name = name;
        }


        @Override
        public String getSerializedName() {
            return name;
        }
    }





}
