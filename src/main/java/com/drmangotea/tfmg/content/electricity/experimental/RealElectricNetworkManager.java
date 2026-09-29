package com.drmangotea.tfmg.content.electricity.experimental;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.experimental.packets.NetworkLoadPacket;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.ComplexValue;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public class RealElectricNetworkManager {
    public static Map<LevelAccessor, RealElectricalNetwork> networks = new HashMap<>();

    public static Map<LevelAccessor, Pair<RealElectricalNetwork, NetworkCalculationData>> updateQueue = new HashMap<>();

    public void onLoadWorld(LevelAccessor world) {
        networks.put(world, new RealElectricalNetwork(world));

    }

    public void onUnloadWorld(LevelAccessor world) {
        networks.remove(world);
    }

    public static RealElectricalNetwork getNetwork(Level level) {
        return networks.get(level);
    }

    public static void handleNetworkUpdate(ServerTickEvent.Post event) {


        networks.forEach((l, n) -> {
            if (n.updateInTicks >= 0) {
                if (n.updateInTicks == 0)
                    n.update();


                n.updateInTicks--;
            }
        });

        if (!updateQueue.isEmpty()) {
            try {
                new HashMap<>(updateQueue).forEach((l, nd) -> {
                    LevelAccessor level = l;
                    NetworkCalculationData data = nd.getSecond();
                    RealElectricalNetwork network = nd.getFirst();

                    int n = data.z.length;
                    if (data.firstIteration) {
                        ComplexValue[][] LU = new ComplexValue[n][n];
                        for (int i = 0; i < n; i++) System.arraycopy(data.A[i], 0, LU[i], 0, n);
                        int[] pivot = new int[n];
                        for (int i = 0; i < n; i++) pivot[i] = i;
                        data.pivot = pivot;
                        data.LU = LU;
                        data.firstIteration = false;
                    }

                    int iterationsToDo = Math.max(n / 2, 1);
                    if (data.iterationsLeft == 0) {
                        network.finishUpdate(data);
                        updateQueue.remove(l);

                    }

                    int iterations = data.iterationsLeft;

                    //for (int i = 0; i < n; i++) {
                    //    String row = i+": ";
                    //    for (int j = 0; j < n; j++) {
                    //        row = row + " "+ data.LU[i][j].abs();
//
                    //    }
                    //    TFMG.LOGGER.debug(row);
                    //}
                    //String row = "Z: ";
                    //for (int i = 0; i < n; i++) {
                    //    row = row + " "+ data.z[i].abs();
                    //}
                    //TFMG.LOGGER.debug(row);

                    if (data.iterationsLeft == 0) {
                        network.finishUpdate(data);
                        updateQueue.remove(l);
                    }

                    //for (int j = 0; j < n; j++) {
                    for (int j = n - iterations; j < n - iterations + iterationsToDo; j++) {
                        if (j < n) {


                            if (data.iterationsLeft == 0) {
                                network.finishUpdate(data);
                                updateQueue.remove(l);
                            }

                            int maxRow = j;
                            double maxVal = data.LU[j][j].abs();
                            for (int i = j + 1; i < n; i++) {
                                if (data.LU[i][j].abs() > maxVal) {
                                    maxVal = data.LU[i][j].abs();
                                    maxRow = i;
                                }
                            }
                            if (maxRow != j) {
                                ComplexValue[] tempRow = data.LU[j];
                                data.LU[j] = data.LU[maxRow];
                                data.LU[maxRow] = tempRow;
                                int tempP = data.pivot[j];
                                data.pivot[j] = data.pivot[maxRow];
                                data.pivot[maxRow] = tempP;
                            }
                            for (int i = j + 1; i < n; i++) {
                                data.LU[i][j] = data.LU[i][j].div(data.LU[j][j]);
                                for (int k = j + 1; k < n; k++) {
                                    data.LU[i][k] = data.LU[i][k].minus(data.LU[i][j].times(data.LU[j][k]));
                                }
                            }
                            data.iterationsLeft--;
                        }
                    }


// 1. Move the loop-invariant check outside the execution loop
                    if (data.iterationsLeft == 0) {
                        network.finishUpdate(data);

                        updateQueue.remove(l);
                    }

                    int limit = n - iterations + iterationsToDo;

                /*
                for (int j = n - iterations; j < limit; j++) {
                    if (j >= n) break;

                    // Cache the current row array reference
                    ComplexValue[] rowJ = data.LU[j];
                    int maxRow = j;
                    double maxVal = rowJ[j].abs();

                    // 2. Optimized Pivot Search
                    for (int i = j + 1; i < n; i++) {
                        double val = data.LU[i][j].abs();
                        if (val > maxVal) {
                            maxVal = val;
                            maxRow = i;
                        }
                    }

                    // 3. Fast Row Swapping
                    if (maxRow != j) {
                        ComplexValue[] tempRow = data.LU[j];
                        data.LU[j] = data.LU[maxRow];
                        data.LU[maxRow] = tempRow;

                        // Update local reference to the new active row J
                        rowJ = data.LU[j];

                        int tempP = data.pivot[j];
                        data.pivot[j] = data.pivot[maxRow];
                        data.pivot[maxRow] = tempP;
                    }

                    ComplexValue pivotVal = rowJ[j];

                    // 4. Cached Pointer Elimination Loops
                    for (int i = j + 1; i < n; i++) {
                        ComplexValue[] rowI = data.LU[i]; // Cache row pointer once per i-loop

                        // Cache the result of the division
                        ComplexValue factor = rowI[j].div(pivotVal);
                        rowI[j] = factor;

                        // Innermost hot loop
                        for (int k = j + 1; k < n; k++) {
                            // Replaces multiple nested array index resolutions with fast local row pointers
                            rowI[k] = rowI[k].minus(factor.times(rowJ[k]));
                        }
                    }

                    data.iterationsLeft--;
                }

                 */
                /*
                for (int j = n - iterations;
                     j < n - iterations + iterationsToDo && j < n;
                     j++) {

                    // Find pivot
                    int maxRow = j;
                    double maxVal = data.LU[j][j].abs();

                    for (int i = j + 1; i < n; i++) {
                        double value = data.LU[i][j].abs();

                        if (value > maxVal) {
                            maxVal = value;
                            maxRow = i;
                        }
                    }

                    // Swap rows
                    if (maxRow != j) {
                        ComplexValue[] tempRow = data.LU[j];
                        data.LU[j] = data.LU[maxRow];
                        data.LU[maxRow] = tempRow;

                        int tempP = data.pivot[j];
                        data.pivot[j] = data.pivot[maxRow];
                        data.pivot[maxRow] = tempP;
                    }

                    final ComplexValue[] pivotRow = data.LU[j];
                    final ComplexValue pivot = pivotRow[j];

                    // Parallelizable
                    int finalJ = j;
                    IntStream.range(j + 1, n).parallel().forEach(i -> {

                        ComplexValue[] row = data.LU[i];

                        ComplexValue multiplier = row[finalJ].div(pivot);
                        row[finalJ] = multiplier;

                        for (int k = finalJ + 1; k < n; k++) {
                            row[k] = row[k].minus(
                                    multiplier.times(pivotRow[k])
                            );
                        }
                    });

                    data.iterationsLeft--;
                }
                */
                });
            } catch (Exception e) {
                TFMG.LOGGER.debug("Whoops");
            }
        }
    }

    public static LevelAccessor getWorldFromNetwork(RealElectricalNetwork network) {

        for (Map.Entry<LevelAccessor, RealElectricalNetwork> entry : networks.entrySet()) {
            if (Objects.equals(network, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void playerLogin(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkLoadPacket packet = new NetworkLoadPacket(RealElectricNetworkManager.networks.values().stream().toList());
            CatnipServices.NETWORK.sendToClient(serverPlayer, packet);

            RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(player.level());
            network.update();


        }
    }


}
