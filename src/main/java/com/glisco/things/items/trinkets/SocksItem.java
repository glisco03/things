package com.glisco.things.items.trinkets;

import com.glisco.things.Things;
import com.glisco.things.client.ThingsClient;
import com.glisco.things.items.ThingsItems;
import com.glisco.things.items.TrinketItemWithOptionalTooltip;
import com.mojang.serialization.Codec;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.slot.SlotReference;
import io.wispforest.accessories.api.slot.SlotType;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.ops.TextOps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class SocksItem extends TrinketItemWithOptionalTooltip {

    public static final ComponentType<Integer> SPEED = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Things.id("socks_speed"),
            ComponentType.<Integer>builder()
                    .codec(Codec.INT)
                    .packetCodec(PacketCodecs.VAR_INT)
                    .build()
    );

    public static final ComponentType<Boolean> JUMPY_AND_ENABLED = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Things.id("jumpy_and_enabled"),
            ComponentType.<Boolean>builder()
                    .codec(Codec.BOOL)
                    .packetCodec(PacketCodecs.BOOL)
                    .build()
    );

    public SocksItem() {
        super(new OwoItemSettings().maxCount(1).group(Things.THINGS_GROUP).component(SPEED, 0));
    }

    public static ItemStack create(int speed, boolean jumpy) {
        var stack = new ItemStack(ThingsItems.SOCKS);
        stack.set(SPEED, speed);
        if (jumpy) stack.set(JUMPY_AND_ENABLED, true);
        return stack;
    }

    @Override
    public void getDynamicModifiers(ItemStack stack, SlotReference reference, AccessoryAttributeBuilder builder) {
        if(!stack.contains(JUMPY_AND_ENABLED)) return;

        builder.addExclusive(EntityAttributes.GENERIC_STEP_HEIGHT, new EntityAttributeModifier(Things.id("socks.step_height"), .45, EntityAttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        if (!(reference.entity() instanceof PlayerEntity player)) return;

        final var sockData = Things.SOCK_DATA.get(player);

        sockData.jumpySocksEquipped = stack.contains(JUMPY_AND_ENABLED);
        if (player.getWorld().isClient) return;

        sockData.updateSockSpeed(reference.slot(), stack.get(SPEED) + 1);

        if (!sockData.jumpySocksEquipped || !stack.getOrDefault(JUMPY_AND_ENABLED, true)) return;
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 5, 1, true, false, true));
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference reference) {
        var entity = reference.entity();

        Things.SOCK_DATA.get(entity).jumpySocksEquipped = false;

        if (!(entity instanceof ServerPlayerEntity player)) return;
        int speed = stack.get(SPEED);

        Things.SOCK_DATA.get(player).modifySpeed(-Things.CONFIG.sockPerLevelSpeedAmplifier() * (speed + 1));
        Things.SOCK_DATA.get(player).clearSockSpeed(reference.slot());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void append(List<Text> tooltip) {
        this.appendWrapped(tooltip, Text.translatable(this.tooltipTranslationKey(), KeyBindingHelper.getBoundKeyOf(ThingsClient.TOGGLE_SOCKS_JUMP_BOOST).getLocalizedText()));
    }

    @Override
    public void getExtraTooltip(ItemStack stack, List<Text> tooltips, TooltipContext tooltipContext, TooltipType tooltipType) {
        var extraTooltips = new ArrayList<Text>();

        tryAppend(extraTooltips);

        if (stack.contains(JUMPY_AND_ENABLED)) {
            extraTooltips.add(TextOps.withColor("↑ ", !stack.get(JUMPY_AND_ENABLED) ? TextOps.color(Formatting.GRAY) : 0x34d49c)
                    .append(TextOps.translateWithColor("item.things.socks.jumpy", TextOps.color(Formatting.GRAY))));
        }

        int speed = stack.get(SPEED);
        if (speed < 3) {
            extraTooltips.add(TextOps.withColor("☄ ", 0x34b1d4)
                    .append(TextOps.translateWithColor("item.things.socks.speed_" + speed, TextOps.color(Formatting.GRAY))));
        } else {
            extraTooltips.add(TextOps.withColor("☄ ", 0x34b1d4)
                    .append(TextOps.translateWithColor("item.things.socks.speed_illegal", TextOps.color(Formatting.RED)))
                    .append(TextOps.withColor(" (" + speed + ")", TextOps.color(Formatting.RED))));
        }

        extraTooltips.add(Text.literal(" "));

        tooltips.addAll(0, extraTooltips);
    }
}
