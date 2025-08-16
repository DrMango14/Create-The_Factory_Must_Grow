package com.drmangotea.tfmg.content.electricity.utilities.segmented_display;

import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlock;
import com.simibubi.create.foundation.utility.DynamicComponent;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class SegmentedDisplayBlockEntity extends ElectricBlockEntity {

    private static final Couple<String> EMPTY = Couple.create("", "");

    private Optional<DynamicComponent> customText;
    private Couple<String> displayedStrings;

    public List<Integer> segmentsToRender = new ArrayList<>();
    public List<Integer> segmentsToRender2 = new ArrayList<>();

    public DyeColor color;

    private int partIndex;

    public SegmentedDisplayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        customText = Optional.empty();
        color = DyeColor.LIME;
    }

    public void setColor(DyeColor color) {


        if(color==DyeColor.BLACK||color == DyeColor.LIGHT_GRAY|| color == DyeColor.GRAY)
            return;

        this.color = color;
        notifyUpdate();
    }

    @Override
    public float resistance() {
        return 100;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if(getPowerUsage()<5||!canWork()){
            segmentsToRender = new ArrayList<>();
            segmentsToRender2 = new ArrayList<>();
        }else {
            segmentsToRender = getSegments();
        }
    }

    public void clearCustomText() {
        partIndex = 0;
        customText = Optional.empty();
    }
    public List<Integer> getSegments(){
        List<Integer> segments = SegmentedDisplaySegments.SYMBOLS_TO_SEGMENTS.get(getDisplayedStrings().get(true).toLowerCase());



        List<Integer> segments2 = SegmentedDisplaySegments.SYMBOLS_TO_SEGMENTS.get(getDisplayedStrings().get(false).toLowerCase());

        if(segments2 == null)
            segments2 = new ArrayList<>();
        if(segments == null)
            segments = new ArrayList<>();

        List<Integer> segments3 = new ArrayList<>();
        for(int segment : segments2){
            segments3.add(segment+10);
        }

        segmentsToRender2 = segments3;


       // segments.addAll(segments3);

        return segments;
    }


    public Couple<String> getDisplayedStrings() {
        if (displayedStrings == null)
            return EMPTY;
        return displayedStrings;
    }

    @Override
    public void initialize() {
        if (level.isClientSide)
            updateDisplayedStrings();
    }


    public void displayCustomText(String tagElement, int partPositionInRow) {
        if (tagElement == null)
            return;
        if (customText.filter(d -> d.sameAs(tagElement))
                .isPresent())
            return;

        DynamicComponent component = customText.orElseGet(DynamicComponent::new);
        component.displayCustomText(level, worldPosition, tagElement);
        customText = Optional.of(component);
        partIndex = partPositionInRow;
        DisplayLinkBlock.notifyGatherers(level, worldPosition);
        notifyUpdate();
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction != getBlockState().getValue(FACING);
    }

    public void updateDisplayedStrings() {

        customText.map(DynamicComponent::resolve)
                .ifPresentOrElse(
                        fullText -> displayedStrings =
                                Couple.create(charOrEmpty(fullText, partIndex * 2), charOrEmpty(fullText, partIndex * 2 + 1)),
                        () -> displayedStrings =
                                Couple.create("0","0"));
    }

    public MutableComponent getFullText() {
        return customText.map(DynamicComponent::get)
                .orElse(Component.literal(""));
    }

    @Override
    public void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        color = NBTHelper.readEnum(compound,"color",DyeColor.class);
        if (compound.contains("CustomText")) {
            DynamicComponent component = customText.orElseGet(DynamicComponent::new);
            component.read( worldPosition, compound,registries);

            if (component.isValid()) {
                customText = Optional.of(component);
                partIndex = compound.getInt("CustomTextIndex");
            } else {
                customText = Optional.empty();
                partIndex = 0;
            }
        }

        if (clientPacket)
            updateDisplayedStrings();
    }



    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);

        NBTHelper.writeEnum(compound,"color",color);

        if (customText.isPresent()) {
            compound.putInt("CustomTextIndex", partIndex);
            customText.get()
                    .write(compound,registries);
        }
    }

    private String charOrEmpty(String string, int index) {
        return string.length() <= index ? " " : string.substring(index, index + 1);
    }
}
