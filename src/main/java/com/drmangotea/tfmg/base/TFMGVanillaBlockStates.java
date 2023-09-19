package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.CreateTFMG;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;

public class TFMGVanillaBlockStates {

    public static void generateWallBlockState(DataGenContext<Block, WallBlock> ctx, RegistrateBlockstateProvider prov,
                                                 String name) {
        prov.wallBlock(ctx.get(), name, CreateTFMG.asResource("block/"+name));
    }


}
