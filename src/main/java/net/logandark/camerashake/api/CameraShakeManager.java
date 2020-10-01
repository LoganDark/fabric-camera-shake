package net.logandark.camerashake.api;

import net.logandark.camerashake.api.event.CameraShakeEvent;
import net.logandark.camerashake.api.provider.CameraShakeProvider;
import net.logandark.camerashake.impl.CameraShakeManagerSingleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * The {@link CameraShakeManager} is what you use to manage camera shake.
 * Through use of the {@link CameraShakeManager}, you can manage
 * {@link CameraShakeEvent}s and {@link CameraShakeProvider}s.
 * <p>
 * Prefer use of this API class rather than the implementation classes in
 * {@code net.logandark.camerashake.impl}. The API is guaranteed to be
 * semantically versioned but the implementation is not.
 * <p>
 * Additionally, when it comes to camera shake, less is often more. "Small"
 * values such as 0.125 or even 0.0625 are still felt and can add positively to
 * the player experience. However, excessive camera shake can be annoying unless
 * accompanied by a sufficiently excessive action.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface CameraShakeManager {
	/**
	 * A {@link CameraShakeManager} is guaranteed to exist for each play
	 * session. As a result of this, API consumers are able to retrieve the
	 * currently existing instance through a static method on the interface.
	 * <p>
	 * In order to be notified whenever the {@link CameraShakeManager} is
	 * created, you may use a {@link CameraShakeInitializer}.
	 *
	 * @return The current instance of the {@link CameraShakeManager}. This may
	 * be {@code null} if the client is not currently in a game.
	 */
	static @Nullable CameraShakeManager getInstance() {
		return CameraShakeManagerSingleton.getInstance();
	}

	/**
	 * Adds the specified event to this {@link CameraShakeManager}. The event
	 * will automatically be removed once
	 * {@link CameraShakeEvent#isValid(double)} returns {@code false} or if the
	 * event is removed manually by {@link #removeEvent(CameraShakeEvent)} or
	 * {@link #removeEventsOfClass(Class)}.
	 *
	 * @param event The event to add
	 * @param <E>   The type of event to add
	 * @return The event that was added, or {@code null} if it was already added
	 */
	<E extends CameraShakeEvent> @Nullable E addEvent(E event);

	/**
	 * Removes the specified event from this {@link CameraShakeManager}.
	 *
	 * @param event The event to remove
	 * @param <E>   The type of event to remove
	 * @return The event that was removed, or {@code null} if it could not be
	 * found
	 */
	<E extends CameraShakeEvent> @Nullable E removeEvent(E event);

	/**
	 * Removes all events of the specified class from this
	 * {@link CameraShakeManager}.
	 *
	 * @param clazz The class
	 * @param <E>   The type of event to remove
	 * @return A collection of all events that were removed
	 */
	<E extends CameraShakeEvent> @NotNull Collection<E> removeEventsOfClass(Class<E> clazz);

	/**
	 * Adds the specified event to this {@link CameraShakeManager}. The provider
	 * will only be removed by a call to
	 * {@link #removeProvider(CameraShakeProvider)} or
	 * {@link #removeProvidersOfClass(Class)}.
	 *
	 * @param provider The provider to add
	 * @param <P>      The type of provider to add
	 * @return The provider that was added, or {@code null} if it was already
	 * added
	 */
	<P extends CameraShakeProvider> @Nullable P addProvider(P provider);

	/**
	 * Removes the specified event from this {@link CameraShakeManager}.
	 *
	 * @param provider The provider to remove
	 * @param <P>      The type of provider to remove
	 * @return The provider that was removed, or {@code null} if it could not be
	 * found
	 */
	<P extends CameraShakeProvider> @Nullable P removeProvider(P provider);

	/**
	 * Removes all providers of the specified class from this
	 * {@link CameraShakeManager}.
	 *
	 * @param clazz The class
	 * @param <P>   The type of provider to remove
	 * @return A collection of all providers that were removed
	 */
	<P extends CameraShakeProvider> @NotNull Collection<P> removeProvidersOfClass(Class<P> clazz);

	/**
	 * @return All events currently being managed by this
	 * {@link CameraShakeManager}.
	 */
	@NotNull Collection<CameraShakeEvent> getEvents();

	/**
	 * @return All providers currently being managed by this
	 * {@link CameraShakeManager}.
	 */
	@NotNull Collection<CameraShakeProvider> getProviders();

	/**
	 * @param clazz The class of events to get
	 * @param <E>   The type of events that will be returned
	 * @return A collection of all events of the specified class
	 */
	<E extends CameraShakeEvent> @NotNull Collection<E> getEventsOfClass(Class<E> clazz);

	/**
	 * @param clazz The class of providers to get
	 * @param <P>   The type of providers that will be returned
	 * @return A collection of all providers of the specified class
	 */
	<P extends CameraShakeProvider> @NotNull Collection<P> getProvidersOfClass(Class<P> clazz);
}
