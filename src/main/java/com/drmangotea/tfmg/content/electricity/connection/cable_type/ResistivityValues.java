package com.drmangotea.tfmg.content.electricity.connection.cable_type;

import com.simibubi.create.api.registry.SimpleRegistry;

import java.util.function.DoubleSupplier;

public class ResistivityValues {
    public static final SimpleRegistry<CableType, DoubleSupplier> RESISTIVITIES = SimpleRegistry.create();

    public static double getResistivity(CableType conductor) {
        DoubleSupplier supplier = RESISTIVITIES.get(conductor);
        return supplier == null ? 0 : supplier.getAsDouble();
    }
}
