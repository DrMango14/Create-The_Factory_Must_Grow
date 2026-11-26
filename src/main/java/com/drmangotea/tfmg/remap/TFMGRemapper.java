package com.drmangotea.tfmg.remap;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;


@EventBusSubscriber
public class TFMGRemapper {
    static {
        Remap.block("copper_encased_brass_pipe", TFMG.asResource("encased_brass_pipe"));
        Remap.block("copper_encased_steel_pipe", TFMG.asResource("encased_steel_pipe"));
        Remap.block("copper_encased_aluminum_pipe", TFMG.asResource("encased_aluminum_pipe"));
        Remap.block("copper_encased_cast_iron_pipe", TFMG.asResource("encased_cast_iron_pipe"));
        Remap.block("copper_encased_plastic_pipe", TFMG.asResource("encased_plastic_pipe"));

        Remap.block("heavy_casing_encased_shaft", TFMG.asResource("heavy_encased_shaft"));
        Remap.block("heavy_casing_encased_steel_cogwheel", TFMG.asResource("heavy_encased_steel_cogwheel"));
        Remap.block("heavy_casing_encased_large_steel_cogwheel", TFMG.asResource("heavy_encased_large_steel_cogwheel"));
        Remap.block("heavy_casing_encased_aluminum_cogwheel", TFMG.asResource("heavy_encased_aluminum_cogwheel"));
        Remap.block("heavy_casing_encased_large_aluminum_cogwheel", TFMG.asResource("heavy_encased_large_aluminum_cogwheel"));

        Remap.item("lit_lithium_blade", TFMG.asResource("lithium_blade"));
        Remap.item("capacitor_item", TFMG.asResource("capacitor"));
        Remap.item("transistor_item", TFMG.asResource("transistor"));

        for (DyeColor color : DyeColor.values()) {
            Remap.item(color.getName() + "_liquid_concrete_bucket", TFMG.asResource("liquid_" + color.getName() + "_concrete_bucket"));
            Remap.fluid(color.getName() + "_liquid_concrete", TFMG.asResource("liquid_" + color.getName() + "_concrete"));
            Remap.fluid("flowing_" + color.getName() + "_liquid_concrete", TFMG.asResource("flowing_liquid_" + color.getName() + "_concrete"));
            Remap.block(color.getName() + "_liquid_concrete", TFMG.asResource("liquid_" + color.getName() + "_concrete"));
        }

        new Remap("freezing", TFMG.asResource("freezer")).forTypes(TFMGRegistries.VAT_OPERATION);
        new Remap("mixing", TFMG.asResource("mixer")).forTypes(TFMGRegistries.VAT_OPERATION);

        Remap.recipe("vat_machine_recipe", TFMG.asResource("vat"));
    }

    @SubscribeEvent
    public static void remap(RegisterEvent event) {
        Remap.remap(event.getRegistry());
    }

    public static void remapComponents(ItemStack stack, RegistryAccess registryAccess) {
        if (ComponentRemapper.engineCylinder(stack, registryAccess)) {
            TFMG.LOGGER.debug("[TFMG Remapper] Remapped old Engine Cylinder components");
        }

        if (ComponentRemapper.flamethrower(stack, registryAccess)) {
            TFMG.LOGGER.debug("[TFMG Remapper] Remapped old Flamethrower components");
        }
    }
}
