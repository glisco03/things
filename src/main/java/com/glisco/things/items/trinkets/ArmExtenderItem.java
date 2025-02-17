package com.glisco.things.items.trinkets;

import com.glisco.things.Things;
import com.glisco.things.items.TrinketItemWithOptionalTooltip;
import io.wispforest.accessories.api.components.AccessoriesDataComponents;
import io.wispforest.accessories.api.components.AccessoryItemAttributeModifiers;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

public class ArmExtenderItem extends TrinketItemWithOptionalTooltip {

    public ArmExtenderItem() {
        super(new OwoItemSettings().maxCount(1).group(Things.THINGS_GROUP)
                .component(
                        AccessoriesDataComponents.ATTRIBUTES,
                        AccessoryItemAttributeModifiers.builder()
                                .addForSlot(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE, new EntityAttributeModifier(Things.id("arm_extender"), 2d, EntityAttributeModifier.Operation.ADD_VALUE), "hand", false)
                                .addForSlot(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Things.id("arm_extender"), 2d, EntityAttributeModifier.Operation.ADD_VALUE), "hand", false)
                                .build())
        );
    }
}
