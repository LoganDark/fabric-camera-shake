package net.logandark.camerashake.api.provider;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.AtomicDouble;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.logandark.camerashake.CameraShake;
import net.logandark.camerashake.api.CameraShakeManager;
import net.logandark.camerashake.api.event.CameraShakeEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This is a {@link CameraShakeProvider} that acts as a
 * {@link CameraShakeManager}. As a part of the API it is also covered by the
 * semantic versioning guarantee.
 * <p>
 * This is not necessarily the singleton that gets returned by
 * {@link CameraShakeManager#getInstance()}. The object returned by that method
 * is an implementation detail, do not make any assumptions other than that it
 * is a {@link CameraShakeManager}!
 */
public class CameraShakeManagerProvider implements CameraShakeManager, CameraShakeProvider {
	protected final Set<CameraShakeEvent> events = new HashSet<>();
	protected final Set<CameraShakeProvider> providers = new HashSet<>();

	@Override
	public <E extends CameraShakeEvent> @Nullable E addEvent(E event) {
		if (events.add(event)) {
			onEventAdded(event);
			return event;
		}

		return null;
	}

	@Override
	public <E extends CameraShakeEvent> @Nullable E removeEvent(E event) {
		if (events.remove(event)) {
			onEventRemoved(event);
			return event;
		}

		return null;
	}

	@Override
	public <E extends CameraShakeEvent> @NotNull Collection<E> removeEventsOfClass(Class<E> clazz) {
		List<E> removed = new ArrayList<>();

		Iterator<CameraShakeEvent> iterator = events.iterator();
		iterator.forEachRemaining(event -> {
			if (clazz.isAssignableFrom(event.getClass())) {
				iterator.remove();
				onEventRemoved(event);
				removed.add(clazz.cast(event));
			}
		});

		return removed;
	}

	@Override
	public <P extends CameraShakeProvider> @Nullable P addProvider(P provider) {
		if (providers.add(provider)) {
			onProviderAdded(provider);
			return provider;
		}

		return null;
	}

	@Override
	public <P extends CameraShakeProvider> @Nullable P removeProvider(P provider) {
		if (providers.remove(provider)) {
			onProviderAdded(provider);
			return provider;
		}

		return null;
	}

	@Override
	public <P extends CameraShakeProvider> @NotNull Collection<P> removeProvidersOfClass(Class<P> clazz) {
		List<P> removed = new ArrayList<>();

		Iterator<CameraShakeProvider> iterator = providers.iterator();
		iterator.forEachRemaining(provider -> {
			if (clazz.isAssignableFrom(provider.getClass())) {
				iterator.remove();
				onProviderRemoved(provider);
				removed.add(clazz.cast(provider));
			}
		});

		return removed;
	}

	@Override
	public @NotNull Collection<CameraShakeEvent> getEvents() {
		return ImmutableSet.copyOf(events);
	}

	@Override
	public @NotNull Collection<CameraShakeProvider> getProviders() {
		return ImmutableSet.copyOf(providers);
	}

	@Override
	public <E extends CameraShakeEvent> @NotNull Collection<E> getEventsOfClass(Class<E> clazz) {
		List<E> ofClass = new ArrayList<>();

		Iterator<CameraShakeEvent> iterator = events.iterator();
		iterator.forEachRemaining(event -> {
			if (clazz.isAssignableFrom(event.getClass())) {
				ofClass.add(clazz.cast(event));
			}
		});

		return ofClass;
	}

	@Override
	public <P extends CameraShakeProvider> @NotNull Collection<P> getProvidersOfClass(Class<P> clazz) {
		List<P> ofClass = new ArrayList<>();

		Iterator<CameraShakeProvider> iterator = providers.iterator();
		iterator.forEachRemaining(event -> {
			if (clazz.isAssignableFrom(event.getClass())) {
				ofClass.add(clazz.cast(event));
			}
		});

		return ofClass;
	}

	/**
	 * A map of every event added to this {@link CameraShakeManagerProvider} and
	 * its creation time, as returned by {@link Util#getMeasuringTimeNano()}.
	 * <p>
	 * Events are added to this map when they start being tracked by this
	 * {@link CameraShakeManager} and are removed when the event is invalid or
	 * removed manually.
	 */
	protected Object2LongOpenHashMap<CameraShakeEvent> eventInceptions = new Object2LongOpenHashMap<>();

	/**
	 * Called every time a new event is added.
	 * <p>
	 * Remember to call {@code super()} first if your override this method.
	 *
	 * @param event The event that was added
	 * @param <E>   The type of event that was added
	 */
	protected <E extends CameraShakeEvent> void onEventAdded(E event) {
		eventInceptions.put(event, Util.getMeasuringTimeNano());
	}

	/**
	 * Called every time an event is removed.
	 * <p>
	 * Remember to call {@code super()} last if your override this method.
	 *
	 * @param event The event that was removed
	 * @param <E>   The type of event that was removed
	 */
	protected <E extends CameraShakeEvent> void onEventRemoved(E event) {
		eventInceptions.removeLong(event);
	}

	/**
	 * Called every time a new provider is added.
	 * <p>
	 * Remember to call {@code super()} first if your override this method.
	 *
	 * @param provider The provider that was added
	 * @param <P>      The type of provider that was added
	 */
	@SuppressWarnings("unused")
	protected <P extends CameraShakeProvider> void onProviderAdded(P provider) {}

	/**
	 * Called every time a provider is removed.
	 * <p>
	 * Remember to call {@code super()} last if your override this method.
	 *
	 * @param provider The provider that was removed
	 * @param <P>      The type of provider that was removed
	 */
	@SuppressWarnings("unused")
	protected <P extends CameraShakeProvider> void onProviderRemoved(P provider) {}

	/**
	 * Gets the time since an event was added to this
	 * {@link CameraShakeManagerProvider}.
	 *
	 * @param event The event to get the time for
	 * @return The seconds since this event was first added
	 */
	protected double getTime(CameraShakeEvent event) {
		if (!eventInceptions.containsKey(event)) {
			throw new IllegalArgumentException("Passed event was not added to this CameraShakeManager");
		}

		long then = eventInceptions.getLong(event);
		long now = Util.getMeasuringTimeNano();
		long delta = now - then;

		return (double) delta / 1_000_000_000.0;
	}

	@Override
	public double getCameraShakeMagnitude(ClientPlayerEntity player) {
		AtomicDouble magnitude = new AtomicDouble();

		Iterator<CameraShakeEvent> eventIterator = events.iterator();
		eventIterator.forEachRemaining(event -> {
			double t;

			try {
				t = getTime(event);
			} catch (IllegalArgumentException e) {
				// The event got added without being tracked somehow. Log and
				// remove it, but after that there's nothing else we can do

				CameraShake.LOGGER.error(
					"Someone has a bug! Untracked event found in my ({}) event set: {}",
					getClass().getName(),
					event
				);

				eventIterator.remove();
				return;
			}

			if (!event.isValid(t)) {
				eventIterator.remove();
				onEventRemoved(event);
			} else {
				magnitude.addAndGet(event.getCameraShakeMagnitude(t));
			}
		});

		for (CameraShakeProvider provider : providers) {
			magnitude.addAndGet(provider.getCameraShakeMagnitude(player));
		}

		return magnitude.get();
	}
}
