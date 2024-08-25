package com.glisco.things.mixin;

import com.glisco.things.items.ThingsItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {

    @Inject(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V"), cancellable = true)
    public void consume(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        var player = (PlayerEntity) user;
        var capability = player.accessoriesCapability();

        if (world.random.nextDouble() > 0.75) return;
        if (capability == null || !capability.isEquipped(ThingsItems.PLACEBO)) return;

        player.incrementStat(Stats.USED.getOrCreateStat((PotionItem) (Object) this));
        cir.setReturnValue(stack);
    }

}
