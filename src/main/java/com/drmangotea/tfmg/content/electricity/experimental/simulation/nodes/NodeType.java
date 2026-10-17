package com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes;

import net.createmod.catnip.theme.Color;

public enum NodeType {

    NEUTRAL("N",new Color(0x30A8FF)),
    PHASE_1("L1",new Color(0xC16B32)),
    PHASE_2("L2",new Color(0x424242)),
    PHASE_3("L3",new Color(0x7892A3)),
    INPUT("IN",new Color(0xE52222)),
    OUTPUT("OUT",new Color(0xEAEAEA)),
    CONNECTOR("CONNECTOR",new Color(0xFFDE3D)),
    MERGED("MERGED", Color.BLACK)

    ;


    NodeType(String name, Color color) {
        this.name = name;
        this.color = color;

    }

    final String name;
    final Color color;

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

}
