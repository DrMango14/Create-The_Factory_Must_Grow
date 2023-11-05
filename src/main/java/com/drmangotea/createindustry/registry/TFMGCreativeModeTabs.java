package com.drmangotea.createindustry.registry;

import com.drmangotea.createindustry.CreateTFMG;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFMGCreativeModeTabs {

        private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
                DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateTFMG.MOD_ID);

        public static final RegistryObject<CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("base",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.create.base"))
                        .withTabsBefore(AllCreativeModeTabs.BUILDING_BLOCKS_TAB.getId())
                        .icon(() -> TFMGBlocks.GASOLINE_ENGINE.asStack())
                        .displayItems(new RegistrateDisplayItemsGenerator(true))
                        .build());

        public static final RegistryObject<CreativeModeTab> BUILDING_BLOCKS_TAB = TAB_REGISTER.register("building",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.create.palettes"))
                        .withTabsBefore(MAIN_TAB.getId())
                        .icon(() -> TFMGBlocks.CONCRETE.asStack())
                        .displayItems(new RegistrateDisplayItemsGenerator(false))
                        .build());

        public static void register(IEventBus modEventBus) {
                TAB_REGISTER.register(modEventBus);
        }

        public static CreativeModeTab getBaseTab() {
                return MAIN_TAB.get();
        }

        public static CreativeModeTab getBuildingTab() {
                return BUILDING_BLOCKS_TAB.get();
        }

        public static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

                private final boolean mainTab;

                public RegistrateDisplayItemsGenerator(boolean mainTab) {
                        this.mainTab = mainTab;
                }
                private static Predicate<Item> makeExclusionPredicate() {
                        Set<Item> exclusions = new ReferenceOpenHashSet<>();

                        List<ItemProviderEntry<?>> simpleExclusions = List.of(
                                TFMGItems.UNPROCESSED_HEAVY_PLATE,
                                TFMGItems.UNFINISHED_GASOLINE_ENGINE,
                                TFMGItems.UNFINISHED_TURBINE_ENGINE,
                                TFMGItems.UNFINISHED_LPG_ENGINE,
                                TFMGItems.UNFINISHED_STEEL_MECHANISM
                        );


                        for (ItemProviderEntry<?> entry : simpleExclusions) {
                                exclusions.add(entry.asItem());
                        }



                        return exclusions::contains;
                }

                //private static List<ItemOrdering> makeOrderings() {
                //        List<ItemOrdering> orderings = new ReferenceArrayList<>();
//
                //        Map<ItemProviderEntry<?>, ItemProviderEntry<?>> simpleBeforeOrderings = Map.of(
                //                AllItems.EMPTY_BLAZE_BURNER, AllBlocks.BLAZE_BURNER,
                //                AllItems.SCHEDULE, AllBlocks.TRACK_STATION
                //        );
//
                //        Map<ItemProviderEntry<?>, ItemProviderEntry<?>> simpleAfterOrderings = Map.of(
                //                AllItems.VERTICAL_GEARBOX, AllBlocks.GEARBOX
                //        );
//
                //        simpleBeforeOrderings.forEach((entry, otherEntry) -> {
                //                orderings.add(ItemOrdering.before(entry.asItem(), otherEntry.asItem()));
                //        });
//
                //        simpleAfterOrderings.forEach((entry, otherEntry) -> {
                //                orderings.add(ItemOrdering.after(entry.asItem(), otherEntry.asItem()));
                //        });
//
                //        return orderings;
                //}

                private static Function<Item, ItemStack> makeStackFunc() {
                        Map<Item, Function<Item, ItemStack>> factories = new Reference2ReferenceOpenHashMap<>();

                        return item -> {
                                Function<Item, ItemStack> factory = factories.get(item);
                                if (factory != null) {
                                        return factory.apply(item);
                                }
                                return new ItemStack(item);
                        };
                }

                private static Function<Item, CreativeModeTab.TabVisibility> makeVisibilityFunc() {
                        Map<Item, CreativeModeTab.TabVisibility> visibilities = new Reference2ObjectOpenHashMap<>();

                      // Map<ItemProviderEntry<?>, CreativeModeTab.TabVisibility> simpleVisibilities = Map.of(
                      //         AllItems.BLAZE_CAKE_BASE, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY
                      // );

                      //  simpleVisibilities.forEach((entry, factory) -> {
                      //          visibilities.put(entry.asItem(), factory);
                      //  });


                        return item -> {
                                CreativeModeTab.TabVisibility visibility = visibilities.get(item);
                                if (visibility != null) {
                                        return visibility;
                                }
                                return CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
                        };
                }

                @Override
                public void accept(CreativeModeTab.ItemDisplayParameters pParameters, CreativeModeTab.Output output) {
                        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                        Predicate<Item> exclusionPredicate = makeExclusionPredicate();
                       // List<ItemOrdering> orderings = makeOrderings();
                        Function<Item, ItemStack> stackFunc = makeStackFunc();
                        Function<Item, CreativeModeTab.TabVisibility> visibilityFunc = makeVisibilityFunc();
                        RegistryObject<CreativeModeTab> tab = mainTab ? MAIN_TAB : BUILDING_BLOCKS_TAB;

                        List<Item> items = new LinkedList<>();
                        items.addAll(collectItems(tab, itemRenderer, true, exclusionPredicate));
                        items.addAll(collectBlocks(tab, exclusionPredicate));
                        items.addAll(collectItems(tab, itemRenderer, false, exclusionPredicate));

                        //applyOrderings(items, orderings);
                        outputAll(output, items, stackFunc, visibilityFunc);
                }

                private List<Item> collectBlocks(RegistryObject<CreativeModeTab> tab, Predicate<Item> exclusionPredicate) {
                        List<Item> items = new ReferenceArrayList<>();
                        for (RegistryEntry<Block> entry : CreateTFMG.REGISTRATE.getAll(Registries.BLOCK)) {
                                if (!CreateTFMG.REGISTRATE.isInCreativeTab(entry, tab))
                                        continue;
                                Item item = entry.get()
                                        .asItem();
                                if (item == Items.AIR)
                                        continue;
                                if (!exclusionPredicate.test(item))
                                        items.add(item);
                        }
                        items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
                        return items;
                }

                private List<Item> collectItems(RegistryObject<CreativeModeTab> tab, ItemRenderer itemRenderer, boolean special,
                                                Predicate<Item> exclusionPredicate) {
                        List<Item> items = new ReferenceArrayList<>();

                        if (!mainTab)
                                return items;

                        for (RegistryEntry<Item> entry : CreateTFMG.REGISTRATE.getAll(Registries.ITEM)) {
                                if (!CreateTFMG.REGISTRATE.isInCreativeTab(entry, tab))
                                        continue;
                                Item item = entry.get();
                                if (item instanceof BlockItem)
                                        continue;
                                BakedModel model = itemRenderer.getModel(new ItemStack(item), null, null, 0);
                                if (model.isGui3d() != special)
                                        continue;
                                if (!exclusionPredicate.test(item))
                                        items.add(item);
                        }
                        return items;
                }

                private static void applyOrderings(List<Item> items, List<ItemOrdering> orderings) {
                        for (ItemOrdering ordering : orderings) {
                                int anchorIndex = items.indexOf(ordering.anchor());
                                if (anchorIndex != -1) {
                                        Item item = ordering.item();
                                        int itemIndex = items.indexOf(item);
                                        if (itemIndex != -1) {
                                                items.remove(itemIndex);
                                                if (itemIndex < anchorIndex) {
                                                        anchorIndex--;
                                                }
                                        }
                                        if (ordering.type() == ItemOrdering.Type.AFTER) {
                                                items.add(anchorIndex + 1, item);
                                        } else {
                                                items.add(anchorIndex, item);
                                        }
                                }
                        }
                }

                private static void outputAll(CreativeModeTab.Output output, List<Item> items, Function<Item, ItemStack> stackFunc, Function<Item, CreativeModeTab.TabVisibility> visibilityFunc) {
                        for (Item item : items) {
                                output.accept(stackFunc.apply(item), visibilityFunc.apply(item));
                        }
                }

                private record ItemOrdering(Item item, Item anchor, ItemOrdering.Type type) {
                        public static ItemOrdering before(Item item, Item anchor) {
                                return new ItemOrdering(item, anchor, ItemOrdering.Type.BEFORE);
                        }

                        public static ItemOrdering after(Item item, Item anchor) {
                                return new ItemOrdering(item, anchor, ItemOrdering.Type.AFTER);
                        }

                        public enum Type {
                                BEFORE,
                                AFTER;
                        }
                }
        }
}
