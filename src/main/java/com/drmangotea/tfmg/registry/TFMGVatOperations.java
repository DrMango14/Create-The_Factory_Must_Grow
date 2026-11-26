package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperation;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperationEntry;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGVatOperations {
    public static final VatOperationEntry NONE = register("none");
    public static final VatOperationEntry DECOMPRESSOR = register("decompressor");
    public static final VatOperationEntry COMPRESSOR = register("compressor");
    public static final VatOperationEntry ELECTRODE = register("electrode");
    public static final VatOperationEntry GRAPHITE_ELECTRODE = register("graphite_electrode");
    public static final VatOperationEntry MIXER = register("mixer");
    public static final VatOperationEntry CENTRIFUGE = register("centrifuge");
    public static final VatOperationEntry FREEZER = register("freezer");

    private static VatOperationEntry register(String name) {
        return REGISTRATE.vatOperation(name, VatOperation::new).register();
    }

    public static void init() {}
}
