package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.config.TFMGResistivity;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableTypeEntry;
import com.simibubi.create.api.contraption.ContraptionType;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.contraptions.Contraption;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;
import static com.simibubi.create.AllContraptionTypes.BY_LEGACY_NAME;

public class TFMGCableTypes {

    public static final CableTypeEntry<CableType> empty = REGISTRATE.cableType("empty", CableType::new)
            .properties((p) -> p.spool(TFMGItems.COPPER_SPOOL))
            .register();

    public static final CableTypeEntry<CableType> copper = REGISTRATE.cableType("copper", CableType::new)
            .properties((p) -> p.color(0xD8735A).spool(TFMGItems.COPPER_SPOOL))
            .transform(TFMGResistivity.setResistivity(0.00188f))
            .register();

    public static final CableTypeEntry<CableType> aluminum = REGISTRATE.cableType("aluminum", CableType::new)
            .properties((p) -> p.color(0xEDEFEF).spool(TFMGItems.ALUMINUM_SPOOL))
            .transform(TFMGResistivity.setResistivity(0.0027f))
            .register();

    public static final CableTypeEntry<CableType> constantan = REGISTRATE.cableType("constantan", CableType::new)
            .properties((p) -> p.color(0xCFC2A8).spool(TFMGItems.CONSTANTAN_SPOOL))
            .transform(TFMGResistivity.setResistivity(1f))
            .register();

    // Why is this a thing? I'll leave it her in case you do need it. - Krystal
    //public static final CableTypeEntry<CableType> steelReinforcedAluminum = REGISTRATE.cableType("steel_reinforced_aluminum", CableType::new)
    //        .properties((p) -> p.color(0xB8A08D).spool(TFMGItems.COPPER_SPOOL))
    //        .transform(TFMGResistivity.setResistivity(0.0027f))
    //        .register();

    public static void init() {

    }
}
