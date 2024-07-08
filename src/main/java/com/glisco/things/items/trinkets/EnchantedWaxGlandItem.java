package com.glisco.things.items.trinkets;

import com.glisco.things.Things;
import com.glisco.things.client.SimplePlayerTrinketRenderer;
import com.glisco.things.items.ThingsItems;
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

public class EnchantedWaxGlandItem extends TrinketItemWithOptionalTooltip implements SimplePlayerTrinketRenderer {

    public EnchantedWaxGlandItem() {
        super(new OwoItemSettings().group(Things.THINGS_GROUP).maxCount(1));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        var entity = reference.entity();

        if (entity.isTouchingWater()) {
            entity.addVelocity(0, 0.005, 0);
        } else if (entity.isInLava() && entity.accessoriesCapability().isEquipped(ThingsItems.HADES_CRYSTAL)) {
            entity.addVelocity(0, 0.02, 0);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public <M extends LivingEntity> void align(ItemStack stack, SlotReference reference, BipedEntityModel<M> model, MatrixStack matrices) {
        AccessoryRenderer.transformToModelPart(matrices, model.body, 0,-0.6,-1);
        matrices.scale(.5f, .5f, .5f);
        matrices.translate(0, 0, -.04);
    }
}
