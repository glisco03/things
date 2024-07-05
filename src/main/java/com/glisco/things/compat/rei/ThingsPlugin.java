package com.glisco.things.compat.rei;

import com.glisco.things.Things;
import com.glisco.things.items.ThingsItems;
import com.glisco.things.items.trinkets.SocksItem;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.brewing.DefaultBrewingDisplay;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.ShapelessRecipe;

import java.util.List;
import java.util.Optional;

public class ThingsPlugin implements REIClientPlugin {

    public static final CategoryIdentifier<BrokenWatchDisplay> BROKEN_WATCH_CATEGORY = CategoryIdentifier.of(Things.id("broken_watch"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new BrokenWatchCategory());
        registry.addWorkstations(BROKEN_WATCH_CATEGORY, EntryStacks.of(Blocks.PISTON));
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void registerDisplays(DisplayRegistry registry) {
        final var awkwardPotion = PotionContentsComponent.createStack(Items.POTION, Potions.AWKWARD);
        final var swiftnessPotion = PotionContentsComponent.createStack(Items.POTION, Potions.STRONG_SWIFTNESS);

        registry.add(new DefaultBrewingDisplay(EntryIngredients.of(awkwardPotion),
                EntryIngredients.of(Items.ENDER_PEARL), EntryStacks.of(ThingsItems.RECALL_POTION)));

        final var averageSocks = SocksItem.create(0, false);

        registry.add(new SockDisplay(averageSocks, new ItemStack(ThingsItems.RABBIT_FOOT_CHARM), SocksItem.create(0, true), true));
        registry.add(new SockDisplay(averageSocks, swiftnessPotion, SocksItem.create(1, false), false));
        registry.add(new SockDisplay(SocksItem.create(1, false), swiftnessPotion, SocksItem.create(2, false), false));
        registry.add(new BrokenWatchDisplay());
    }

    @Override
    public void registerEntries(EntryRegistry registry) {
        registry.removeEntry(EntryStacks.of(ThingsItems.AGGLOMERATION));
        if (!Things.CONFIG.enableAgglomeration()) registry.removeEntry(EntryStacks.of(ThingsItems.EMPTY_AGGLOMERATION));
    }

    private static class SockDisplay extends DefaultCraftingDisplay<ShapelessRecipe> {

        public SockDisplay(ItemStack socc, ItemStack addition, ItemStack result, boolean compound) {
            super(List.of(EntryIngredients.of(socc),
                            EntryIngredients.of(compound ? ThingsItems.GLEAMING_COMPOUND : ThingsItems.GLEAMING_POWDER),
                            EntryIngredients.of(addition)),
                    List.of(EntryIngredients.of(result)),
                    Optional.empty());
        }

        @Override
        public int getWidth() {
            return 2;
        }

        @Override
        public int getHeight() {
            return 2;
        }
    }
}
