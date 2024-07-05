package com.glisco.things.client;

import com.glisco.things.Things;
import com.glisco.things.ThingsNetwork;
import com.glisco.things.blocks.ThingsBlocks;
import com.glisco.things.items.ThingsItems;
import com.glisco.things.items.generic.DisplacementTomeItem;
import com.glisco.things.items.trinkets.AgglomerationItem;
import com.glisco.things.items.trinkets.AppleTrinket;
import com.glisco.things.items.trinkets.SocksItem;
import com.glisco.things.mixin.client.access.CreativeSlotAccessor;
import com.glisco.things.mixin.client.access.HandledScreenAccessor;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ThingsClient implements ClientModInitializer {

    public static final String THINGS_CATEGORY = "category." + Things.MOD_ID + "." + Things.MOD_ID;

    public static final KeyBinding PLACE_ITEM =
            KeyBindingHelper.registerKeyBinding(new KeyBinding(keybindId("place_item"), GLFW.GLFW_KEY_J, THINGS_CATEGORY));

    public static final KeyBinding OPEN_ENDER_CHEST =
            KeyBindingHelper.registerKeyBinding(new KeyBinding(keybindId("openenderchest"), GLFW.GLFW_KEY_G, THINGS_CATEGORY));

    public static final KeyBinding TOGGLE_SOCKS_JUMP_BOOST =
            KeyBindingHelper.registerKeyBinding(new KeyBinding(keybindId("toggle_socks_jump_boost"), GLFW.GLFW_KEY_CAPS_LOCK, THINGS_CATEGORY));

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ThingsBlocks.PLACED_ITEM_BLOCK_ENTITY, PlacedItemBlockEntityRenderer::new);

        HandledScreens.register(Things.DISPLACEMENT_TOME_SCREEN_HANDLER, DisplacementTomeScreen::new);

        ModelPredicateProviderRegistry.register(ThingsItems.DISPLACEMENT_TOME, Identifier.of("pages"), new DisplacementTomeItem.PredicateProvider());
        ModelPredicateProviderRegistry.register(ThingsItems.SOCKS, Identifier.of("jumpy"), (stack, world, entity, seed) -> stack.contains(SocksItem.JUMPY_AND_ENABLED) ? 1 : 0);

        TrinketRendererRegistry.registerRenderer(Items.APPLE, new AppleTrinket.Renderer());

        registerRenderedTrinket(ThingsItems.ENCHANTED_WAX_GLAND);
        registerRenderedTrinket(ThingsItems.ENDER_POUCH);
        registerRenderedTrinket(ThingsItems.HADES_CRYSTAL);
        registerRenderedTrinket(ThingsItems.LUCK_OF_THE_IRISH);
        registerRenderedTrinket(ThingsItems.MONOCLE);
        registerRenderedTrinket(ThingsItems.MOSS_NECKLACE);
        // TODO agglomeration rendering
//        registerRenderedTrinket(ThingsItems.AGGLOMERATION);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (PLACE_ITEM.wasPressed()) {
                if (!(client.crosshairTarget instanceof BlockHitResult blockResult)) return;
                ThingsNetwork.CHANNEL.clientHandle().send(new ThingsNetwork.PlaceItemPacket(blockResult));
            }

            while (OPEN_ENDER_CHEST.wasPressed()) {
                if (!Things.hasTrinket(client.player, ThingsItems.ENDER_POUCH)) return;
                ThingsNetwork.CHANNEL.clientHandle().send(new ThingsNetwork.OpenEnderChestPacket());
            }

            while (TOGGLE_SOCKS_JUMP_BOOST.wasPressed()) {
                if (!Things.hasTrinket(client.player, ThingsItems.SOCKS)) return;
                ThingsNetwork.CHANNEL.clientHandle().send(new ThingsNetwork.ToggleSocksJumpBoostPacket());
            }
        });

        // TODO agglomeration networking
//        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
//            if (!(screen instanceof HandledScreen) || !Things.CONFIG.enableAgglomerationInvScrollSelection()) return;
//
//            ScreenMouseEvents.allowMouseScroll(screen).register((screen1, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
//                var slot = ((HandledScreenAccessor) screen1).thing$getSlotAt(mouseX, mouseY);
//
//                if (slot == null) return true;
//
//                var slotStack = slot.getStack();
//                int slotId = slot.id;
//
//                //This is required due to Screen Handler Mismatch for hotbar items with a given Itemgroup open in Creative Mode
//                boolean fromPlayerInv = screen1 instanceof CreativeInventoryScreen && slot.inventory instanceof PlayerInventory && slot.getIndex() < 9;
//
//                if (slot instanceof CreativeSlotAccessor creativeSlot) {
//                    slotId = creativeSlot.things$getSlot().id;
//                }
//
//                if (slotStack.getItem() instanceof AgglomerationItem && slotStack.has(AgglomerationItem.ITEMS_KEY)) {
//                    ThingsNetwork.CHANNEL.clientHandle().send(new AgglomerationItem.ScrollStackFromSlotTrinket(fromPlayerInv, fromPlayerInv ? slot.getIndex() : slotId));
//
//                    return false;
//                }
//
//                return true;
//            });
//        });
    }

    private static String keybindId(String name) {
        return "key." + Things.MOD_ID + "." + name;
    }

    private void registerRenderedTrinket(Item trinket) {
        TrinketRendererRegistry.registerRenderer(trinket, (TrinketRenderer) trinket);
    }
}
