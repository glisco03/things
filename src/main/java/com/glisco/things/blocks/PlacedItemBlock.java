package com.glisco.things.blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class PlacedItemBlock extends BlockWithEntity {

    public static DirectionProperty FACING = Properties.FACING;

    private final VoxelShape OUTLINE_SHAPE_DOWN = Block.createCuboidShape(5, 0, 5, 11, 1, 11);
    private final VoxelShape OUTLINE_SHAPE_UP = Block.createCuboidShape(5, 15, 5, 11, 16, 11);
    private final VoxelShape OUTLINE_SHAPE_NORTH = Block.createCuboidShape(5, 5, 0, 11, 11, 1);
    private final VoxelShape OUTLINE_SHAPE_SOUTH = Block.createCuboidShape(5, 5, 15, 11, 11, 16);
    private final VoxelShape OUTLINE_SHAPE_EAST = Block.createCuboidShape(15, 5, 5, 16, 11, 11);
    private final VoxelShape OUTLINE_SHAPE_WEST = Block.createCuboidShape(0, 5, 5, 1, 11, 11);
    private final VoxelShape EMPTY_SHAPE = Block.createCuboidShape(0, 0, 0, 0, 0, 0);

    public PlacedItemBlock() {
        super(FabricBlockSettings.create().nonOpaque().hardness(-1).sounds(BlockSoundGroup.METAL));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlacedItemBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(FACING)) {
            case UP:
                return OUTLINE_SHAPE_UP;
            case DOWN:
                return OUTLINE_SHAPE_DOWN;
            case EAST:
                return OUTLINE_SHAPE_EAST;
            case WEST:
                return OUTLINE_SHAPE_WEST;
            case NORTH:
                return OUTLINE_SHAPE_NORTH;
            case SOUTH:
                return OUTLINE_SHAPE_SOUTH;
        }
        return EMPTY_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return EMPTY_SHAPE;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        PlacedItemBlockEntity entity = (PlacedItemBlockEntity) world.getBlockEntity(pos);
        entity.changeRotation(!player.isSneaking());
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        player.getInventory().offerOrDrop(((PlacedItemBlockEntity) world.getBlockEntity(pos)).getItem());
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return direction == state.get(FACING) && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite());
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), ((PlacedItemBlockEntity) world.getBlockEntity(pos)).getItem());
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof PlacedItemBlockEntity placedItem) {
            return placedItem.getItem().copy();
        } else {
            return super.getPickStack(world, pos, state);
        }
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }
}
