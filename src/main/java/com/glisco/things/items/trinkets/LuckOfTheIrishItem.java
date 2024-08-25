package com.glisco.things.items.trinkets;

import com.glisco.things.Things;
import com.glisco.things.client.SimplePlayerTrinketRenderer;
import com.glisco.things.items.TrinketItemWithOptionalTooltip;
import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class LuckOfTheIrishItem extends TrinketItemWithOptionalTooltip implements SimplePlayerTrinketRenderer {

    public LuckOfTheIrishItem() {
        super(new OwoItemSettings().maxCount(1).group(Things.THINGS_GROUP));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public <M extends LivingEntity> void align(ItemStack stack, SlotReference reference, BipedEntityModel<M> model, MatrixStack matrices) {
        AccessoryRenderer.transformToModelPart(matrices, model.body, -0.55, .65, 1);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
        matrices.scale(.25f, .25f, .25f);
        matrices.translate(0, 0, -.055);
    }
}
