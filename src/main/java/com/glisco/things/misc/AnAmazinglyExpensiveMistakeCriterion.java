package com.glisco.things.misc;

import com.mojang.serialization.Codec;
import io.wispforest.endec.SerializationAttributes;
import io.wispforest.endec.SerializationContext;
import io.wispforest.endec.impl.StructEndecBuilder;
import io.wispforest.owo.serialization.CodecUtils;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class AnAmazinglyExpensiveMistakeCriterion extends AbstractCriterion<AnAmazinglyExpensiveMistakeCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public static class Conditions implements AbstractCriterion.Conditions {

        public static final Codec<Conditions> CODEC = CodecUtils.toCodec(StructEndecBuilder.of(
                CodecUtils.toEndec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC).optionalFieldOf("player", c -> c.playerPredicate, (LootContextPredicate) null),
                Conditions::new
        ), SerializationContext.attributes(SerializationAttributes.HUMAN_READABLE));

        private final LootContextPredicate playerPredicate;

        public Conditions(LootContextPredicate playerPredicate) {
            this.playerPredicate = playerPredicate;
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.ofNullable(this.playerPredicate);
        }
    }
}
