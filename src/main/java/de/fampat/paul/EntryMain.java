package de.fampat.paul;

import de.fampat.paul.registry.ModRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryMain implements ModInitializer {
	public static final String NAMESPACE = "paul";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    public static final Identifier S2C_NETWORK_PACKET_ID_PAUL_SIT = id("s2c_network_packet_id_paul_sit");
    public static final Identifier C2S_NETWORK_PACKET_ID_PAUL_SIT = id("c2s_network_packet_id_paul_sit");
    public static final Identifier S2C_NETWORK_PACKET_ID_PAUL_BONE = id("s2c_network_packet_id_paul_bone");
    public static final Identifier C2S_NETWORK_PACKET_ID_PAUL_SPAWN = id("c2s_network_packet_id_paul_spawn");

	@Override
	public void onInitialize() {
        LOGGER.info("Woof world! iam now initialized, woof!");
		ModRegistry.init();
	}

	public static Identifier id(String path) {
		return new Identifier(NAMESPACE, path);
	}
}
