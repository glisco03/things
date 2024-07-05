package com.glisco.things.mixin;

import com.glisco.things.Things;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {

    @Inject(method = "isValidIngredient", at = @At("HEAD"), cancellable = true)
    private void allowEnderPearl(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (Things.recallPotionIngredient() == null) return;
        if (stack.isOf(Things.recallPotionIngredient())) cir.setReturnValue(true);
    }

}
