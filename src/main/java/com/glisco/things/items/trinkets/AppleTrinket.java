package com.glisco.things.items.trinkets;

import com.glisco.things.Things;
import com.glisco.things.client.SimplePlayerTrinketRenderer;
import io.wispforest.accessories.api.Accessory;
import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.RotationAxis;

public class AppleTrinket implements Accessory {

    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        if (!(reference.entity() instanceof ServerPlayerEntity player)) return;

        if (player.getHungerManager().getFoodLevel() > 16) return;

        if (!player.accessoriesCapability().isEquipped(Items.APPLE)) return;

        player.getHungerManager().eat(Items.APPLE.getComponents().get(DataComponentTypes.FOOD));
        stack.decrement(1);

        player.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1, 1);
    }

    @Environment(EnvType.CLIENT)
    public static class Renderer implements SimplePlayerTrinketRenderer {

        @Override
        public <M extends LivingEntity> void render(ItemStack stack, SlotReference reference, MatrixStack matrices, EntityModel<M> model, VertexConsumerProvider multiBufferSource, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!Things.CONFIG.renderAppleTrinket()) return;

            SimplePlayerTrinketRenderer.super.render(stack, reference, matrices, model, multiBufferSource, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }

        @Override
        public <M extends LivingEntity> void align(ItemStack stack, SlotReference reference, BipedEntityModel<M> model, MatrixStack matrices) {
            AccessoryRenderer.translateToFace(matrices, model, reference.entity());

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
            matrices.translate(0, -.1, -.03);
        }
    }

}
