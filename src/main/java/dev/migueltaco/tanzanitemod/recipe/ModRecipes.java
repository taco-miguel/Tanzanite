package dev.migueltaco.tanzanitemod.recipe;

import dev.migueltaco.tanzanitemod.TanzaniteMod;
import dev.migueltaco.tanzanitemod.recipe.custom.StuffedPipeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TanzaniteMod.MOD_ID);

    public static final RegistryObject<SimpleCraftingRecipeSerializer<StuffedPipeRecipe>> STUFFED_PIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafting_special_stuffed_pipe",
                    () -> new SimpleCraftingRecipeSerializer<>(StuffedPipeRecipe::new));
}