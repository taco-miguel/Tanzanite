package dev.migueltaco.tanzanitemod.item;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties TANZANITE_POWDER = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.3f)
            .alwaysEat()
            .build();
}
