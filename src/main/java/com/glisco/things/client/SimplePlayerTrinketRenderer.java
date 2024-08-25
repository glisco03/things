package com.glisco.things.client;

import com.glisco.things.Things;
import io.wispforest.accessories.api.client.SimpleAccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface SimplePlayerTrinketRenderer extends SimpleAccessoryRenderer {

    @Environment(EnvType.CLIENT)
    @Override
    default <M extends LivingEntity> void render(ItemStack stack, SlotReference reference, MatrixStack matrices, EntityModel<M> model, VertexConsumerProvider multiBufferSource, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!Things.CONFIG.renderTrinkets() || !(model instanceof BipedEntityModel<M>)) return;

        SimpleAccessoryRenderer.super.render(stack, reference, matrices, model, multiBufferSource, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }

    @Environment(EnvType.CLIENT)
    @Override
    default <M extends LivingEntity> void align(ItemStack stack, SlotReference reference, EntityModel<M> model, MatrixStack matrices) {
        align(stack, reference, (BipedEntityModel<M>) model, matrices);
    }

    @Environment(EnvType.CLIENT)
    <M extends LivingEntity> void align(ItemStack stack, SlotReference reference, BipedEntityModel<M> model, MatrixStack matrices);
}
