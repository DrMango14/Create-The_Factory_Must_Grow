package com.drmangotea.tfmg.content.electricity;

import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectableElectricalNode;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;


public class WireConnection {

    public ConnectableElectricalNode node1;
    public ConnectableElectricalNode node2;
    public double resistance;
    public boolean render;
    public final PartialModel model;

    public WireConnection(ConnectableElectricalNode node1, ConnectableElectricalNode node2, double resistance,boolean render){
        this(node1,node2,resistance,render, TFMGPartialModels.STEEL_REINFORCED_CABLE);
    }

    public WireConnection(ConnectableElectricalNode node1, ConnectableElectricalNode node2, double resistance,boolean render,PartialModel model){
        this.node1 = node1;
        this.node2 = node2;
        this.resistance = resistance;
        this.render = render;
        this.model = model;
    }
}
