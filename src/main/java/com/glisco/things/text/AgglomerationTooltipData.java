package com.glisco.things.text;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.text.Text;

public record AgglomerationTooltipData(Text beginningText, ItemStack stack, Text endText) implements TooltipData {}
