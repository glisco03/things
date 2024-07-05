package com.glisco.things.blocks;

import com.glisco.things.Things;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.List;

public class ThingsBlocks implements BlockRegistryContainer {

    public static final Block STONE_GLOWSTONE_FIXTURE = new GlowstoneFixtureBlock();
    public static final Block QUARTZ_GLOWSTONE_FIXTURE = new GlowstoneFixtureBlock();
    public static final Block DEEPSLATE_GLOWSTONE_FIXTURE = new GlowstoneFixtureBlock();

    public static final Block GLEAMING_ORE = new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), AbstractBlock.Settings.copy(Blocks.DIAMOND_ORE).luminance($ -> 5).requiresTool());
    public static final Block DEEPSLATE_GLEAMING_ORE = new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), AbstractBlock.Settings.copy(Blocks.DEEPSLATE_DIAMOND_ORE).luminance($ -> 5).requiresTool());

    public static final Block DIAMOND_PRESSURE_PLATE = new DiamondPressurePlateBlock();
    public static final BlockItem DIAMOND_PRESSURE_PLATE_ITEM = new BlockItem(ThingsBlocks.DIAMOND_PRESSURE_PLATE, new OwoItemSettings().group(Things.THINGS_GROUP)) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            super.appendTooltip(stack, context, tooltip, type);
            tooltip.add(Text.literal("Players only").formatted(Formatting.GRAY));
        }
    };

    @NoBlockItem
    public static final Block PLACED_ITEM = new PlacedItemBlock();
    public static final BlockEntityType<PlacedItemBlockEntity> PLACED_ITEM_BLOCK_ENTITY = BlockEntityType.Builder.create(PlacedItemBlockEntity::new, PLACED_ITEM).build();

    @Override
    public BlockItem createBlockItem(Block block, String identifier) {
        return block == DIAMOND_PRESSURE_PLATE ? DIAMOND_PRESSURE_PLATE_ITEM : new BlockItem(block, new OwoItemSettings().group(Things.THINGS_GROUP));
    }

    @Override
    public void afterFieldProcessing() {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Things.id("placed_item"), PLACED_ITEM_BLOCK_ENTITY);
    }
}
