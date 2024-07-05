package com.glisco.things.mixin;

import com.glisco.things.Things;
import com.glisco.things.items.ThingsItems;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityMixin {

    @Inject(method = "canCraft", at = @At("HEAD"), cancellable = true)
    private static void checkCraft(BrewingRecipeRegistry brewingRecipeRegistry, DefaultedList<ItemStack> slots, CallbackInfoReturnable<Boolean> cir) {
        if (Things.recallPotionIngredient() == null) return;
        if (!slots.get(3).isOf(Things.recallPotionIngredient())) return;

        for (int i = 0; i < 3; i++) {
            if (!(slots.get(i).getItem() instanceof PotionItem)) continue;
            if (!slots.get(i).getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).matches(Potions.AWKWARD)) continue;

            cir.setReturnValue(true);
            return;
        }
    }

    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    private static void doCraft(World world, BlockPos pos, DefaultedList<ItemStack> slots, CallbackInfo ci) {
        if (Things.recallPotionIngredient() == null) return;

        var addition = slots.get(3);
        if (!addition.isOf(Things.recallPotionIngredient())) return;

        addition.decrement(1);

        for (int i = 0; i < 3; i++) {
            if (!(slots.get(i).getItem() instanceof PotionItem)) continue;
            if (!slots.get(i).getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).matches(Potions.AWKWARD)) continue;

            slots.set(i, new ItemStack(ThingsItems.RECALL_POTION));
        }

        world.syncWorldEvent(WorldEvents.BREWING_STAND_BREWS, pos, 0);
        ci.cancel();
    }

}
