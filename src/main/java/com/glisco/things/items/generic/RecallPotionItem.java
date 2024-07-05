package com.glisco.things.items.generic;

import com.glisco.things.Things;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class RecallPotionItem extends Item {

    public RecallPotionItem() {
        super(new OwoItemSettings().group(Things.THINGS_GROUP).maxCount(16));
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 15;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        var player = user instanceof PlayerEntity ? (PlayerEntity) user : null;
        if (player == null) return stack;

        if (player instanceof ServerPlayerEntity serverPlayer) {

            Criteria.CONSUME_ITEM.trigger(serverPlayer, stack);

            if (serverPlayer.getSpawnPointPosition() != null) {
                var respawnPos = ((ServerPlayerEntity) player).getRespawnTarget(false, TeleportTarget.NO_OP);
                player.teleportTo(respawnPos);

                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                    if (stack.isEmpty()) {
                        stack = new ItemStack(Items.GLASS_BOTTLE);
                    } else {
                        player.getInventory().offerOrDrop(new ItemStack(Items.GLASS_BOTTLE));
                    }
                }
            } else {
                serverPlayer.sendMessage(Text.literal("No respawn point"), true);
            }
        }

        return stack;
    }

}
