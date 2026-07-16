package dev.migueltaco.tanzanitemod.event;

import dev.migueltaco.tanzanitemod.TanzaniteMod;
import dev.migueltaco.tanzanitemod.block.ModBlocks;
import dev.migueltaco.tanzanitemod.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import dev.migueltaco.tanzanitemod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Mod.EventBusSubscriber(
        modid = TanzaniteMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
) //jak sie kopie to typu efekt fantasmagorii
public class ModForgeEvents {
    private static final float PHANTASMAGORIA_CHANCE = 0.15F; // 15%
    private static final int ADDED_DURATION = 20 * 15; // 15 seconds
    private static final int MAX_DURATION = 20 * 60; // 60 seconds

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();

        if (player.level().isClientSide) return;

        BlockState state = event.getState();

        if (!state.is(ModBlocks.TANZANITE_ORE.get()) && !state.is(ModBlocks.DEESPLATE_TANZANITE_ORE.get())) return;

        if (player.getRandom().nextFloat() > PHANTASMAGORIA_CHANCE) return;

        MobEffectInstance current = player.getEffect(ModEffects.PHANTASMAGORIA.get());

        int currentDuration = current == null ? 0 : current.getDuration();
        int newDuration = Math.min(currentDuration + ADDED_DURATION, MAX_DURATION);

        player.addEffect(new MobEffectInstance(
                ModEffects.PHANTASMAGORIA.get(),
                newDuration,
                0,
                false,
                false,
                true
        ));
    }
    //czyszczenie dirty raw tanzanitu w raw tanzanit
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (!stack.is(ModItems.DIRTY_RAW_TANZANITE.get()))
            return;
        BlockState state = level.getBlockState(pos);

        if (!state.is(Blocks.WATER_CAULDRON))
            return;

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));

        if (level.isClientSide)
            return;
        stack.shrink(1);

        ItemStack cleaned = new ItemStack(ModItems.RAW_TANZANITE.get());

        if (!player.addItem(cleaned)) {
            player.drop(cleaned, false);
        }

        int cauldronLevel = state.getValue(LayeredCauldronBlock.LEVEL);

        if (cauldronLevel <= 1) {
            level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
        } else {
            level.setBlock(pos, state.setValue(LayeredCauldronBlock.LEVEL, cauldronLevel - 1), 3);
        }

        level.playSound(
                null,
                pos,
                SoundEvents.GENERIC_SPLASH,
                SoundSource.BLOCKS,
                0.8F,
                1.2F
        );

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.SPLASH,
                    pos.getX() + 0.5,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.5,
                    8,
                    0.25,
                    0.15,
                    0.25,
                    0.02
            );
        }
    }
}