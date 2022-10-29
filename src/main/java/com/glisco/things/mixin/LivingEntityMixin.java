package com.glisco.things.mixin;

import com.glisco.things.Things;
import com.glisco.things.items.ThingsItems;
import com.glisco.things.misc.ExtendedStatusEffectInstance;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "takeShieldHit", at = @At("HEAD"))
    public void onShieldHit(LivingEntity attacker, CallbackInfo ci) {

        LivingEntity user = (LivingEntity) (Object) this;

        if (!Things.isShield(user.getActiveItem().getItem())) return;
        if (!EnchantmentHelper.fromNbt(user.getActiveItem().getEnchantments()).containsKey(Things.RETRIBUTION)) return;
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 40, 0));
    }

    @Inject(method = "blockedByShield", at = @At("RETURN"))
    public void onShieldBlock(DamageSource source, CallbackInfoReturnable<Boolean> cir) {

        if (!cir.getReturnValue()) return;

        LivingEntity user = (LivingEntity) (Object) this;

        if (!Things.isShield(user.getActiveItem().getItem())) return;
        if (!EnchantmentHelper.fromNbt(user.getActiveItem().getEnchantments()).containsKey(Things.RETRIBUTION)) return;
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 40, 0));
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z", ordinal = 1), ordinal = 1)
    public float waxGlandWater(float j) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return j;

        if (!TrinketsApi.getTrinketComponent(player).get().isEquipped(ThingsItems.ENCHANTED_WAX_GLAND)) return j;

        return j * Things.CONFIG.waxGlandMultiplier;
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
    public float waxGlandLava(float speed) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return speed;

        if (TrinketsApi.getTrinketComponent(player).get().isEquipped(ThingsItems.ENCHANTED_WAX_GLAND) && TrinketsApi.getTrinketComponent(player).get().isEquipped(ThingsItems.HADES_CRYSTAL)) {
            int depthStrider = EnchantmentHelper.getDepthStrider(player);
            return 0.0175f * Things.CONFIG.waxGlandMultiplier + 0.1f * depthStrider;
        }

        return speed;
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "handleFallDamage", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;computeFallDamage(FF)I"))
    private int decreaseFallDamage(int originalFallDamage) {
        if (Things.getTrinkets((LivingEntity) (Object) this).isEquipped(ThingsItems.SHOCK_ABSORBER)) {
            return originalFallDamage / 4;
        } else {
            return originalFallDamage;
        }
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float decreaseKineticDamage(DamageSource source, float damage) {
        if (source != DamageSource.FLY_INTO_WALL)
            return damage;

        if (Things.getTrinkets((LivingEntity) (Object) this).isEquipped(ThingsItems.SHOCK_ABSORBER)) {
            return damage / 4;
        } else {
            return damage;
        }
    }

    @ModifyArg(method = "readCustomDataFromNbt", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), index = 1)
    private Object attachPlayerToEffect(Object effect) {
        ((ExtendedStatusEffectInstance) effect).things$setAttachedEntity((LivingEntity)(Object) this);

        return effect;
    }

    @Inject(method = "onStatusEffectApplied", at = @At("HEAD"))
    private void attachPlayerToEffect(StatusEffectInstance effect, Entity source, CallbackInfo ci) {
        ((ExtendedStatusEffectInstance) effect).things$setAttachedEntity((LivingEntity)(Object) this);
    }

    @Inject(method = "onStatusEffectUpgraded", at = @At("HEAD"))
    private void attachPlayerToEffect(StatusEffectInstance effect, boolean reapplyEffect, Entity source, CallbackInfo ci) {
        ((ExtendedStatusEffectInstance) effect).things$setAttachedEntity((LivingEntity)(Object) this);
    }
}
