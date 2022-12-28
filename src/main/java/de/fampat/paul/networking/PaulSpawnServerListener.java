package de.fampat.paul.networking;

import de.fampat.paul.EntryMain;
import de.fampat.paul.entities.PaulEntity;
import de.fampat.paul.interfaces.IEntityModPersistentData;
import de.fampat.paul.registry.ModRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class PaulSpawnServerListener {
    public static String paulUUIDName = EntryMain.NAMESPACE + ".UUID";

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(EntryMain.C2S_NETWORK_PACKET_ID_PAUL_SPAWN, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                EntryMain.LOGGER.info("Paul: Spawning or despawning Paul!");

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
                    EntryMain.LOGGER.info("Paul: Pauls UUID is not yet set, spawning Paul now...");
                    player.sendMessage(Text.translatable("paul_come_here.text.paul.fampat.de"), false);
                    spawnPaul(player, serverWorld, playerModPersistentData);
                } else {
                    EntryMain.LOGGER.info("Paul: Pauls UUID is already set, despawning Paul...");
                    player.sendMessage(Text.translatable("paul_leave.text.paul.fampat.de"), false);
                    despawnPaul(serverWorld, playerModPersistentData);
                }
            });
        });
    }

    private static void spawnPaul(ServerPlayerEntity player, ServerWorld serverWorld, NbtCompound playerModPersistentData) {
        // New Paul instance with new owner
        PaulEntity paulEntity = new PaulEntity(ModRegistry.PAUL_ENTITY_TYPE, serverWorld);
        
        // His name is hard-coded! For Paul!
        paulEntity.setCustomName(Text.literal("Pauli"));
        
        // Set the player as Pauls owner
        paulEntity.setOwnerUuid(player.getUuid());

        // Attach Pauls uuid to the players modded persistent data
        playerModPersistentData.putUuid(paulUUIDName, paulEntity.getUuid());

        // Position Paul to player and spawn him
        paulEntity.setPos(player.getPos().x, player.getPos().y, player.getPos().z);
        serverWorld.spawnEntity(paulEntity);

        // Generate spawn particles for each client watching Paul spawning
        for (ServerPlayerEntity watchingPlayer : PlayerLookup.tracking(paulEntity)) {
            serverWorld.spawnParticles(watchingPlayer, ParticleTypes.HEART, false,  paulEntity.getPos().x, paulEntity.getPos().y + 1D, paulEntity.getPos().z, 6, 0, 0, 0, 0);
            serverWorld.spawnParticles(watchingPlayer, ParticleTypes.POOF, false,  paulEntity.getPos().x, paulEntity.getPos().y + 1D, paulEntity.getPos().z, 6, 0, 0, 0, 0);
        }
    }

    private static void despawnPaul(ServerWorld serverWorld, NbtCompound playerModPersistentData) {
        PaulEntity paulEntity = (PaulEntity) serverWorld.getEntity(playerModPersistentData.getUuid(paulUUIDName));

        if (paulEntity != null) {
            // Generate despawn particles for each client watching Paul despawning
            for (ServerPlayerEntity watchingPlayer : PlayerLookup.tracking(paulEntity)) {
                serverWorld.spawnParticles(watchingPlayer, ParticleTypes.ANGRY_VILLAGER, false,  paulEntity.getPos().x, paulEntity.getPos().y + 0.5D, paulEntity.getPos().z, 6, 0, 0, 0, 0);
            }

            // Despawn Paul
            paulEntity.remove(RemovalReason.DISCARDED);
        }

        // Remove the UUID from the player
        playerModPersistentData.remove(paulUUIDName);
    }
}
