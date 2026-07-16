package dev.migueltaco.tanzanitemod.recipe.custom;

import dev.migueltaco.tanzanitemod.item.ModItems;
import dev.migueltaco.tanzanitemod.recipe.ModRecipes;
import dev.migueltaco.tanzanitemod.item.custom.StuffedPipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class StuffedPipeRecipe extends CustomRecipe {
    public StuffedPipeRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        boolean foundPipe = false;
        boolean foundPowder = false;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);

            if (stack.isEmpty()) continue;

            if (stack.is(ModItems.PIPE.get())) {
                if (foundPipe) return false;
                foundPipe = true;
                continue;
            }

            if (stack.is(ModItems.TANZANITE_POWDER.get())) {
                if (foundPowder) return false;
                foundPowder = true;
                continue;
            }

            return false;
        }

        return foundPipe && foundPowder;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack pipeStack = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);

            if (stack.is(ModItems.PIPE.get())) {
                pipeStack = stack;
                break;
            }
        }

        ItemStack stuffedPipe = new ItemStack(ModItems.STUFFED_PIPE.get());

        if (pipeStack.hasTag()) {
            CompoundTag copiedTag = pipeStack.getTag().copy();
            copiedTag.putInt(StuffedPipe.USES_TAG, StuffedPipe.MAX_USES);
            stuffedPipe.setTag(copiedTag);
        } else {
            stuffedPipe.getOrCreateTag().putInt(StuffedPipe.USES_TAG, StuffedPipe.MAX_USES);
        }

        return stuffedPipe;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return new ItemStack(ModItems.STUFFED_PIPE.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.STUFFED_PIPE_SERIALIZER.get();
    }
}