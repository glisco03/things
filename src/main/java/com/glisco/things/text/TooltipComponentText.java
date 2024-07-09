package com.glisco.things.text;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.text.*;

import java.util.List;

public record TooltipComponentText(TooltipData tooltipData) implements Text {

    @Override
    public Style getStyle() {
        return Style.EMPTY;
    }

    @Override
    public TextContent getContent() {
        return PlainTextContent.EMPTY;
    }

    @Override
    public List<Text> getSiblings() {
        return List.of();
    }

    @Override
    public OrderedText asOrderedText() {
        return new TooltipDataAsOrderedText(tooltipData());
    }

    public record TooltipDataAsOrderedText(TooltipData tooltipData) implements OrderedText {
        @Override
        public boolean accept(CharacterVisitor visitor) {
            return false;
        }
    }
}
