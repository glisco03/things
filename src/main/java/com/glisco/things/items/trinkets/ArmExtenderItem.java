package com.glisco.things.items.trinkets;

import com.glisco.things.Things;
import com.glisco.things.items.TrinketItemWithOptionalTooltip;
import dev.emi.trinkets.api.TrinketsAttributeModifiersComponent;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

import java.util.Optional;

public class ArmExtenderItem extends TrinketItemWithOptionalTooltip {

    public ArmExtenderItem() {
        super(new OwoItemSettings().maxCount(1).group(Things.THINGS_GROUP)
                .component(
                        TrinketsAttributeModifiersComponent.TYPE,
                        TrinketsAttributeModifiersComponent.builder()
                                .add(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE, new EntityAttributeModifier(Things.id("arm_extender"), 2d, EntityAttributeModifier.Operation.ADD_VALUE), Optional.of("hand/glove"))
                                .add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Things.id("arm_extender"), 2d, EntityAttributeModifier.Operation.ADD_VALUE), Optional.of("hand/glove"))
                                .build())
        );
    }
}
