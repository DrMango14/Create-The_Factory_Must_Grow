package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.decoration.encased.TFMGEncasedCogwheelBlock;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.SpoolItem;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGEncasedBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.*;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.simibubi.create.content.logistics.box.PackageStyles;
import com.simibubi.create.content.logistics.packagePort.postbox.PostboxBlock;
import com.simibubi.create.content.logistics.tableCloth.TableClothBlock;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.TagDependentIngredientItem;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import it.unimi.dsi.fastutil.objects.*;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.ApiStatus;


import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.drmangotea.tfmg.TFMG.MOD_ID;
import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TFMG_MAIN = REGISTER.register("tfmg_main", () -> CreativeModeTab.builder()
            .withTabsBefore(AllCreativeModeTabs.BASE_CREATIVE_TAB.getId())
            .title(Component.translatable("creative_tab.tfmg_main"))
            .icon(()-> TFMGItems.STEEL_MECHANISM.get().asItem().getDefaultInstance())
           // .displayItems(new RegistrateDisplayItemsGenerator(true, TFMGCreativeTabs.TFMG_MAIN))
            .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TFMG_DECORATION = REGISTER.register("tfmg_decoration", () -> CreativeModeTab.builder()
            .withTabsBefore(TFMG_MAIN.getId())
            .title(Component.translatable("creative_tab.tfmg_decoration"))
            .icon(()-> TFMGBlocks.CONCRETE.block.get().asItem().getDefaultInstance())
           // .displayItems(new RegistrateDisplayItemsGenerator(true, TFMGCreativeTabs.TFMG_DECORATION))
            .build());
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {


        if(event.getTab() == TFMGCreativeTabs.TFMG_MAIN.get()){
            event.acceptAll(customAdditions());
            for(RegistryEntry<Item, ?> item : REGISTRATE.getAll(Registries.ITEM)){

                if(!CreateRegistrate.isInCreativeTab(item,TFMG_MAIN))
                    continue;
                if(blacklist().contains(item))
                    continue;
                if(item.get() instanceof SequencedAssemblyItem)
                    continue;
                if(item.get() instanceof SpoolItem&&!item.is(TFMGItems.EMPTY_SPOOL.get())){
                    continue;
                }
                event.accept(item.get(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            }

        }

        if (event.getTab() == TFMG_DECORATION.get()){
            for(RegistryEntry<Item, Item> item : REGISTRATE.getAll(Registries.ITEM)){
                if(!CreateRegistrate.isInCreativeTab(item, TFMG_DECORATION))
                    continue;
                if(blacklist().contains(item))
                    continue;
                if(item.get() instanceof BlockItem blockItem && blockItem.getBlock() instanceof TFMGEncasedCogwheelBlock)
                    continue;
                if(item.get() instanceof SequencedAssemblyItem)
                    continue;

                event.accept(item.get(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);

            }

        }
    }


    public static List<RegistryEntry<Item, ? extends Item>> blacklist(){
        List<RegistryEntry<Item, ? extends Item>> list = new ArrayList<>();

        list.add(TFMGItems.LIT_LITHIUM_BLADE);
        list.add(TFMGItems.GOLDEN_TURBO);
        list.add(TFMGItems.ALUMINUM_SPOOL);
        list.add(TFMGItems.COPPER_SPOOL);
        list.add(TFMGItems.CONSTANTAN_SPOOL);

        return list;
    }
    public static List<ItemStack> customAdditions(){
        List<ItemStack> list = new ArrayList<>();

        ItemStack copperSpool = TFMGItems.COPPER_SPOOL.asStack();
        copperSpool.set(TFMGDataComponents.SPOOL_AMOUNT,1000);
        list.add(copperSpool);

        ItemStack aluminumSpool = TFMGItems.ALUMINUM_SPOOL.asStack();
        aluminumSpool.set(TFMGDataComponents.SPOOL_AMOUNT,1000);
        list.add(aluminumSpool);

        ItemStack constantanSpool = TFMGItems.CONSTANTAN_SPOOL.asStack();
        constantanSpool.set(TFMGDataComponents.SPOOL_AMOUNT,1000);
        list.add(constantanSpool);

        CompoundTag gasolineTag = new CompoundTag();
        gasolineTag.putString("gasoline", "c:gasoline");
        gasolineTag.putString("kerosene", "c:kerosene");
        gasolineTag.putString("naphtha", "c:naphtha");
        CompoundTag gasolineTagName = new CompoundTag();
        gasolineTagName.putString("gasoline", "fluid.tfmg.gasoline");
        gasolineTagName.putString("kerosene", "fluid.tfmg.kerosene");
        gasolineTagName.putString("naphtha", "fluid.tfmg.naphtha");
        //
        CompoundTag creosoteTag = new CompoundTag();
        creosoteTag.putString("creosote", "c:creosote");
        creosoteTag.putString("furnace_gas", "c:furnace_gas");
        CompoundTag creosoteTagName = new CompoundTag();
        creosoteTagName.putString("creosote", "fluid.tfmg.creosote");
        creosoteTagName.putString("furnace_gas", "fluid.tfmg.furnace_gas");
        //
        CompoundTag dieselTag = new CompoundTag();
        dieselTag.putString("diesel", "c:diesel");
        CompoundTag dieselTagName = new CompoundTag();
        dieselTagName.putString("diesel", "fluid.tfmg.diesel");
        //
        CompoundTag lpgTag = new CompoundTag();
        lpgTag.putString("lpg", "c:lpg");
        CompoundTag lpgTagName = new CompoundTag();
        lpgTagName.putString("lpg", "fluid.tfmg.lpg");
        //
        CompoundTag keroseneTag = new CompoundTag();
        keroseneTag.putString("kerosene", "c:kerosene");
        CompoundTag keroseneTagName = new CompoundTag();
        keroseneTagName.putString("kerosene", "fluid.tfmg.kerosene");
        //


        ItemStack gasoline = TFMGItems.ENGINE_CYLINDER.asStack();
        gasoline.set(TFMGDataComponents.FUELS, gasolineTagName);
        gasoline.set(TFMGDataComponents.FUEL_TAGS, gasolineTag);
        list.add(gasoline);
        ItemStack diesel = TFMGItems.DIESEL_ENGINE_CYLINDER.asStack();
        diesel.set(TFMGDataComponents.FUELS,  dieselTagName);
        diesel.set(TFMGDataComponents.FUEL_TAGS, dieselTag);
        list.add(diesel);
        ItemStack lpg = TFMGItems.ENGINE_CYLINDER.asStack();
        lpg.set(TFMGDataComponents.FUELS, lpgTagName);
        lpg.set(TFMGDataComponents.FUEL_TAGS, lpgTag);
        list.add(lpg);
        ItemStack creosote = TFMGItems.SIMPLE_ENGINE_CYLINDER.asStack();
        creosote.set(TFMGDataComponents.FUELS, creosoteTagName);
        creosote.set(TFMGDataComponents.FUEL_TAGS, creosoteTag);
        list.add(creosote);


        ItemStack kerosene = TFMGItems.TURBINE_BLADE.asStack();
        kerosene.set(TFMGDataComponents.FUELS, keroseneTagName);
        kerosene.set(TFMGDataComponents.FUEL_TAGS, keroseneTag);
        list.add(kerosene);

        return list;
    }

    @ApiStatus.Internal
    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }


}


