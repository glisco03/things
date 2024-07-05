package com.glisco.things.items.generic;

import com.glisco.things.Things;
import com.glisco.things.items.ThingsItems;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ExtractionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class BaterWucketItem extends BucketItem {

    public BaterWucketItem() {
        super(Fluids.WATER, new OwoItemSettings().group(Things.THINGS_GROUP).maxCount(1));
        FluidStorage.ITEM.registerForItems((stack, ctx) -> new Storage(), this);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    public static void registerCauldronBehavior() {
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(ThingsItems.BATER_WUCKET, (state, world, pos, player, hand, stack) -> {
            if (world.isClient) return ItemActionResult.SUCCESS;

            world.setBlockState(pos, Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3));
            player.incrementStat(Stats.FILL_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1f, 1f);
            world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);

            return ItemActionResult.CONSUME;
        });
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand).copy();
        var result = super.use(world, user, hand);
        return new TypedActionResult<>(
                result.getResult(),
                stack
        );
    }

    private static class Storage implements ExtractionOnlyStorage<FluidVariant>, SingleSlotStorage<FluidVariant> {
        private static final FluidVariant WATER = FluidVariant.of(Fluids.WATER);

        @Override
        public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            StoragePreconditions.notBlankNotNegative(resource, maxAmount);

            if (resource.equals(WATER)) {
                return Math.min(maxAmount, FluidConstants.BUCKET);
            }

            return 0;
        }

        @Override
        public boolean isResourceBlank() {
            return false;
        }

        @Override
        public FluidVariant getResource() {
            return WATER;
        }

        @Override
        public long getAmount() {
            return FluidConstants.BUCKET;
        }

        @Override
        public long getCapacity() {
            return FluidConstants.BUCKET;
        }
    }
}
