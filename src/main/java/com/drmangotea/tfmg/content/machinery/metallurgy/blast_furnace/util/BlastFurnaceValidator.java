package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.util;

import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

import static com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.util.BlastFurnaceLayer.LAYER_SIZE;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class BlastFurnaceValidator {

    private Level level;
    private BlockPos tuyerePos;
    private BlockPos outputPos;
    private BlockPos startingOutputPos;

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

    public BlastFurnaceValidator(BlockPos outputPos, Level levelIn) {
        level = levelIn;
        startingOutputPos = outputPos;
    }

    public record ValidationResult(int height, boolean isReinforced) {}
    public record LayerScanResult(String pattern, BlockPos tuyerePos, BlockPos outputPos) {}

    public ValidationResult validateFurnace() {

        // Early exit if the block is air or invalid
        BlockState outputState = level.getBlockState(startingOutputPos);
        if (!outputState.is(TFMGBlocks.BLAST_FURNACE_OUTPUT.get())) {
            return new ValidationResult(0, false);
        }

        resetState();

        BlockPos baseCenterPos = startingOutputPos.relative(level.getBlockState(startingOutputPos).getValue(FACING).getOpposite());

        // Validate base layer (must contain output)
        LayerScanResult layerScan = scanLayer(baseCenterPos);

        //
        BlastFurnaceLayer layer = new BlastFurnaceLayer(layerScan.pattern());

        if (!layer.isBaseLayer() || !validSpecialBlocks(layerScan, true)) {
            return new ValidationResult(0, false);
        }

        int height = 1;
        boolean isReinforced = layer.isReinforced();

        // Validate stacked layers
        int maxHeight = TFMGConfigs.common().machines.blastFurnaceMaxHeight.get();

        for (int i = 1; i < maxHeight; i++) {
            layerScan = scanLayer(baseCenterPos.above(i));
            layer = new BlastFurnaceLayer(layerScan.pattern());

            if (!layer.isWallLayer() || !validSpecialBlocks(layerScan, false)) {
                break;
            }

            height++;
            isReinforced &= layer.isReinforced();
        }

        return new ValidationResult(height, isReinforced);
    }

    private void resetState() {
        tuyerePos = null;
        outputPos = null;
    }

    /**
     * Scans a 5x5 square of blocks in the world and converts it to a layer String.
     * @param center center BlockPos of a layer.
     * @return row-major String representation of the scanned layer.
     */
    private LayerScanResult scanLayer(BlockPos center) {
        StringBuilder pattern = new StringBuilder(25);
        BlockPos tuyerePos = null;
        BlockPos outputPos = null;

        for (int dz = -LAYER_SIZE / 2; dz <= LAYER_SIZE / 2; dz++) {      // Z = rows
            for (int dx = -LAYER_SIZE / 2; dx <= LAYER_SIZE / 2; dx++) {   // X = columns
                BlockPos pos = center.offset(dx, 0, dz);
                char symbol = getBlockSymbol(level.getBlockState(pos));

                if (symbol == 'T') tuyerePos = pos;
                else if (symbol == 'O') outputPos = pos;

                pattern.append(symbol);
            }
        }
        return new LayerScanResult(pattern.toString(), tuyerePos, outputPos);
    }



    private boolean validSpecialBlocks(LayerScanResult scan, boolean isBaseLayer) {
        // Handle output (only allowed in base layer)
        if (scan.outputPos() != null) {
            if (!isBaseLayer || outputPos != null) return false; // Output in wrong layer or duplicate
            outputPos = scan.outputPos();
        }

        // Handle tuyere
        if (scan.tuyerePos() != null) {
            if (tuyerePos != null) return false; // Duplicate tuyere
            tuyerePos = scan.tuyerePos();
        }

        return true;
    }

    public BlockPos getTuyerePos() {
        return tuyerePos;
    }

    public static char getBlockSymbol(BlockState state) {
        return BLOCK_SYMBOLS.getOrDefault(state.getBlock(), '*');
    }
}
