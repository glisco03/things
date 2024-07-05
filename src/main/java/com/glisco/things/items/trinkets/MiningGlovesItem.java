package com.glisco.things.items.trinkets;

import com.glisco.things.Things;
import com.glisco.things.items.TrinketItemWithOptionalTooltip;
import dev.emi.trinkets.api.SlotReference;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;

public class MiningGlovesItem extends TrinketItemWithOptionalTooltip {

    public MiningGlovesItem() {
        super(new OwoItemSettings().maxCount(1).group(Things.THINGS_GROUP));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!(entity instanceof ServerPlayerEntity player)) return;

        player.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(Things.MOMENTUM), 5,
                Things.CONFIG.effectLevels.miningGloveMomentum() - 1, true, false, true));
    }
}