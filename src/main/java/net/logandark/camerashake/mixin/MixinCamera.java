package net.logandark.camerashake.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.logandark.camerashake.impl.CameraShakeManagerSingletonManager;
import net.logandark.camerashake.impl.ImmersivePortalsClassloadingDeferrer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class MixinCamera {
	@Shadow
	protected abstract void moveBy(double x, double y, double z);

	@Inject(
		method = "update",
		at = @At(
			// Inject before the call to clipToSpace
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V",
			shift = At.Shift.BY,
			by = 1
		)
	)
	private void camerashake$onUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
		double x = CameraShakeManagerSingletonManager.getAvgX();
		double y = CameraShakeManagerSingletonManager.getAvgY();

		if (FabricLoader.getInstance().isModLoaded("imm_ptl_core")) {
			if (ImmersivePortalsClassloadingDeferrer.getPortalLayer() > 0) {
				return;
			}

			x *= ImmersivePortalsClassloadingDeferrer.getViewBobFactor();
			y *= ImmersivePortalsClassloadingDeferrer.getViewBobFactor();
		}

		moveBy(.0, y, x);
	}
}
