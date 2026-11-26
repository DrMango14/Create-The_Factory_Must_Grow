package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.content.machinery.vat.base.registry.types.VatType;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.types.VatTypeEntry;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGVatTypes {
    public static final VatTypeEntry CAST_IRON = register("cast_iron");
    public static final VatTypeEntry STEEL = register("steel");
    public static final VatTypeEntry FIREPROOF = register("fireproof");

    private static VatTypeEntry register(String name) {
        return REGISTRATE.vatType(name, VatType::new).register();
    }

    public static void init() {}
}
