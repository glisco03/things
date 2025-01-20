package com.glisco.things.items.generic;

import com.glisco.things.Things;
import com.glisco.things.items.ThingsItems;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ExtractionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.world.event.GameEvent;

public class BemptyUcketItem extends BucketItem {

    public BemptyUcketItem() {
        super(Fluids.EMPTY, new OwoItemSettings().group(Things.THINGS_GROUP).maxCount(1));
        FluidStorage.ITEM.registerForItems((stack, ctx) -> new Storage(), this);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    public static void registerCauldronBehavior() {
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map().put(ThingsItems.BEMPTY_UCKET, (state, world, pos, player, hand, stack) -> {
            return CauldronBehavior.emptyCauldron(state, world, pos, player, hand, stack, stack.copy(), blockState -> blockState.get(LeveledCauldronBlock.LEVEL) == 3, SoundEvents.ITEM_BUCKET_FILL);
        });

        CauldronBehavior.LAVA_CAULDRON_BEHAVIOR.map().put(ThingsItems.BEMPTY_UCKET, (state, world, pos, player, hand, stack) -> {
            return CauldronBehavior.emptyCauldron(state, world, pos, player, hand, stack, stack.copy(), blockState -> true, SoundEvents.ITEM_BUCKET_FILL_LAVA);
        });
    }

    private static class Storage implements net.fabricmc.fabric.api.transfer.v1.storage.Storage<FluidVariant>, SingleSlotStorage<FluidVariant> {

        @Override
        public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            return 0;
        }

        @Override
        public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            return 0;
        }

        @Override
        public boolean isResourceBlank() {
            return true;
        }

        @Override
        public FluidVariant getResource() {
            return FluidVariant.blank();
        }

        @Override
        public long getAmount() {
            return 0;
        }

        @Override
        public long getCapacity() {
            return FluidConstants.BUCKET;
        }
    }
}
