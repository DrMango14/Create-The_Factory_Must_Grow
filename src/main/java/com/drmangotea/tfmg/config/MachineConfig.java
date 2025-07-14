package com.drmangotea.tfmg.config;


import net.createmod.catnip.config.ConfigBase;

public class MachineConfig extends ConfigBase {


    public final ConfigInt electricMotorMinimumPower = i(250, 1, "electricMotorMinimumPower", Comments.electricMotorMinimumPower);
    public final ConfigInt electricMotorMinimumVoltage = i(150, 1, "electricMotorMinimumVoltage", Comments.electricMotorMinimumVoltage);
    public final ConfigFloat electricMotorInternalResistance = f(100, 0, "electricMotorInternalResistance", Comments.electricMotorInternalResistance);
    public final ConfigInt cokeOvenMaxSize = i(5, 1, "cokeOvenMaxSize", Comments.cokeOvenMaxSize);
    public final ConfigFloat FEtoWattTickConversionRate = f(1, 0, "FEtoWattTickConversionRate", Comments.FEtoWattTickConversionRate);

    public final ConfigInt graphiteElectrodeCurrent = i(10, 1, "graphiteElectrodeCurrent", Comments.graphiteElectrodeCurrent);
    public final ConfigInt electrolysisMinimumCurrent = i(5, 1, "electrolysisMinimumCurrent", Comments.electrolysisMinimumCurrent);
    public final ConfigInt engineMaxLength = i(5, 1, "engineMaxLength", Comments.engineMaxLength);
    public final ConfigInt surfaceScannerScanDepth = i(-64, -512, "surfaceScannerScanDepth", Comments.surfaceScannerScanDepth);
    public final ConfigInt polarizerItemChargingRate = i(1000, 1, "polarizerItemChargingRate", Comments.polarizerItemChargingRate);


    public final ConfigGroup accumulator = group(1, "accumulator", "Accumulator");
    public final ConfigInt accumulatorStorage = i(100000, 1, "accumulatorStorage", Comments.accumulatorStorage);
    public final ConfigInt accumulatorVoltage = i(12, 1, "accumulatorVoltage", Comments.accumulatorVoltage);
    public final ConfigInt accumulatorMaxAmpOutput = i(20, 1, "accumulatorMaxAmpOutput", Comments.accumulatorMaxAmpOutput);
    public final ConfigInt accumulatorChargingRate = i(100, 1, "accumulatorChargingRate", Comments.accumulatorChargingRate);

    public final ConfigGroup firebox = group(1, "firebox", "Firebox");
    public final ConfigBool fireboxExhaustRequirement = b(true, "fireboxExhaustRequirement", Comments.fireboxExhaustRequirement);
    public final ConfigInt fireboxFuelConsumption = i(100, 1, "fireboxFuelConsumption", Comments.fireboxFuelConsumption);

    public final ConfigGroup engines = group(1, "engines", "Engines");
    public final ConfigFloat engineLoudness = f(1,0, "engineLoudness", Comments.engineLoudness);


    public final ConfigGroup generators = group(1, "generators", "Generators");
    public final ConfigFloat largeGeneratorModifier = f(4, 0, "largeGeneratorModifier", Comments.largeGenerator);
    public final ConfigFloat largeGeneratorMinSpeed = f(70, 0, "largeGeneratorMinSpeed", Comments.largeGeneratorMinSpeed);
    //
    public final ConfigFloat generatorModifier = f(1.4f, 0, "GeneratorModifier", Comments.generator);
    public final ConfigFloat generatorMinSpeed = f(40, 0, "generatorMinSpeed", Comments.generatorMinSpeed);

    public final ConfigGroup blast_furnace = group(1, "blast_furnace", "Blast Furnace");
    public final ConfigInt blastFurnaceMaxHeight = i(10, 3, "blastFurnaceMaxHeight", Comments.blastFurnaceHeight);
    public final ConfigFloat blastFurnaceHeightSpeedModifier = f(1f, 0.1f, "blastFurnaceHeightSpeedModifier", Comments.blastFurnaceHeightSpeedModifier);
    public final ConfigInt blastFurnaceFuelConsumption = i(600, 1, "blastFurnaceFuelConsumption", Comments.blastFurnaceFuelConsumption);

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
        static String electricMotorMinimumPower = "Determines the minimum power an electric motor can run on.";
        static String electricMotorMinimumVoltage = "Determines the minimum voltage an electric motor can run on.";
        static String electricMotorInternalResistance = "Sets the internal resistance of the electric motor.";
        static String cokeOvenMaxSize = "Determines the maximum size of coke ovens.";
        static String accumulatorStorage = "Determines the storage space of accumulators.";
        static String accumulatorVoltage = "Determines the voltage accumulators output.";
        static String accumulatorMaxAmpOutput = "Sets the maximum amperage an accumulator can provide.";
        static String accumulatorChargingRate = "Sets the maximum charging rate of accumulators.";
        static String fireboxExhaustRequirement = "If set to true,fireboxes will require exhaust management.";
        static String fireboxFuelConsumption = "Determines the amount of fuel a firebox needs to run for 3 seconds.";
        static String graphiteElectrodeCurrent = "The minimum electric current that will make graphite electrodes superheated.";
        static String electrolysisMinimumCurrent = "The minimum electric current that will make electrolyzers operational.";
        static String engineMaxLength = "The maximum length of engines.";
        static String surfaceScannerScanDepth = "Y level surface scanner scan at.";
        static String FEtoWattTickConversionRate = "How much Forge Energy is in one watt-tick.";
        static String polarizerItemChargingRate = "How much FE can polarizer charge per tick.";
        static String engineLoudness = "Changes the volume of engines.";
    }
}
