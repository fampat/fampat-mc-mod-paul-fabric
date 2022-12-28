package de.fampat.paul.client.networking;

import de.fampat.paul.EntryMain;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class PaulSitServerCaller {
    public static void callServerWith() {
        // Create and prepare the network payload for the server
        PacketByteBuf buf = PacketByteBufs.empty();
        
        // Send the network packet to the server, on the server-side we already know the player
        ClientPlayNetworking.send(EntryMain.C2S_NETWORK_PACKET_ID_PAUL_SIT, buf);
    }
}
