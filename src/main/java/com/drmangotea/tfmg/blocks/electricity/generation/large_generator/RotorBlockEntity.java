package com.drmangotea.tfmg.blocks.electricity.generation.large_generator;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.KineticElectricBlockEntity;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.*;


import static com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock.AXIS;
import static net.minecraft.core.Direction.*;
import static com.drmangotea.tfmg.blocks.electricity.generation.large_generator.StatorBlock.*;

public class RotorBlockEntity extends KineticElectricBlockEntity {




    LerpedFloat visualSpeed = LerpedFloat.linear();
    float angle;

    int FEProduction = 0;

    List<BlockPos> stators = new ArrayList<>();

    public static final Map<Direction.Axis,Map<StatorOffset, BlockState>> statorPosition = setStatorPositons();



    public RotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(2);
    }



    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
            visualSpeed.chase(getGeneratedSpeed(), 1 / 128f, LerpedFloat.Chaser.EXP);
    }


    public void setFEProduction(){

        float modifier = TFMGConfigs.server().machines.largeGeneratorFeModifier.getF();

        if(stators.size()!=8){
            FEProduction = 0;
            return;
        }

        FEProduction = (int) (((Math.log(Math.abs(getSpeed())-68.25)/Math.log(1.026))-22)*modifier);


    }

    @Override
    public int voltageGeneration() {
        if(stators.size()!=8)
            return 0;

        if(speed == 0)
            return 0;

        return Math.min(5000,FEProduction*7);
    }

    @Override
    public void tick() {
        super.tick();
        manageRotation();



        setFEProduction();

    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        findStators();
    }

    @Override
    public void onPlaced() {
        super.onPlaced();
        findStators();
    }

    @Override
    public int FECapacity() {
        return 10000;
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        setFEProduction();
    }

    public void findStators(){

        Direction.Axis axis = getBlockState().getValue(AXIS);


        Map<StatorOffset, BlockState> position = statorPosition.get(axis);
        stators = new ArrayList<>();
        position.forEach(((offset, state) -> {
            BlockPos pos = getBlockPos().relative(offset.direction1);
            if (offset.direction2.isPresent())
                pos = pos.relative(offset.direction2.get());

            if (level.getBlockEntity(pos) instanceof StatorBlockEntity be) {

                if (be.rotor == null || be.rotor == getBlockPos()) {



                stators.add(pos);
                level.setBlock(pos, state, 2);
                be.rotor = getBlockPos();

            } else {
                    stators = new ArrayList<>();
                    setFEProduction();
                    needsNetworkUpdate();
                    needsVoltageUpdate();
                }
            } else {
                stators = new ArrayList<>();
                setFEProduction();
                needsNetworkUpdate();
                needsVoltageUpdate();
            }

        }));



    }

    @Override
    public int FEProduction() {
        return FEProduction;
    }

    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.GENERATOR.get());
    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {



        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    public void manageRotation(){
        float targetSpeed = getSpeed();
        visualSpeed.updateChaseTarget(targetSpeed);
        visualSpeed.tickChaser();
        angle += visualSpeed.getValue() * 3 / 10f;
        angle %= 360;
    }


    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction.getAxis()!=getBlockState().getValue(AXIS);
    }

    public static Map<Direction.Axis,Map<StatorOffset, BlockState>> setStatorPositons(){

        Map<Direction.Axis,Map<StatorOffset, BlockState>> statorPositions = new HashMap<>();

        BlockState defaultState = TFMGBlocks.STATOR.getDefaultState();

        BlockState cornerState = defaultState.setValue(STATOR_STATE,StatorState.CORNER);
        BlockState horizontal = defaultState.setValue(STATOR_STATE,StatorState.CORNER_HORIZONTAL);
        BlockState sideState = defaultState.setValue(STATOR_STATE,StatorState.SIDE);

        BlockState cornerFlipped = cornerState.setValue(VALUE, false);
        BlockState sideFlipped = sideState.setValue(VALUE, false);

        Map<StatorOffset, BlockState> xPos = new HashMap<>();
            xPos.put(pos(UP         ), sideFlipped  .setValue(FACING,DOWN  ));
            xPos.put(pos(DOWN       ), sideFlipped  .setValue(FACING,UP    ));
            xPos.put(pos(NORTH      ), sideFlipped  .setValue(FACING,SOUTH ));
            xPos.put(pos(SOUTH      ), sideFlipped  .setValue(FACING,NORTH ));
            xPos.put(pos(UP,NORTH   ), cornerState  .setValue(FACING,EAST  ));
            xPos.put(pos(UP,SOUTH   ), cornerState  .setValue(FACING,WEST  ));
            xPos.put(pos(DOWN,NORTH ), cornerFlipped.setValue(FACING,EAST  ));
            xPos.put(pos(DOWN,SOUTH ), cornerFlipped.setValue(FACING,WEST  ));

        Map<StatorOffset, BlockState> yPos = new HashMap<>();
            yPos.put(pos(EAST       ), sideState    .setValue(FACING,WEST  ));
            yPos.put(pos(WEST       ), sideState    .setValue(FACING,EAST  ));
            yPos.put(pos(SOUTH      ), sideState    .setValue(FACING,NORTH ));
            yPos.put(pos(NORTH      ), sideState    .setValue(FACING,SOUTH ));
            yPos.put(pos(NORTH,WEST  ), horizontal   .setValue(FACING,NORTH));
            yPos.put(pos(SOUTH,EAST  ), horizontal   .setValue(FACING,SOUTH));
            yPos.put(pos(SOUTH,WEST ), horizontal   .setValue(FACING,WEST  ));
            yPos.put(pos(NORTH,EAST ), horizontal   .setValue(FACING,EAST  ));

        Map<StatorOffset, BlockState> zPos = new HashMap<>();
            zPos.put(pos(UP         ), sideState    .setValue(FACING,DOWN  ));
            zPos.put(pos(DOWN       ), sideState    .setValue(FACING,UP    ));
            zPos.put(pos(WEST       ), sideFlipped  .setValue(FACING,EAST  ));
            zPos.put(pos(EAST       ), sideFlipped  .setValue(FACING,WEST  ));
            zPos.put(pos(UP,WEST    ), cornerState  .setValue(FACING,UP    ));
            zPos.put(pos(UP,EAST    ), cornerState  .setValue(FACING,SOUTH ));
            zPos.put(pos(DOWN,WEST  ), cornerFlipped.setValue(FACING,UP    ));
            zPos.put(pos(DOWN,EAST  ), cornerFlipped.setValue(FACING,SOUTH ));

        statorPositions.put(Axis.X,xPos);
        statorPositions.put(Axis.Y,yPos);
        statorPositions.put(Axis.Z,zPos);


        return statorPositions;
    }



    private static StatorOffset pos(Direction dir1){
        return new StatorOffset(dir1, Optional.empty());
    }

    private static StatorOffset pos(Direction dir1,Direction dir2){
        return new StatorOffset(dir1, Optional.of(dir2));
    }



    private static class StatorOffset{
        public final Direction direction1;
        public final Optional<Direction> direction2;

        public StatorOffset(Direction dir1,Optional<Direction> dir2){
            this.direction1 = dir1;
            this.direction2 = dir2;

        }


    }



}