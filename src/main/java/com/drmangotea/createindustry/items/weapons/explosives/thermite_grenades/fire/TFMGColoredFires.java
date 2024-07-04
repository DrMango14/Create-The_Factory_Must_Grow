package com.drmangotea.createindustry.items.weapons.explosives.thermite_grenades.fire;

import com.drmangotea.createindustry.CreateTFMG;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * making blockstate for fire in registrate is pain/idk how to do it
 * so no registrate for those :skull:
 */
public class TFMGColoredFires {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CreateTFMG.MOD_ID);


    public static final RegistryObject<Block> GREEN_FIRE = BLOCKS.register("green_fire",
            () -> new GreenFireBlock(BlockBehaviour.Properties.of(Material.FIRE)
            .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(s -> 15)
    ));
    public static final RegistryObject<Block> BLUE_FIRE = BLOCKS.register("blue_fire",
            () -> new BlueFireBlock(BlockBehaviour.Properties.of(Material.FIRE)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(s -> 15)
            ));


    public static void register (IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
