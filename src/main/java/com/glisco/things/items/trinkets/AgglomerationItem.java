package com.glisco.things.items.trinkets;

import dev.emi.trinkets.api.TrinketItem;
import io.wispforest.endec.Endec;
import io.wispforest.endec.impl.KeyedEndec;
import io.wispforest.owo.serialization.endec.MinecraftEndecs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;

// TODO agglomeration
public class AgglomerationItem extends TrinketItem /*implements TrinketRenderer*/ {

    public static final KeyedEndec<Byte> SELECTED_TRINKET_KEY = Endec.BYTE.keyed("SelectedTrinket", (byte) 0);
    public static final KeyedEndec<List<ItemStack>> ITEMS_KEY = MinecraftEndecs.ITEM_STACK.listOf().keyed("Items", ArrayList::new);

//    private final static LoadingCache<ItemStack, StackData> CACHE = CacheBuilder.newBuilder()
//            .concurrencyLevel(1)
//            .maximumSize(200)
//            .weakKeys()
//            .build(CacheLoader.from(StackData::new));

    public AgglomerationItem() {
        super(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));
    }

    //--------

//    @Override
//    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
//        return getStackAndRun(stack, player, innerStack -> {
//            return innerStack.onClicked(ItemStack.EMPTY, slot, clickType, player, cursorStackReference);
//        }, () -> false);
//    }
//
//    @Override
//    public ActionResult useOnBlock(ItemUsageContext context) {
//        return getStackAndRun(context.getStack(), context.getPlayer(), innerStack -> {
//            return innerStack.useOnBlock(new ItemUsageContext(context.getWorld(), context.getPlayer(), context.getHand(), innerStack, ((ItemUsageContextAccessor)context).things$getHitResult()));
//        }, () -> ActionResult.FAIL);
//    }
//
//    @Override
//    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
//        return getStackAndRun(stack, user instanceof PlayerEntity player ? player : null, innerStack -> {
//            return innerStack.finishUsing(world, user);
//        }, () -> stack);
//    }
//
//    @Override
//    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
//        return getStackAndRun(stack, player, innerStack -> innerStack.onStackClicked(slot, clickType, player), () -> false);
//    }
//
//    @Override
//    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
//        return getStackAndRun(stack, attacker instanceof PlayerEntity player ? player : null, innerStack -> {
//            innerStack.postHit(target, ((PlayerEntity) attacker));
//
//            return true;
//        }, () -> false);
//    }
//
//    @Override
//    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
//        return getStackAndRun(stack, miner instanceof PlayerEntity player ? player : null, innerStack -> {
//            innerStack.postMine(world, state, pos, ((PlayerEntity) miner));
//
//            return true;
//        }, () -> false);
//    }
//
//    @Override
//    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
//        return getStackAndRun(stack, user, innerStack -> innerStack.useOnEntity(user, entity, hand), () -> ActionResult.FAIL);
//    }
//
//    @Override
//    public boolean isUsedOnRelease(ItemStack stack) {
//        return getStackAndRun(stack, null, ItemStack::isUsedOnRelease, () -> false);
//    }
//
//    //--------
//
//    public <T> T getStackAndRun(ItemStack stack, PlayerEntity player, Function<ItemStack, T> methodPassthru, Supplier<T> error){
//        var data = getDataFor(stack);
//
//        var selectedTrinket = stack.get(SELECTED_TRINKET_KEY);
//
//        if(selectedTrinket >= data.subStacks.size()) return error.get();
//
//        T value = methodPassthru.apply(data.subStacks.get(selectedTrinket));
//        data.updateStackIfNeeded(selectedTrinket, player);
//
//        return value;
//    }
//
//    public static void scrollSelectedStack(ItemStack stack){
//        stack.put(SELECTED_TRINKET_KEY, (byte) (stack.has(SELECTED_TRINKET_KEY) && stack.get(SELECTED_TRINKET_KEY) == 0 ? 1 : 0));
//    }
//
//    private static StackData getDataFor(ItemStack stack) {
//        StackData data = CACHE.getUnchecked(stack);
//
//        if (data.isInvalid()) {
//            CACHE.refresh(stack);
//            data = CACHE.getUnchecked(stack);
//        }
//
//        return data;
//    }
//
//    public static ItemStack createStack(ItemStack... items) {
//        var stack = new ItemStack(ThingsItems.AGGLOMERATION, 1);
//
//        stack.put(ITEMS_KEY, Arrays.asList(items));
//        stack.put(SELECTED_TRINKET_KEY, (byte) 0);
//
//        return stack;
//    }
//
//    public static List<ItemStack> getStacks(ItemStack stack) {
//        return getDataFor(stack).subStacks;
//    }
//
//    public static boolean hasStack(ItemStack stack, Predicate<ItemStack> predicate) {
//        var data = getDataFor(stack);
//
//        for (int i = 0; i < data.subStacks.size(); i++) {
//            var subStack = data.subStacks.get(i);
//
//            if (predicate.test(subStack)) {
//                return true;
//            }
//
//            if (subStack.isOf(ThingsItems.AGGLOMERATION) && hasStack(subStack, predicate)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        var data = getDataFor(user.getStackInHand(hand));
//        for (var stack : data.subStacks) {
//            if (stack.isEmpty()) {
//                var cake = new ItemStack(Items.CAKE);
//                cake.set(DataComponentTypes.ITEM_NAME, Text.translatable("item.things.consolation_cake"));
//
//                user.getInventory().offerOrDrop(cake);
//                return TypedActionResult.success(ItemStack.EMPTY);
//            }
//        }
//
//        return super.use(world, user, hand);
//    }
//
//    @Override
//    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
//        var data = getDataFor(stack);
//
//        for (int i = 0; i < data.subStacks.size(); i++) {
//            ItemStack subStack = data.subStacks.get(i);
//
//            TrinketsApi.getTrinket(subStack.getItem()).onEquip(subStack, slot, entity);
//
//            data.updateStackIfNeeded(i, entity);
//        }
//    }
//
//    @Override
//    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
//        var data = getDataFor(stack);
//
//        for (int i = 0; i < data.subStacks.size(); i++) {
//            ItemStack subStack = data.subStacks.get(i);
//
//            TrinketsApi.getTrinket(subStack.getItem()).onUnequip(subStack, slot, entity);
//
//            data.updateStackIfNeeded(i, entity);
//        }
//    }
//
//    @Override
//    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
//        super.tick(stack, slot, entity);
//
//        var data = getDataFor(stack);
//
//        for (int i = 0; i < data.subStacks.size(); i++) {
//            ItemStack subStack = data.subStacks.get(i);
//
//            TrinketsApi.getTrinket(subStack.getItem()).tick(subStack, slot, entity);
//
//            data.updateStackIfNeeded(i, entity);
//        }
//    }
//
//    @Override
//    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
//        var data = getDataFor(stack);
//
//        for (int i = 0; i < data.subStacks.size(); i++) {
//            ItemStack subStack = data.subStacks.get(i);
//
//            if (!TrinketsApi.evaluatePredicateSet(slot.inventory().getSlotType().getValidatorPredicates(), subStack, slot, entity)) {
//                return false;
//            }
//
//            if (!TrinketsApi.getTrinket(subStack.getItem()).canEquip(subStack, slot, entity)) {
//                return false;
//            }
//        }
//
//        return super.canEquip(stack, slot, entity);
//    }
//
//    @Override
//    public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
//        var data = getDataFor(stack);
//
//        for (int i = 0; i < data.subStacks.size(); i++) {
//            ItemStack subStack = data.subStacks.get(i);
//
//            if (!TrinketsApi.getTrinket(subStack.getItem()).canUnequip(subStack, slot, entity)) {
//                return false;
//            }
//        }
//
//        return super.canUnequip(stack, slot, entity);
//    }
//
//    @Override
//    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
//        var modifiers = super.getModifiers(stack, slot, entity, uuid);
//
//        var data = getDataFor(stack);
//
//        for (int i = 0; i < data.subStacks.size(); i++) {
//            ItemStack subStack = data.subStacks.get(i);
//
//            modifiers.putAll(TrinketsApi.getTrinket(subStack.getItem()).getModifiers(subStack, slot, entity, uuid));
//        }
//
//        return modifiers;
//    }
//
//    @Environment(EnvType.CLIENT)
//    @Override
//    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
//        if (!Things.CONFIG.renderAgglomerationTrinket()) return;
//
//        var data = getDataFor(stack);
//
//        for (int i = 0; i < data.subStacks.size(); i++) {
//            ItemStack subStack = data.subStacks.get(i);
//
//            var renderer = TrinketRendererRegistry.getRenderer(subStack.getItem()).orElse(null);
//
//            if (renderer != null) {
//                matrices.push();
//                renderer.render(subStack, slotReference, contextModel, matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
//                matrices.pop();
//            }
//        }
//    }
//
//    @Override
//    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
//        var data = getDataFor(stack);
//
//        for (int i = 0; i < data.subStacks.size(); i++) {
//            var subTooltip = data.subStacks.get(i).getTooltip(null, context);
//
//            for (int j = 0; j < subTooltip.size(); j++) {
//                if (j == 0) {
//                    tooltip.add(Text.literal(stack.hasNbt() && stack.get(SELECTED_TRINKET_KEY) == i ? "> " : "• ").append(subTooltip.get(j)));
//                } else {
//                    tooltip.add(Text.literal("  ").append(subTooltip.get(j)));
//                }
//            }
//        }
//
//        for (var subStack : data.subStacks) {
//            if (!subStack.isEmpty()) continue;
//            tooltip.add(Text.empty());
//            tooltip.add(Text.translatable("item.things.consolation_cake.hint"));
//        }
//    }
//
//    private static class StackData {
//        private final ItemStack stack;
//        private final List<ItemStack> subStacks = new ArrayList<>();
//        private final List<ItemStack> defensiveCopies = new ArrayList<>();
//        private NbtCompound defensiveNbtData;
//
//        public StackData(ItemStack stack) {
//            this.stack = stack;
//
//            if (stack.hasNbt()) {
//                this.defensiveNbtData = stack.getNbt().copy();
//
//                var items = stack.get(ITEMS_KEY);
//                for (var item : items) {
//                    this.subStacks.add(item);
//                    this.defensiveCopies.add(item.copy());
//                }
//            }
//        }
//
//        public boolean isInvalid() {
//            return !Objects.equals(stack.getNbt(), defensiveNbtData);
//        }
//
//        public void updateStackIfNeeded(int idx, LivingEntity entity) {
//            if (ItemStack.areEqual(subStacks.get(idx), defensiveCopies.get(idx))) return;
//            if (subStacks.get(idx).isOf(Items.AIR) && entity instanceof ServerPlayerEntity player) {
//                Things.AN_AMAZINGLY_EXPENSIVE_MISTAKE_CRITERION.trigger(player);
//            }
//
//            defensiveCopies.set(idx, subStacks.get(idx).copy());
//
//            var itemsTag = stack.get(ITEMS_KEY);
//            itemsTag.set(idx, subStacks.get(idx));
//
//            defensiveNbtData = stack.getNbt().copy();
//        }
//    }
//
//    public static record ScrollHandStackTrinket(boolean mainHandStack){
//
//        public static void scrollItemStack(ScrollHandStackTrinket message, ServerAccess access){
//            var stack = message.mainHandStack ? access.player().getMainHandStack() : access.player().getOffHandStack();
//
//            AgglomerationItem.scrollSelectedStack(stack);
//
//            var data = AgglomerationItem.getDataFor(stack);
//
//            access.player().sendMessageToClient(Text.literal("> ")
//                    .append(Text.translatable(data.subStacks.get(stack.get(SELECTED_TRINKET_KEY)).getTranslationKey())), true);
//        }
//    }
//
//    public static record ScrollStackFromSlotTrinket(boolean fromPlayerInv, int slotId){
//
//        public static void scrollItemStack(ScrollStackFromSlotTrinket message, ServerAccess access){
//            var stack = message.fromPlayerInv
//                    ? access.player().getInventory().getStack(message.slotId)
//                    : access.player().currentScreenHandler.getSlot(message.slotId).getStack();
//
//            if(stack == null) return;
//
//            AgglomerationItem.scrollSelectedStack(stack);
//        }
//    }
}
