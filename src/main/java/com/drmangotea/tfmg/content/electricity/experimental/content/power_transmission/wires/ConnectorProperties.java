package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.wires;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ConnectingElectricalNode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class ConnectorProperties extends ElectricalProperties {
    public ConnectorProperties(long pos1) {
        super(pos1);

        BlockPos pos = BlockPos.of(position);
        nodes.add(new ConnectingElectricalNode(position,0,new CablePos(0.5f,0.5f,0.5f)));
    }
    


    @Override
    public int getId() {
        return 2;
    }

    public CompoundTag saveData(CompoundTag compound){
            return compound;
    }
}
