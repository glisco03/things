package com.glisco.things.mixin.trinkets;

import dev.emi.trinkets.api.LivingEntityTrinketComponent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntityTrinketComponent.class)
public class LivingEntityTrinketComponentMixin {
    // TODO agglomeration trinket mixin
//    @ModifyVariable(method = "isEquipped", at = @At("HEAD"), argsOnly = true, remap = false)
//    private Predicate<ItemStack> checkAgglomerationsAsWell(Predicate<ItemStack> oldPredicate) {
//        return oldPredicate.or(stack -> stack.isOf(ThingsItems.AGGLOMERATION) && AgglomerationItem.hasStack(stack, oldPredicate));
//    }
//
//    @ModifyArg(method = "getEquipped", at = @At(value = "INVOKE", target = "Ldev/emi/trinkets/api/LivingEntityTrinketComponent;forEach(Ljava/util/function/BiConsumer;)V"), remap = false)
//    private BiConsumer<SlotReference, ItemStack> iterateThroughAgglomerations(BiConsumer<SlotReference, ItemStack> consumer) {
//        return consumer.andThen((slot, stack) -> {
//            if (stack.isOf(ThingsItems.AGGLOMERATION)) {
//                AgglomerationItem.getStacks(stack).forEach(subStack -> consumer.accept(slot, subStack));
//            }
//        });
//    }
}
