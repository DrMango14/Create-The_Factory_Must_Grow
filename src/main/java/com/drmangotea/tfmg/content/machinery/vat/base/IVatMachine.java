package com.drmangotea.tfmg.content.machinery.vat.base;

public interface IVatMachine {


    /**
     * id of an operation this machine provides
     */
    String getOperationId();

    /**
     * operations that cant mix with this machine
     */
    default String[] doesntWorkWith() {
        return new String[0];
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
        TOP_CENTER
        ;


    }

}
