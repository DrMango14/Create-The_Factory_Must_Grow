package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single blast furnace layer as a String.
 * Stores all 4 rotations of the layer.
 * ------------------------------------------------------
 * Component blocks are represented by characters:
 * B = fireproof B-ricks, F = F-ireproof reinforcement
 * R = blast furnace R-einforcement, W = blast furnace R-einforced wall
 * A = Air, T = Tuyere
 * ------------------------------------------------------
 * Layers strings are row-major, left-to-right: e.g.
 * WWW
 * WAW
 * WWW
 * turns into WWWWAWWWW.
 */
public class BlastFurnaceLayer {

    private final Set<String> layerRotations;

    public static final int LAYER_SIZE = 5;
    public static final int LAYER_AREA = LAYER_SIZE * LAYER_SIZE;


    public BlastFurnaceLayer(String layer) {
        if (layer == null || layer.length() != LAYER_AREA) {
            throw new IllegalArgumentException(
                    String.format("Layer must be a non-null %s length string (got %s)",
                            LAYER_AREA,
                            layer == null ? "null" : layer.length())
            );
        }
        layerRotations = Collections.unmodifiableSet(computeRotations(layer));
    }

    private Set<String> computeRotations(String baseLayer) {
        Set<String> rotations = new HashSet<>();
        rotations.add(baseLayer);
        rotations.add(rotateSquare90(baseLayer));
        rotations.add(rotateSquare180(baseLayer));
        rotations.add(rotateSquare270(baseLayer));
        return rotations;
    }

    private String rotateSquare90(String baseLayer) {
        int size = (int) Math.sqrt(baseLayer.length());

        char[] rotated = new char[baseLayer.length()];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Transpose and reverse columns
                rotated[row * size + col] =
                        baseLayer.charAt((size - 1 - col) * size + row);
            }
        }
        return new String(rotated);
    }

    private String rotateSquare180(String baseLayer) {
        return new StringBuilder(baseLayer).reverse().toString();
    }

    private String rotateSquare270(String baseLayer) {
        int size = (int) Math.sqrt(baseLayer.length());

        char[] rotated = new char[baseLayer.length()];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Transpose and reverse rows
                rotated[row * size + col] =
                        baseLayer.charAt(col * size + (size - 1 - row));
            }
        }
        return new String(rotated);
    }

    public boolean matchesAnyPattern(Set<String> patterns) {
        return patterns.stream().anyMatch(pattern ->
                layerRotations.stream().anyMatch(rotation ->
                        matchesWithWildcards(rotation, pattern)
                )
        );
    }

    private boolean matchesWithWildcards(String actualLayer, String pattern) {
        if (actualLayer.length() != pattern.length()) return false;

        for (int i = 0; i < actualLayer.length(); i++) {
            char p = pattern.charAt(i);
            char a = actualLayer.charAt(i);

            // '*' in pattern matches any character (including air)
            if (p != '*' && p != a) {
                return false;
            }
            if (pattern.contains("T") && !actualLayer.contains("T")) {
                return false; // Tuyere required but missing
            }
            if (pattern.contains("O") && !actualLayer.contains("O")) {
                return false; // Output required but missing
            }
        }
        return true;
    }

    public boolean isBaseLayer() {
        return matchesAnyPattern(BlastFurnaceLayerPatterns.BASE_LAYERS);
    }

    public boolean isWallLayer() {
        return matchesAnyPattern(BlastFurnaceLayerPatterns.WALL_LAYERS);
    }

    public boolean isReinforced() {
        return matchesAnyPattern(BlastFurnaceLayerPatterns.REINFORCED_LAYERS);
    }
}
