package net.logandark.camerashake.mixin;

import net.logandark.camerashake.CameraShake;
import net.logandark.camerashake.impl.CameraShakeManagerSingleton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
	@Shadow
	public abstract @Nullable ClientPlayNetworkHandler getNetworkHandler();

	@Inject(
		method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V",
		at = @At("HEAD")
	)
	private void camerashake$onDisconnect(Screen screen, CallbackInfo ci) {
		if (getNetworkHandler() == null) {
			return;
		}

		CameraShake.LOGGER.debug("Invalidating CameraShakeManagerSingleton!");
		//noinspection ConstantConditions
		CameraShakeManagerSingleton.getInstance().invalidate();
		CameraShake.LOGGER.debug("Invalidated CameraShakeManagerSingleton!");
	}
}
