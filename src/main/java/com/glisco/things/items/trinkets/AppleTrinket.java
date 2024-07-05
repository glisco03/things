package com.glisco.things.items.trinkets;

import com.glisco.things.Things;
import com.glisco.things.client.SimplePlayerTrinketRenderer;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.RotationAxis;

public class AppleTrinket implements Trinket {

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!(entity instanceof ServerPlayerEntity player)) return;

        if (player.getHungerManager().getFoodLevel() > 16) return;

        if (!TrinketsApi.getTrinketComponent(player).get().isEquipped(Items.APPLE)) return;

        player.getHungerManager().eat(Items.APPLE.getComponents().get(DataComponentTypes.FOOD));
        stack.decrement(1);

        player.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1, 1);
    }

    @Environment(EnvType.CLIENT)
    public static class Renderer implements SimplePlayerTrinketRenderer {

        @Override
        public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
            if (!Things.CONFIG.renderAppleTrinket()) return;
            SimplePlayerTrinketRenderer.super.render(stack, slotReference, contextModel, matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
        }

        @Override
        public void align(AbstractClientPlayerEntity player, PlayerEntityModel<AbstractClientPlayerEntity> model, MatrixStack matrices, float headYaw, float headPitch) {
            TrinketRenderer.translateToFace(matrices, model, player, headYaw, headPitch);

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
            matrices.translate(0, -.1, -.03);
        }
    }

}
