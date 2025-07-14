package com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.fire;

import com.drmangotea.tfmg.TFMG;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


/**
 * aint making blockstates with registrate for these
 */
public class TFMGColoredFires {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, TFMG.MOD_ID);


    public static final DeferredHolder<Block,GreenFireBlock> GREEN_FIRE = BLOCKS.register("green_fire",
            () -> new GreenFireBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FIRE)
            .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(s -> 15)
    ));
    public static final DeferredHolder<Block,BlueFireBlock> BLUE_FIRE = BLOCKS.register("blue_fire",
            () -> new BlueFireBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FIRE)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(s -> 15)
            ));
    public static final DeferredHolder<Block,LithiumFireBlock> LITHIUM_FIRE = BLOCKS.register("lithium_fire",
            () -> new LithiumFireBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FIRE)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(s -> 15)
            ));


    public static void register (IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
