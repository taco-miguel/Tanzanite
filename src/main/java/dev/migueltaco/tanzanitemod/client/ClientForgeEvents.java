package dev.migueltaco.tanzanitemod.client;

import com.mojang.blaze3d.shaders.AbstractUniform;
import dev.migueltaco.tanzanitemod.TanzaniteMod;
import dev.migueltaco.tanzanitemod.effect.ModEffects;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(
        modid = TanzaniteMod.MOD_ID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ClientForgeEvents {
    private static boolean shaderActive = false;
    private static CameraType lastCameraType = null;

    private static float intensityProgress = 0.0F;

    private static final int FADE_IN_TICKS = 40;
    private static final int FADE_OUT_TICKS = 40;

    private static Field postEffectField;
    private static Field passesField;
    private static boolean reflectionWarningPrinted = false;

    private static final ResourceLocation PHANTASMAGORIA_SHADER =
            new ResourceLocation(TanzaniteMod.MOD_ID, "shaders/post/phantasmagoria.json");

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) {
            forceDisableShader(mc);
            return;
        }

        boolean hasEffect = mc.player.hasEffect(ModEffects.PHANTASMAGORIA.get());

        if (!hasEffect && !shaderActive) {
            intensityProgress = 0.0F;
            return;
        }

        CameraType currentCameraType = mc.options.getCameraType();
        boolean cameraChanged = lastCameraType != currentCameraType;

        if ((hasEffect && !shaderActive) || (shaderActive && cameraChanged)) {
            mc.gameRenderer.loadEffect(PHANTASMAGORIA_SHADER);
            shaderActive = true;
        }

        lastCameraType = currentCameraType;

        if (hasEffect) {
            intensityProgress += 1.0F / FADE_IN_TICKS;
        } else {
            intensityProgress -= 1.0F / FADE_OUT_TICKS;
        }

        intensityProgress = clamp(intensityProgress, 0.0F, 1.0F);

        float intensity = smoothStep(intensityProgress);
        setShaderIntensity(mc, intensity);

        if (!hasEffect && intensityProgress <= 0.0F) {
            forceDisableShader(mc);
        }
    }

    private static void forceDisableShader(Minecraft mc) {
        if (shaderActive) {
            mc.gameRenderer.shutdownEffect();
        }

        shaderActive = false;
        lastCameraType = null;
        intensityProgress = 0.0F;
    }

    private static float smoothStep(float value) {
        return value * value * (3.0F - 2.0F * value);
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    @SuppressWarnings("unchecked")
    private static void setShaderIntensity(Minecraft mc, float intensity) {
        try {
            if (postEffectField == null) {
                postEffectField = mc.gameRenderer.getClass().getDeclaredField("postEffect");
                postEffectField.setAccessible(true);
            }

            PostChain postChain = (PostChain) postEffectField.get(mc.gameRenderer);
            if (postChain == null) return;

            if (passesField == null) {
                passesField = PostChain.class.getDeclaredField("passes");
                passesField.setAccessible(true);
            }

            List<PostPass> passes = (List<PostPass>) passesField.get(postChain);

            for (PostPass pass : passes) {
                AbstractUniform uniform = pass.getEffect().safeGetUniform("Intensity");

                if (uniform != null) {
                    uniform.set(intensity);
                }
            }
        } catch (Exception e) {
            if (!reflectionWarningPrinted) {
                System.out.println("Failed to set Phantasmagoria shader intensity:");
                e.printStackTrace();
                reflectionWarningPrinted = true;
            }
        }
    }
}