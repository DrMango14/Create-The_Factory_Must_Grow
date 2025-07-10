package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace;

import com.drmangotea.tfmg.registry.TFMGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Map;
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

    private final Set<String> validRotations;

    private static final Map<Block, Character> BLOCK_SYMBOLS = Map.of(
            TFMGBlocks.BLAST_FURNACE_HATCH.get(), 'T',
            TFMGBlocks.FIREPROOF_BRICKS.get(),'B',
            TFMGBlocks.FIREPROOF_BRICK_REINFORCEMENT.get(), 'F',
            TFMGBlocks.BLAST_FURNACE_REINFORCEMENT.get(), 'R',
            TFMGBlocks.RUSTED_BLAST_FURNACE_REINFORCEMENT.get(), 'R',
            TFMGBlocks.BLAST_FURNACE_REINFORCEMENT_WALL.get(), 'W',
            TFMGBlocks.RUSTED_BLAST_FURNACE_REINFORCEMENT_WALL.get(), 'W',
            TFMGBlocks.BLAST_FURNACE_OUTPUT.get(), 'O',
            Blocks.AIR, 'A'
    );

    public BlastFurnaceLayer(String layer) {
        if (layer == null || layer.length() != 25) {
            throw new IllegalArgumentException(
                    String.format("Layer must be a non-null 25 length string (got %s)",
                            layer == null ? "null" : layer.length())
            );
        }
        validRotations = computeRotations(layer);
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

    boolean isValidLayer(BlockPos center, Level level) {
        String builtLayer = flattenLayer(center, level);
        return validRotations.contains(builtLayer);
    }

    /**
     * Scans a 5x5 square of blocks in the world and converts it to a layer String.
     * @param center center BlockPos of a layer.
     * @param level Level (world).
     * @return row-major String representation of the scanned layer.
     */
    String flattenLayer(BlockPos center, Level level) {
        int size = 5; // 5x5 layers max
        StringBuilder sb = new StringBuilder(size * size);
        for (int dz = -2; dz <= 2; dz++) {      // Z = rows
            for (int dx = -2; dx <= 2; dx++) {   // X = columns
                BlockPos pos = center.offset(dx, 0, dz);
                sb.append(getBlockSymbol(level.getBlockState(pos)));
            }
        }
        return sb.toString();
    }

    char getBlockSymbol(BlockState state) {
        return BLOCK_SYMBOLS.getOrDefault(state.getBlock(), '*');
    }
}
