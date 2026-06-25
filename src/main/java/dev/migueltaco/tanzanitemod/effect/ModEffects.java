package dev.migueltaco.tanzanitemod.effect;

import dev.migueltaco.tanzanitemod.TanzaniteMod;
import dev.migueltaco.tanzanitemod.effect.custom.PhantasmagoriaEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TanzaniteMod.MOD_ID);

    public static final RegistryObject<MobEffect> PHANTASMAGORIA =
            MOB_EFFECTS.register("phantasmagoria",
                    () -> new PhantasmagoriaEffect(MobEffectCategory.HARMFUL, 0xE27183));
}
