package com.drmangotea.tfmg.datagen.integration;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.datagen.integration.sable.BlockPhysicsProvider;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorShapes;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.concurrent.CompletableFuture;

@NothingNullByDefault
public class TFMGSableProvider extends BlockPhysicsProvider {
	public TFMGSableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(TFMG.MOD_ID, output, registries);
	}
	
	@Override
	protected void generate() {
		//engines
		add(TFMGBlocks.TURBINE_ENGINE.getDelegate(), b -> b.mass(1.5d)
			//TODO: Overrides for volume based on blockstate
		);
		add(TFMGBlocks.REGULAR_ENGINE.getDelegate(), b -> b.mass(1.5d)
			.volume(shapeVolume(TFMGShapes.ENGINE))
		);
		add(TFMGBlocks.RADIAL_ENGINE.getDelegate(),  b -> b.mass(1.5d));
		add(TFMGBlocks.LARGE_ENGINE.getDelegate(), b -> b.mass(2d));
		add(TFMGBlocks.SIMPLE_LARGE_ENGINE.getDelegate(), b -> b.mass(2d)
			.volume(shapeVolume(AllShapes.STEAM_ENGINE_WALL))
		);
		add(TFMGBlocks.ENGINE_GEARBOX.getDelegate(), b -> b.mass(2d)
			.volume(shapeVolume(TFMGShapes.ENGINE_GEARBOX))
		);
		//
		add(TFMGBlocks.INDUSTRIAL_PIPE.getDelegate(), b -> b
			.volume(shapeVolume(TFMGShapes.INDUSTRIAL_PIPE))
		);
		
		
		add(TFMGBlocks.ALUMINUM_BLOCK.getDelegate(), b -> b.mass(0.5d));
		add(TFMGBlocks.ALUMINUM_CASING.getDelegate(), b -> b.mass(0.5d));
		add(TFMGBlocks.ALUMINUM_CABLE_HUB.getDelegate(), b -> b.mass(0.5d));
		add(TFMGBlocks.ALUMINUM_BARS.getDelegate(), b -> b.mass(0.1d));
		add(TFMGBlocks.ALUMINUM_DOOR.getDelegate(), b -> b.mass(0.1d)
			.volume(shapeVolume(SlidingDoorShapes.get(Direction.NORTH, false, false)))
		);
		add(TFMGBlocks.ALUMINUM_SCAFFOLD.getDelegate(), b -> b.mass(0.1d));
	}
	
	private double shapeVolume(VoxelShaper shape) {
		return shapeVolume(shape.get(Direction.NORTH));
	}
	
	private double shapeVolume(VoxelShape shape) {
		return shape.toAabbs().stream()
			.map(box -> (box.maxX - box.minX)*(box.maxY - box.minY)*(box.maxZ - box.minZ))
			.reduce(0d, Double::sum);
	}
}
