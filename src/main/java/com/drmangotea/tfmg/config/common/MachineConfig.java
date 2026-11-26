package com.drmangotea.tfmg.config.common;


import net.createmod.catnip.config.ConfigBase;

public class MachineConfig extends ConfigBase {

    public final ConfigFloat electricMotorInternalResistance = f(30, 0, "electricMotorInternalResistance", Comments.electricMotorInternalResistance);
    //public final ConfigFloat FEtoWattTickConversionRate = f(1, 0, "FEtoWattTickConversionRate", Comments.FEtoWattTickConversionRate);
    public final ConfigInt forgeEnergyConversionVoltage = i(230,1,"forgeEnergyConversionVoltage", Comments.forgeEnergyConversionVoltage);

    public final ConfigInt engineMaxLength = i(5, 1, "engineMaxLength", Comments.engineMaxLength);

    public final ConfigGroup accumulator = group(1, "accumulator", "Accumulator");
    public final ConfigInt accumulatorStorage = i(2500000, 1, "accumulatorStorage", Comments.accumulatorStorage);


    public final ConfigGroup firebox = group(1, "firebox", "Firebox");
    public final ConfigBool fireboxExhaustRequirement = b(true, "fireboxExhaustRequirement", Comments.fireboxExhaustRequirement);
    public final ConfigInt fireboxFuelConsumption = i(100, 1, "fireboxFuelConsumption", Comments.fireboxFuelConsumption);

    public final ConfigGroup engines = group(1, "engines", "Engines");
    public final ConfigFloat engineLoudness = f(1,0, "engineLoudness", Comments.engineLoudness);
    public final ConfigFloat engineFuelConsumption = f(100,0, "engineFuelConsumption", Comments.engineFuelConsumption);
    public final ConfigFloat enginePower = f(100,0, "enginePower", Comments.enginePower);
    public final ConfigFloat engineElectricPower = f(100,0, "engineElectricPower", Comments.engineElectricPower);


    public final ConfigGroup generators = group(1, "generators", "Generators");
    public final ConfigFloat largeGeneratorModifier = f(4, 0, "largeGeneratorModifier", Comments.largeGenerator);
    public final ConfigFloat largeGeneratorMinSpeed = f(70, 0, "largeGeneratorMinSpeed", Comments.largeGeneratorMinSpeed);
    public final ConfigFloat generatorModifier = f(1.4f, 0, "GeneratorModifier", Comments.generator);
    public final ConfigFloat generatorMinSpeed = f(40, 0, "generatorMinSpeed", Comments.generatorMinSpeed);

    public final ConfigGroup blast_furnace = group(1, "blast_furnace", "Blast Furnace");
    public final ConfigInt blastFurnaceMaxHeight = i(10, 3, "blastFurnaceMaxHeight", Comments.blastFurnaceHeight);
    public final ConfigFloat blastFurnaceHeightSpeedModifier = f(1f, 0.1f, "blastFurnaceHeightSpeedModifier", Comments.blastFurnaceHeightSpeedModifier);
    public final ConfigInt blastFurnaceFuelConsumption = i(600, 1, "blastFurnaceFuelConsumption", Comments.blastFurnaceFuelConsumption);

    public final ConfigGroup chemicalVat = group(1, "chemical_vat", "Chemical Vat");
    public final ConfigInt electrolysisMinimumCurrent = i(5, 1, "electrolysisMinimumCurrent", Comments.electrolysisMinimumCurrent);
    public final ConfigInt freezerMinimumCurrent = i(3, 1, "freezerMinimumCurrent", Comments.freezerMinimumCurrent);
    public final ConfigInt compressorMinimumRPM = i(120, 1, "compressorMinimumRPM", Comments.compressorMinimumRPM);
    public final ConfigInt industrialMixerMinimumRPM = i(30, 1, "industrialMixerMinimumRPM", Comments.industrialMixerMinimumRPM);

    public final ConfigGroup surfaceScanner = group(1, "surface_scanner", "Surface Scanner");
    public final ConfigInt surfaceScannerMinimumRPM = i(64, 1, "surfaceScannerMinimumRPM", Comments.surfaceScannerMinimumRPM);

    public final ConfigGroup cokeOven = group(1, "coke_oven", "Coke Oven");
    public final ConfigInt cokeOvenMaxSize = i(5, 1, "cokeOvenMaxSize", Comments.cokeOvenMaxSize);

    public final ConfigGroup distillationTower = group(1, "distillation_tower", "Distillation Tower");
    public final ConfigInt distillationRecipeGapTicks = i(1200, 20, "distillationRecipeGapTicks", Comments.distillationRecipeGapTicks);


    @Override
    public String getName() {
        return "machines";
    }


    private static class Comments {
        static String largeGenerator = "Determines how powerful the large generator is.";
        static String generator = "Determines how powerful the generator is.";
        static String largeGeneratorMinSpeed = "Changes the lowest speed the large generator can work on.";
        static String generatorMinSpeed = "Changes the lowest speed the generator can work on.";
        static String blastFurnaceHeight = "Changes the maximum height of the blast furnace.";
        static String blastFurnaceHeightSpeedModifier = "Sets the maximum time that can be saved by increasing blast furnace height.";
        static String blastFurnaceFuelConsumption = "Determines how many ticks does it take to consume one fuel.";
        static String electricMotorInternalResistance = "Sets the internal resistance of the electric motor.";
        static String cokeOvenMaxSize = "Determines the maximum size of coke ovens.";
        static String accumulatorStorage = "Determines the storage space of accumulators.";
        static String fireboxExhaustRequirement = "If set to true, fireboxes will require exhaust management.";
        static String fireboxFuelConsumption = "Determines the amount of fuel a firebox needs to run for 3 seconds.";
        static String engineMaxLength = "The maximum length of engines.";
        static String engineFuelConsumption = "Modifier of engine fuel consumption in %.";
        static String enginePower = "Modifier of engine stress capacity in %.";
        static String engineElectricPower = "Modifier of engine power generation in %.";
        static String FEtoWattTickConversionRate = "How much Forge Energy is in one watt-tick.";
        static String engineLoudness = "Changes the volume of engines.";
        static String forgeEnergyConversionVoltage = "What voltage is created when FE is converted.";

        static String electrolysisMinimumCurrent = "Minimum current required for an Electrode Holder to operate.";
        static String compressorMinimumRPM = "The minimum RPM required for a Compressor to operate.";
        static String freezerMinimumCurrent = "The minimum current required for a Freezer to operate.";
        static String industrialMixerMinimumRPM = "The minimum RPM required for an Industrial Mixer to operate.";

        static String surfaceScannerMinimumRPM = "The minimum RPM required for a Surface Scanner to operate.";

        static String distillationRecipeGapTicks = "The amount of ticks to decrement before the Distillation Tower can process a recipe, affected by heat level.";
    }
}
