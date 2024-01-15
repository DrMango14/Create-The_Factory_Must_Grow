package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.CreateTFMG;


import com.drmangotea.tfmg.base.DebugBlock;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFMGCreativeModeTabs {
        private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
                DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateTFMG.MOD_ID);

        public static final RegistryObject<CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.tfmg.base"))
                        .withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId())
                        .icon(TFMGBlocks.GASOLINE_ENGINE::asStack)
                        .displayItems(new RegistrateDisplayItemsGenerator(TFMGCreativeModeTabs.MAIN_TAB))
                        .build());

        public static final RegistryObject<CreativeModeTab> BUILDING_TAB = TAB_REGISTER.register("building",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.tfmg.building"))
                        .withTabsBefore(MAIN_TAB.getId())
                        .icon(TFMGBlocks.CONCRETE::asStack)
                        .displayItems(new RegistrateDisplayItemsGenerator(TFMGCreativeModeTabs.BUILDING_TAB))
                        .build());

        public static void register(IEventBus modEventBus) {
                TAB_REGISTER.register(modEventBus);
        }

        public static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {



                private final RegistryObject<CreativeModeTab> tabFilter;
                public RegistrateDisplayItemsGenerator(RegistryObject<CreativeModeTab> tabFilter) {

                        this.tabFilter = tabFilter;
                }

                private List<Item> collectBlocks() {
                        List<Item> items = new ReferenceArrayList<>();
                        for (RegistryEntry<Block> entry : CreateTFMG.REGISTRATE.getAll(Registries.BLOCK)) {
                                if (!CreateRegistrate.isInCreativeTab(entry, tabFilter))
                                        continue;
                                Item item = entry.get()
                                        .asItem();
                                if (item == Items.AIR)
                                        continue;


                                items.add(item);
                        }
                        items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
                        return items;
                }

                private List<Item> collectItems(RegistryObject<CreativeModeTab> tab, Predicate<Item> exclusionPredicate) {
                        List<Item> items = new ReferenceArrayList<>();


                        for (RegistryEntry<Item> entry : CreateTFMG.REGISTRATE.getAll(Registries.ITEM)) {
                                if (!CreateTFMG.REGISTRATE.isInCreativeTab(entry, tab))
                                        continue;
                                Item item = entry.get();
                                if (item instanceof BlockItem)
                                        continue;
                                if (!exclusionPredicate.test(item))
                                        items.add(item);
                        }
                        return items;
                }

                private static void outputAll(CreativeModeTab.Output output, List<Item> items) {
                        for (Item item : items) {
                                output.accept(item);
                        }
                }

                List<Item> exclude = List.of(
                        TFMGItems.UNPROCESSED_HEAVY_PLATE.get(),
                        TFMGItems.UNFINISHED_GASOLINE_ENGINE.get(),
                        TFMGItems.UNFINISHED_TURBINE_ENGINE.get(),
                        TFMGItems.UNFINISHED_LPG_ENGINE.get(),
                        TFMGItems.UNFINISHED_STEEL_MECHANISM.get(),
                        TFMGBlocks.RADIAL_ENGINE_INPUT_PONDER.get().asItem()


                );


                @Override
                public void accept(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
                        List<Item> items = new LinkedList<>();
                        items.addAll(collectBlocks());

                        items.addAll(collectItems(tabFilter, (item) -> exclude.contains(item)));

                        outputAll(output, items);
                }
        }
}