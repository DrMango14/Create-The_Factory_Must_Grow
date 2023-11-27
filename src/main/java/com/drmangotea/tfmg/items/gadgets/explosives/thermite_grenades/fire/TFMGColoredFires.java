package com.drmangotea.tfmg.items.gadgets.explosives.thermite_grenades.fire;

import com.drmangotea.tfmg.CreateTFMG;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * making blockstate for fire in registrate is pain/idk how to do it
 * so no registrate for those :skull:
 */

/**
 * i know how to do it with registrate now
 * but i am too lazy to do it :3
 *
 */
public class TFMGColoredFires {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CreateTFMG.MOD_ID);


    public static final RegistryObject<Block> GREEN_FIRE = BLOCKS.register("green_fire",
            () -> new GreenFireBlock(BlockBehaviour.Properties.copy(Blocks.FIRE)
            .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(s -> 15)
    ));
    public static final RegistryObject<Block> BLUE_FIRE = BLOCKS.register("blue_fire",
            () -> new BlueFireBlock(BlockBehaviour.Properties.copy(Blocks.FIRE)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(s -> 15)
            ));


    public static void register (IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
