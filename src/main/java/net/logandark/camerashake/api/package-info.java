/**
 * The Camera Shake library is only semantically versioned according to its API.
 * This means that while the API is guaranteed to be compatible across minor and
 * patch version changes, the implementation code is not.
 * <p>
 * All dependents of Camera Shake should only use this API package to interface
 * with the library.
 * {@link net.logandark.camerashake.api.CameraShakeManager#getInstance()} can be
 * used to obtain an instance of a {@code CameraShakeManager} to add
 * {@link net.logandark.camerashake.api.provider.CameraShakeProvider}s and
 * {@link net.logandark.camerashake.api.event.CameraShakeEvent}s.
 * <p>
 * However, the {@code impl} package is not officially supported and may change
 * unpredictably across minor and even patch version changes. No guarantees are
 * made about its compatibility.
 */
package net.logandark.camerashake.api;
