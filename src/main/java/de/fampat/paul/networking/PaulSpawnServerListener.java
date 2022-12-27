package de.fampat.paul.networking;

import de.fampat.paul.EntryMain;
import de.fampat.paul.interfaces.IEntityModPersistentData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
//import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class PaulSpawnServerListener {
    public static String paulUUIDName = EntryMain.NAMESPACE + ".UUID";

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(EntryMain.C2S_NETWORK_PACKET_ID_PAUL_SPAWN, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                EntryMain.LOGGER.info("Paul: Spawning Paul!");

                if (!player.getName().equals(Text.literal("Devpat")) && !player.getName().equals(Text.literal("StyPat"))) {
                    return;
                }

                //ServerWorld world = player.getWorld();

                NbtCompound modPersistentData = ((IEntityModPersistentData) player).getModPersistentData();

                if (!modPersistentData.contains(paulUUIDName)) {
                    EntryMain.LOGGER.info("Paul: Pauls UUID is not yet set, setting it now...");
                    modPersistentData.putString(paulUUIDName, "MOEP");
                } else {
                    EntryMain.LOGGER.info("Paul: Pauls UUID is already set, its value: " + modPersistentData.getString(paulUUIDName));
                }
            });
        });
    }
}
