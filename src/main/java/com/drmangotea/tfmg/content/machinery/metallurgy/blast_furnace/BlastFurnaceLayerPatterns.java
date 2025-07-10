package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace;

public final class BlastFurnaceLayerPatterns {

    // Fireproof bricks + corner brick reinforcements
    public static final BlastFurnaceLayer THIN_REGULAR = new BlastFurnaceLayer("AAAAAAFBFAABABAAFBFAAAAAA");

    // Fireproof bricks + corner brick reinforcements, 1 tuyere
    public static final BlastFurnaceLayer THIN_REGULAR_TUYERE = new BlastFurnaceLayer("AA*AAAFTFAABABAAFBFAAAAAA");

    // Fireproof bricks all around
    public static final BlastFurnaceLayer THICK_REGULAR = new BlastFurnaceLayer("AAAAAABBBAABABAABBBAAAAAA");

    // Fireproof bricks all around, 1 tuyere
    public static final BlastFurnaceLayer THICK_REGULAR_TUYERE = new BlastFurnaceLayer("AA*AAABTBAABABAABBBAAAAAA");

    // Reinforced walls + fireproof brick sides, corner blast furnace reinforcements
    public static final BlastFurnaceLayer THIN_REINFORCED = new BlastFurnaceLayer("AARAAARBRARBABRARBRAAARAA");

    // Reinforced walls + fireproof brick sides, corner blast furnace reinforcements, 1 tuyere
    public static final BlastFurnaceLayer THIN_REINFORCED_TUYERE = new BlastFurnaceLayer("AA*AAARTRARBABRARBRAAARAA");

    // Reinforced walls + fireproof bricks all around
    public static final BlastFurnaceLayer THICK_REINFORCED = new BlastFurnaceLayer("ARRRARBBBRRBABRRBBBRARRRA");

    // Reinforced walls + fireproof bricks all around, 1 tuyere
    public static final BlastFurnaceLayer THICK_REINFORCED_TUYERE = new BlastFurnaceLayer("AR*RARBTBRRBABRRBBBRARRRA");

    // Fireproof bricks + corner brick reinforcements, 1 output
    public static final BlastFurnaceLayer THIN_REGULAR_OUTPUT = new BlastFurnaceLayer("AA*AAAFOFAABABAAFBFAAAAAA");

    // Fireproof bricks all around, 1 output
    public static final BlastFurnaceLayer THICK_REGULAR_OUTPUT = new BlastFurnaceLayer("AA*AAABOBAABABAABBBAAAAAA");

    // Reinforced walls + fireproof brick sides, corner blast furnace reinforcements, 1 output
    public static final BlastFurnaceLayer THIN_REINFORCED_BASE = new BlastFurnaceLayer("AA*AAARORARBABRARBRAAARAA");

    // Reinforced walls + fireproof bricks all around, 1 output
    public static final BlastFurnaceLayer THICK_REINFORCED_OUTPUT = new BlastFurnaceLayer("AR*RARBOBRRBABRRBBBRARRRA");

}
