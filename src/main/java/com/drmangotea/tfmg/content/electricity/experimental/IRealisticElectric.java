package com.drmangotea.tfmg.content.electricity.experimental;

import com.drmangotea.tfmg.TFMG;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IRealisticElectric {




    default ElectricalProperties getProperties() {
        return new ElectricalProperties(this.getPos());
    }

    long getPos();

    default void updateNetwork(BlockPos pos) {

        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(getWorld());
        if(network==null)
            return;
        network.addMember(pos,getProperties());
        network.update();
        TFMG.ELECTRICAL_NETWORK_DATA.markDirty();
    }


    default void removeBlock(){
        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(getWorld());
        network.members.remove(this.getPos());
        TFMG.ELECTRICAL_NETWORK_DATA.markDirty();
    //   network.update();
    }

    Level getWorld();

}
