package dev.migueltaco.tanzanitemod.block.custom;

import dev.migueltaco.tanzanitemod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrushingMortar extends Block {
    public static final BooleanProperty HAS_CRYSTAL = BooleanProperty.create("has_crystal");
    public static final IntegerProperty CRUSH_PROGRESS = IntegerProperty.create("crush_progress", 0, 3);

    private static final int MAX_HITS = 4;

    private static final VoxelShape EMPTY_SHAPE = Shapes.or(
            // bottom/base
            Block.box(5, 0, 5, 11, 1, 11),
            Block.box(6, 1, 6, 10, 2, 10),
            Block.box(5, 2, 5, 11, 3, 11),

            // north/front wall
            Block.box(6, 2, 4, 10, 4, 5),
            Block.box(6, 3, 3, 10, 8, 4),
            Block.box(4, 3, 4, 6, 8, 5),
            Block.box(10, 3, 4, 12, 8, 5),

            // south/back wall
            Block.box(6, 2, 11, 10, 4, 12),
            Block.box(6, 3, 12, 10, 8, 13),
            Block.box(4, 3, 11, 6, 8, 12),
            Block.box(10, 3, 11, 12, 8, 12),

            // west/left wall
            Block.box(4, 2, 6, 5, 4, 10),
            Block.box(3, 3, 6, 4, 8, 10),
            Block.box(4, 3, 5, 5, 8, 6),
            Block.box(4, 3, 10, 5, 8, 11),

            // east/right wall
            Block.box(11, 2, 6, 12, 4, 10),
            Block.box(12, 3, 6, 13, 8, 10),
            Block.box(11, 3, 5, 12, 8, 6),
            Block.box(11, 3, 10, 12, 8, 11),

            // small corner chunks
            Block.box(5, 3, 5, 6, 4, 6),
            Block.box(5, 3, 10, 6, 4, 11),
            Block.box(10, 3, 5, 11, 4, 6),
            Block.box(10, 3, 10, 11, 4, 11)
    );

    public CrushingMortar(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HAS_CRYSTAL, false)
                .setValue(CRUSH_PROGRESS, 0));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);

        boolean hasCrystal = state.getValue(HAS_CRYSTAL);
        int progress = state.getValue(CRUSH_PROGRESS);

        if (!hasCrystal && heldItem.is(ModItems.TANZANITE.get())) {
            if (level.isClientSide) return InteractionResult.SUCCESS;

            if (!player.getAbilities().instabuild) {
                heldItem.shrink(1);
            }

            level.setBlock(pos, state
                    .setValue(HAS_CRYSTAL, true)
                    .setValue(CRUSH_PROGRESS, 0), 3);

            level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.8F, 1.2F);

            return InteractionResult.CONSUME;
        }

        if (hasCrystal && heldItem.is(ModItems.PESTLE.get())) {
            if (level.isClientSide) return InteractionResult.SUCCESS;

            int newProgress = progress + 1;

            if (!player.getAbilities().instabuild) {
                heldItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }

            level.playSound(null, pos, SoundEvents.SPYGLASS_USE, SoundSource.BLOCKS, 0.7F, 1.4F);

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.CRIT,
                        pos.getX() + 0.5,
                        pos.getY() + 1.05,
                        pos.getZ() + 0.5,
                        8,
                        0.25,
                        0.08,
                        0.25,
                        0.05
                );
            }

            if (newProgress >= MAX_HITS) {
                level.setBlock(pos, state
                        .setValue(HAS_CRYSTAL, false)
                        .setValue(CRUSH_PROGRESS, 0), 3);

                popResource(level, pos.above(), new ItemStack(ModItems.TANZANITE_POWDER.get()));

                level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 0.9F, 1.1F);
            } else {
                level.setBlock(pos, state.setValue(CRUSH_PROGRESS, newProgress), 3);
            }

            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (!level.isClientSide && state.getValue(HAS_CRYSTAL)) {
                popResource(level, pos.above(), new ItemStack(ModItems.TANZANITE.get()));
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return EMPTY_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return EMPTY_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_CRYSTAL, CRUSH_PROGRESS);
    }
}