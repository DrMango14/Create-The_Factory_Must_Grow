package com.drmangotea.createindustry.ponder;


import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.ponder.scenes.MetallurgyScenes;
import com.drmangotea.createindustry.ponder.scenes.OilScenes;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;


public class TFMGPonderIndex {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CreateTFMG.MOD_ID);

    public static void register() {
        HELPER.forComponents(TFMGBlocks.GASOLINE_ENGINE, TFMGBlocks.GASOLINE_ENGINE_BACK,
                        TFMGBlocks.LPG_ENGINE, TFMGBlocks.LPG_ENGINE_BACK,
                        TFMGBlocks.TURBINE_ENGINE, TFMGBlocks.TURBINE_ENGINE_BACK)

                .addStoryBoard("small_engines", OilScenes::small_engines, TFMGPonderTag.OIL);


        HELPER.forComponents(TFMGBlocks.DIESEL_ENGINE)

                .addStoryBoard("diesel_engine", OilScenes::diesel_engine, TFMGPonderTag.OIL)
                .addStoryBoard("diesel_engine_expansion", OilScenes::diesel_engine_expansion, TFMGPonderTag.OIL);

        HELPER.forComponents(TFMGBlocks.SURFACE_SCANNER)

                .addStoryBoard("surface_scanner", OilScenes::surface_scanner, TFMGPonderTag.OIL);

        HELPER.forComponents(TFMGBlocks.PUMPJACK_BASE,TFMGBlocks.PUMPJACK_CRANK,TFMGBlocks.PUMPJACK_HAMMER_HOLDER)
                .addStoryBoard("pumpjack", OilScenes::pumpjack, TFMGPonderTag.OIL);
        HELPER.forComponents(TFMGBlocks.STEEL_DISTILLATION_CONTROLLER,TFMGBlocks.STEEL_DISTILLATION_OUTPUT)

                .addStoryBoard("distillation_tower", OilScenes::distillation_tower, TFMGPonderTag.OIL);
        /////////////////////////////////////////

        HELPER.forComponents(TFMGBlocks.BLAST_FURNACE_OUTPUT)

                .addStoryBoard("blast_furnace", MetallurgyScenes::blast_furnace, TFMGPonderTag.METALLURGY);


        HELPER.forComponents(TFMGBlocks.COKE_OVEN)

                .addStoryBoard("coke_oven", MetallurgyScenes::coke_oven, TFMGPonderTag.METALLURGY);


        HELPER.forComponents(TFMGBlocks.CASTING_BASIN,TFMGBlocks.CASTING_SPOUT)

                .addStoryBoard("casting", MetallurgyScenes::casting, TFMGPonderTag.METALLURGY);


    }
        public static void registerTags() {
            PonderRegistry.TAGS.forTag(TFMGPonderTag.OIL)
                    .add(TFMGBlocks.GASOLINE_ENGINE)
                    .add(TFMGBlocks.GASOLINE_ENGINE_BACK)
                    .add(TFMGBlocks.LPG_ENGINE)
                    .add(TFMGBlocks.LPG_ENGINE_BACK)
                    .add(TFMGBlocks.TURBINE_ENGINE)
                    .add(TFMGBlocks.TURBINE_ENGINE_BACK)
                    .add(TFMGBlocks.STEEL_DISTILLATION_OUTPUT)
                    .add(TFMGBlocks.STEEL_DISTILLATION_CONTROLLER)
                    .add(TFMGBlocks.PUMPJACK_BASE)
                    .add(TFMGBlocks.PUMPJACK_HAMMER_HOLDER)
                    .add(TFMGBlocks.DIESEL_ENGINE)
                    .add(TFMGBlocks.DIESEL_ENGINE_EXPANSION)
                    .add(TFMGBlocks.PUMPJACK_CRANK);

            PonderRegistry.TAGS.forTag(TFMGPonderTag.METALLURGY)
                    .add(TFMGBlocks.COKE_OVEN)
                    .add(TFMGBlocks.CASTING_BASIN)
                    .add(TFMGBlocks.CASTING_SPOUT)
                    .add(TFMGBlocks.FIREPROOF_BRICKS)
                    .add(TFMGBlocks.FIREPROOF_BRICK_REINFORCEMENT)
                    .add(TFMGBlocks.BLAST_FURNACE_OUTPUT);



        }

    }
