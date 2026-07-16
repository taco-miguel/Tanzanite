package dev.migueltaco.tanzanitemod.item;

import dev.migueltaco.tanzanitemod.TanzaniteMod;
import dev.migueltaco.tanzanitemod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TanzaniteMod.MOD_ID);


    public static final RegistryObject<CreativeModeTab> TANZANITE_TAB = CREATIVE_MODE_TABS.register("tanzanite_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.RAW_TANZANITE.get()))
                    .title(Component.translatable("creativetab.tanzanitemod.tanzanite_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.TANZANITE.get());
                        pOutput.accept(ModItems.RAW_TANZANITE.get());
                        pOutput.accept(ModItems.DIRTY_RAW_TANZANITE.get());
                        pOutput.accept(ModItems.TANZANITE_POWDER.get());
                        pOutput.accept(ModBlocks.TANZANITE_BLOCK.get());
                        pOutput.accept(ModBlocks.RAW_TANZANITE_BLOCK.get());
                        pOutput.accept(ModBlocks.TANZANITE_ORE.get());
                        pOutput.accept(ModBlocks.DEESPLATE_TANZANITE_ORE.get());
                        pOutput.accept(ModItems.PESTLE.get());
                        pOutput.accept(ModBlocks.MORTAR.get());
                        pOutput.accept(ModItems.PIPE.get());
                        pOutput.accept(ModItems.STUFFED_PIPE.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
