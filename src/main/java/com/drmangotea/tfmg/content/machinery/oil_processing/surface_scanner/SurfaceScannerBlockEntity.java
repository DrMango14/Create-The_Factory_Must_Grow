package com.drmangotea.tfmg.content.machinery.oil_processing.surface_scanner;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.machinery.misc.machine_input.MachineInputBlockEntity;
import com.drmangotea.tfmg.integration.sable.SurfaceScannerSable;
import com.drmangotea.tfmg.registry.TFMGDataAttachments;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SurfaceScannerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    private long lastScanTick = Long.MIN_VALUE;
    private ChunkPos lastScanPos = null;
	private BlockPos nearestDeposit = null;
	private EnumMap<Direction, Integer> signals = blankSignal();
	private boolean operational = false;
	
    public boolean[][] grid = new boolean[7][7];

    public SurfaceScannerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(20);
    }
	
	private static EnumMap<Direction, Integer> blankSignal() {
		return new EnumMap<>(Map.of(
			Direction.DOWN, 0,
			Direction.UP, 0,
			Direction.NORTH,0,
			Direction.SOUTH, 0,
			Direction.WEST, 0,
			Direction.EAST, 0
		));
	}

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    public void findDeposits() {
        if (level == null) return;

		BlockPos actualPosition = SurfaceScannerSable.getActualPosition(this);
		ChunkPos chunkPos = level.getChunk(actualPosition).getPos();
		
        for (int x = 0; x < 7; x++) { for (int z = 0; z < 7; z++) {
			ChunkAccess chunk = level.getChunk(chunkPos.x + x - 3, chunkPos.z + z - 3);
			boolean oil = chunk.hasData(TFMGDataAttachments.FLUID_RESERVOIR);
			grid[x][z] = oil;
			if (!oil) continue;
			BlockPos midpoint = chunk.getPos().getMiddleBlockPosition(0).north().west();
			if (nearestDeposit == null) {
				nearestDeposit = midpoint;
			} else {
				if (nearestDeposit.equals(midpoint)) continue;
				float currentDistance = TFMGUtils.getDistance(actualPosition, nearestDeposit, true);
				float newDistance = TFMGUtils.getDistance(actualPosition, midpoint, true);
				if (newDistance < currentDistance) nearestDeposit = midpoint;
			}
		} }
    }
	
	public boolean operational() { //just in case it's needed
		return operational;
	}

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        TFMGTexts.header("surface_scanner")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        if (operational) {
            int depositsFound = 0;
            for (boolean[] row : grid) {
                for (boolean light : row) {
                    if (light) depositsFound++;
                }
            }
            if (depositsFound > 0) {
                TFMGTexts.SurfaceScanner.deposits(depositsFound).forGoggles(tooltip);
            } else {
				TFMGTexts.SurfaceScanner.noDeposit().forGoggles(tooltip);
            }
        } else {
			TFMGTexts.CommonMachines.minRPM(TFMGConfigs.common().machines.surfaceScannerMinimumRPM.get())
				.style(ChatFormatting.DARK_RED).forGoggles(tooltip);
        }
        return true;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level == null) return;
		
		if (level.getBlockEntity(getBlockPos().below()) instanceof MachineInputBlockEntity input &&
			Math.abs(input.getSpeed()) >= TFMGConfigs.common().machines.surfaceScannerMinimumRPM.get()
		) {
			if (!operational) operational = true;
		} else {
			if (operational) { //logic when power is lost
				operational = false;
				grid = new boolean[7][7];
				nearestDeposit = null;
				//so that you don't need to wait for rescan right after regaining power
				lastScanTick = Long.MIN_VALUE;
				//zero out redstone
				signals = blankSignal();
				notifyUpdate();
			}
		}
		
		if (!operational) return;
		
		BlockPos actualPosition = SurfaceScannerSable.getActualPosition(this);
		ChunkPos actualChunkPos = level.getChunk(actualPosition).getPos();
		Quaterniond currentRot = SurfaceScannerSable.getSublevelRot(this);
		recalculateSignals(actualPosition, currentRot);
		boolean moved = lastScanPos == null || !lastScanPos.equals(actualChunkPos);
		long currentTick = level.getGameTime();
		boolean intervalElapsed = lastScanTick == Long.MIN_VALUE || (currentTick - lastScanTick) >= 2400;
		
		if (!moved && !intervalElapsed) return;
		
		findDeposits();
		lastScanPos = actualChunkPos;
		lastScanTick = currentTick;
	}
	
	public int getDirectionalSignal (Direction side) {
		if (!operational) return 0;
		return signals.get(side);
	}
	
	private void recalculateSignals (BlockPos actualPosition, Quaterniond rot) {
		if (level == null) return;
		if (nearestDeposit == null) {
			signals = blankSignal();
			return;
		}
		
		Vec3 toNearest = Vec3.atLowerCornerOf(actualPosition.subtract(nearestDeposit));
		//2d distance:
		double invDist = Mth.invSqrt(toNearest.x() * toNearest.x() + toNearest.z() * toNearest.z());
		//normalized vector towards nearest deposit:
		Vec3 toNearestNormalized =  new Vec3(toNearest.x() * invDist, 0, toNearest.z() * invDist);
		
		EnumMap<Direction,Vec3> normals = TFMGUtils.getRotatedNormals(rot);
		
		double maxSignal = 30d / Math.PI;
		Block block = getBlockState().getBlock();
		boolean update = false;
		for (Map.Entry<Direction, Integer> e : signals.entrySet()) {
			Direction d = e.getKey();
			if (d == Direction.DOWN || d == Direction.UP) continue;
			int signal = (int) Math.max(0, maxSignal * Math.asin(toNearestNormalized.dot(normals.get(d))));
			if (e.getValue() == signal) continue;
			
			update = true;
			signals.put(d, signal);
			level.updateNeighborsAt(worldPosition.relative(d), block);
		}
		
		if (update) {
			notifyUpdate();
			level.updateNeighborsAt(worldPosition, block);
		}
	}
}
