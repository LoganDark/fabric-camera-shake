package net.logandark.camerashake.mixin;

import com.mojang.authlib.GameProfile;
import net.logandark.camerashake.CameraShake;
import net.logandark.camerashake.impl.CameraShakeManagerSingleton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientPlayPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler implements ClientPlayPacketListener {
	@Inject(
		method = "<init>",
		at = @At("RETURN")
	)
	private void camerashake$onInit(
		MinecraftClient client,
		Screen screen,
		ClientConnection connection,
		GameProfile profile,
		CallbackInfo ci
	) {
		CameraShake.LOGGER.debug("Creating CameraShakeManagerSingleton!");
		new CameraShakeManagerSingleton();
		CameraShake.LOGGER.debug("Created CameraShakeManagerSingleton!");
	}
}
