package com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.base;

import java.util.ArrayList;
import java.util.List;

public class FluidReservoir {

    public final long id;

    public int oilReserves;

    public List<Long> deposits = new ArrayList<>();

    public FluidReservoir(long id){
        this.id = id;
        deposits.add(id);
    }



}
