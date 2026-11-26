package com.drmangotea.tfmg.base.fire;

import com.drmangotea.tfmg.TFMG;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;


/**
 * aint making blockstates with registrate for these
 */
public class TFMGColoredFires {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, TFMG.MOD_ID);

	public static final DeferredHolder<Block, GreenFireBlock>
		GREEN_FIRE = fire("green_fire", GreenFireBlock::new);
    public static final DeferredHolder<Block, BlueFireBlock>
		BLUE_FIRE = fire("blue_fire", BlueFireBlock::new);
    public static final DeferredHolder<Block, LithiumFireBlock>
		LITHIUM_FIRE = fire("lithium_fire", LithiumFireBlock::new);
	
	public static <T extends BaseTFMGFireBlock> DeferredHolder<Block, T> fire(
		String name,
		Function<BlockBehaviour.Properties, T> constructor
	) {
		return BLOCKS.register(name, () -> constructor.apply(
			BlockBehaviour.Properties.ofFullCopy(Blocks.FIRE)
			.requiresCorrectToolForDrops()
			.noOcclusion()
			.lightLevel(s -> 15)
		));
	}

    public static void register (IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
