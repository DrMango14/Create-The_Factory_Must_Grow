package com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.base;

import com.drmangotea.tfmg.TFMG;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class TestSavedData extends SavedData {

    private List<FluidReservoir> list = new ArrayList<>();


    @Override
    public CompoundTag save(CompoundTag compound, HolderLookup.Provider provider) {
        TestSavedDataManager manager = TFMG.DEPOSITS;
        compound.putInt("reservoirCount", manager.list.size());
        for (int i = 0; i < manager.list.size(); i++) {
            FluidReservoir reservoir = manager.list.get(i);
            CompoundTag reservoirNBT = new CompoundTag();
            reservoirNBT.putLong("Id", reservoir.id);
            reservoirNBT.putInt("Reserves", reservoir.oilReserves);
            //reservoirNBT.putLongArray("Deposits", reservoir.deposits);
            reservoirNBT.putInt("DepositCount", reservoir.deposits.size());
            for (int j = 0; j < reservoir.deposits.size(); j++) {
                reservoirNBT.putLong("Deposit"+j,reservoir.deposits.get(j));
            }

            //
            compound.put("FluidReservoir" + i, reservoirNBT);

        }
        return compound;
    }

    public List<FluidReservoir> getLogisticsNetworks() {
        return list;
    }


    public static TestSavedData load(CompoundTag compound, HolderLookup.Provider registries) {
        TestSavedData sd = new TestSavedData();
        sd.list = new ArrayList<>();
        for (int i = 0; i < compound.getInt("reservoirCount"); i++) {
            CompoundTag reservoirNBT = compound.getCompound("FluidReservoir" + i);

            FluidReservoir reservoir = new FluidReservoir(reservoirNBT.getLong("Id"));
           // long[] depositArray = compound.getLongArray("Deposits");


            //reservoir.deposits = Arrays.stream(depositArray).boxed().toList();
            for (int j = 0; j < compound.getInt("DepositCount"); j++) {
                reservoir.deposits.add(reservoirNBT.getLong("Deposit"+j));
            }

            reservoir.oilReserves = reservoirNBT.getInt("Reserves");
            sd.list.add(reservoir);
        }

        return sd;
    }
    public static SavedData.Factory<TestSavedData> factory() {
        return new SavedData.Factory<>(TestSavedData::new, TestSavedData::load);
    }

    public static TestSavedData load(MinecraftServer server) {
        return server.overworld()
                .getDataStorage()
                .computeIfAbsent(factory(), "tfmg_deposits");
    }


}
