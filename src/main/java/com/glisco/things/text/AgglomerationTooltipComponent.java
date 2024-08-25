package com.glisco.things.text;

import io.wispforest.owo.ui.base.BaseOwoTooltipComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.VerticalAlignment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public final class AgglomerationTooltipComponent extends BaseOwoTooltipComponent<FlowLayout> {
    public AgglomerationTooltipComponent(AgglomerationTooltipData data) {
        super(() -> create(data.beginningText(), data.stack(), data.endText()));
    }

    private static FlowLayout create(Text beginningText, ItemStack stack, Text endText) {
        var layout = Containers.horizontalFlow(Sizing.content(), Sizing.content());

        layout.child(Components.label(beginningText))
                .child(Components.item(stack)
                        .sizing(Sizing.fixed(10))
                        .margins(Insets.of(0,0,-2,2))
                )
                .child(Components.label(endText));

        layout.verticalAlignment(VerticalAlignment.CENTER)
                .margins(Insets.of(0, 1, 0, 0));

        return layout;
    }
}
