package com.glisco.things.misc;

import com.glisco.things.Things;
import com.glisco.things.items.ThingsItems;
import com.glisco.things.items.trinkets.AgglomerationItem;
import io.wispforest.accessories.api.AccessoriesAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class AgglomerateRecipe extends SpecialCraftingRecipe {
    public AgglomerateRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        int totalItems = 0;
        for (int i = 0; i < input.getSize(); i++) {
            if (input.getStackInSlot(i).isEmpty()) continue;
            totalItems++;
        }
        if (totalItems != 3) return false;

        if (!matchOnce(input, stack -> stack.isOf(ThingsItems.EMPTY_AGGLOMERATION))) return false;

        ItemStack firstStack = matchOne(input, AgglomerateRecipe::isValidItem);
        if (firstStack == null) return false;

        var firstValidSlots = AccessoriesAPI.getStackSlotTypes(world, firstStack);

        return matchOnce(input, stack -> {
            boolean anyCompatibleSlot = false;

            for (var slotType : AccessoriesAPI.getStackSlotTypes(world, firstStack)) {
                if(firstValidSlots.contains(slotType)) {
                    anyCompatibleSlot = true;

                    break;
                }
            }

            return anyCompatibleSlot && !ItemStack.areItemsEqual(stack, firstStack) && isValidItem(stack);
        });
    }

    private static boolean isValidItem(ItemStack stack) {
        return !stack.isEmpty() && !stack.isOf(ThingsItems.EMPTY_AGGLOMERATION) && !stack.isOf(ThingsItems.AGGLOMERATION)
                && !stack.isIn(Things.AGGLOMERATION_BLACKLIST);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack firstTrinket = matchOne(input, stack -> !stack.isEmpty() && !stack.isOf(ThingsItems.EMPTY_AGGLOMERATION));
        ItemStack secondTrinket = matchOne(input, stack -> !stack.isEmpty() && stack != firstTrinket && !stack.isOf(ThingsItems.EMPTY_AGGLOMERATION));

        return AgglomerationItem.createStack(firstTrinket, secondTrinket);
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
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

    private static ItemStack matchOne(CraftingRecipeInput input, Predicate<ItemStack> condition) {
        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i);

            if (!condition.test(stack)) continue;

            return stack;
        }

        return null;
    }

    public static class Serializer extends SpecialRecipeSerializer<AgglomerateRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
            super(AgglomerateRecipe::new);
        }
    }
}
