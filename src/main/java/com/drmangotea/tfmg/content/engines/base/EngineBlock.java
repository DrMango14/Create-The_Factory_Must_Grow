package com.drmangotea.tfmg.content.engines.base;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerBlockEntity;
import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.radial_engine.RadialEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.turbine_engine.TurbineEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.upgrades.EnginePipingUpgrade;
import com.drmangotea.tfmg.content.engines.upgrades.TransmissionUpgrade;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

import static com.drmangotea.tfmg.content.engines.base.EngineBlock.EngineState.NORMAL;
import static com.drmangotea.tfmg.content.engines.base.EngineBlock.EngineState.SHAFT;

public class EngineBlock extends HorizontalKineticBlock {

    public static final EnumProperty<EngineState> ENGINE_STATE = EnumProperty.create("engine_state", EngineState.class);

    public static final Property<Direction> SHAFT_FACING = DirectionProperty.create("shaft_facing", Direction.Plane.HORIZONTAL);

    public EngineBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ENGINE_STATE, EngineState.NORMAL));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level.getBlockEntity(pos) instanceof AbstractSmallEngineBlockEntity be) {
            if (be.hasUpgrade()) {

                if (be.upgrade.isPresent()) {

                    if (be.upgrade.get() instanceof TransmissionUpgrade) {
                        if (be.getControllerBE().engineController != null) {
                            if (level.getBlockEntity(be.getControllerBE().engineController) instanceof EngineControllerBlockEntity engineController) {
                                engineController.engineStarted = false;
                                engineController.accelerationRate = 0;
                                engineController.shift = TransmissionUpgrade.TransmissionState.NEUTRAL;
                                be.shift = TransmissionUpgrade.TransmissionState.NEUTRAL;
                                be.clutchPressed = false;
                                engineController.engine = null;
                                engineController.enginePos = null;
                                engineController.disconnectEngine();
                                engineController.sendData();
                            }
                        }
                        //if(!level.isClientSide)
                        //    TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new UpdateInFrontPacket(BlockPos.of(be.getPos())));
                        be.getControllerBE().engineController = null;
                        be.getControllerBE().highestSignal = 0;
                        be.getControllerBE().connectNextTick = true;
                        be.getControllerBE().fuelInjectionRate = 0;
                        be.updateGeneratedRotation();
                        be.getControllerBE().updateGeneratedRotation();
                        be.getControllerBE().sendData();

                    }

                    be.playRemovalSound();
                    be.dropItem(be.upgrade.get().getItem().getDefaultInstance());

                }
                be.upgrade = Optional.empty();
                be.updateRotation();

                return InteractionResult.SUCCESS;
            }
            if (!(be instanceof RadialEngineBlockEntity) && !(be instanceof TurbineEngineBlockEntity))
                if (state.getValue(ENGINE_STATE) == SHAFT) {
                    be.playRemovalSound();
                    be.dropItem(AllBlocks.SHAFT.asStack());
                    level.setBlock(pos, state.setValue(ENGINE_STATE, NORMAL), 2);
                    be.connectNextTick = true;
                    be.detachKinetics();
                    be.getControllerBE().updateGeneratedRotation();
                    be.updateGeneratedRotation();
                    if (be.getOrCreateNetwork() != null)
                        be.getOrCreateNetwork().remove(be);
                    be.setChanged();
                    be.sendData();
                    return InteractionResult.SUCCESS;
                }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof AbstractSmallEngineBlockEntity be) {
            if (be.insertItem(player.getItemInHand(hand), player.isShiftKeyDown(), player, hand))
                return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }



    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos pos, CollisionContext p_60558_) {
        return state.getValue(ENGINE_STATE) == SHAFT ? TFMGShapes.ENGINE_FRONT.get(state.getValue(HORIZONTAL_FACING).getOpposite()) : TFMGShapes.ENGINE.get(state.getValue(HORIZONTAL_FACING));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return state.getValue(ENGINE_STATE) == EngineState.SHAFT && face == state.getValue(SHAFT_FACING);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ENGINE_STATE, SHAFT_FACING);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighbor, boolean b) {
        if (level.getBlockEntity(pos) instanceof AbstractSmallEngineBlockEntity be) {
            if (be.hasUpgrade() && be.upgrade.get().getItem() == TFMGBlocks.INDUSTRIAL_PIPE.asItem()) {
                ((EnginePipingUpgrade) be.upgrade.get()).findTank(be);
            }
        }

        super.neighborChanged(state, level, pos, block, neighbor, b);
    }


    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }

    public enum EngineState implements StringRepresentable {

        NORMAL("normal"),
        SHAFT("front"),
        BACK("back"),
        SINGLE("single");
        private final String name;

        EngineState(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
