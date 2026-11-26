package com.drmangotea.tfmg.datagen.integration;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGPaletteStoneTypes;
import com.simibubi.create.AllItems;
import dev.metallurgists.rutile.api.data.provider.composition.FluidCompositionProvider;
import dev.metallurgists.rutile.api.data.provider.composition.ItemCompositionProvider;
import dev.metallurgists.rutile.registry.RutileElements;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TFMGRutileProvider {

    public static class Item extends ItemCompositionProvider {
        public Item(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(TFMG.MOD_ID, output, registries);
        }

        @Override
        public void generate(HolderLookup.Provider registries) {
            addData("sulfur", List.of(TFMGBlocks.SULFUR.asItem(), TFMGItems.SULFUR_DUST.asItem()), c -> c.element(RutileElements.S));
            addData("nickel", List.of(TFMGBlocks.RAW_NICKEL_BLOCK.asItem(), TFMGBlocks.NICKEL_BLOCK.asItem(), TFMGItems.RAW_NICKEL.asItem(), TFMGItems.NICKEL_INGOT.asItem(), TFMGItems.NICKEL_NUGGET.asItem(), TFMGItems.NICKEL_SHEET.asItem(), AllItems.CRUSHED_NICKEL.asItem()),c -> c.element(RutileElements.Ni));
            addData("lead", List.of(TFMGBlocks.RAW_LEAD_BLOCK.asItem(), TFMGBlocks.LEAD_BLOCK.asItem(), TFMGItems.RAW_LEAD.asItem(), TFMGItems.LEAD_INGOT.asItem(), TFMGItems.LEAD_NUGGET.asItem(), TFMGItems.LEAD_SHEET.asItem(), AllItems.CRUSHED_LEAD.asItem()), c -> c.element(RutileElements.Pb));
            addData("lithium", List.of(TFMGBlocks.RAW_LITHIUM_BLOCK.asItem(), TFMGBlocks.LITHIUM_BLOCK.asItem(), TFMGItems.RAW_LITHIUM.asItem(), TFMGItems.LITHIUM_INGOT.asItem(), TFMGItems.LITHIUM_NUGGET.asItem(), TFMGItems.CRUSHED_LITHIUM.asItem()), c -> c.element(RutileElements.Li));
            addData("steel", List.of(TFMGBlocks.STEEL_BLOCK.asItem(), TFMGItems.STEEL_INGOT.asItem(), TFMGItems.STEEL_NUGGET.asItem(), TFMGItems.HEAVY_PLATE.asItem()), c -> c.element(RutileElements.Fe));
            addData("cast_iron", List.of(TFMGBlocks.CAST_IRON_BLOCK.asItem(), TFMGItems.CAST_IRON_INGOT.asItem(), TFMGItems.CAST_IRON_NUGGET.asItem(), TFMGItems.CAST_IRON_SHEET.asItem()), c -> c.element(RutileElements.Fe));
            addData("aluminum", List.of(TFMGBlocks.ALUMINUM_BLOCK.asItem(), TFMGItems.ALUMINUM_INGOT.asItem(), TFMGItems.ALUMINUM_NUGGET.asItem(), TFMGItems.ALUMINUM_SHEET.asItem()), c -> c.element(RutileElements.Al));
            addData("constantan", List.of(TFMGBlocks.CONSTANTAN_BLOCK.asItem(), TFMGItems.CONSTANTAN_INGOT.asItem(), TFMGItems.CONSTANTAN_NUGGET.asItem()), c -> c.element(RutileElements.Cu).element(RutileElements.Ni));
            addData("coal_coke", List.of(TFMGBlocks.COAL_COKE_BLOCK.asItem(), TFMGItems.COAL_COKE.asItem(), TFMGItems.COAL_COKE_DUST.asItem()), c -> c.element(RutileElements.C));
            addData("magnetic_alloy", List.of(TFMGBlocks.LAMINATED_MAGNETIC_ALLOY_BLOCK.asItem(), TFMGItems.MAGNETIC_ALLOY_INGOT.asItem(), TFMGItems.MAGNETIC_ALLOY_SHEET.asItem(), TFMGItems.MAGNET.asItem()), c -> c.element(RutileElements.Ni, 2).element(RutileElements.Si).element(RutileElements.Fe, 2));
            addData("limesand", List.of(TFMGItems.LIMESAND.asItem()), c -> c.element(RutileElements.Ca).element(RutileElements.C).element(RutileElements.O, 3));
            addData("nitrate_dust", List.of(TFMGItems.NITRATE_DUST.asItem()), c -> c.element(RutileElements.K).element(RutileElements.N).element(RutileElements.O, 3));
            addData("silicon", List.of(TFMGItems.SILICON_INGOT.asItem()), c -> c.element(RutileElements.Si));
            addData("copper_sulfate", List.of(TFMGItems.COPPER_SULFATE.asItem()), c -> c.element(RutileElements.Cu).element(RutileElements.S).element(RutileElements.O, 4));
            addData("copper", List.of(TFMGItems.COPPER_ELECTRODE.asItem()), c -> c.element(RutileElements.Cu));
            addData("zinc", List.of(TFMGItems.ZINC_ELECTRODE.asItem()), c -> c.element(RutileElements.Zn));
            addData("graphite", List.of(TFMGItems.GRAPHITE_ELECTRODE.asItem()), c -> c.element(RutileElements.C));
            addData("bauxite", List.of(TFMGPaletteStoneTypes.BAUXITE.getBaseBlock().get().asItem(), TFMGItems.BAUXITE_POWDER.asItem()), c -> c.element(RutileElements.Al, 2).element(RutileElements.O, 3).element(RutileElements.H, 2));
            addData("galena", List.of(TFMGPaletteStoneTypes.GALENA.getBaseBlock().get().asItem()), c -> c.element(RutileElements.Pb).element(RutileElements.S));
        }
    }

    public static class Fluid extends FluidCompositionProvider {
        public Fluid(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(TFMG.MOD_ID, output, registries);
        }

        @Override
        public void generate(HolderLookup.Provider registries) {
            addData("lpg", TFMGFluids.LPG.get().getSource(), c -> c
                    .element(RutileElements.C, 5)
                    .element(RutileElements.H, 12));
            addData("butane", TFMGFluids.BUTANE.get().getSource(), c -> c
                    .element(RutileElements.C, 4)
                    .element(RutileElements.H, 10));
            addData("propane", TFMGFluids.PROPANE.get().getSource(), c -> c
                    .element(RutileElements.C, 3)
                    .element(RutileElements.H, 8));
            addData("hydrogen", TFMGFluids.HYDROGEN.get().getSource(), c -> c.element(RutileElements.H));
            addData("ethylene", TFMGFluids.ETHYLENE.get().getSource(), c -> c
                    .element(RutileElements.C, 2)
                    .element(RutileElements.H, 4));
            addData("propylene", TFMGFluids.PROPYLENE.get().getSource(), c -> c
                    .element(RutileElements.C, 3)
                    .element(RutileElements.H, 6));
            addData("neon", TFMGFluids.NEON.get().getSource(), c -> c.element(RutileElements.Ne));
            addData("carbon_dioxide", TFMGFluids.CARBON_DIOXIDE.get().getSource(), c -> c
                    .element(RutileElements.C)
                    .element(RutileElements.O, 2));
            addData("gasoline", TFMGFluids.GASOLINE.get().getSource(), c -> c
                    .element(RutileElements.C, 12)
                    .element(RutileElements.H, 2));
            addData("diesel", TFMGFluids.DIESEL.get().getSource(), c -> c
                    .element(RutileElements.C, 12)
                    .element(RutileElements.H, 23));
            addData("naphtha", TFMGFluids.NAPHTHA.get().getSource(), c -> c
                    .element(RutileElements.C, 5)
                    .element(RutileElements.H, 10));
            addData("kerosene", TFMGFluids.KEROSENE.get().getSource(), c -> c
                    .element(RutileElements.C, 12)
                    .element(RutileElements.H, 26));
            addData("creosote", TFMGFluids.CREOSOTE.get().getSource(), c -> c
                    .element(RutileElements.C)
                    .element(RutileElements.H)
                    .setAmount(2).next()
                    .element(RutileElements.O));
            addData("molten_steel", TFMGFluids.MOLTEN_STEEL.get().getSource(), c -> c.element(RutileElements.Fe));
            addData("molten_slag", TFMGFluids.MOLTEN_SLAG.get().getSource(), c -> c
                    .element(RutileElements.Si)
                    .element(RutileElements.O, 2)
                    .setAmount(13).next()
                    .element(RutileElements.Fe)
                    .element(RutileElements.O)
                    .setAmount(20).next()
                    .element(RutileElements.Ca)
                    .element(RutileElements.O)
                    .setAmount(40)
            );

            addData("liquid_silicon", TFMGFluids.LIQUID_SILICON.get().getSource(), c -> c.element(RutileElements.Si));
            addData("napalm", TFMGFluids.NAPALM.get().getSource(), c -> c
                    .element(RutileElements.C, 12)
                    .element(RutileElements.H, 5)
                    .next()
                    .element(RutileElements.Al));
            addData("sulfuric_acid", TFMGFluids.SULFURIC_ACID.get().getSource(), c -> c
                    .element(RutileElements.H, 2)
                    .element(RutileElements.S)
                    .element(RutileElements.O, 4));
        }
    }
}
