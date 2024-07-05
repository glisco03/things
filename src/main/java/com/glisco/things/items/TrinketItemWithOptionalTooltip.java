package com.glisco.things.items;

import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public abstract class TrinketItemWithOptionalTooltip extends TrinketItem implements ExtendableTooltipProvider {

    public TrinketItemWithOptionalTooltip(Settings settings) {
        super(settings);
    }

    @Override
    public String tooltipTranslationKey() {
        return this.getTranslationKey() + ".tooltip";
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tryAppend(tooltip);
    }
}
