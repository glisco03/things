package com.glisco.things.misc;

import com.glisco.things.Things;
import com.glisco.things.ThingsNetwork;
import com.glisco.things.items.ThingsItems;
import com.glisco.things.items.generic.DisplacementTomeItem;
import io.wispforest.owo.client.screens.ScreenUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class DisplacementTomeScreenHandler extends ScreenHandler {

    private ItemStack book;

    public DisplacementTomeScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ItemStack.EMPTY);
    }

    public DisplacementTomeScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack book) {
        super(Things.DISPLACEMENT_TOME_SCREEN_HANDLER, syncId);
        this.book = book;
    }

    @Override
    public void addListener(ScreenHandlerListener listener) {
        super.addListener(listener);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof DisplacementTomeItem || player.getStackInHand(Hand.OFF_HAND).getItem() instanceof DisplacementTomeItem;
    }

    public void setBook(ItemStack book) {
        this.book = book;
    }

    public void requestTeleport(String location) {
        if (this.player() instanceof ServerPlayerEntity serverPlayer) {
            int currentFuel = book.get(DisplacementTomeItem.FUEL);

            if (currentFuel < Things.CONFIG.displacementTomeFuelConsumption()) {
                serverPlayer.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1, 0);
                return;
            }

            var targets = book.get(DisplacementTomeItem.TARGETS);
            if (!targets.containsKey(location)) return;

            currentFuel -= Things.CONFIG.displacementTomeFuelConsumption();
            book.set(DisplacementTomeItem.FUEL, currentFuel);

            targets.get(location).teleportPlayer(serverPlayer);
            serverPlayer.getWorld().playSound(null, serverPlayer.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.MASTER, 1, 1);
            serverPlayer.closeHandledScreen();
        } else {
            ThingsNetwork.CHANNEL.clientHandle().send(ActionPacket.teleport(location));
        }
    }

    public void addPoint(String name) {
        if (this.player() instanceof ServerPlayerEntity player) {
            player.getInventory().getStack(player.getInventory().getSlotWithStack(new ItemStack(ThingsItems.DISPLACEMENT_PAGE))).decrement(1);
            sendContentUpdates();
            DisplacementTomeItem.storeTeleportTargetInBook(book,
                    DisplacementTomeItem.Target.fromPlayer(player), name, false);
            updateClient();
        } else {
            ThingsNetwork.CHANNEL.clientHandle().send(ActionPacket.create(name));
        }
    }

    public boolean deletePoint(String name) {
        if (this.player() instanceof ServerPlayerEntity) {
            boolean result = DisplacementTomeItem.deletePoint(book, name);
            updateClient();
            return result;
        } else {
            ThingsNetwork.CHANNEL.clientHandle().send(ActionPacket.delete(name));
            return true;
        }
    }

    public boolean renamePoint(String data) {
        if (this.player() instanceof ServerPlayerEntity) {
            boolean result = DisplacementTomeItem.rename(book, data);
            updateClient();
            return result;
        } else {
            ThingsNetwork.CHANNEL.clientHandle().send(ActionPacket.rename(data));
            return true;
        }
    }

    private void updateClient() {
        ThingsNetwork.CHANNEL.serverHandle(this.player()).send(new UpdateClientPacket(book));
    }

    public ItemStack getBook() {
        return book;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (!(player instanceof ServerPlayerEntity)) {
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1, 1);
        }
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return ScreenUtils.handleSlotTransfer(this, index, 0);
    }

    public record UpdateClientPacket(ItemStack tome) {}

    public record ActionPacket(Action action, String data) {
        public enum Action {TELEPORT, DELETE_POINT, RENAME_POINT, CREATE_POINT}

        public static ActionPacket teleport(String where) {
            return new ActionPacket(Action.TELEPORT, where);
        }

        public static ActionPacket create(String what) {
            return new ActionPacket(Action.CREATE_POINT, what);
        }

        public static ActionPacket rename(String which) {
            return new ActionPacket(Action.RENAME_POINT, which);
        }

        public static ActionPacket delete(String which) {
            return new ActionPacket(Action.DELETE_POINT, which);
        }
    }
}
