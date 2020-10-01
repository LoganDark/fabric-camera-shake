package net.logandark.camerashake.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.logandark.camerashake.api.CameraShakeManager;
import net.logandark.camerashake.api.event.BoomShake;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding {
	@Inject(
		method = "onKeyPressed",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void camerashake$onKeyPressed(InputUtil.Key key, CallbackInfo ci) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			if (key.getCode() == GLFW.GLFW_KEY_K) {
				ci.cancel();
				//noinspection ConstantConditions
				CameraShakeManager.getInstance().addEvent(new BoomShake(.25, .0, .5));
			}
		}
	}
}
