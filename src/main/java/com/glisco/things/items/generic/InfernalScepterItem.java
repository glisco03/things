package com.glisco.things.items.generic;

import com.glisco.things.Things;
import com.glisco.things.items.ItemWithExtendableTooltip;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collections;

public class InfernalScepterItem extends ItemWithExtendableTooltip {

    public InfernalScepterItem() {
        super(new OwoItemSettings().group(Things.THINGS_GROUP).maxCount(1).maxDamage(Things.CONFIG.infernalScepterDurability()).fireproof());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.getInventory().containsAny(Collections.singleton(Items.FIRE_CHARGE)))
            return TypedActionResult.fail(user.getStackInHand(hand));
        user.setCurrentHand(hand);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) return;
        if (72000 - remainingUseTicks < 20) return;

        final var inventory = player.getInventory();
        if (!inventory.containsAny(Collections.singleton(Items.FIRE_CHARGE))) return;

        if (!world.isClient) {
            Vec3d vec3d = player.getRotationVec(0.0F);
            double vX = (player.getX() + vec3d.x * 4.0D) - player.getX();
            double vY = (player.getY() + vec3d.y * 4.0D) - player.getY();
            double vZ = (player.getZ() + vec3d.z * 4.0D) - player.getZ();

            FireballEntity fireball = new FireballEntity(world, user, new Vec3d(vX, vY, vZ), 3);
            fireball.updatePosition(player.getX() + vec3d.x * 2.0D, player.getEyeY() - 1, player.getZ() + vec3d.z * 2.0D);
            world.spawnEntity(fireball);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1, 1);

            stack.damage(1, user, LivingEntity.getSlotForHand(user.getActiveHand()));
        }

        for (int slot = 0; slot < inventory.size(); slot++) {
            final var ammoStack = inventory.getStack(slot);

            if (ammoStack.isOf(Items.FIRE_CHARGE)) {
                ammoStack.decrement(1);
                break;
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }
}
