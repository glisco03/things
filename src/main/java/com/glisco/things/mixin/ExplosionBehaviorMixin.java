package com.glisco.things.mixin;

import com.glisco.things.Things;
import com.glisco.things.mixin.access.ContainerLockAccessor;
import com.glisco.things.mixin.access.LockableContainerBlockEntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ExplosionBehavior.class)
public class ExplosionBehaviorMixin {

    @Inject(method = "getBlastResistance", at = @At("HEAD"), cancellable = true)
    private void disallowBreakingLockedContainers(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState, CallbackInfoReturnable<Optional<Float>> cir) {
        if (!Things.CONFIG.makeLockedContainersUnbreakable()) return;

        if (!(world.getBlockEntity(pos) instanceof LockableContainerBlockEntityAccessor lockable) ||
                ((ContainerLockAccessor) (Object) lockable.things$getLock()).things$getKey().isEmpty()) return;

        cir.setReturnValue(Optional.of(3600000f));
    }

}
