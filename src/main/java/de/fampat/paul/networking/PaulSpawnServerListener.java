package de.fampat.paul.networking;

import org.jetbrains.annotations.Nullable;

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
                // Only specific players are allowed to call Paul
                if (!player.getName().equals(Text.literal("Devpat")) && !player.getName().equals(Text.literal("StyPat"))) {
                    return;
                }

                // Get the players modded persistent data
                NbtCompound playerModPersistentData = ((IEntityModPersistentData) player).getModPersistentData();

                // Check if the player already has called Paul before
                if (!playerModPersistentData.contains(paulUUIDName)) {
                    player.sendMessage(Text.translatable("paul_come_here.text.paul.fampat.de"), false);
                    spawnPaul(player, playerModPersistentData);
                } else {
                    despawnPaul(player, playerModPersistentData);
                }
            });
        });
    }

    private static void spawnPaul(ServerPlayerEntity player, NbtCompound playerModPersistentData) {
        // Server world, thats where we are right now
        ServerWorld serverWorld = player.getWorld();

        // New Paul instance with new owner
        PaulEntity paulEntity = new PaulEntity(ModRegistry.PAUL_ENTITY_TYPE, serverWorld);
        
        // His name is hard-coded! For Paul!
        paulEntity.setCustomName(Text.literal("Pauli"));
        
        // Set the player as Pauls owner
        paulEntity.setOwnerUuid(player.getUuid());

        // Attach Pauls uuid to the players modded persistent data
        playerModPersistentData.putUuid(paulUUIDName, paulEntity.getUuid());

        // Position Paul to player and spawn him
        paulEntity.setPos(player.getPos().x, player.getPos().y + 0.08D, player.getPos().z);
        serverWorld.spawnEntity(paulEntity);

        // Generate spawn particles for each client watching Paul spawning
        for (ServerPlayerEntity watchingPlayer : PlayerLookup.tracking(paulEntity)) {
            serverWorld.spawnParticles(watchingPlayer, ParticleTypes.HEART, false,  paulEntity.getPos().x, paulEntity.getPos().y + 1D, paulEntity.getPos().z, 6, 0, 0, 0, 0);
            serverWorld.spawnParticles(watchingPlayer, ParticleTypes.POOF, false,  paulEntity.getPos().x, paulEntity.getPos().y + 1D, paulEntity.getPos().z, 6, 0, 0, 0, 0);
        }
    }

    public static void despawnPaul(ServerPlayerEntity serverPlayer, @Nullable NbtCompound playerModPersistentData) {
        if (playerModPersistentData == null) {
            // Get the players modded persistent data
            playerModPersistentData = ((IEntityModPersistentData) serverPlayer).getModPersistentData();
        }

        // Check if the player has called Paul
        if (playerModPersistentData.contains(PaulSpawnServerListener.paulUUIDName)) {
            ServerWorld serverWorld = serverPlayer.getWorld();
            PaulEntity paulEntity = (PaulEntity) serverWorld.getEntity(playerModPersistentData.getUuid(paulUUIDName));

            if (paulEntity != null) {
                // Generate despawn particles for each client watching Paul despawning
                for (ServerPlayerEntity watchingPlayer : PlayerLookup.tracking(paulEntity)) {
                    serverWorld.spawnParticles(watchingPlayer, ParticleTypes.ANGRY_VILLAGER, false,  paulEntity.getPos().x, paulEntity.getPos().y + 0.5D, paulEntity.getPos().z, 6, 0, 0, 0, 0);
                }

                // Despawn Paul
                paulEntity.remove(RemovalReason.DISCARDED);

                // Despawn message to player
                serverPlayer.sendMessage(Text.translatable("paul_despawned.text.paul.fampat.de"), false);
            } else {
                EntryMain.LOGGER.info("Paul is null, this should not happen, world where it happened: " + serverWorld.getDimension().toString());
            }

            // Remove the UUID from the player
            playerModPersistentData.remove(paulUUIDName);
        }
    }
}
