package com.drmangotea.tfmg.base.lang;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.theme.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;

/**
 * Utility class for storing translations and texts that could occur more than once.
 * Easier than copying the lang.translate() method everywhere.
 */
public class TFMGTexts {
    public static String percent(double value) {
        return TFMGLang.number(value) + "%";
    }
    // Electricity info
    public static String power(double value) {
        return TFMGUtils.formatUnits(value, "W");
    }
    public static String voltage(double value) {
        return TFMGUtils.formatUnits(value, "V");
    }
    public static String current(double value) {
        return TFMGUtils.formatUnits(value, "A");
    }
    public static String resistance(double value) {
        return TFMGUtils.formatUnits(value, "Ω");
    }
    public static String forgeEnergy(double value) {
        return TFMGUtils.formatUnits(value, "FE");
    }
    public static LangBuilder electricalCapacity(double value) {
        return TFMGLang.translate("electricity.capacity", forgeEnergy(value)).color(0x127799);
    }
    public static LangBuilder electricalMaxCapacity(double value) {
        return TFMGLang.translate("electricity.max_capacity", TFMGLang.number(value)).color(0x127799);
    }
    public static LangBuilder chargingRate(double value) {
        return TFMGLang.translate("electricity.charging_rate", TFMGLang.number(value)).color(0x127799);
    }
    public static LangBuilder progress(double value) {
        return TFMGLang.translate("goggles.progress", value);
    }
    public static LangBuilder progress(String value) {
        return TFMGLang.translate("goggles.progress", value);
    }
    public static LangBuilder turnsLeft(int turns) {
        return TFMGLang.translate("goggles.winding_machine.turns").add(TFMGLang.text(String.valueOf(turns)));
    }

    public static LangBuilder invalidMachine() {
        return TFMGLang.translate("goggles.invalid_machine").style(ChatFormatting.DARK_RED);
    }

    public static LangBuilder header(String identifier) {
        return TFMGLang.translate(identifier + ".header");
    }

    public static LangBuilder heatStatus(HeatCondition heatCondition) {
        return TFMGLang.translate("goggles.heat_status")
                .add(TFMGLang.translate(heatCondition == HeatCondition.NONE ? "goggles.no_heat" : heatCondition == HeatCondition.HEATED ? "goggles.heated" : "goggles.superheated"))
                .color(heatCondition == HeatCondition.NONE ? 0x7a7a77 : heatCondition == HeatCondition.HEATED ? 0xdea216 : 0x16c7de);
    }

    // Multimeter Tooltips
    public static class Multimeter {
        public static LangBuilder separator() {
            return TFMGLang.translate("multimeter.separator").color(Color.WHITE);
        }
        public static LangBuilder notEnoughPower() {
            return TFMGLang.translate("multimeter.insufficient_power").color(Color.RED);
        }
        public static LangBuilder notEnoughPower(double requirement) {
            return TFMGLang.translate("multimeter.insufficient_power.requirement", TFMGTexts.power(requirement)).style(ChatFormatting.RED);
        }
        public static LangBuilder notEnoughCurrent(int minimum) {
            return TFMGLang.translate("multimeter.insufficient_current", TFMGTexts.current(minimum)).style(ChatFormatting.RED);
        }
        public static LangBuilder charge(double value) {
            return TFMGLang.translate("multimeter.charge").add(TFMGLang.text(percent(value))).style(ChatFormatting.DARK_AQUA);
        }
        public static LangBuilder group(int groupId) {
            return TFMGLang.translate("multimeter.group", TFMGLang.number(groupId)).color(0xd8db27);
        }
        public static LangBuilder transformerRatio(float ratio) {
            return TFMGLang.translate("multimeter.transformer_ratio").add(TFMGLang.number(ratio)).color(0xc6e82c);
        }
        public static LangBuilder powerGenerated(double value) {
            return TFMGLang.translate("multimeter.power_generated", power(value)).color(0x852e4a);
        }
        public static LangBuilder voltageGenerated(double value) {
            return TFMGLang.translate("multimeter.voltage_generated", voltage(value)).color(0x127799);
        }
        public static LangBuilder networkGeneration(double value) {
            return TFMGLang.translate("multimeter.network.generation", power(value)).color(0xcc4b74);
        }
        public static LangBuilder networkConsumption(double value) {
            return TFMGLang.translate("multimeter.network.consumption", power(value)).color(0xcc4b74);
        }
        public static LangBuilder resistance(double value) {
            return TFMGLang.text("   R = " +  TFMGTexts.resistance(value)).color(0xc98969);
        }
        public static LangBuilder voltage(double value) {
            return TFMGLang.text("   U = " + TFMGTexts.voltage(value)).color(0x4bbbcc);
        }
        public static LangBuilder current(double value) {
            return TFMGLang.text("   I = " + TFMGTexts.current(value)).color(0x22a146);
        }
        public static LangBuilder power(double value) {
            return TFMGLang.text("   P = " + TFMGTexts.power(value)).color(0xcc4b74);
        }
    }

    // Voltmeter Tooltips
    public static class Voltmeter {
        public static LangBuilder range(int range) {
            return TFMGLang.translate("goggles.voltmeter.range", range).style(ChatFormatting.DARK_AQUA);
        }
        public static LangBuilder mode(String langKey) {
            return TFMGLang.translate(langKey).style(ChatFormatting.DARK_GRAY);
        }
        public static LangBuilder value(float value, String unit) {
            return TFMGLang.text(TFMGUtils.formatUnits(value, unit)).style(ChatFormatting.AQUA);
        }
    }

    // Engine Tooltips
    public static class Engine {
        public static LangBuilder unfinished() {
            return TFMGLang.translate("goggles.engine.unfinished")
                    .color(0xde5050);
        }
        public static LangBuilder nextComponent(ItemStack item) {
            return TFMGLang.translate("goggles.engine.next_component", item.getHoverName()).color(0xfff240);
        }
        public static LangBuilder lastRequirement(String type) {
            return TFMGLang.translate("goggles.engine."+type+"_missing").color(0xde5050);
        }
        public static LangBuilder shift(String langKey) {
            return TFMGLang.translate("engine.shift", TFMGLang.translate(langKey));
        }
        public static LangBuilder speedEfficiency(float efficiency) {
            return TFMGLang.translate("engine.speed_efficiency", TFMGLang.number(efficiency));
        }
        public static LangBuilder efficiency(float efficiency) {
            return TFMGLang.translate("engine.efficiency", TFMGLang.number(efficiency));
        }
        public static LangBuilder fuelConsumption(float consumption) {
            return TFMGLang.translate("goggles.engine.fuel_consumption", TFMGLang.number(consumption)).color(0xfcad03);
        }
        public static LangBuilder rpm(float rpm) {
            return TFMGLang.translate("goggles.engine.rpm", TFMGLang.number(rpm)).color(0xa36f00);
        }
        public static LangBuilder length(int length) {
            return TFMGLang.translate("engine.length", TFMGLang.number(length));
        }
        public static LangBuilder torque(float torque) {
            return TFMGLang.translate("goggles.engine.torque", TFMGLang.number(torque)).color(0xa36f00);
        }
        public static LangBuilder signal(int signal) {
            return TFMGLang.translate("goggles.engine.signal", TFMGLang.number(signal)).color(0xfcad03);
        }
        public static LangBuilder type(String langKey) {
            return TFMGLang.translate("goggles.engine.type", TFMGLang.translate(langKey)).color(0xfcad03);
        }
        public static LangBuilder oil(int oil) {
            return TFMGLang.translate("goggles.engine.oil", TFMGLang.number(oil)).color(0xf5dd42);
        }
        public static LangBuilder coolingFluid(int fluid) {
            return TFMGLang.translate("goggles.engine.cooling_fluid", TFMGLang.number(fluid)).color(0x51bdb9);
        }
    }

    // Blast Furnace Tooltips
    public static class BlastFurnace {
        public static LangBuilder stats(int count) {
            return TFMGLang.translate("goggles.blast_furnace.stats", count).style(ChatFormatting.GRAY);
        }
        public static LangBuilder height(int height) {
            return TFMGLang.translate("goggles.blast_furnace.height", TFMGLang.number(height));
        }
        public static LangBuilder fuelAmount(int amount) {
            return TFMGLang.translate("goggles.blast_furnace.fuel_amount", TFMGLang.number(amount));
        }
        public static LangBuilder timer(int timer) {
            return TFMGLang.translate("goggles.blast_furnace.timer", timer).style(ChatFormatting.GOLD);
        }
        public static LangBuilder reinforced() {
            return TFMGLang.translate("goggles.blast_furnace.reinforced").style(ChatFormatting.GREEN);
        }
    }

    // Distillation Tower Tooltips
    public static class Distillation {
        public static LangBuilder level(int activeHeat) {
            ChatFormatting color = activeHeat > 0 ? ChatFormatting.GOLD : ChatFormatting.RED;
            return TFMGLang.translate("goggles.distillation_tower.level", activeHeat).style(color);
        }
        public static LangBuilder outputs(int outputs) {
            ChatFormatting color = outputs > 0 ? ChatFormatting.GOLD : ChatFormatting.RED;
            return TFMGLang.translate("goggles.distillation_tower.found_outputs", TFMGLang.number(outputs)).style(color);
        }
        public static LangBuilder tankNotFound() {
            return TFMGLang.translate("goggles.distillation_tower.tank_not_found").style(ChatFormatting.RED);
        }
    }

    // Surface Scanner Tooltips
    public static class SurfaceScanner {
        public static LangBuilder deposits(int deposits) {
            return TFMGLang.translate("goggles.surface_scanner.deposits_found", TFMGLang.number(deposits)).style(ChatFormatting.GREEN);
        }
        public static LangBuilder noDeposit() {
            return TFMGLang.translate("goggles.surface_scanner.no_deposit").style(ChatFormatting.RED);
        }
        public static LangBuilder noRotation() {
            return TFMGLang.translate("goggles.surface_scanner.no_rotation").style(ChatFormatting.DARK_RED);
        }
    }

    // Vat Tooltips
    public static class Vat {
        public static LangBuilder operation(String operationId) {
            return TFMGLang.translate("goggles.vat." + operationId.replace(":","."));
        }
        public static LangBuilder notOperational() {
            return TFMGLang.text(" - ").add(TFMGLang.translate("goggles.vat.not_operational").style(ChatFormatting.RED));
        }
        public static LangBuilder contents() {
            return TFMGLang.translate("goggles.vat.contents");
        }
        public static LangBuilder attachments() {
            return TFMGLang.translate("goggles.vat.attachments");
        }
    }
}
