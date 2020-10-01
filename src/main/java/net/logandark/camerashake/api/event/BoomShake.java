package net.logandark.camerashake.api.event;

import net.logandark.camerashake.api.CameraShakeManager;

/**
 * A basic {@link CameraShakeEvent}, a "boom". It starts at full intensity,
 * stays at full intensity for some length of time, and then fades away over a
 * specified duration. Then it is no longer valid and gets removed by the
 * {@link CameraShakeManager}.
 */
public class BoomShake implements CameraShakeEvent {
	public double magnitude;
	public double sustain;
	public double fade;

	/**
	 * @param magnitude The starting magnitude of the shake
	 * @param sustain   How long the event stays at full magnitude, can be 0
	 * @param fade      How long it takes to fade out after the sustain
	 */
	public BoomShake(double magnitude, double sustain, double fade) {
		this.magnitude = magnitude;
		this.sustain = sustain;
		this.fade = fade;
	}

	@Override
	public boolean isValid(double t) {
		return t < sustain + fade;
	}

	@Override
	public double getCameraShakeMagnitude(double t) {
		if (t <= sustain) {
			return magnitude;
		}

		return magnitude * (1 - (t - sustain) / fade);
	}
}
