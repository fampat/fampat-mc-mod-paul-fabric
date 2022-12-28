package de.fampat.paul.networking;

import de.fampat.paul.EntryMain;
import de.fampat.paul.entities.PaulEntity;
import de.fampat.paul.interfaces.IEntityModPersistentData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class PaulSitServerListener {
    public static String paulUUIDName = EntryMain.NAMESPACE + ".UUID";

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(EntryMain.C2S_NETWORK_PACKET_ID_PAUL_SIT, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                EntryMain.LOGGER.info("Sit or unsit Paul!");

                // Only specific players are allowed to call Paul
                if (!player.getName().equals(Text.literal("Devpat")) && !player.getName().equals(Text.literal("StyPat"))) {
                    return;
                }

                // Server world, thats where we are right now
                ServerWorld serverWorld = player.getWorld();

                // Get the players modded persistent data
                NbtCompound playerModPersistentData = ((IEntityModPersistentData) player).getModPersistentData();

                // Check if the player already has called Paul before
                if (!playerModPersistentData.contains(paulUUIDName)) {
                    EntryMain.LOGGER.info("Pauls UUID is not yet set, cannot order sit or unsit command...");
                } else {
                    // Load Paul from players persistent storage by using the Pauls uuid
                    PaulEntity paulEntity = (PaulEntity) serverWorld.getEntity(playerModPersistentData.getUuid(paulUUIDName));

                    // Check if Paul is present
                    if (paulEntity != null) {
                        if (paulEntity.isSitting()) {
                            // Make him unsit
                            player.sendMessage(Text.translatable("paul_unsit.text.paul.fampat.de"), false);
                            paulEntity.setSitting(false);
                            paulEntity.setInSittingPose(false);
                        } else {
                            // Make him sit
                            player.sendMessage(Text.translatable("paul_sit.text.paul.fampat.de"), false);
                            paulEntity.setSitting(true);
                            paulEntity.setInSittingPose(true);
                        }
                    }
                }
            });
        });
    }
}
