package de.fampat.paul;

import de.fampat.paul.registry.PaulRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mod implements ModInitializer {
	public static final String NAMESPACE = "paul";
	public static final Logger LOGGER = LoggerFactory.getLogger("paul");

	@Override
	public void onInitialize() {
		PaulRegistry.init();
	}

	public static Identifier id(String path) {
		return new Identifier(NAMESPACE, path);
	}
}
