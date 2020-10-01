package net.logandark.camerashake.api;

/**
 * {@link CameraShakeInitializer}s are called every time a new
 * {@link CameraShakeManager} becomes available, which is whenever the client
 * joins a new server. The {@link CameraShakeInitializer}s get the first
 * opportunity to add their providers to the {@link CameraShakeManager}.
 * <p>
 * To use, implement this interface on something and specify it as a
 * {@code "camera-shake"} entrypoint in your {@code fabric.mod.json}.
 * <p>
 * Only the global {@link CameraShakeManager} should call these initializers.
 * However, that does not mean you should depend on any implementation details
 * of the default {@link CameraShakeManager}.
 */
public interface CameraShakeInitializer {
	/**
	 * Called every time the global {@link CameraShakeManager} gets initialized,
	 * which may be multiple times if the client joins multiple servers.
	 *
	 * @param manager The {@link CameraShakeManager} getting initialized
	 */
	void onCameraShakeManager(CameraShakeManager manager);
}
