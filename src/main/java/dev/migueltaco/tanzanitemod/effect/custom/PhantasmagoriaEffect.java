package dev.migueltaco.tanzanitemod.effect.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class PhantasmagoriaEffect extends MobEffect {
    public PhantasmagoriaEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity.level() instanceof ServerLevel level)) return;

        level.sendParticles(
                ParticleTypes.WITCH,
                entity.getX(),
                entity.getY() + 1.0,
                entity.getZ(),
                3,
                0.35,
                0.45,
                0.35,
                0.01

        );
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 5 == 0;
    }
}
