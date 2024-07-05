package com.glisco.things.mixin;

import com.glisco.things.Things;
import com.glisco.things.mixin.access.ContainerLockAccessor;
import com.glisco.things.mixin.access.LockableContainerBlockEntityAccessor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {

    @Inject(method = "getHardness", at = @At("HEAD"), cancellable = true)
    private void disallowBreakingLockedContainers(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (!Things.CONFIG.makeLockedContainersUnbreakable()) return;

        if (!(world.getBlockEntity(pos) instanceof LockableContainerBlockEntityAccessor lockable) ||
                ((ContainerLockAccessor) (Object) lockable.things$getLock()).things$getKey().isEmpty()) return;

        cir.setReturnValue(-1f);
    }

}
