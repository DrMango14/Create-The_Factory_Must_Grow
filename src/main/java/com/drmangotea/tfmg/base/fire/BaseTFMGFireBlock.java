package com.drmangotea.tfmg.base.fire;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.createmod.catnip.data.Iterate;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
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

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@NothingNullByDefault
public abstract class BaseTFMGFireBlock extends BaseFireBlock {
	
	public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
	public static final BooleanProperty
		NORTH = PipeBlock.NORTH,
		EAST = PipeBlock.EAST,
		SOUTH = PipeBlock.SOUTH,
		WEST = PipeBlock.WEST,
		UP = PipeBlock.UP;
	protected static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((e) -> e.getKey() != Direction.DOWN).collect(Util.toMap());
	
	protected static final VoxelShape
		UP_AABB = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D),
		WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D),
		EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
		NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D),
		SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
	
	protected final Map<BlockState, VoxelShape> shapesCache;
	
	protected final Object2IntMap<Block> igniteOdds = new Object2IntOpenHashMap<>();
	
	public BaseTFMGFireBlock(Properties properties, float fireDamage) {
		super(properties, fireDamage);
		
		this.registerDefaultState(this.stateDefinition.any()
			.setValue(AGE, 0)
			.setValue(NORTH, false)
			.setValue(EAST, false)
			.setValue(SOUTH, false)
			.setValue(WEST, false)
			.setValue(UP, false)
		);
		
		this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream()
			.filter((state) -> state.getValue(AGE) == 0)
			.collect(Collectors.toMap(Function.identity(), BaseTFMGFireBlock::calculateShape))
		);
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE, NORTH, EAST, SOUTH, WEST, UP);
	}
	
	private static VoxelShape calculateShape(BlockState state) {
		VoxelShape voxelshape = Shapes.empty();
		if (state.getValue(UP)) {
			voxelshape = UP_AABB;
		}
		
		if (state.getValue(NORTH)) {
			voxelshape = Shapes.or(voxelshape, NORTH_AABB);
		}
		
		if (state.getValue(SOUTH)) {
			voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
		}
		
		if (state.getValue(EAST)) {
			voxelshape = Shapes.or(voxelshape, EAST_AABB);
		}
		
		if (state.getValue(WEST)) {
			voxelshape = Shapes.or(voxelshape, WEST_AABB);
		}
		
		return voxelshape.isEmpty() ? DOWN_AABB : voxelshape;
	}
	
	public BlockState getStateForPlacement(BlockGetter level, BlockPos pos) {
		BlockPos blockpos = pos.below();
		BlockState blockstate = level.getBlockState(blockpos);
		if (canCatchFire(level, pos, Direction.UP) || blockstate.isFaceSturdy(level, blockpos, Direction.UP)) {
			return defaultBlockState();
		}
		
		BlockState defaultState = this.defaultBlockState();
		
		for(Direction direction : Direction.values()) {
			BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(direction);
			if (booleanproperty != null) {
				defaultState = defaultState.setValue(booleanproperty, this.canCatchFire(level, pos.relative(direction), direction.getOpposite()));
			}
		}
		
		return defaultState;
	}
	
	protected BlockState getStateWithAge(LevelAccessor level, BlockPos pos, int age) {
		return getStateForPlacement(level, pos).setValue(AGE, age);
	}
	
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		return canSurvive(state, level, pos) ? getStateWithAge(level, pos, state.getValue(AGE)) : Blocks.AIR.defaultBlockState();
	}
	
	public VoxelShape getShape(BlockState p_53474_, BlockGetter p_53475_, BlockPos p_53476_, CollisionContext p_53477_) {
		return this.shapesCache.get(p_53474_.setValue(AGE, 0));
	}
	
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(state, level, pos, oldState, isMoving);
		level.scheduleTick(pos, this, getFireTickDelay(level.random));
	}
	
	protected static int getFireTickDelay(RandomSource random) {
		return 30 + random.nextInt(10);
	}
	
	@Deprecated
	public int getIgniteOdds(BlockState state) {
		return state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED) ? 0 : this.igniteOdds.getInt(state.getBlock());
	}
	
	@Deprecated
	protected boolean canBurn(BlockState state) {
		return this.getIgniteOdds(state) > 0;
	}
	
	public boolean canCatchFire(BlockGetter world, BlockPos pos, Direction face) {
		return world.getBlockState(pos).isFlammable(world, pos, face);
	}
	
	protected boolean isValidFireLocation(BlockGetter level, BlockPos pos) {
		for(Direction direction : Direction.values()) {
			if (this.canCatchFire(level, pos.relative(direction), direction.getOpposite())) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean canSurvive(BlockState p_53454_, LevelReader p_53455_, BlockPos p_53456_) {
		BlockPos blockpos = p_53456_.below();
		return p_53455_.getBlockState(blockpos).isFaceSturdy(p_53455_, blockpos, Direction.UP) || this.isValidFireLocation(p_53455_, p_53456_);
	}
	
	protected int getIgniteOdds(LevelReader p_221157_, BlockPos p_221158_) {
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
	
	protected boolean isNearRain(Level level, BlockPos pos) {
		if (level.isRainingAt(pos)) return true;
		
		return Arrays.stream(Iterate.horizontalDirections).map(d -> level.isRainingAt(pos.relative(d)))
			.reduce((a, b) -> a||b).orElse(false);
	}
	
	protected void tryCatchFire(Level p_53432_, BlockPos p_53433_, int p_53434_, RandomSource p_53435_, Direction face) {
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
	
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		level.scheduleTick(pos, this, getFireTickDelay(level.random));
		
		if (!level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) return;
		
		if (!state.canSurvive(level, pos)) {
			level.removeBlock(pos, false);
		}
		
		BlockState blockstate = level.getBlockState(pos.below());
		boolean notSource = !blockstate.isFireSource(level, pos, Direction.UP);
		int age = state.getValue(AGE);
		if (notSource && level.isRaining() && this.isNearRain(level, pos) && random.nextFloat() < 0.2F + 0.03F*age) {
			level.removeBlock(pos, false);
			return;
		}
		
		int newAge = Math.min(15, age + random.nextInt(3) / 2);
		if (age != newAge) {
			state = state.setValue(AGE, newAge);
			level.setBlock(pos, state, 4);
		}
		
		if (notSource) {
			if (!this.isValidFireLocation(level, pos)) {
				BlockPos blockpos = pos.below();
				if (!level.getBlockState(blockpos).isFaceSturdy(level, blockpos, Direction.UP) || age > 3) {
					level.removeBlock(pos, false);
				}
				
				return;
			}
			
			if (age == 15 && random.nextInt(4) == 0 && !this.canCatchFire(level, pos.below(), Direction.UP)) {
				level.removeBlock(pos, false);
				return;
			}
		}
		
		boolean flag1 = level.getBiome(pos).is(BiomeTags.INCREASED_FIRE_BURNOUT);
		int k = flag1 ? -50 : 0;
		tryCatchFire(level, pos.east(), 300 + k, random, Direction.WEST);
		tryCatchFire(level, pos.west(), 300 + k, random, Direction.EAST);
		tryCatchFire(level, pos.below(), 250 + k, random, Direction.UP);
		tryCatchFire(level, pos.above(), 250 + k, random, Direction.DOWN);
		tryCatchFire(level, pos.north(), 300 + k, random, Direction.SOUTH);
		tryCatchFire(level, pos.south(), 300 + k, random, Direction.NORTH);
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		
		for(int l = -1; l <= 1; ++l) {
			for(int i1 = -1; i1 <= 1; ++i1) {
				for(int j1 = -1; j1 <= 4; ++j1) {
					if (l != 0 || j1 != 0 || i1 != 0) {
						int k1 = 100;
						if (j1 > 1) {
							k1 += (j1 - 1) * 100;
						}
						
						blockpos$mutableblockpos.setWithOffset(pos, l, j1, i1);
						int l1 = this.getIgniteOdds(level, blockpos$mutableblockpos);
						if (l1 > 0) {
							int i2 = (l1 + 40 + level.getDifficulty().getId() * 7) / (age + 30);
							if (flag1) {
								i2 /= 2;
							}
							
							if (i2 > 0 && random.nextInt(k1) <= i2 && (!level.isRaining() || !this.isNearRain(level, blockpos$mutableblockpos))) {
								int j2 = Math.min(15, age + random.nextInt(5) / 4);
								level.setBlock(blockpos$mutableblockpos, this.getStateWithAge(level, blockpos$mutableblockpos, j2), 3);
							}
						}
					}
				}
			}
		}
		
	}
}
