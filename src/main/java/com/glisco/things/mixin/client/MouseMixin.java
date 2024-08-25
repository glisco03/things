package com.glisco.things.mixin.client;

import com.glisco.things.ThingsNetwork;
import com.glisco.things.items.trinkets.AgglomerationItem;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.wispforest.accessories.api.components.AccessoriesDataComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    // TODO agglomeration item select
    @WrapWithCondition(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"))
    private boolean beforePlayerScrollHotbar(PlayerInventory instance, double scrollAmount) {
        ClientPlayerEntity player = this.client.player;

        if (!player.shouldCancelInteraction()) return true;

        boolean scrollMainHandStack;

        var mainHandStack = player.getMainHandStack();
        var offHandStack = player.getOffHandStack();

        if (mainHandStack.getItem() instanceof AgglomerationItem && mainHandStack.contains(AccessoriesDataComponents.NESTED_ACCESSORIES)) {
            scrollMainHandStack = true;
        } else if (offHandStack.getItem() instanceof AgglomerationItem && offHandStack.contains(AccessoriesDataComponents.NESTED_ACCESSORIES)) {
            scrollMainHandStack = false;
        } else {
            return true;
        }

        ThingsNetwork.CHANNEL.clientHandle().send(new AgglomerationItem.ScrollHandStackTrinket(scrollMainHandStack));

        return false;
    }
}
