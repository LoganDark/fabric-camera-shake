package net.logandark.camerashake.impl;

import net.minecraft.client.MinecraftClient;

import java.util.Random;

public class CameraShakeManagerSingletonManager {
	private static Random random;
	private static int smooth;
	private static double[] pastX;
	private static double[] pastY;
	private static int pastI;
	private static double avgX;
	private static double avgY;

	public static void reset() {
		smooth = 3;
		random = new Random();
		pastX = new double[smooth];
		pastY = new double[smooth];
		pastI = 0;
		avgX = 0;
		avgY = 0;
	}

	static {
		reset();
	}

	public static void newFrame() {
		CameraShakeManagerSingleton singleton = CameraShakeManagerSingleton.getInstance();

		if (singleton == null) {
			throw new NullPointerException("CameraShakeManagerSingleton is not present");
		}

		double magnitude = singleton.getCameraShakeMagnitude(MinecraftClient.getInstance().player);

		double x = (random.nextDouble() - .5) * magnitude;
		double y = (random.nextDouble() - .5) * magnitude;

		pastX[pastI] = x;
		pastY[pastI++] = y;
		pastI %= smooth;

		calculateAvg();
	}

	public static void calculateAvg() {
		avgX = .0;
		avgY = .0;

		for (int i = 0; i < smooth; i++) {
			avgX += pastX[i];
			avgY += pastY[i];
		}

		avgX /= smooth;
		avgY /= smooth;
	}

	public static double getAvgX() {
		return avgX;
	}

	public static double getAvgY() {
		return avgY;
	}
}
