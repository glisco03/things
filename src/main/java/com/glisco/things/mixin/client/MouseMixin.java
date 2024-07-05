package com.glisco.things.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    // TODO agglomeration item select
//    @Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
//    private void beforePlayerScrollHotbar(long window, double horizontal, double vertical_, CallbackInfo ci, double vertical, boolean bl, double d, double e, double f, int i, int j, int k) {
//        ClientPlayerEntity player = this.client.player;
//
//        if (!player.shouldCancelInteraction()) return;
//
//        boolean scrollMainHandStack;

//        if(player.getMainHandStack().getItem() instanceof AgglomerationItem && player.getMainHandStack().getOrCreateNbt().contains("Items")){
//            scrollMainHandStack = true;
//        } else if(player.getOffHandStack().getItem() instanceof AgglomerationItem && player.getOffHandStack().getOrCreateNbt().contains("Items")){
//            scrollMainHandStack = false;
//        } else {
//            return;
//        }

//        ThingsNetwork.CHANNEL.clientHandle().send(new AgglomerationItem.ScrollHandStackTrinket(scrollMainHandStack));

//        ci.cancel();
//}

}
