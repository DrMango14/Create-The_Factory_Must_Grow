package com.drmangotea.createindustry.items.weapons.flamethrover;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.registry.TFMGFluids;

public class BuiltinFlamethrowerFuelTypes {
    
    public static final FlamethrowerFuelType
    
    FALLBACK = create("fallback")
            .spread(0)
            .speed(0)
            .amount(0)
            .color(0x000000)
            .register(),
    
    GASOLINE = create("gasoline")
        .spread(15)
        .speed(1)
        .amount(3)
        .color(0xC4AA76)
        .registerAndAssign(TFMGFluids.GASOLINE::get),
        
    DIESEL = create("diesel")
        .spread(7)
        .speed(2)
        .amount(3)
        .color(0xBA9177)
        .registerAndAssign(TFMGFluids.DIESEL::get),
    
    KEROSENE = create("kerosene")
        .spread(10)
        .speed(1.3f)
        .amount(4)
        .color(0x7876D5)
        .registerAndAssign(TFMGFluids.KEROSENE::get),
    
    NAPHTHA = create("naphtha")
        .spread(20)
        .speed(0.8f)
        .amount(1)
        .color(0x5E1B0A)
        .registerAndAssign(TFMGFluids.NAPHTHA::get),
    
    LPG = create("lpg")
        .spread(35)
        .speed(0.6f)
        .amount(15)
        .color(0xE0BB48)
        .registerAndAssign(TFMGFluids.LPG::get),
    
    NAPALM = create("napalm")
        .spread(20)
        .speed(1.8f)
        .amount(15)
        .color(0xA3C649)
        .registerAndAssign(TFMGFluids.NAPALM::get),
    
    MOLTEN_SLAG = create("molten_slag")
        .spread(15)
        .speed(0.3f)
        .amount(15)
        .color(0xFF9621)
        .registerAndAssign(TFMGFluids.MOLTEN_SLAG::get),
    
    COOLING_FLUID = create("cooling_fluid")
        .spread(12)
        .speed(0.8f)
        .amount(15)
        .cold()
        .color(0x4edbdb)
        .registerAndAssign(TFMGFluids.COOLING_FLUID::get)
        
    ;
    
    
    
    
    private static FlamethrowerFuelType.Builder create(String name) {
        return new FlamethrowerFuelType.Builder(CreateTFMG.asResource(name));
    }
    
    public static void register() {}
}
