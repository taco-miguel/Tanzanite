package dev.migueltaco.tanzanitemod.item.custom;

import dev.migueltaco.tanzanitemod.effect.ModEffects;
import dev.migueltaco.tanzanitemod.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StuffedPipe extends Item {
    public static final String USES_TAG = "TanzaniteUses";
    public static final int MAX_USES = 5;

    private static final int ADDED_DURATION = 20 * 15;
    private static final int USE_DURATION = 32;

    public StuffedPipe(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (getUsesLeft(stack) <= 0) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (level.isClientSide) {
            return stack;
        }

        applyPhantasmagoria(entity);
        playPipeEffects(level, entity);

        if (entity instanceof Player player && player.getAbilities().instabuild) {
            return stack;
        }

        int usesLeft = getUsesLeft(stack) - 1;

        if (usesLeft <= 0) {
            return createEmptyPipeCopy(stack);
        }

        stack.getOrCreateTag().putInt(USES_TAG, usesLeft);
        return stack;
    }

    private static void applyPhantasmagoria(LivingEntity entity) {
        MobEffectInstance current = entity.getEffect(ModEffects.PHANTASMAGORIA.get());

        int currentDuration = current == null ? 0 : current.getDuration();
        int newDuration = currentDuration + ADDED_DURATION;

        entity.addEffect(new MobEffectInstance(
                ModEffects.PHANTASMAGORIA.get(),
                newDuration,
                0,
                false,
                false,
                true
        ));
    }

    private static void playPipeEffects(Level level, LivingEntity entity) {
        level.playSound(
                null,
                entity.blockPosition(),
                SoundEvents.CAMPFIRE_CRACKLE,
                SoundSource.PLAYERS,
                0.8F,
                1.3F
        );

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    entity.getX(),
                    entity.getEyeY(),
                    entity.getZ(),
                    12,
                    0.25,
                    0.2,
                    0.25,
                    0.02
            );
        }
    }

    public static int getUsesLeft(ItemStack stack) {
        CompoundTag tag = stack.getTag();

        if (tag == null || !tag.contains(USES_TAG)) {
            return MAX_USES;
        }

        return tag.getInt(USES_TAG);
    }

    private static ItemStack createEmptyPipeCopy(ItemStack oldStack) {
        ItemStack emptyPipe = new ItemStack(ModItems.PIPE.get());

        if (oldStack.hasTag()) {
            CompoundTag copiedTag = oldStack.getTag().copy();
            copiedTag.remove(USES_TAG);
            emptyPipe.setTag(copiedTag);
        }

        return emptyPipe;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Tanzanite left: " + getUsesLeft(stack)).withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}