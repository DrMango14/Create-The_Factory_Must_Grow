package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.content.decoration.kinetics.encased.TFMGEncasedCogwheelBlock;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

import static com.drmangotea.tfmg.TFMG.MOD_ID;
import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TFMG_MAIN = REGISTER.register("tfmg_main", () -> CreativeModeTab.builder()
            .withTabsBefore(AllCreativeModeTabs.BASE_CREATIVE_TAB.getId())
            .title(Component.translatable("creative_tab.tfmg_main"))
            .icon(() -> TFMGItems.STEEL_MECHANISM.get().asItem().getDefaultInstance())
            // .displayItems(new RegistrateDisplayItemsGenerator(true, TFMGCreativeTabs.TFMG_MAIN))
            .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TFMG_DECORATION = REGISTER.register("tfmg_decoration", () -> CreativeModeTab.builder()
            .withTabsBefore(TFMG_MAIN.getId())
            .title(Component.translatable("creative_tab.tfmg_decoration"))
            .icon(() -> TFMGBlocks.CONCRETE.block.get().asItem().getDefaultInstance())
            // .displayItems(new RegistrateDisplayItemsGenerator(true, TFMGCreativeTabs.TFMG_DECORATION))
            .build());

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == TFMGCreativeTabs.TFMG_MAIN.get()) {
            for (RegistryEntry<Item, ?> item : REGISTRATE.getAll(Registries.ITEM)) {

                if (!CreateRegistrate.isInCreativeTab(item, TFMG_MAIN))
                    continue;
                if (blacklist().contains(item))
                    continue;
                if (item.get() instanceof SequencedAssemblyItem)
                    continue;
                event.accept(item.get(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            }
        }

        if (event.getTab() == TFMG_DECORATION.get()) {
            for (RegistryEntry<Item, Item> item : REGISTRATE.getAll(Registries.ITEM)) {
                if (!CreateRegistrate.isInCreativeTab(item, TFMG_DECORATION))
                    continue;
                if (blacklist().contains(item))
                    continue;
                if (item.get() instanceof BlockItem blockItem && blockItem.getBlock() instanceof TFMGEncasedCogwheelBlock)
                    continue;
                if (item.get() instanceof SequencedAssemblyItem)
                    continue;

                event.accept(item.get(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            }
        }
    }


    public static List<RegistryEntry<Item, ? extends Item>> blacklist() {
        List<RegistryEntry<Item, ? extends Item>> list = new ArrayList<>();
		
		list.add(TFMGItems.GOLDEN_TURBO);
		//add these to the blacklist so there aren't duplicate entries

        return list;
    }

    @ApiStatus.Internal
    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
