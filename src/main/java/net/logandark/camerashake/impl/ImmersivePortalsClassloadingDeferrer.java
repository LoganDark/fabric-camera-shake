package net.logandark.camerashake.impl;

import com.qouteall.immersive_portals.render.context_management.PortalRendering;
import com.qouteall.immersive_portals.render.context_management.RenderStates;

/**
 * mixin crashes trying to classload immersive portals if I try to access any of
 * their classes from inside a mixin
 * <p>
 * so in order to do that stuff I need a dummy class to do the access for me, so
 * that if IP isn't installed, mixin only loads this class and does nothing with
 * the info
 */
public class ImmersivePortalsClassloadingDeferrer {
	public static int getPortalLayer() {
		return PortalRendering.getPortalLayer();
	}

	public static double getViewBobFactor() {
		return RenderStates.viewBobFactor;
	}
}
