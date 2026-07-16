package dev.migueltaco.tanzanitemod.item;

import dev.migueltaco.tanzanitemod.TanzaniteMod;
import dev.migueltaco.tanzanitemod.item.custom.StuffedPipe;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TanzaniteMod.MOD_ID);

    public static final RegistryObject<Item> TANZANITE = ITEMS.register("tanzanite",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TANZANITE_POWDER = ITEMS.register("tanzanite_powder",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PIPE = ITEMS.register("pipe",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> STUFFED_PIPE = ITEMS.register("stuffed_pipe",
            () -> new StuffedPipe(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RAW_TANZANITE = ITEMS.register("raw_tanzanite",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIRTY_RAW_TANZANITE = ITEMS.register("dirty_raw_tanzanite",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PESTLE = ITEMS.register("pestle",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
