package de.fampat.paul.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.fampat.paul.networking.PaulSpawnServerListener;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityEventsMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    protected void onOnDeath(CallbackInfo info) {
        // Fetch the target class instance
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) (Object) this;

        // Despawn paul
        PaulSpawnServerListener.despawnPaul(serverPlayer, null);
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    protected void onOnDisconnect(CallbackInfo info) {
        // Fetch the target class instance
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) (Object) this;
        
        PaulSpawnServerListener.despawnPaul(serverPlayer, null);
    }

    @Inject(method = "moveToWorld", at = @At("HEAD"))
    protected void onMoveToWorld(CallbackInfoReturnable<Entity> info) {
        // Fetch the target class instance
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) (Object) this;

        PaulSpawnServerListener.despawnPaul(serverPlayer, null);
    }
}
