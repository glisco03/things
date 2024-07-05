package com.glisco.things.misc;

import com.glisco.things.items.ThingsItems;
import com.glisco.things.items.trinkets.SocksItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class SockUpgradeRecipe extends SpecialCraftingRecipe {

    public SockUpgradeRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (!matchOnce(input, stack -> stack.isOf(ThingsItems.GLEAMING_POWDER))) return false;
        if (!matchOnce(input, stack -> stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).matches(Potions.STRONG_SWIFTNESS))) return false;

        return matchOnce(input, stack -> stack.isOf(ThingsItems.SOCKS) && stack.getOrDefault(SocksItem.SPEED, 1) < 2);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack socc = null;

        for (int i = 0; i < input.getSize(); i++) {
            final var stack = input.getStackInSlot(i);
            if (!stack.isOf(ThingsItems.SOCKS)) continue;

            socc = stack.copy();
            break;
        }

        if (socc == null) return ItemStack.EMPTY;
        socc.apply(SocksItem.SPEED, 0, speed -> speed + 1);

        return socc;
    }

    private static boolean matchOnce(CraftingRecipeInput input, Predicate<ItemStack> condition) {
        boolean found = false;

        for (int i = 0; i < input.getSize(); i++) {
            if (!condition.test(input.getStackInSlot(i))) continue;
            if (found) return false;

            found = true;
        }

        return found;
    }

    @Override
    public boolean fits(int width, int height) {
        return width > 1 && height > 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Type implements RecipeType<SockUpgradeRecipe> {
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer extends SpecialRecipeSerializer<SockUpgradeRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
            super(SockUpgradeRecipe::new);
        }
    }
}
