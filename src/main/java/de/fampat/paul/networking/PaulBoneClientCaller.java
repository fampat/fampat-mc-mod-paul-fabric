package de.fampat.paul.networking;

import de.fampat.paul.EntryMain;
import de.fampat.paul.entities.PaulEntity;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class PaulBoneClientCaller {
    public static void callAllClientsWith(PaulEntity paulEntity, Boolean shallBoneCarry) {
        // Create and prepare the network payload for the client
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(paulEntity.getId());
        buf.writeBoolean(shallBoneCarry);
        
        // Communicate the payload to all server-players
        for (ServerPlayerEntity player : PlayerLookup.world((ServerWorld) paulEntity.world)) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, EntryMain.S2C_NETWORK_PACKET_ID_PAUL_BONE, buf);
        }
    }
}
