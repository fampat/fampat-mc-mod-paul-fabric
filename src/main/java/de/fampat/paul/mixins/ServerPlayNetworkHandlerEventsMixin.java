package de.fampat.paul.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.fampat.paul.networking.PaulSpawnServerListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;

@Mixin(ServerPlayNetworkHandler.class)
abstract class ServerPlayNetworkHandlerEventsMixin {
    @Inject(method = "requestTeleport(DDDFF)V", at = @At("HEAD"))
    public void onRequestTeleport1(CallbackInfo info) {
        // Fetch the target class instance
        ServerPlayNetworkHandler networkHandler = (ServerPlayNetworkHandler) (Object) this;
        
        // Despawn paul
        PaulSpawnServerListener.despawnPaul(networkHandler.player, null);
    }

    @Inject(method = "requestTeleport(DDDFFLjava/util/Set;)V", at = @At("HEAD"))
    public void onRequestTeleport2(CallbackInfo info) {       
        // Fetch the target class instance
        ServerPlayNetworkHandler networkHandler = (ServerPlayNetworkHandler) (Object) this;
        
        // Despawn paul
        PaulSpawnServerListener.despawnPaul(networkHandler.player, null);
    }

    @Inject(method = "requestTeleport(DDDFFLjava/util/Set;Z)V", at = @At("HEAD"))
    public void onRequestTeleport3(CallbackInfo info) {     
        // Fetch the target class instance
        ServerPlayNetworkHandler networkHandler = (ServerPlayNetworkHandler) (Object) this;
        
        // Despawn paul
        PaulSpawnServerListener.despawnPaul(networkHandler.player, null);
    }
}
