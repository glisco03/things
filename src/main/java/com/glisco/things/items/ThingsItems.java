package com.glisco.things.items;

import com.glisco.things.Things;
import com.glisco.things.items.generic.*;
import com.glisco.things.items.trinkets.*;
import com.glisco.things.mixin.access.ItemAccessor;
import dev.emi.trinkets.api.TrinketsApi;
import io.wispforest.lavender.book.LavenderBookItem;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.ops.TextOps;
import io.wispforest.owo.registration.annotations.IterationIgnored;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import io.wispforest.owo.util.TagInjector;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;

@SuppressWarnings("unused")
public class ThingsItems implements ItemRegistryContainer {

    @IterationIgnored
    public static final Item THINGS_ALMANAC = LavenderBookItem.registerForBook(Things.id("almanac"), Things.id("things_almanac"), new OwoItemSettings().group(Things.THINGS_GROUP).maxCount(1));

    public static final Item RECALL_POTION = new RecallPotionItem();
    public static final Item CONTAINER_KEY = new ContainerKeyItem();
    public static final Item BATER_WUCKET = new BaterWucketItem();
    public static final Item BEMPTY_UCKET = new BemptyUcketItem();
    public static final Item ENDER_POUCH = new EnderPouchItem();
    public static final Item MONOCLE = new MonocleItem();
    public static final Item MOSS_NECKLACE = new MossNecklaceItem();
    public static final Item PLACEBO = new PlaceboItem();
    public static final Item DISPLACEMENT_TOME = new DisplacementTomeItem();
    public static final Item DISPLACEMENT_PAGE = new Item(new OwoItemSettings().group(Things.THINGS_GROUP).maxCount(8));
    public static final Item MINING_GLOVES = new MiningGlovesItem();
    public static final Item RIOT_GAUNTLET = new RiotGauntletItem();
    public static final Item INFERNAL_SCEPTER = new InfernalScepterItem();
    public static final Item HADES_CRYSTAL = new HadesCrystalItem();
    public static final Item ENCHANTED_WAX_GLAND = new EnchantedWaxGlandItem();
    public static final Item ITEM_MAGNET = new ItemMagnetItem();
    public static final Item RABBIT_FOOT_CHARM = new RabbitFootCharmItem();
    public static final Item LUCK_OF_THE_IRISH = new LuckOfTheIrishItem();
    public static final Item HARDENING_CATALYST = new HardeningCatalystItem();
    public static final Item SOCKS = new SocksItem();
    public static final Item ARM_EXTENDER = new ArmExtenderItem();
    public static final Item SHOCK_ABSORBER = new ShockAbsorberItem();
    public static final Item BROKEN_WATCH = new BrokenWatchItem();

    public static final Item EMPTY_AGGLOMERATION = new EmptyAgglomerationItem();
    public static final Item AGGLOMERATION = new AgglomerationItem();

    public static final Item GLEAMING_POWDER = new GleamingItem();
    public static final Item GLEAMING_COMPOUND = new GleamingItem();

    @Override
    public void afterFieldProcessing() {
        if (Things.CONFIG.appleTrinket()) {
            TrinketsApi.registerTrinket(Items.APPLE, new AppleTrinket());
            TagInjector.inject(Registries.ITEM, Identifier.of("trinkets", "head/face"), Items.APPLE);
        }

        BaterWucketItem.registerCauldronBehavior();
        BemptyUcketItem.registerCauldronBehavior();
        ((ItemAccessor) BATER_WUCKET).things$setRecipeRemainder(BATER_WUCKET);
        ((ItemAccessor) Items.POTION).things$setRecipeRemainder(Items.GLASS_BOTTLE);
    }

    private static final class GleamingItem extends Item {
        public GleamingItem() {
            super(new OwoItemSettings().group(Things.THINGS_GROUP).rarity(Rarity.UNCOMMON));
        }

        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(TextOps.translateWithColor("text.things.crafting_component", TextOps.color(Formatting.GRAY)));
        }
    }

    private static final class HardeningCatalystItem extends ItemWithExtendableTooltip {
        public HardeningCatalystItem() {
            super(new OwoItemSettings().group(Things.THINGS_GROUP).maxCount(1).rarity(Rarity.UNCOMMON).fireproof());
        }

        @Override
        public boolean hasGlint(ItemStack stack) {
            return true;
        }
    }

    private static final class EmptyAgglomerationItem extends ItemWithExtendableTooltip {
        public EmptyAgglomerationItem() {
            super(new OwoItemSettings().group(Things.THINGS_GROUP).maxCount(1).rarity(Rarity.UNCOMMON));
        }
    }
}
