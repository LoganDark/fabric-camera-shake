package net.logandark.camerashake.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.logandark.camerashake.impl.CameraShakeManagerSingletonManager;
import net.logandark.camerashake.impl.ImmersivePortalsClassloadingDeferrer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(
		method = "render",
		at = @At("HEAD")
	)
	private void camerashake$onRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		if (!client.skipGameRender && tick && client.world != null) {
			CameraShakeManagerSingletonManager.newFrame();
		}
	}

	// replaced by camera mixin because immersive portals overrides it
	//@Inject(
	//	method = "renderWorld",
	//	at = @At(
	//		value = "INVOKE",
	//		target = "Lnet/minecraft/client/render/GameRenderer;bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"
	//	)
	//)
	//private void camerashake$shakeCamera(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
	//	matrices.translate(
	//		ICantPutThisAllInOneMixinSoImGoingToHaveToStoreSomeStaticVariablesHereHopeYouDontMindAndAlsoPleaseDontUseThisClassItsNotVeryGreat.getAvgX(),
	//		ICantPutThisAllInOneMixinSoImGoingToHaveToStoreSomeStaticVariablesHereHopeYouDontMindAndAlsoPleaseDontUseThisClassItsNotVeryGreat.getAvgY(),
	//		.0
	//	);
	//}

	@Inject(
		method = "renderHand",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/GameRenderer;bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"
		)
	)
	private void camerashake$shakeHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
		double x = CameraShakeManagerSingletonManager.getAvgX();
		double y = CameraShakeManagerSingletonManager.getAvgY();

		if (FabricLoader.getInstance().isModLoaded("imm_ptl_core")) {
			x *= ImmersivePortalsClassloadingDeferrer.getViewBobFactor();
			y *= ImmersivePortalsClassloadingDeferrer.getViewBobFactor();
		}

		matrices.translate(x, -y, .0); // opposite of camera
	}
}
