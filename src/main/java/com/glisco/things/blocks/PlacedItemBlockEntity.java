package com.glisco.things.blocks;

import io.wispforest.endec.Endec;
import io.wispforest.endec.impl.KeyedEndec;
import io.wispforest.owo.ops.WorldOps;
import io.wispforest.owo.serialization.endec.MinecraftEndecs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlacedItemBlockEntity extends BlockEntity {

    private static final KeyedEndec<ItemStack> ITEM_KEY = MinecraftEndecs.ITEM_STACK.keyed("item", ItemStack.EMPTY);
    private static final KeyedEndec<Integer> ROTATION_KEY = Endec.INT.keyed("rotation", 0);

    private @NotNull ItemStack item = ItemStack.EMPTY;
    private int rotation = 0;

    public PlacedItemBlockEntity(BlockPos pos, BlockState state) {
        super(ThingsBlocks.PLACED_ITEM_BLOCK_ENTITY, pos, state);
    }

    public void setItem(@NotNull ItemStack item) {
        this.item = item;
    }

    public @NotNull ItemStack getItem() {
        return item;
    }

    @Override
    public void writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(tag, registries);

        tag.put(ITEM_KEY, this.item);
        tag.put(ROTATION_KEY, rotation);
    }

    @Override
    public void readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(tag, registries);
        this.item = tag.get(ITEM_KEY);
        this.rotation = tag.get(ROTATION_KEY);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        WorldOps.updateIfOnServer(world, pos);
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        if (this.rotation > 7) this.rotation = 0;
        if (this.rotation < 0) this.rotation = 7;
        this.markDirty();
    }

    public void changeRotation(boolean direction) {
        setRotation(direction ? rotation + 1 : rotation - 1);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        var tag = new NbtCompound();
        this.writeNbt(tag, registries);
        return tag;
    }
}
