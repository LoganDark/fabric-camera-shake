package net.logandark.camerashake.impl;

import net.fabricmc.loader.api.FabricLoader;
import net.logandark.camerashake.CameraShake;
import net.logandark.camerashake.api.CameraShakeInitializer;
import net.logandark.camerashake.api.provider.CameraShakeManagerProvider;
import org.jetbrains.annotations.Nullable;

public class CameraShakeManagerSingleton extends CameraShakeManagerProvider {
	private static CameraShakeManagerSingleton INSTANCE = null;

	public static @Nullable CameraShakeManagerSingleton getInstance() {
		return INSTANCE;
	}

	public CameraShakeManagerSingleton() {
		if (INSTANCE != null)
			throw new IllegalStateException("Only one CameraShakeManagerSingleton may exist at a time!");

		INSTANCE = this;

		FabricLoader.getInstance()
			.getEntrypointContainers("camera-shake", CameraShakeInitializer.class)
			.forEach(container -> {
				try {
					container.getEntrypoint().onCameraShakeManager(this);
				} catch (Throwable throwable) {
					CameraShake.LOGGER.error(
						"An error occurred running a CameraShake initializer," +
							" this is an issue with the mod '{}'!",
						container.getProvider().getMetadata().getId(),
						throwable
					);
				}
			});
	}

	public void invalidate() {
		INSTANCE = null;
	}
}
