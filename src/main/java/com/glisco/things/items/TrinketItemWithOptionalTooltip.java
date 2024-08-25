package com.glisco.things.items;

import io.wispforest.accessories.api.AccessoryItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class TrinketItemWithOptionalTooltip extends AccessoryItem implements ExtendableTooltipProvider {

    public TrinketItemWithOptionalTooltip(Settings settings) {
        super(settings);
    }

    @Override
    public String tooltipTranslationKey() {
        return this.getTranslationKey() + ".tooltip";
    }

    @Override
    public void getExtraTooltip(ItemStack stack, List<Text> tooltips, TooltipContext tooltipContext, TooltipType tooltipType) {
        var extraData = new ArrayList<Text>();

        tryAppend(extraData);

        tooltips.addAll(0, extraData);
    }
}
