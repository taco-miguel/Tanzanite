package dev.migueltaco.tanzanitemod.item;

import dev.migueltaco.tanzanitemod.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties TANZANITE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.3f)
            .alwaysEat()
            .effect(() ->new MobEffectInstance(
                    ModEffects.PHANTASMAGORIA.get(), 20 * 15, 0, false, false, true), 1.0f)
            .build();
}
