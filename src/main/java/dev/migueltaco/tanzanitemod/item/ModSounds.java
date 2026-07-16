package dev.migueltaco.tanzanitemod.item;

import dev.migueltaco.tanzanitemod.TanzaniteMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TanzaniteMod.MOD_ID);

    public static final RegistryObject<SoundEvent> CRYSTAL_EAT =
            SOUND_EVENTS.register("crystal_eat",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(TanzaniteMod.MOD_ID, "crystal_eat")));
}
