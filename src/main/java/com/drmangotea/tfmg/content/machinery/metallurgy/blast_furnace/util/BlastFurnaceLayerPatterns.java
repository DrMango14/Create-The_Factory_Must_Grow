package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.util;

import java.util.Set;

public final class BlastFurnaceLayerPatterns {

    // Fireproof bricks + corner brick reinforcements
    public static final String THIN_REGULAR =
                    "*****" +  // Row 1
                    "*FBF*" +  // Row 2
                    "*BAB*" +  // Row 3
                    "*FBF*" +  // Row 4
                    "*****";   // Row 5

    // Fireproof bricks + corner brick reinforcements, 1 tuyere
    public static final String THIN_REGULAR_TUYERE =
                    "*****" +  // Row 1 (wildcard for tuyere access)
                    "*FTF*" +  // Row 2 (T = tuyere)
                    "*BAB*" +  // Row 3
                    "*FBF*" +  // Row 4
                    "*****";   // Row 5

    // Fireproof bricks all around
    public static final String THICK_REGULAR =
                    "*****" +  // Row 1
                    "*BBB*" +  // Row 2
                    "*BAB*" +  // Row 3
                    "*BBB*" +  // Row 4
                    "*****";   // Row 5

    // Fireproof bricks all around, 1 tuyere
    public static final String THICK_REGULAR_TUYERE =
                    "*****" +  // Row 1 (wildcard for tuyere access)
                    "*BTB*" +  // Row 2 (T = tuyere)
                    "*BAB*" +  // Row 3
                    "*BBB*" +  // Row 4
                    "*****";   // Row 5

    // Reinforced walls + fireproof brick sides, corner blast furnace reinforcements
    public static final String THIN_REINFORCED =
                    "**W**" +  // Row 1
                    "*RBR*" +  // Row 2
                    "WBABW" +  // Row 3
                    "*RBR*" +  // Row 4
                    "**W**";   // Row 5

    // Reinforced walls + fireproof brick sides, corner blast furnace reinforcements, 1 tuyere
    public static final String THIN_REINFORCED_TUYERE =
                    "*****" +  // Row 1 (wildcard for tuyere access)
                    "*RTR*" +  // Row 2 (T = tuyere)
                    "WBABW" +  // Row 3
                    "*RBR*" +  // Row 4
                    "**W**";   // Row 5

    // Reinforced walls + fireproof bricks all around, 1 tuyere
    public static final String THICK_REINFORCED =
                    "*WWW*" +  // Row 1
                    "WBBBW" +  // Row 2
                    "WBABW" +  // Row 3
                    "WBBBW" +  // Row 4
                    "*WWW*";   // Row 5

    // Reinforced walls + fireproof bricks all around, 1 tuyere
    public static final String THICK_REINFORCED_TUYERE =
                    "*W*W*" +  // Row 1 (wildcard for tuyere access)
                    "WBTBW" +  // Row 2 (T = tuyere)
                    "WBABW" +  // Row 3
                    "WBBBW" +  // Row 4
                    "*WWW*";   // Row 5

    // Fireproof bricks + corner brick reinforcements, 1 output
    public static final String THIN_REGULAR_OUTPUT =
                    "*****" +  // Row 1 (wildcard for output access)
                    "*FOF*" +  // Row 2 (O = output)
                    "*BBB*" +  // Row 3
                    "*FBF*" +  // Row 4
                    "*****";   // Row 5

    // Fireproof bricks all around, 1 output
    public static final String THICK_REGULAR_OUTPUT =
                    "*****" +  // Row 1 (wildcard for output access)
                    "*BOB*" +  // Row 2 (O = output)
                    "*BBB*" +  // Row 3
                    "*BBB*" +  // Row 4
                    "*****";   // Row 5

    // Reinforced walls + fireproof brick sides, corner blast furnace reinforcements, 1 output
    public static final String THIN_REINFORCED_OUTPUT =
                    "*****" +  // Row 1 (wildcard for output access)
                    "*ROR*" +  // Row 2 (O = output)
                    "WBBBW" +  // Row 3
                    "*RBR*" +  // Row 4
                    "**W**";   // Row 5

    // Reinforced walls + fireproof bricks all around, 1 output
    public static final String THICK_REINFORCED_OUTPUT =
                    "*W*W*" +  // Row 1 (wildcard for output access)
                    "WBOBW" +  // Row 2 (O = output)
                    "WBBBW" +  // Row 3
                    "WBBBW" +  // Row 4
                    "*WWW*";   // Row 5

    public static final Set<String> REINFORCED_LAYERS = Set.of(
            THIN_REINFORCED,
            THIN_REINFORCED_TUYERE,
            THIN_REINFORCED_OUTPUT,
            THICK_REINFORCED,
            THICK_REINFORCED_TUYERE,
            THICK_REINFORCED_OUTPUT
    );

    public static final Set<String> BASE_LAYERS = Set.of(
            THIN_REGULAR_OUTPUT,
            THICK_REGULAR_OUTPUT,
            THIN_REINFORCED_OUTPUT,
            THICK_REINFORCED_OUTPUT
    );

    public static final Set<String> WALL_LAYERS = Set.of(
            THIN_REINFORCED,
            THIN_REINFORCED_TUYERE,
            THICK_REINFORCED,
            THICK_REINFORCED_TUYERE,
            THIN_REGULAR,
            THIN_REGULAR_TUYERE,
            THICK_REGULAR,
            THICK_REGULAR_TUYERE
    );

}
