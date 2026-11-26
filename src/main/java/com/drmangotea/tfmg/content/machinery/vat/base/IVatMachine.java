package com.drmangotea.tfmg.content.machinery.vat.base;

import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperation;

import java.util.List;

public interface IVatMachine {
    /**
     * id of an operation this machine provides
     */
    VatOperation getOperationId();

    /**
     * checks if this machine can operate
     */
    boolean canOperate(VatBlockEntity vat);

    /**
     * operations that cant mix with this machine
     */
    default List<VatOperation> doesntWorkWith() {
        return List.of();
    }

    /**
     * speed modifier of this machine
     */
    default int getWorkPercentage(){
        return 100;
    }
    /**
     * determines the position this machine can be in relative to the chemical vat
     */
    default PositionRequirement getPositionRequirement(){
        return PositionRequirement.ANY;
    }

    default void vatUpdated(VatBlockEntity be){}

    enum PositionRequirement{
        ANY,
        BOTTOM,
        TOP,
        ANY_CENTER,
        BOTTOM_CENTER,
        TOP_CENTER;
    }

}
