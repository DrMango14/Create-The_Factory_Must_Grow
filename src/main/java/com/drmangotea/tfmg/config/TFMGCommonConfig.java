package com.drmangotea.tfmg.config;


import com.drmangotea.tfmg.config.common.EquipmentConfig;
import com.drmangotea.tfmg.config.common.MachineConfig;
import com.drmangotea.tfmg.config.common.TFMGWorldGen;
import net.createmod.catnip.config.ConfigBase;

public class TFMGCommonConfig extends ConfigBase {

    public final MachineConfig machines = nested(0, MachineConfig::new, "Config options for TFMG's machinery");
    public final TFMGWorldGen worldGen = nested(1, TFMGWorldGen::new, "Config options for TFMG's World Generation");
    public final EquipmentConfig equipment = nested(0, EquipmentConfig::new, "Config options for TFMG's equipment");

    @Override
    public String getName() {
        return "common";
    }


}
