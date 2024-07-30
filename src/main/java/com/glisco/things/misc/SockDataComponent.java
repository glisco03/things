package com.glisco.things.misc;

import com.glisco.things.Things;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.Component;

public class SockDataComponent implements Component {

    private final PlayerEntity bearer;

    public boolean jumpySocksEquipped = false;

    private float speedModification = 0;
    private final Int2IntMap sockSpeeds = new Int2IntOpenHashMap();

    public SockDataComponent(PlayerEntity bearer) {
        this.bearer = bearer;
    }

    public void updateSockSpeed(int slot, int speed) {
        if (this.sockSpeeds.get(slot) == speed) return;

        this.modifySpeed(-Things.CONFIG.sockPerLevelSpeedAmplifier() * this.sockSpeeds.get(slot));

        this.sockSpeeds.put(slot, speed);
        this.modifySpeed(Things.CONFIG.sockPerLevelSpeedAmplifier() * speed);
    }

    public void modifySpeed(float amount) {
        if (amount == 0) return;

        float cleanWalkSpeed = bearer.getAbilities().getWalkSpeed() - speedModification;
        speedModification += amount;
        if (speedModification < 0) speedModification = 0;

        final var modifiedSpeed = cleanWalkSpeed + speedModification;

        bearer.getAbilities().setWalkSpeed(modifiedSpeed);
        bearer.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(modifiedSpeed);
        bearer.sendAbilitiesUpdate();
    }

    public void setModifier(float target) {
        modifySpeed(target - speedModification);
    }

    public void clearSockSpeed(int slot) {
        this.sockSpeeds.remove(slot);
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registries) {
        this.speedModification = tag.getFloat("SpeedModification");

        this.sockSpeeds.clear();
        tag.getList("SockSpeeds", NbtElement.COMPOUND_TYPE).forEach(element -> {
            var nbt = (NbtCompound) element;
            this.sockSpeeds.put(nbt.getInt("Slot"), nbt.getInt("Speed"));
        });
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registries) {
        tag.putFloat("SpeedModification", speedModification);

        var list = new NbtList();
        this.sockSpeeds.forEach((slot, speed) -> {
            var nbt = new NbtCompound();
            nbt.putInt("Slot", slot);
            nbt.putInt("Speed", speed);
            list.add(nbt);
        });
        tag.put("SockSpeeds", list);
    }
}
