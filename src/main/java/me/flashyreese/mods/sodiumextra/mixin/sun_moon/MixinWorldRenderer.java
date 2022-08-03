package me.flashyreese.mods.sodiumextra.mixin.sun_moon;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Mutable
    @Shadow
    @Final
    private static Identifier SUN;

    @Mutable
    @Shadow
    @Final
    private static Identifier MOON_PHASES;

    @Redirect(
            method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/DimensionEffects;getFogColorOverride(FF)[F"
            )
    )
    public float[] redirectGetFogColorOverride(DimensionEffects instance, float skyAngle, float tickDelta) {
        if (SodiumExtraClientMod.options().detailSettings.sunMoon) {
            return instance.getFogColorOverride(skyAngle, tickDelta);
        } else {
            return null;
        }
    }

    @Inject(
            method = "reload()V",
            at = @At(value = "TAIL")
    )
    private void postWorldRendererReload(CallbackInfo ci) {
        if (SodiumExtraClientMod.options().detailSettings.sunMoon) {
            MOON_PHASES = new Identifier("textures/environment/moon_phases.png");
            SUN = new Identifier("textures/environment/sun.png");
        } else {
            MOON_PHASES = new Identifier("sodiumextra", "textures/transparent.png");
            SUN = new Identifier("sodiumextra", "textures/transparent.png");
        }
    }
}
