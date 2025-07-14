package com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.fire;

import com.drmangotea.tfmg.registry.TFMGMobEffects;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LithiumFireBlock extends BaseFireBlock {
    public static final MapCodec<LithiumFireBlock> CODEC = simpleCodec(LithiumFireBlock::new);
    public static final int MAX_AGE = 15;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty UP = PipeBlock.UP;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((p_53467_) -> {
        return p_53467_.getKey() != Direction.DOWN;
    }).collect(Util.toMap());
    private static final VoxelShape UP_AABB = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private final Map<BlockState, VoxelShape> shapesCache;
    private final Object2IntMap<Block> igniteOdds = new Object2IntOpenHashMap<>();
    private final Object2IntMap<Block> burnOdds = new Object2IntOpenHashMap<>();

    public LithiumFireBlock(Properties p_53425_) {
        super(p_53425_, 1.0F);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)).setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)).setValue(UP, Boolean.valueOf(false)));
        this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream().filter((p_53497_) -> {
            return p_53497_.getValue(AGE) == 0;
        }).collect(Collectors.toMap(Function.identity(), LithiumFireBlock::calculateShape)));
    }

    private static VoxelShape calculateShape(BlockState p_53491_) {
        VoxelShape voxelshape = Shapes.empty();
        if (p_53491_.getValue(UP)) {
            voxelshape = UP_AABB;
        }

        if (p_53491_.getValue(NORTH)) {
            voxelshape = Shapes.or(voxelshape, NORTH_AABB);
        }

        if (p_53491_.getValue(SOUTH)) {
            voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
        }

        if (p_53491_.getValue(EAST)) {
            voxelshape = Shapes.or(voxelshape, EAST_AABB);
        }

        if (p_53491_.getValue(WEST)) {
            voxelshape = Shapes.or(voxelshape, WEST_AABB);
        }

        return voxelshape.isEmpty() ? DOWN_AABB : voxelshape;
    }

    public void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
            if (entity.getRemainingFireTicks() == 0) {
                entity.setRemainingFireTicks(8);
            }
            if(entity instanceof LivingEntity livingEntity)
                livingEntity.addEffect(new MobEffectInstance(TFMGMobEffects.HELLFIRE, 20*15));
        }

        entity.hurt(level.damageSources().inFire(), 2);
        super.entityInside(blockState, level, pos, entity);
    }

    public BlockState updateShape(BlockState p_53458_, Direction p_53459_, BlockState p_53460_, LevelAccessor p_53461_, BlockPos p_53462_, BlockPos p_53463_) {
        return this.canSurvive(p_53458_, p_53461_, p_53462_) ? this.getStateWithAge(p_53461_, p_53462_, p_53458_.getValue(AGE)) : Blocks.AIR.defaultBlockState();
    }

    public VoxelShape getShape(BlockState p_53474_, BlockGetter p_53475_, BlockPos p_53476_, CollisionContext p_53477_) {
        return this.shapesCache.get(p_53474_.setValue(AGE, Integer.valueOf(0)));
    }

    @Override
    protected MapCodec<? extends BaseFireBlock> codec() {
        return CODEC;
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_53427_) {
        return this.getStateForPlacement(p_53427_.getLevel(), p_53427_.getClickedPos());
    }

    protected BlockState getStateForPlacement(BlockGetter p_53471_, BlockPos p_53472_) {
        BlockPos blockpos = p_53472_.below();
        BlockState blockstate = p_53471_.getBlockState(blockpos);
        if (!this.canCatchFire(p_53471_, p_53472_, Direction.UP) && !blockstate.isFaceSturdy(p_53471_, blockpos, Direction.UP)) {
            BlockState blockstate1 = this.defaultBlockState();

            for(Direction direction : Direction.values()) {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(direction);
                if (booleanproperty != null) {
                    blockstate1 = blockstate1.setValue(booleanproperty, Boolean.valueOf(this.canCatchFire(p_53471_, p_53472_.relative(direction), direction.getOpposite())));
                }
            }

            return blockstate1;
        } else {
            return this.defaultBlockState();
        }
    }

    public boolean canSurvive(BlockState p_53454_, LevelReader p_53455_, BlockPos p_53456_) {
        BlockPos blockpos = p_53456_.below();
        return p_53455_.getBlockState(blockpos).isFaceSturdy(p_53455_, blockpos, Direction.UP) || this.isValidFireLocation(p_53455_, p_53456_);
    }

    public void tick(BlockState p_221160_, ServerLevel p_221161_, BlockPos p_221162_, RandomSource p_221163_) {
        p_221161_.scheduleTick(p_221162_, this, getFireTickDelay(p_221161_.random));
        if (p_221161_.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            if (!p_221160_.canSurvive(p_221161_, p_221162_)) {
                p_221161_.removeBlock(p_221162_, false);
            }

            BlockState blockstate = p_221161_.getBlockState(p_221162_.below());
            boolean flag = blockstate.isFireSource(p_221161_, p_221162_, Direction.UP);
            int i = p_221160_.getValue(AGE);
            if (!flag && p_221161_.isRaining() && this.isNearRain(p_221161_, p_221162_) && p_221163_.nextFloat() < 0.2F + (float)i * 0.03F) {
                p_221161_.removeBlock(p_221162_, false);
            } else {
                int j = Math.min(15, i + p_221163_.nextInt(3) / 2);
                if (i != j) {
                    p_221160_ = p_221160_.setValue(AGE, Integer.valueOf(j));
                    p_221161_.setBlock(p_221162_, p_221160_, 4);
                }

                if (!flag) {
                    if (!this.isValidFireLocation(p_221161_, p_221162_)) {
                        BlockPos blockpos = p_221162_.below();
                        if (!p_221161_.getBlockState(blockpos).isFaceSturdy(p_221161_, blockpos, Direction.UP) || i > 3) {
                            p_221161_.removeBlock(p_221162_, false);
                        }

                        return;
                    }

                    if (i == 15 && p_221163_.nextInt(4) == 0 && !this.canCatchFire(p_221161_, p_221162_.below(), Direction.UP)) {
                        p_221161_.removeBlock(p_221162_, false);
                        return;
                    }
                }

                boolean flag1 = p_221161_.getBiome(p_221162_).is(BiomeTags.INCREASED_FIRE_BURNOUT);
                int k = flag1 ? -50 : 0;
                this.tryCatchFire(p_221161_, p_221162_.east(), 300 + k, p_221163_, i, Direction.WEST);
                this.tryCatchFire(p_221161_, p_221162_.west(), 300 + k, p_221163_, i, Direction.EAST);
                this.tryCatchFire(p_221161_, p_221162_.below(), 250 + k, p_221163_, i, Direction.UP);
                this.tryCatchFire(p_221161_, p_221162_.above(), 250 + k, p_221163_, i, Direction.DOWN);
                this.tryCatchFire(p_221161_, p_221162_.north(), 300 + k, p_221163_, i, Direction.SOUTH);
                this.tryCatchFire(p_221161_, p_221162_.south(), 300 + k, p_221163_, i, Direction.NORTH);
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for(int l = -1; l <= 1; ++l) {
                    for(int i1 = -1; i1 <= 1; ++i1) {
                        for(int j1 = -1; j1 <= 4; ++j1) {
                            if (l != 0 || j1 != 0 || i1 != 0) {
                                int k1 = 100;
                                if (j1 > 1) {
                                    k1 += (j1 - 1) * 100;
                                }

                                blockpos$mutableblockpos.setWithOffset(p_221162_, l, j1, i1);
                                int l1 = this.getIgniteOdds(p_221161_, blockpos$mutableblockpos);
                                if (l1 > 0) {
                                    int i2 = (l1 + 40 + p_221161_.getDifficulty().getId() * 7) / (i + 30);
                                    if (flag1) {
                                        i2 /= 2;
                                    }

                                    if (i2 > 0 && p_221163_.nextInt(k1) <= i2 && (!p_221161_.isRaining() || !this.isNearRain(p_221161_, blockpos$mutableblockpos))) {
                                        int j2 = Math.min(15, i + p_221163_.nextInt(5) / 4);
                                        p_221161_.setBlock(blockpos$mutableblockpos, this.getStateWithAge(p_221161_, blockpos$mutableblockpos, j2), 3);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    protected boolean isNearRain(Level p_53429_, BlockPos p_53430_) {
        return p_53429_.isRainingAt(p_53430_) || p_53429_.isRainingAt(p_53430_.west()) || p_53429_.isRainingAt(p_53430_.east()) || p_53429_.isRainingAt(p_53430_.north()) || p_53429_.isRainingAt(p_53430_.south());
    }

    @Deprecated //Forge: Use IForgeBlockState.getFlammability, Public for default implementation only.
    public int getBurnOdds(BlockState p_221165_) {
        return p_221165_.hasProperty(BlockStateProperties.WATERLOGGED) && p_221165_.getValue(BlockStateProperties.WATERLOGGED) ? 0 : this.burnOdds.getInt(p_221165_.getBlock());
    }

    @Deprecated //Forge: Use IForgeBlockState.getFireSpreadSpeed
    public int getIgniteOdds(BlockState p_221167_) {
        return p_221167_.hasProperty(BlockStateProperties.WATERLOGGED) && p_221167_.getValue(BlockStateProperties.WATERLOGGED) ? 0 : this.igniteOdds.getInt(p_221167_.getBlock());
    }

    private void tryCatchFire(Level p_53432_, BlockPos p_53433_, int p_53434_, RandomSource p_53435_, int p_53436_, Direction face) {
        int i = p_53432_.getBlockState(p_53433_).getFlammability(p_53432_, p_53433_, face);
        if (p_53435_.nextInt(p_53434_) < i) {
            BlockState blockstate = p_53432_.getBlockState(p_53433_);
            if (p_53435_.nextInt(p_53434_ + 10) < 5 && !p_53432_.isRainingAt(p_53433_)) {
                int j = Math.min(p_53434_ + p_53435_.nextInt(5) / 4, 15);
                p_53432_.setBlock(p_53433_, this.getStateWithAge(p_53432_, p_53433_, j), 3);
            } else {
                p_53432_.removeBlock(p_53433_, false);
            }

            blockstate.onCaughtFire(p_53432_, p_53433_, face, null);
        }

    }
    private BlockState getStateWithAge(LevelAccessor p_53438_, BlockPos p_53439_, int p_53440_) {
        BlockState blockstate = getState(p_53438_, p_53439_);
        return blockstate.is(TFMGColoredFires.GREEN_FIRE.get()) ? blockstate.setValue(AGE, Integer.valueOf(p_53440_)) : blockstate;
    }

    private boolean isValidFireLocation(BlockGetter p_53486_, BlockPos p_53487_) {
        for(Direction direction : Direction.values()) {
            if (this.canCatchFire(p_53486_, p_53487_.relative(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    private int getIgniteOdds(LevelReader p_221157_, BlockPos p_221158_) {
        if (!p_221157_.isEmptyBlock(p_221158_)) {
            return 0;
        } else {
            int i = 0;

            for(Direction direction : Direction.values()) {
                BlockState blockstate = p_221157_.getBlockState(p_221158_.relative(direction));
                i = Math.max(blockstate.getFireSpreadSpeed(p_221157_, p_221158_.relative(direction), direction.getOpposite()), i);
            }

            return i;
        }
    }

    @Deprecated //Forge: Use canCatchFire with more context
    protected boolean canBurn(BlockState p_53489_) {
        return this.getIgniteOdds(p_53489_) > 0;
    }

    public void onPlace(BlockState p_53479_, Level p_53480_, BlockPos p_53481_, BlockState p_53482_, boolean p_53483_) {
        super.onPlace(p_53479_, p_53480_, p_53481_, p_53482_, p_53483_);
        p_53480_.scheduleTick(p_53481_, this, getFireTickDelay(p_53480_.random));
    }

    private static int getFireTickDelay(RandomSource p_221149_) {
        return 30 + p_221149_.nextInt(10);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53465_) {
        p_53465_.add(AGE, NORTH, EAST, SOUTH, WEST, UP);
    }
    public static BlockState getState(BlockGetter p_49246_, BlockPos p_49247_) {
        return ((LithiumFireBlock)TFMGColoredFires.LITHIUM_FIRE.get()).getStateForPlacement(p_49246_, p_49247_);
    }
    private void setFlammable(Block p_53445_, int p_53446_, int p_53447_) {
        if (p_53445_ == Blocks.AIR) throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
        this.igniteOdds.put(p_53445_, p_53446_);
        this.burnOdds.put(p_53445_, p_53447_);
    }


    public boolean canCatchFire(BlockGetter world, BlockPos pos, Direction face) {
        return world.getBlockState(pos).isFlammable(world, pos, face);
    }

}

