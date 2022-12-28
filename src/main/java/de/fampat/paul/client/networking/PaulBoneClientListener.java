package de.fampat.paul.client.networking;

import de.fampat.paul.EntryMain;
import de.fampat.paul.entities.PaulEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PaulBoneClientListener {
    public static void register() {
        // Register client network code for rendering his bone on clients
        ClientPlayNetworking.registerGlobalReceiver(EntryMain.S2C_NETWORK_PACKET_ID_PAUL_BONE, (client, handler, buf, responseSender) -> {
            // Read network packet payload (entity id)
            Integer paulEntityId = buf.readInt();
            Boolean paulShallCarryBone = buf.readBoolean();

            // Everything in this lambda is run on the render thread
            client.execute(() -> {                
                if (paulEntityId != null && paulEntityId > 0) {
                    // Load Paul...
                    PaulEntity paulEntity = (PaulEntity) client.world.getEntityById(paulEntityId);

                    // ... and toggle his bone layer
                    paulEntity.carryBone(paulShallCarryBone);
                }
            });
        });
    }
}
