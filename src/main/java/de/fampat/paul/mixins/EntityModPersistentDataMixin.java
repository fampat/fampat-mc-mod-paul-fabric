package de.fampat.paul.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.fampat.paul.EntryMain;
import de.fampat.paul.interfaces.IEntityModPersistentData;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

// Define that we whant to mix into Minecraft:Entity
// Note: We need to apply out interface that we need to cast on player,
// or other entities to access the "getModPersistentData"-method
@Mixin(Entity.class)
public class EntityModPersistentDataMixin implements IEntityModPersistentData {
    // Add a new property to class Entity
    private NbtCompound persistentData;

    // Add a new method to class Entity
    public NbtCompound getModPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }

        return this.persistentData;
    }

    // Inject into Entity:writeNbt-method on top (HEAD) and put our mods data in
    // Check: src\main\java\de\fampat\paul\networking\PaulSpawnServerListener.java
    // Because the injected method does have a return value, we add as a second
    // argument "CallbackInfoReturnable<T> info"
    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectMethodWriteNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info) {
        if (this.persistentData != null) {
            nbt.put(EntryMain.NAMESPACE, this.persistentData);
        }
    }

    // Inject into Entity:readNbt-method on top (HEAD) and read our mods data
    // Check: src\main\java\de\fampat\paul\networking\PaulSpawnServerListener.java
    // Because the injected method does not have a return value, we add as a second
    // argument "CallbackInfo info"
    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectMethodReadNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains(EntryMain.NAMESPACE)) {
            this.persistentData = nbt.getCompound(EntryMain.NAMESPACE);
        }
    }
}
