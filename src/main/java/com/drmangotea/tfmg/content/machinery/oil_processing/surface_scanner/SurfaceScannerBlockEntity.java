package com.drmangotea.tfmg.content.machinery.oil_processing.surface_scanner;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.machinery.misc.machine_input.MachineInputBlockEntity;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.Create;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;

import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class SurfaceScannerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {


    public Boolean[][] grid = new Boolean[5][5];

    public SurfaceScannerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(20);

    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CreateLang.translate("goggles.surface_scanner.header")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        if(level.getBlockEntity(getBlockPos().below()) instanceof MachineInputBlockEntity be&&Math.abs(be.getSpeed())>=64) {
            int depositsFound = 0;
            for(Boolean[] row : grid){
                for(Boolean light : row){
                    if(light)
                        depositsFound++;
                }
            }

            if(depositsFound>0){
                CreateLang.number(depositsFound)
                        .add(CreateLang.translate("goggles.surface_scanner.deposits_found"))
                        .style(ChatFormatting.GREEN)
                        .forGoggles(tooltip);
            }else
                CreateLang.translate("goggles.surface_scanner.no_deposit")
                        .style(ChatFormatting.RED)
                        .forGoggles(tooltip);

        }else
            CreateLang.translate("goggles.surface_scanner.no_rotation")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip);

        return true;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    public void findDeposits(){

        if(!level.isClientSide)
            return;

        for(int x = 0;x<5;x++){
            for(int z = 0;z<5;z++){
                grid[x][z] = hasOil(new BlockPos(getBlockPos().getX() + (x-2)*16, TFMGConfigs.common().machines.surfaceScannerScanDepth.get(),getBlockPos().getZ() + (z-2)*16));
            }
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        if(level.getBlockEntity(getBlockPos().below()) instanceof MachineInputBlockEntity be&&Math.abs(be.getSpeed())>=64) {
            findDeposits();
        }else {
            grid = new Boolean[5][5];
        }
    }

    public boolean hasOil(BlockPos pos){
        ChunkAccess chunk = level.getChunk(pos);
        AABB checkedArea = new AABB(chunk.getPos().getMiddleBlockPosition(TFMGConfigs.common().machines.surfaceScannerScanDepth.get()).north().west());
        checkedArea = checkedArea.inflate(7,0,7);
        for(BlockState state : chunk.getBlockStates(checkedArea).toList()){
            if(state.is(TFMGTags.TFMGBlockTags.SURFACE_SCANNER_FINDABLE.tag))
                return true;
        }

        return false;
    }





}
