package com.drmangotea.tfmg.content.machinery.vat.base;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;

import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.DeferredSoundType;

public class VatBlock extends Block implements IWrenchable, IBE<VatBlockEntity> {

    public final String vatType;

    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);



    public static VatBlock steel(Properties properties){
        return new VatBlock(properties,"tfmg:steel_vat");
    }
    public static VatBlock cast_iron(Properties properties){
        return new VatBlock(properties,"tfmg:cast_iron_vat");
    }
    public static VatBlock fireproof(Properties properties){
        return new VatBlock(properties,"tfmg:firebrick_lined_vat");
    }

    public VatBlock(Properties properties, String vatType) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(TOP, true)
                .setValue(BOTTOM, true)
                .setValue(SHAPE, Shape.PLAIN));
        this.vatType = vatType;
    }
    public static boolean isVat(BlockState state) {
        return state.getBlock() instanceof VatBlock;
    }
    public static void updateVatState(BlockState pState, Level pLevel, BlockPos tankPos) {
        BlockState tankState = pLevel.getBlockState(tankPos);

        if (!(tankState.getBlock()instanceof VatBlock tank))
            return;
        VatBlockEntity vatBE = tank.getBlockEntity(pLevel, tankPos);
        if (vatBE == null)
            return;

        vatBE.evaluateNextTick=true;
    }
    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
        if (oldState.getBlock() == state.getBlock())
            return;
        if (moved)
            return;

        withBlockEntityDo(world, pos, VatBlockEntity::updateConnectivity);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(TOP, BOTTOM, SHAPE);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        VatBlockEntity vatAt = ConnectivityHandler.partAt(getBlockEntityType(), world, pos);
        if (vatAt == null)
            return 0;
        VatBlockEntity controllerTE = (VatBlockEntity) vatAt.getControllerBE();
        if (controllerTE == null || !controllerTE.window)
            return 0;
        return vatAt.luminosity;
    }
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        withBlockEntityDo(context.getLevel(), context.getClickedPos(), VatBlockEntity::toggleWindows);
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
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (!(be instanceof VatBlockEntity))
                return;
            VatBlockEntity tankBE = (VatBlockEntity) be;
            world.removeBlockEntity(pos);
            ConnectivityHandler.splitMulti(tankBE);
        }
    }
    @Override
    public Class<VatBlockEntity> getBlockEntityClass() {
        return VatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends VatBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.CHEMICAL_VAT.get();
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
            return TFMGLang.asId(name());
        }
    }

    // Tanks are less noisy when placed in batch
    public static final SoundType SILENCED_METAL =
            new DeferredSoundType(0.1F, 1.5F, () -> SoundEvents.METAL_BREAK, () -> SoundEvents.METAL_STEP,
                    () -> SoundEvents.METAL_PLACE, () -> SoundEvents.METAL_HIT, () -> SoundEvents.METAL_FALL);

    @Override
    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
        SoundType soundType = super.getSoundType(state, world, pos, entity);
        if (entity != null && entity.getPersistentData()
                .contains("SilenceTankSound"))
            return SILENCED_METAL;
        return soundType;
    }

    //@Override
    //public boolean hasAnalogOutputSignal(BlockState state) {
    //    return true;
    //}
//
    //@Override
    //public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
    //    return getBlockEntityOptional(worldIn, pos).map(SteelTankBlockEntity::getControllerBE)
    //            .map(te -> ComparatorUtil.fractionToRedstoneLevel(te.getFillState()))
    //            .orElse(0);
    //}


}
