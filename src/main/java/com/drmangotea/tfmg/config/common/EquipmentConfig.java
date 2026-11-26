package com.drmangotea.tfmg.config.common;

import net.createmod.catnip.config.ConfigBase;

public class EquipmentConfig extends ConfigBase {

    public final ConfigInt fireExtinguisherClearRadius = i(1, 0, "fireExtinguisherClearRadius", Comments.fireExtinguisherClearRadius);

    @Override
    public String getName() {
        return "equipment";
    }

    private static class Comments {
        static String fireExtinguisherClearRadius = "Changes the radius fire extinguishers can remove fire in.";
    }
}
