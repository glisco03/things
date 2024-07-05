package com.glisco.things.mixin;

import com.glisco.things.Things;
import com.glisco.things.items.ThingsItems;
import com.glisco.things.mixin.access.ForgingScreenHandlerAccessor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {

    @Shadow
    @Final
    private Property levelCost;

    @Shadow
    private String newItemName;

    @Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
    public void outputCheckOverride(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> cir) {
        ForgingScreenHandlerAccessor handler = (ForgingScreenHandlerAccessor) this;

        if (!handler.things$getInput().getStack(1).getItem().equals(ThingsItems.HARDENING_CATALYST)) return;

        cir.setReturnValue(levelCost.get() <= player.experienceLevel);
        cir.cancel();
    }

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    public void setOutput(CallbackInfo ci) {
        ForgingScreenHandlerAccessor forgingHandler = (ForgingScreenHandlerAccessor) this;

        final var inputInventory = forgingHandler.things$getInput();
        if (!inputInventory.getStack(1).getItem().equals(ThingsItems.HARDENING_CATALYST)) return;

        final var baseStack = inputInventory.getStack(0);

        if (!baseStack.getItem().getComponents().contains(DataComponentTypes.MAX_DAMAGE) || baseStack.isIn(Things.HARDENING_CATALYST_BLACKLIST)) return;
        if (baseStack.contains(DataComponentTypes.UNBREAKABLE)) return;

        ItemStack newOutput = baseStack.copy();
        newOutput.set(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true));

        if (!StringUtils.isBlank(newItemName)) {
            newOutput.set(DataComponentTypes.CUSTOM_NAME, Text.literal(newItemName));
        } else {
            newOutput.remove(DataComponentTypes.CUSTOM_DATA);
        }

        forgingHandler.things$getOutput().setStack(0, newOutput);
        levelCost.set(30);

        ci.cancel();
    }
}
