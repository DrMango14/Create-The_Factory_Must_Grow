package com.drmangotea.tfmg.content.electricity.content.devices.segmented_display;

import com.drmangotea.tfmg.content.electricity.content.devices.base.ResistiveLoadBlockEntity;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlock;
import com.simibubi.create.foundation.utility.DynamicComponent;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SegmentedDisplayBlockEntity extends ResistiveLoadBlockEntity {
    private Optional<DynamicComponent> customText;
    private Couple<String> displayedStrings;
    public Couple<List<Integer>> segmentsToRender = clearSegments();

    public DyeColor color;

    private int partIndex;
	
	//ElectricalProperties properties;

    public SegmentedDisplayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        customText = Optional.empty();
        color = DyeColor.LIME;
		//properties = new SegmentedDisplayProperties(getPos(), state.getValue(FACING));
    }
	
	/*@Override
	public ElectricalProperties getProperties() {
		return properties;
	}*/

    public void setColor(DyeColor color) {
        if(color==DyeColor.BLACK||color == DyeColor.LIGHT_GRAY|| color == DyeColor.GRAY)
            return;
        this.color = color;
        notifyUpdate();
    }

    @Override
    public double resistance() {
        return 100;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (current*voltage < 5) {
            segmentsToRender = clearSegments();
        } else {
            segmentsToRender = getSegments();
        }
    }

    public void clearCustomText() {
        partIndex = 0;
        customText = Optional.empty();
    }
	
    public Couple<List<Integer>> getSegments() {
		if (displayedStrings == null) return clearSegments();
		
		return displayedStrings.mapWithContext((symbol,first) -> {
			List<Integer> segments = SegmentedDisplaySegments.SYMBOLS_TO_SEGMENTS.get(symbol.toLowerCase());
			if (segments == null) return List.of();
			if (first) return segments;
			return segments.stream().map(i -> i+10).toList();
		});
    }

	public Couple<List<Integer>> clearSegments() {
		return Couple.create(List.of(),List.of());
	}

    @Override
    public void initialize() {
        if (level == null) return;

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

    public void updateDisplayedStrings() {
        customText.map(DynamicComponent::resolve).ifPresentOrElse(
			fullText -> displayedStrings = Couple.create(
				charOrEmpty(fullText, partIndex * 2),
				charOrEmpty(fullText, partIndex * 2 + 1)
			),
			() -> displayedStrings = Couple.create("0","0"));
    }

    public MutableComponent getFullText() {
        return customText.map(DynamicComponent::get).orElse(Component.literal(""));
    }

    @Override
    public void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        color = NBTHelper.readEnum(compound,"color",DyeColor.class);
        if (compound.contains("CustomText")) {
            DynamicComponent component = customText.orElseGet(DynamicComponent::new);
            component.read(worldPosition, compound, registries);

            if (component.isValid()) {
                customText = Optional.of(component);
                partIndex = compound.getInt("CustomTextIndex");
            } else {
                customText = Optional.empty();
                partIndex = 0;
            }
        } else {
            customText = Optional.empty();
            partIndex = 0;
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
            customText.get().write(compound, registries);
        }
    }

    private String charOrEmpty(String string, int index) {
        return string.length() <= index ? " " : string.substring(index, index + 1);
    }
	
	@Override
	public BlockPos getPos() {
		return worldPosition;
	}
	
	@Override
	public Level getWorld() {
		return level;
	}
}
