package com.drmangotea.tfmg.content.electricity.experimental;

import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ConnectableElectricalNode;




public class WireConnection {

    public ConnectableElectricalNode node1;
    public ConnectableElectricalNode node2;
    public double resistance;
    public boolean render;

    public WireConnection(ConnectableElectricalNode node1, ConnectableElectricalNode node2, double resistance,boolean render){
        this.node1 = node1;
        this.node2 = node2;
        this.resistance = resistance;
        this.render = render;
    }
}
