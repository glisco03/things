package com.glisco.things;

import com.glisco.things.blocks.PlacedItemBlockEntity;
import com.glisco.things.blocks.ThingsBlocks;
import com.glisco.things.client.DisplacementTomeScreen;
import com.glisco.things.items.ThingsItems;
import com.glisco.things.items.trinkets.SocksItem;
import com.glisco.things.misc.DisplacementTomeScreenHandler;
import io.wispforest.owo.network.OwoNetChannel;
import io.wispforest.owo.ops.ItemOps;
import io.wispforest.owo.ops.WorldOps;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThingsNetwork {

    public static final Logger LOGGER = LogManager.getLogger("things-network");
    public static final OwoNetChannel CHANNEL = OwoNetChannel.create(Things.id("main"));

    private static final NamedScreenHandlerFactory ENDER_POUCH_FACTORY = new SimpleNamedScreenHandlerFactory((syncId, inv, player) ->
            GenericContainerScreenHandler.createGeneric9x3(syncId, inv, player.getEnderChestInventory()),
            Text.translatable("container.enderpouch"));

    public static void init() {
        CHANNEL.registerServerbound(OpenEnderChestPacket.class, (message, access) -> {
            final var player = access.player();

            if (!Things.hasTrinket(player, ThingsItems.ENDER_POUCH)) {
                LOGGER.warn("Received illegal openEChest packet");
                return;
            }

            player.openHandledScreen(ENDER_POUCH_FACTORY);
        });

        CHANNEL.registerServerbound(DisplacementTomeScreenHandler.ActionPacket.class, (message, access) -> {
            if (!(access.player().currentScreenHandler instanceof DisplacementTomeScreenHandler handler)) return;
            final var action = message.action();

            switch (action) {
                case TELEPORT -> handler.requestTeleport(message.data());
                case CREATE_POINT -> handler.addPoint(message.data());
                case DELETE_POINT -> {
                    if (!handler.deletePoint(message.data())) ThingsNetwork.LOGGER.warn("Received invalid DELETE_POINT request");
                }
                case RENAME_POINT -> {
                    if (!handler.renamePoint(message.data())) ThingsNetwork.LOGGER.warn("Received invalid RENAME_POINT request");
                }
            }
        });

        CHANNEL.registerClientbound(DisplacementTomeScreenHandler.UpdateClientPacket.class, (message, access) -> {
            if (!(access.runtime().currentScreen instanceof final DisplacementTomeScreen tomeScreen)) return;
            tomeScreen.getScreenHandler().setBook(message.tome());
            tomeScreen.clearAndInit();
        });

        CHANNEL.registerServerbound(PlaceItemPacket.class, (message, access) -> {
            final var target = message.target();
            final var pos = target.getBlockPos().offset(target.getSide());

            final var world = access.player().getWorld();
            if (!world.getBlockState(pos).isAir()) return;
            if (!world.canSetBlock(pos)) {
                LOGGER.warn("Received illegal place item packet");
                return;
            }

            final var stack = access.player().getStackInHand(Hand.MAIN_HAND);
            if (stack.isEmpty()) return;

            if (!world.getBlockState(target.getBlockPos()).isSideSolidFullSquare(world, target.getBlockPos(), target.getSide())) return;

            world.setBlockState(pos, ThingsBlocks.PLACED_ITEM.getDefaultState().with(Properties.FACING, target.getSide().getOpposite()));
            ((PlacedItemBlockEntity) world.getBlockEntity(pos)).setItem(ItemOps.singleCopy(stack));
            stack.decrement(1);
        });

        CHANNEL.registerServerbound(ToggleSocksJumpBoostPacket.class, (message, access) -> {
            var player = access.player();
            if (!Things.hasTrinket(player, ThingsItems.SOCKS)) return;

            var socks = Things.getTrinkets(player).getEquipped(ThingsItems.SOCKS).get(0).getRight();
            if (!socks.contains(SocksItem.JUMPY_AND_ENABLED)) return;

            socks.set(SocksItem.JUMPY_AND_ENABLED, !socks.get(SocksItem.JUMPY_AND_ENABLED));

            WorldOps.playSound(player.getWorld(), player.getPos(), SoundEvents.UI_TOAST_IN, SoundCategory.PLAYERS, 1, 2);
            Things.TOGGLE_JUMP_BOOST_PARTICLES.spawn(player.getWorld(), player.getPos());
        });

        // TODO more agglomeration networking
//        CHANNEL.registerServerbound(AgglomerationItem.ScrollHandStackTrinket.class, AgglomerationItem.ScrollHandStackTrinket::scrollItemStack);
//        CHANNEL.registerServerbound(AgglomerationItem.ScrollStackFromSlotTrinket.class, AgglomerationItem.ScrollStackFromSlotTrinket::scrollItemStack);
    }


    public record OpenEnderChestPacket() {}

    public record PlaceItemPacket(BlockHitResult target) {}

    public record ToggleSocksJumpBoostPacket() {}
}