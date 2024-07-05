package com.glisco.things.mixin;

import com.glisco.things.Things;
import com.glisco.things.items.ThingsItems;
import com.glisco.things.misc.ExtendedStatusEffectInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectUtil.class)
public class StatusEffectUtilMixin {

    @Unique
    private static final ThreadLocal<LivingEntity> cachedEntity = new ThreadLocal<>();

    @Inject(method = "hasHaste", at = @At("HEAD"), cancellable = true)
    private static void hasMomentum(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity.hasStatusEffect(Registries.STATUS_EFFECT.getEntry(Things.MOMENTUM))) cir.setReturnValue(true);
    }

    @Inject(method = "getHasteAmplifier", at = @At("HEAD"))
    private static void storeEntity(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        cachedEntity.set(entity);
    }

    @ModifyVariable(method = "getHasteAmplifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z", ordinal = 1), ordinal = 0)
    private static int getMomentumAmplifier(int i) {
        LivingEntity entity = cachedEntity.get();

        if (entity.hasStatusEffect(Registries.STATUS_EFFECT.getEntry(Things.MOMENTUM))) {
            i += entity.getStatusEffect(Registries.STATUS_EFFECT.getEntry(Things.MOMENTUM)).getAmplifier();
            if (entity.hasStatusEffect(StatusEffects.HASTE) && i == 0) i++;
        }
        cachedEntity.set(null);
        return i;
    }

    @ModifyVariable(method = "getDurationText", at = @At(value = "HEAD"), argsOnly = true, ordinal = 0)
    private static float extendTime(float multiplier, StatusEffectInstance instance, float unused) {
        var entity = ((ExtendedStatusEffectInstance) instance).things$getAttachedEntity();
        if (entity != null && Things.hasTrinket(entity, ThingsItems.BROKEN_WATCH)) {
            return multiplier * 1.5F;
        } else {
            return multiplier;
        }
    }
}
