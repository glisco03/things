package com.glisco.things.items.generic;

import com.glisco.things.Things;
import com.mojang.serialization.Codec;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.particles.ClientParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ItemMagnetItem extends Item {

    private static final int USE_COST = 50;
    private static final int MAX_CHARGE = 200;

    public static final ComponentType<Integer> CHARGE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Things.id("item_magnet_charge"),
            ComponentType.<Integer>builder()
                    .codec(Codec.INT)
                    .packetCodec(PacketCodecs.VAR_INT)
                    .build()
    );

    public ItemMagnetItem() {
        super(new OwoItemSettings().group(Things.THINGS_GROUP).maxCount(1).component(CHARGE, MAX_CHARGE));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        if (stack.get(CHARGE) < USE_COST) return TypedActionResult.pass(stack);

        var teleportedItems = new HashSet<>();
        boolean blue = true;

        for (double i = 2; i < 10; i += 0.15) {
            var result = user.raycast(i, 0, false);

            if (world.isClient) {
                blue = !blue;
                var particle = new DustParticleEffect(new Vector3f(blue ? 0.5f : 1, 0, blue ? 1 : 0.5f), 1);
                world.addParticle(particle, result.getPos().x, result.getPos().y, result.getPos().z, 0, 0, 0);

                if (i > 9.5) {
                    displayTerminator(world, result.getPos(), 0.65);
                }
            }

            if (!result.getType().equals(HitResult.Type.MISS)) {

                if (world.isClient) {
                    HitResult terminatorPosition = user.raycast(i - 0.75, 0, false);
                    displayTerminator(world, terminatorPosition.getPos(), 0.25);
                }

                break;
            }

            double radius = 1.25 + (stack.get(CHARGE) / (double) MAX_CHARGE) * 2;
            Vec3d box1 = result.getPos().add(-radius, -radius, -radius);
            Vec3d box2 = result.getPos().add(radius, radius, radius);

            for (var item : world.getNonSpectatingEntities(ItemEntity.class, new Box(box1, box2))) {
                if (!teleportedItems.add(item)) continue;

                if (world.isClient) {
                    ClientParticles.setParticleCount(2);
                    ClientParticles.spawn(ParticleTypes.POOF, world, item.getPos().add(0, .35, 0), .1f);
                } else {
                    item.updatePosition(user.getX(), user.getY(), user.getZ());
                    item.setNoGravity(true);
                    item.setVelocity(Vec3d.ZERO);
                    item.setPickupDelay(0);
                }
            }
        }

        user.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.125f, 2);
        stack.set(CHARGE, stack.get(CHARGE) - USE_COST);

        return TypedActionResult.success(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable(this.getTranslationKey() + ".tooltip", stack.get(CHARGE)));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (stack.get(CHARGE) >= MAX_CHARGE) return;
        stack.apply(CHARGE, MAX_CHARGE, energy -> Math.min(energy + 1 + energy / 80, MAX_CHARGE));
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return stack.get(CHARGE) < MAX_CHARGE;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return (int) (13 * (stack.get(CHARGE) / (float) MAX_CHARGE));
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        float energy = stack.get(CHARGE) / (float) MAX_CHARGE;

        int r = (int) (100 + 155 * (1 - energy));
        int b = (int) (127 + 128 * energy);

        return r << 16 | b;
    }

    @Environment(EnvType.CLIENT)
    private static void displayTerminator(World world, Vec3d at, double spread) {
        ClientParticles.setParticleCount(5);
        ClientParticles.spawn(ParticleTypes.WITCH, world, at, spread);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return Objects.equals(oldStack.get(CHARGE), newStack.get(CHARGE));
    }
}
