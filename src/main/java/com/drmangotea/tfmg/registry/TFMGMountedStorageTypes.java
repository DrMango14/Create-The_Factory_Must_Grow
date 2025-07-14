package com.drmangotea.tfmg.registry;

import com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorageType;
import com.simibubi.create.content.fluids.tank.storage.FluidTankMountedStorageType;
import com.tterrag.registrate.util.entry.RegistryEntry;

import java.util.function.Supplier;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGMountedStorageTypes {

    public static final RegistryEntry<MountedFluidStorageType<?>,FluidTankMountedStorageType> TFMG_FLUID_TANK = simpleFluid("tfmg_fluid_tank", FluidTankMountedStorageType::new);


    private static <T extends MountedFluidStorageType<?>> RegistryEntry<MountedFluidStorageType<?>, T> simpleFluid(String name, Supplier<T> supplier) {
        return REGISTRATE.mountedFluidStorage(name, supplier).register();
    }

    public static void register() {
    }
}
