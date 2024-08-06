package com.drmangotea.createindustry.items.weapons.explosives.thermite_grenades.fire;

import com.drmangotea.createindustry.CreateTFMG;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

/**
 * making blockstate for fire in registrate is pain/idk how to do it
 * so no registrate for those :skull:
 */
public class TFMGColoredFires {

    public static final LazyRegistrar<Block> BLOCKS =
            LazyRegistrar.create(Registry.BLOCK, CreateTFMG.MOD_ID);


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


    public static void register (){
        BLOCKS.register();
    }
}
