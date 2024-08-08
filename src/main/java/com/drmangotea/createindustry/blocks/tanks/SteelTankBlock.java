package com.drmangotea.createindustry.blocks.tanks;


import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankItem;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidHelper.FluidExchange;
import com.simibubi.create.foundation.utility.Lang;
import io.github.fabricators_of_create.porting_lib.block.CustomSoundTypeBlock;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Consumer;

import static com.simibubi.create.content.fluids.tank.FluidTankBlock.SILENCED_METAL;

public class SteelTankBlock extends Block implements IWrenchable, IBE<SteelTankBlockEntity>, CustomSoundTypeBlock {

    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);
    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light_level", 0, 15);

    public static SteelTankBlock regular(Properties p_i48440_1_) {
            return new SteelTankBlock(p_i48440_1_);
        }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }

    protected SteelTankBlock(Properties p_i48440_1_) {
        super(setLightFunction(p_i48440_1_));
        registerDefaultState(defaultBlockState().setValue(TOP, true)
                .setValue(BOTTOM, true)
                .setValue(SHAPE, Shape.WINDOW)
                .setValue(LIGHT_LEVEL, 0));
    }

    private static Properties setLightFunction(Properties properties) {
        return properties.lightLevel(state -> state.getValue(LIGHT_LEVEL));
    }

    public static boolean isTank(BlockState state) {
            return state.getBlock() instanceof SteelTankBlock;
        }

        @Override
        public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
            if (oldState.getBlock() == state.getBlock())
                return;
            if (moved)
                return;
            Consumer<SteelTankBlockEntity> consumer = FluidTankItem.IS_PLACING_NBT
                    ? SteelTankBlockEntity::queueConnectivityUpdate
                    : SteelTankBlockEntity::updateConnectivity;
            withBlockEntityDo(world, pos, consumer);
        }

        @Override
        protected void createBlockStateDefinition(Builder<Block, BlockState> p_206840_1_) {
            p_206840_1_.add(TOP, BOTTOM, SHAPE);
        }

        @Override
        public InteractionResult onWrenched(BlockState state, UseOnContext context) {
            withBlockEntityDo(context.getLevel(), context.getClickedPos(), SteelTankBlockEntity::toggleWindows);
            return InteractionResult.SUCCESS;
        }

        static final VoxelShape CAMPFIRE_SMOKE_CLIP = Block.box(0, 4, 0, 16, 16, 16);

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos,
                                        CollisionContext pContext) {
        if (pContext == CollisionContext.empty())
            return CAMPFIRE_SMOKE_CLIP;
        return pState.getShape(pLevel, pPos);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return Shapes.block();
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState,
                                  LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (pDirection == Direction.DOWN && pNeighborState.getBlock() != this)
            withBlockEntityDo(pLevel, pCurrentPos, FluidTankBlockEntity::updateBoilerTemperature);
        return pState;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult ray) {
        ItemStack heldItem = player.getItemInHand(hand);
        boolean onClient = world.isClientSide;

        if (heldItem.isEmpty())
            return InteractionResult.PASS;
        if (!player.isCreative())
            return InteractionResult.PASS;

        FluidExchange exchange = null;
        SteelTankBlockEntity be = ConnectivityHandler.partAt(getBlockEntityType(), world, pos);
        if (be == null)
            return InteractionResult.FAIL;

        Direction direction = ray.getDirection();
        Storage<FluidVariant> fluidTank = be.getFluidStorage(direction);
        if (fluidTank == null)
            return InteractionResult.PASS;

        FluidStack prevFluidInTank = TransferUtil.firstCopyOrEmpty(fluidTank);

        if (FluidHelper.tryEmptyItemIntoBE(world, player, hand, heldItem, be, direction))
            exchange = FluidExchange.ITEM_TO_TANK;
        else if (FluidHelper.tryFillItemFromBE(world, player, hand, heldItem, be, direction))
            exchange = FluidExchange.TANK_TO_ITEM;

        if (exchange == null) {
            if (GenericItemEmptying.canItemBeEmptied(world, heldItem)
                    || GenericItemFilling.canItemBeFilled(world, heldItem))
                return InteractionResult.SUCCESS;
            return InteractionResult.PASS;
        }

        SoundEvent soundevent = null;
        BlockState fluidState = null;
        FluidStack fluidInTank = TransferUtil.firstOrEmpty(fluidTank);

        if (exchange == FluidExchange.ITEM_TO_TANK) {
            Fluid fluid = fluidInTank.getFluid();
            fluidState = fluid.defaultFluidState()
                    .createLegacyBlock();
            soundevent = FluidVariantAttributes.getEmptySound(FluidVariant.of(fluid));
        }

        if (exchange == FluidExchange.TANK_TO_ITEM) {
            Fluid fluid = prevFluidInTank.getFluid();
            fluidState = fluid.defaultFluidState()
                    .createLegacyBlock();
            soundevent = FluidVariantAttributes.getFillSound(FluidVariant.of(fluid));
        }

        if (soundevent != null && !onClient) {
            float pitch = Mth
                    .clamp(1 - (1f * fluidInTank.getAmount() / (FluidTankBlockEntity.getCapacityMultiplier() * 16)), 0, 1);
            pitch /= 1.5f;
            pitch += .5f;
            pitch += (world.random.nextFloat() - .5f) / 4f;
            world.playSound(null, pos, soundevent, SoundSource.BLOCKS, .5f, pitch);
        }

        if (!fluidInTank.isFluidEqual(prevFluidInTank)) {
            if (be instanceof SteelTankBlockEntity) {
                SteelTankBlockEntity controllerBE = be.getControllerBE();
                if (controllerBE != null) {
                    if (fluidState != null && onClient) {
                        BlockParticleOption blockParticleData =
                                new BlockParticleOption(ParticleTypes.BLOCK, fluidState);
                        float level = (float) fluidInTank.getAmount() / TransferUtil.firstCapacity(fluidTank);

                        boolean reversed = FluidVariantAttributes.isLighterThanAir(fluidInTank.getType());
                        if (reversed)
                            level = 1 - level;

                        Vec3 vec = ray.getLocation();
                        vec = new Vec3(vec.x, controllerBE.getBlockPos()
                                .getY() + level * (controllerBE.height - .5f) + .25f, vec.z);
                        Vec3 motion = player.position()
                                .subtract(vec)
                                .scale(1 / 20f);
                        vec = vec.add(motion);
                        world.addParticle(blockParticleData, vec.x, vec.y, vec.z, motion.x, motion.y, motion.z);
                        return InteractionResult.SUCCESS;
                    }

                    controllerBE.sendDataImmediately();
                    controllerBE.setChanged();
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (!(be instanceof FluidTankBlockEntity))
                return;
            FluidTankBlockEntity tankBE = (FluidTankBlockEntity) be;
            world.removeBlockEntity(pos);
            ConnectivityHandler.splitMulti(tankBE);
        }
    }

    @Override
    public Class<SteelTankBlockEntity> getBlockEntityClass() {
            return SteelTankBlockEntity.class;
        }

    @Override
    public BlockEntityType<? extends SteelTankBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.STEEL_FLUID_TANK.get();
    }

        @Override
        public BlockState mirror(BlockState state, Mirror mirror) {
            if (mirror == Mirror.NONE)
                return state;
            boolean x = mirror == Mirror.FRONT_BACK;
            switch (state.getValue(SHAPE)) {
                case WINDOW_NE:
                    return state.setValue(SHAPE, x ? Shape.WINDOW_NW : Shape.WINDOW_SE);
                case WINDOW_NW:
                    return state.setValue(SHAPE, x ? Shape.WINDOW_NE : Shape.WINDOW_SW);
                case WINDOW_SE:
                    return state.setValue(SHAPE, x ? Shape.WINDOW_SW : Shape.WINDOW_NE);
                case WINDOW_SW:
                    return state.setValue(SHAPE, x ? Shape.WINDOW_SE : Shape.WINDOW_NW);
                default:
                    return state;
            }
        }

        @Override
        public BlockState rotate(BlockState state, Rotation rotation) {
            for (int i = 0; i < rotation.ordinal(); i++)
                state = rotateOnce(state);
            return state;
        }

        private BlockState rotateOnce(BlockState state) {
            switch (state.getValue(SHAPE)) {
                case WINDOW_NE:
                    return state.setValue(SHAPE, Shape.WINDOW_SE);
                case WINDOW_NW:
                    return state.setValue(SHAPE, Shape.WINDOW_NE);
                case WINDOW_SE:
                    return state.setValue(SHAPE, Shape.WINDOW_SW);
                case WINDOW_SW:
                    return state.setValue(SHAPE, Shape.WINDOW_NW);
                default:
                    return state;
            }
        }

        public enum Shape implements StringRepresentable {
            PLAIN, WINDOW, WINDOW_NW, WINDOW_SW, WINDOW_NE, WINDOW_SE;

            @Override
            public String getSerializedName() {
                return Lang.asId(name());
            }
        }

        @Override
        public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
            SoundType soundType = getSoundType(state);
            if (entity != null && entity.getExtraCustomData()
                    .contains("SilenceTankSound"))
                return SILENCED_METAL;
            return soundType;
        }

        @Override
        public boolean hasAnalogOutputSignal(BlockState state) {
            return true;
        }

        @Override
        public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
            return getBlockEntityOptional(worldIn, pos).map(SteelTankBlockEntity::getControllerBE)
                    .map(te -> ComparatorUtil.fractionToRedstoneLevel(te.getFillState()))
                    .orElse(0);
        }

        public static boolean updateTowerState(Level pLevel, BlockPos tankPos, boolean assemble,boolean simulate) {
            BlockState tankState = pLevel.getBlockState(tankPos);

            if (!(tankState.getBlock()instanceof SteelTankBlock tank))
                return false;

            SteelTankBlockEntity tankBE = tank.getBlockEntity(pLevel, tankPos);
            if (tankBE == null)
                return false;

            if(assemble && tankBE.getControllerBE().isDistillationTower)
                return false;



            if(!simulate) {
                tankBE.getControllerBE().updateBoilerState();
                tankBE.getControllerBE().isDistillationTower = assemble;
                tankBE.getControllerBE().refreshCapability();


                tankBE.updateBoilerState();
                tankBE.isDistillationTower = assemble;
                tankBE.refreshCapability();


            }


            return true;

        }

    }
