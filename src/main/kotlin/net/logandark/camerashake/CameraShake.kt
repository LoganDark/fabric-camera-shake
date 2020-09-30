package net.logandark.camerashake

import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Suppress("unused")
object CameraShake : ModInitializer {
	val logger: Logger = LogManager.getLogger()

	override fun onInitialize() {
		logger.info("Hello Fabric world!")
	}
}
