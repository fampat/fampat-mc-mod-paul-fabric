package de.fampat.paul.entities;

import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.AttackWithOwnerGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.PounceAtTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TrackOwnerAttackerGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import de.fampat.paul.goals.PaulEatGrassGoal;
import de.fampat.paul.interfaces.IEntityModPersistentData;
import de.fampat.paul.networking.PaulBoneClientCaller;
import de.fampat.paul.networking.PaulSpawnServerListener;
import de.fampat.paul.registry.ModRegistry;

public class PaulEntity extends TameableEntity {
    private static final TrackedData<Boolean> BEGGING = DataTracker.registerData(PaulEntity.class,
            TrackedDataHandlerRegistry.BOOLEAN);

    private float begAnimationProgress;
    private float lastBegAnimationProgress;
    private boolean furWet;
    private boolean canShakeWaterOff;
    private float shakeProgress;
    private float lastShakeProgress;

    private int tongueTick;
    private boolean tongueTickForward;
    public int eatGrassTimer;

    private PaulEatGrassGoal eatGrassGoal;

    private boolean carryBone = false;
    private int carryBoneTimer = 0;
    private int carryBoneMaxTime = 6000;

    @Nullable
    private UUID angryAt;

    public PaulEntity(EntityType<? extends PaulEntity> entityType, World world) {
        super((EntityType<? extends TameableEntity>) entityType, world);

        this.setTamed(true);
        this.setPathfindingPenalty(PathNodeType.POWDER_SNOW, -1.0f);
        this.setPathfindingPenalty(PathNodeType.DANGER_POWDER_SNOW, -1.0f);
        this.tongueTick = 1;
        this.tongueTickForward = true;
    }

    @Override
    protected void initGoals() {
        this.eatGrassGoal = new PaulEatGrassGoal(this);
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(4, new PounceAtTargetGoal(this, 0.4f));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0f, 2.0f, false));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
        this.targetSelector.add(7,
                new ActiveTargetGoal<AbstractSkeletonEntity>((MobEntity) this, AbstractSkeletonEntity.class, false));
        this.goalSelector.add(120, this.eatGrassGoal);
    }

    public static DefaultAttributeContainer.Builder createPaulAttributes() {
        return MobEntity
                .createMobAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.1)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BEGGING, false);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15f, 1.0f);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.random.nextInt(5) == 0) {
            return ModRegistry.PAUL_AMBIENT;
        }

        return SoundEvents.ENTITY_WOLF_STEP;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModRegistry.PAUL_BARK_0;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.6f;
    }

    // Get current tick
    public int getTongueTick() {
        return this.tongueTick;
    }

    @Override
    public boolean isPushedByFluids() {
        // Stronk Paul does not get pushed!
        return false;
    }
    
    @Environment(value = EnvType.CLIENT)
    public float getNeckAngle(float delta) {
        if (this.eatGrassTimer <= 0) {
            return 0.0f;
        }
        if (this.eatGrassTimer >= 4 && this.eatGrassTimer <= 36) {
            return 1.0f;
        }
        if (this.eatGrassTimer < 4) {
            return ((float) this.eatGrassTimer - delta) / 4.0f;
        }
        return -((float) (this.eatGrassTimer - 40) - delta) / 4.0f;
    }

    @Environment(value = EnvType.CLIENT)
    public float getHeadAngle(float delta) {
        if (this.eatGrassTimer > 4 && this.eatGrassTimer <= 36) {
            float f = ((float) (this.eatGrassTimer - 4) - delta) / 32.0f;
            return 0.62831855f + 0.21991149f * MathHelper.sin(f * 28.7f);
        }
        if (this.eatGrassTimer > 0) {
            return 0.62831855f;
        }
        return this.getPitch() * ((float) Math.PI / 180);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.world.isClient) {
            this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
        }

        if (!this.world.isClient && this.furWet && !this.canShakeWaterOff && !this.isNavigating() && this.onGround) {
            this.canShakeWaterOff = true;
            this.shakeProgress = 0.0f;
            this.lastShakeProgress = 0.0f;
            this.world.sendEntityStatus(this, EntityStatuses.SHAKE_OFF_WATER);
        }
    }

    // Tongue aninmation ticking
    private void handleTongueTick() {
        if (this.tongueTick == 10) {
            this.tongueTickForward = false;
        } else if (this.tongueTick == 1) {
            this.tongueTickForward = true;
        }

        if (this.tongueTickForward) {
            this.tongueTick++;
        } else {
            this.tongueTick--;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.isAlive()) {
            return;
        }

        // Tick for tongue animation ticking
        this.handleTongueTick();

        // Tick for bone carrying
        this.handleBoneCarryTick();

        this.lastBegAnimationProgress = this.begAnimationProgress;
        this.begAnimationProgress = this.isBegging()
                ? (this.begAnimationProgress += (1.0f - this.begAnimationProgress) * 0.4f)
                : (this.begAnimationProgress += (0.0f - this.begAnimationProgress) * 0.4f);

        if (this.isWet()) {
            this.furWet = true;
            if (this.canShakeWaterOff && !this.world.isClient) {
                this.world.sendEntityStatus(this, EntityStatuses.RESET_WOLF_SHAKE);
                this.resetShake();
            }
        } else if ((this.furWet || this.canShakeWaterOff) && this.canShakeWaterOff) {
            if (this.shakeProgress == 0.0f) {
                this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(),
                        (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
                this.emitGameEvent(GameEvent.ENTITY_SHAKE);
                this.carryBone(false);
            }
            this.lastShakeProgress = this.shakeProgress;
            this.shakeProgress += 0.05f;

            if (this.lastShakeProgress >= 2.0f) {
                this.furWet = false;
                this.canShakeWaterOff = false;
                this.lastShakeProgress = 0.0f;
                this.shakeProgress = 0.0f;
            }

            if (this.shakeProgress > 0.4f) {
                float f = (float) this.getY();
                int i = (int) (MathHelper.sin((this.shakeProgress - 0.4f) * (float) Math.PI) * 7.0f);
                Vec3d vec3d = this.getVelocity();
                for (int j = 0; j < i; ++j) {
                    float g = (this.random.nextFloat() * 2.0f - 1.0f) * this.getWidth() * 0.5f;
                    float h = (this.random.nextFloat() * 2.0f - 1.0f) * this.getWidth() * 0.5f;
                    this.world.addParticle(ParticleTypes.SPLASH, this.getX() + (double) g, f + 0.8f,
                            this.getZ() + (double) h, vec3d.x, vec3d.y, vec3d.z);
                }
            }
        }
    }

    private void resetShake() {
        this.canShakeWaterOff = false;
        this.shakeProgress = 0.0f;
        this.lastShakeProgress = 0.0f;
    }

    private void removeFromOwner() {
        NbtCompound playerModPersistentData = ((IEntityModPersistentData) this.getOwner()).getModPersistentData();
        if (playerModPersistentData.contains(PaulSpawnServerListener.paulUUIDName)) {
            playerModPersistentData.remove(PaulSpawnServerListener.paulUUIDName);
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.furWet = false;
        this.canShakeWaterOff = false;
        this.carryBone = false;
        this.carryBoneTimer = 0;
        this.eatGrassTimer = 0;
        this.tongueTick = 0;
        this.lastShakeProgress = 0.0f;
        this.shakeProgress = 0.0f;
        this.removeFromOwner();
        super.onDeath(damageSource);
    }

    @Override
    public void onRemoved() {
        this.removeFromOwner();
        super.onRemoved();
    }

    private void handleBoneCarryTick() {
        // No bone "lives" forever
        if (this.isCarryBone() && this.carryBoneTimer > 0) {
            // Countdown bone-carrying
            this.carryBoneTimer--;

            // Done with carrying bone
            if (this.carryBoneTimer <= 0) {
                this.carryBoneTimer = 0;
                this.carryBone(false);
            }
        }
    }

    // Enable or disable carrying a bone
    public void carryBone(boolean shallCarry) {
        if (this.eatGrassTimer == 0) {
            this.carryBone = shallCarry;

            // Handle timer
            if (shallCarry) {
                this.carryBoneTimer = this.carryBoneMaxTime;
            } else {
                this.carryBoneTimer = 0;
            }

            // Sync bone status to clients
            if (!this.world.isClient()) {
                PaulBoneClientCaller.callAllClientsWith(this, shallCarry);
            }
        }
    }

    public boolean isCarryBone() {
        return this.carryBone;
    }

    /**
     * Returns whether Paul's fur is wet.
     * <p>
     * Paul's fur will remain wet until the he shakes.
     */
    public boolean isFurWet() {
        return this.furWet;
    }

    /**
     * Returns Pauls's brightness multiplier based on the fur wetness.
     * <p>
     * The brightness multiplier represents how much darker Paul gets while his fur
     * is wet. The multiplier changes (from 0.75 to 1.0 incrementally) when Paul
     * shakes.
     * 
     * @return Brightness as a float value between 0.75 and 1.0.
     * @see net.minecraft.client.render.entity.model.TintableAnimalModel#setColorMultiplier(float,
     *      float, float)
     * 
     * @param tickDelta progress for linearly interpolating between the previous and
     *                  current game state
     */
    public float getFurWetBrightnessMultiplier(float tickDelta) {
        return Math.min(0.5f + MathHelper.lerp(tickDelta, this.lastShakeProgress, this.shakeProgress) / 2.0f * 0.5f,
                1.0f);
    }

    public float getShakeAnimationProgress(float tickDelta, float f) {
        float g = (MathHelper.lerp(tickDelta, this.lastShakeProgress, this.shakeProgress) + f) / 1.8f;
        if (g < 0.0f) {
            g = 0.0f;
        } else if (g > 1.0f) {
            g = 1.0f;
        }
        return MathHelper.sin(g * (float) Math.PI) * MathHelper.sin(g * (float) Math.PI * 11.0f) * 0.15f
                * (float) Math.PI;
    }

    public float getBegAnimationProgress(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.lastBegAnimationProgress, this.begAnimationProgress) * 0.15f
                * (float) Math.PI;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.8f;
    }

    @Override
    public int getMaxLookPitchChange() {
        if (this.isInSittingPose()) {
            return 20;
        }
        return super.getMaxLookPitchChange();
    }

    @Override
    public boolean isInvulnerable() {
        // Paul cannot die! NEVER!
        return true;
    }

    @Override
    protected void mobTick() {
        this.eatGrassTimer = this.eatGrassGoal.getTimer();
        super.mobTick();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        // Paul cannot be harmed! NEVER!
        return false;
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = target.damage(DamageSource.mob(this),
                (int) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        if (bl) {
            this.applyDamageEffects(this, target);
        }
        return bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);

        // Determine if held item is bone oder food
        boolean isBone = heldItem.isOf(Items.BONE);
        boolean isFood = heldItem.isFood();

        // Maybe not an bone, but nontheless a sort of bone
        if (!isBone) {
            // Check if we have a bone-tagged item
            isBone = heldItem.isIn(TagKey.of(RegistryKeys.ITEM, new Identifier("bones")));
        }

        // ...If its something to eat and he is not carriying a bone, continue
        if (!this.isCarryBone() && (isFood || isBone)) {
            if (this.world.isClient) {
                // Some lovely particles
                for (int i = 0; i < 10; i++) {
                    this.world.addParticle(i % 5 == 0 ? ParticleTypes.HEART : ParticleTypes.HAPPY_VILLAGER,
                            this.getPos().x + this.random.nextFloat() - 0.5f,
                            this.getPos().y + 0.5d + this.random.nextFloat() - 0.5f,
                            this.getPos().z + this.random.nextFloat() - 0.5f, 0, 0, 0);
                }

                // Remove the item from players hand on client, nithing more is needed,
                // rest is handled server-side
                return ActionResult.CONSUME;
            }

            // Reduce count in hand
            heldItem.decrement(1);

            if (isBone) {
                // If its a bone, carry it...
                this.carryBone(true);
            } else {
                // ... else make eat-noises
                this.playSound(SoundEvents.ENTITY_FOX_EAT, this.getSoundVolume(),
                        (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }

            // Add a speed and dmg-boost effect after feeding
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 36000, 0, true, true, false));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 36000, 0, true, true, false));

            return ActionResult.CONSUME;
        } else {
            // Paul is also a mobile ender-chest!
            EnderChestInventory playerEnderChestInventory = player.getEnderChestInventory();

            // Try to open it up!
            if (playerEnderChestInventory != null) {
                // Instantiate the ender-chest ui of interacting player
                player.openHandledScreen(
                        new SimpleNamedScreenHandlerFactory(
                                (syncId, inventory, thisPlayer) -> GenericContainerScreenHandler
                                        .createGeneric9x3(syncId, inventory, playerEnderChestInventory),
                                Text.translatable("paul_enderchest_title.text.paul.fampat.de")));

                // Stats are important!
                player.incrementStat(Stats.OPEN_ENDERCHEST);
                return ActionResult.CONSUME;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.SHAKE_OFF_WATER) {
            this.canShakeWaterOff = true;
            this.shakeProgress = 0.0f;
            this.lastShakeProgress = 0.0f;
        } else if (status == EntityStatuses.RESET_WOLF_SHAKE) {
            this.resetShake();
        } else if (status == EntityStatuses.SET_SHEEP_EAT_GRASS_TIMER_OR_PRIME_TNT_MINECART) {
            this.eatGrassTimer = 40;
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    public int getLimitPerChunk() {
        return 1;
    }

    public void setBegging(boolean begging) {
        this.dataTracker.set(BEGGING, begging);
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return false;
    }

    public boolean isBegging() {
        return this.dataTracker.get(BEGGING);
    }

    @Override
    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
        if (target instanceof PlayerEntity && owner instanceof PlayerEntity
                && !((PlayerEntity) owner).shouldDamagePlayer((PlayerEntity) target)) {
            return false;
        }
        if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity) target).isTame()) {
            return false;
        }
        return !(target instanceof TameableEntity) || !((TameableEntity) target).isTamed();
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Override
    public PassiveEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return null;
    }

    public static boolean canSpawn(EntityType<PaulEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return false;
    }
}
