package dev.migueltaco.tanzanitemod.client;

import dev.migueltaco.tanzanitemod.TanzaniteMod;
import dev.migueltaco.tanzanitemod.effect.ModEffects;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = TanzaniteMod.MOD_ID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ClientForgeEvents {
    private static boolean shaderActive = false;
    private static CameraType lastCameraType = null;

    private static final ResourceLocation PHANTASMAGORIA_SHADER =
            new ResourceLocation(TanzaniteMod.MOD_ID, "shaders/post/phantasmagoria.json");

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) {
            disableShader(mc);
            return;
        }

        boolean shouldBeActive = mc.player.hasEffect(ModEffects.PHANTASMAGORIA.get());
        CameraType currentCameraType = mc.options.getCameraType();

        boolean cameraChanged = lastCameraType != currentCameraType;
        lastCameraType = currentCameraType;

        if (shouldBeActive && (!shaderActive || cameraChanged)) {
            mc.gameRenderer.loadEffect(PHANTASMAGORIA_SHADER);
            shaderActive = true;
        }

        if (!shouldBeActive && shaderActive) {
            disableShader(mc);
        }
    }

    private static void disableShader(Minecraft mc) {
        mc.gameRenderer.shutdownEffect();
        shaderActive = false;
        lastCameraType = null;
    }
}