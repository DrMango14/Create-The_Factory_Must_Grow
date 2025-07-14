package com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;

public class TestSavedDataManager {

    public List<FluidReservoir> list;

    private TestSavedData savedData;

    public TestSavedDataManager() {
        list = new ArrayList<>();
    }

    public FluidReservoir getReservoirFor(long pos) {

        for (FluidReservoir reservoir : TFMG.DEPOSITS.list) {
            if (reservoir.deposits.contains(pos))
                return reservoir;
        }
        return null;
    }
    public void removeDeposit(long pos) {
        for (FluidReservoir reservoir : TFMG.DEPOSITS.list) {
            if (reservoir.deposits.contains(pos)) {
                reservoir.deposits.remove(pos);
                if (reservoir.deposits.isEmpty())
                    TFMG.DEPOSITS.list.remove(reservoir);
                return;
            }
        }
    }

    public void removeEmptyDeposits() {
        TFMG.DEPOSITS.list.removeIf(reservoir -> reservoir.oilReserves == 0);
    }
    public boolean isReservoirNearby(BlockPos pos) {

        for (int x = -32; x < 32; x++) {
            for (int z = -32; z < 32; z++) {
                BlockPos checkedPos = pos.offset(x, 0, z);
                for (int i = 0; i < TFMG.DEPOSITS.list.size(); i++) {
                    FluidReservoir reservoir = TFMG.DEPOSITS.list.get(i);
                    if (reservoir.id == checkedPos.asLong()) {
                        TFMG.DEPOSITS.list.get(i).deposits.add(pos.asLong());
                        return true;
                    }
                }

            }
        }

        return false;
    }
    public void addDeposit(Level level, long pos) {



        if (!level.getBlockState(BlockPos.of(pos)).is(TFMGBlocks.OIL_DEPOSIT.get()))
            return;

        if (containsDeposit(pos))
            return;

        for (FluidReservoir reservoir : TFMG.DEPOSITS.list) {
            if (reservoir.id != pos) {
                if (isReservoirNearby(BlockPos.of(pos))) {
                    return;
                }
            }
        }

        RandomSource randomSource = level.random;
        FluidReservoir reservoir = new FluidReservoir(pos);
        reservoir.oilReserves = randomSource.nextInt(1000, TFMGConfigs.common().worldgen.depositMaxReserves.get());
        if (!reservoir.deposits.isEmpty()) {
            TFMG.DEPOSITS.list.add(reservoir);
            TFMG.DEPOSITS.markDirty();
        }
    }

    public void removeData() {

        //reservoirs = new ArrayList<>();
        TFMG.DEPOSITS.markDirty();
    }
    public static boolean containsDeposit(long pos) {
        BlockPos findPos = BlockPos.of(pos);

        //if (TFMG.DEPOSITS.depositData.reservoirs.size() == 0)

        for (FluidReservoir reservoir : TFMG.DEPOSITS.list) {



            for(long deposit : reservoir.deposits){

                if(deposit == pos)
                    return true;
            }
            if (reservoir.deposits.contains(pos)) {

                return true;
            }
        }
        return false;
    }

    public void levelLoaded(LevelAccessor level) {
        MinecraftServer server = level.getServer();
        if (server == null || server.overworld() != level)
            return;
        list = new ArrayList<>();
        savedData = null;
        loadLogisticsData(server);
    }
    private void loadLogisticsData(MinecraftServer server) {
        if (savedData != null)
            return;
        savedData = TestSavedData.load(server);
        list = savedData.getLogisticsNetworks();
    }
    public void markDirty() {
        if (savedData != null)
            savedData.setDirty();
    }

}
