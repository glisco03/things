package com.glisco.things.mixin;

import com.glisco.things.Things;
import com.glisco.things.items.ThingsItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Collections;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract Vec3d getPos();

    @Shadow
    public abstract boolean isRemoved();

    @Shadow
    public World world;

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    public abstract EntityType<?> getType();

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;lengthSquared()D", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    private void pistonCrushing(MovementType movementType, Vec3d movement, CallbackInfo ci, Vec3d vec3d) {
        if (!((Object) this instanceof ItemEntity itemEntity)) return;

        if (movementType != MovementType.PISTON) return;
        if (vec3d.lengthSquared() != 0) return;
        if (this.isRemoved()) return;

        final var thisItem = itemEntity.getStack().getItem();
        if (!Things.brokenWatchRecipe().contains(thisItem)) return;
        final var recipe = new ArrayList<>(Things.brokenWatchRecipe());

        recipe.remove(thisItem);
        int craftCount = itemEntity.getStack().getCount();

        final var items = this.world.getEntitiesByClass(ItemEntity.class, new Box(this.getBlockPos()), ItemEntity::isAlive);
        final var craftingParticipants = new ArrayList<>(Collections.singleton(itemEntity));

        for (var item : items) {
            final var scrutinee = item.getStack();
            if (recipe.contains(scrutinee.getItem())) {
                recipe.remove(scrutinee.getItem());

                craftCount = Math.min(scrutinee.getCount(), craftCount);
                craftingParticipants.add(item);
            }
        }

        if (recipe.isEmpty()) {
            for (var item : craftingParticipants) {
                final var stack = item.getStack();
                stack.decrement(craftCount);

                if (stack.isEmpty()) item.discard();
            }

            for (int i = 0; i < craftCount; i++) {
                this.world.spawnEntity(new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), ThingsItems.BROKEN_WATCH.getDefaultStack()));
            }
        }
    }

}
