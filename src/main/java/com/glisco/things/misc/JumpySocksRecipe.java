package com.glisco.things.misc;

import com.glisco.things.items.ThingsItems;
import com.glisco.things.items.trinkets.SocksItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class JumpySocksRecipe extends SpecialCraftingRecipe {

    public JumpySocksRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (!matchOnce(input, stack -> stack.isOf(ThingsItems.GLEAMING_COMPOUND))) return false;
        if (!matchOnce(input, stack -> stack.isOf(ThingsItems.RABBIT_FOOT_CHARM))) return false;

        return matchOnce(input, stack -> stack.isOf(ThingsItems.SOCKS) && !stack.contains(SocksItem.JUMPY_AND_ENABLED));
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
        socc.set(SocksItem.JUMPY_AND_ENABLED, true);

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

    public static class Type implements RecipeType<JumpySocksRecipe> {
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer extends SpecialRecipeSerializer<JumpySocksRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
            super(JumpySocksRecipe::new);
        }
    }
}
