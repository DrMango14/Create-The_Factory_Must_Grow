package com.drmangotea.tfmg.content.electricity.experimental.saved_data;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.base.FluidReservoir;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;

public class NetworkDataManager {

        public List<RealElectricalNetwork> list;

        private NetworkSavedData savedData;

        public NetworkDataManager() {
            list = new ArrayList<>();
            list.addAll(RealElectricNetworkManager.networks.values());
        }




        public void removeMember(long pos) {
            for (FluidReservoir reservoir : TFMG.DEPOSITS.list) {
                if (reservoir.deposits.contains(pos)) {
                    reservoir.deposits.remove(pos);
                    if (reservoir.deposits.isEmpty())
                        TFMG.DEPOSITS.list.remove(reservoir);
                    return;
                }
            }
        }





        public void levelLoaded(LevelAccessor level) {

            MinecraftServer server = level.getServer();
            if (server == null || server.overworld() != level)
                return;
           // list = new ArrayList<>();
            savedData = null;
            loadNetworkData(server);


        }
        private void loadNetworkData(MinecraftServer server) {
            if (savedData != null)
                return;
            savedData = NetworkSavedData.load(server);
            list = savedData.getNetworks();
        }
        public void markDirty() {
            if (savedData != null)
                savedData.setDirty();
        }


}
