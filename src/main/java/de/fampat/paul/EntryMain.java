package de.fampat.paul;

import de.fampat.paul.registry.ModRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryMain implements ModInitializer {
	public static final String NAMESPACE = "paul";
	public static final Logger LOGGER = LoggerFactory.getLogger("paul");
    public static final Identifier S2C_NETWORK_PACKET_ID_PAUL_BONE = id("s2c_network_packet_id_paul_bone");

	@Override
	public void onInitialize() {
		ModRegistry.init();
	}

	public static Identifier id(String path) {
		return new Identifier(NAMESPACE, path);
	}
}
