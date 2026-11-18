package art.tiary.mfqm.physics;

import art.tiary.mfqm.MfqmReborn;
import art.tiary.mfqm.network.SinkNetwork;
import art.tiary.mfqm.state.SinkCapability;
import art.tiary.mfqm.state.SinkState;
import java.util.UUID;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MfqmReborn.MOD_ID)
public final class SinkEngine {
    public static final UUID SLOWNESS_ID = UUID.fromString("035c9be5-623c-4d21-8145-67c31f5ac309");
    private SinkEngine() { }

    @SubscribeEvent
    public static void tick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.level instanceof ServerLevel level)) return;
        for (var entity : level.getAllEntities()) {
            if (entity instanceof LivingEntity living && !living.isRemoved()) settle(level, living);
        }
    }

    public static boolean exempt(LivingEntity entity) {
        return !entity.isAlive() || entity.isSpectator() || entity.isInvulnerable()
                || entity instanceof Player p && (p.isCreative() || p.getAbilities().invulnerable);
    }

    /** Public for integration tests; the guard also protects against accidental repeated callbacks. */
    public static void settle(ServerLevel level, LivingEntity entity) {
        SinkState data = SinkCapability.get(entity);
        if (data == null) return;
        long time = level.getGameTime();
        String dimension = level.dimension().location().toString();
        if (data.lastTick == time && data.lastDimension.equals(dimension)) return;
        data.lastTick = time;
        data.lastDimension = dimension;
        data.settlements++;
        boolean before = data.active() || data.wasActive;
        if (!finitePosition(entity)) {
            cleanup(entity, data, true);
            // Never emit a non-finite coordinate. Vanilla normally rejects these before this point.
            var spawn = level.getSharedSpawnPos();
            entity.setPos(spawn.getX() + 0.5, spawn.getY() + 1, spawn.getZ() + 0.5);
            entity.setDeltaMovement(Vec3.ZERO);
            MfqmReborn.LOGGER.warn("Recovered non-finite entity coordinates for {}", entity.getUUID());
            return;
        }
        if (exempt(entity)) {
            cleanup(entity, data, true);
            finish(entity, data, before, time);
            return;
        }
        if (data.active() && data.hasPrevious && entity instanceof ServerPlayer player && SinkNetwork.connected(player)) {
            reconcilePlayer(player, data);
        }
        var contact = ContactSampler.sample(level, entity);
        if (contact.material() == null) {
            data.leaveContact();
            removeModifier(entity);
        } else {
            data.material = contact.material();
            data.immersion = contact.immersion();
            data.contacts = contact.cells();
            data.mudDepth = contact.mudDepth();
            data.residueType = data.material.id;
            data.residueTicks = Math.min(10000, data.residueTicks + 5);
            if (data.inputTick == Long.MIN_VALUE || time - data.inputTick > 10) data.jumpHeld = false;
            boolean struggle = data.jumpHeld && (data.lastStruggleTick == Long.MIN_VALUE || time - data.lastStruggleTick >= 8);
            if (struggle) data.lastStruggleTick = time;
            boolean moving = data.hasPrevious && Math.hypot(entity.getX() - data.previousX, entity.getZ() - data.previousZ) > 0.0015;
            var velocity = entity.getDeltaMovement();
            var result = SinkMath.solve(data.material, new SinkMath.Motion(velocity.x, velocity.y, velocity.z),
                    data.immersion, contact.depthBlocks(), contact.centerX(), contact.centerZ(), moving, struggle, entity.onGround());
            data.stuck = result.stuck();
            var v = result.motion();
            data.lastVertical = v.y();
            entity.setDeltaMovement(v.x(), v.y(), v.z());
            entity.hasImpulse = true;
            entity.fallDistance = 0;
            setModifier(entity, result.speedFactor());
            if (entity instanceof ServerPlayer player && player.connection != null) {
                player.connection.send(new ClientboundSetEntityMotionPacket(entity));
            }
        }
        data.eyeCovered = contact.eyes();
        boolean oxygen = entity instanceof Player ? SinkConfig.PLAYER_OXYGEN.get() : SinkConfig.MOB_OXYGEN.get();
        data.air = (int) SinkMath.clamp(data.air, -1, 300);
        if (data.eyeCovered && oxygen) data.air = Math.max(-1, data.air - 1);
        else data.air = oxygen ? Math.min(300, data.air + 5) : 300;
        if (data.eyeCovered && (!oxygen || data.air < 0) && time % 16 == 0 && data.lastDamageTick != time) {
            data.lastDamageTick = time;
            if (oxygen) data.air = 0;
            float damage = (float) (Math.max(entity.getMaxHealth() * 0.1, 2) * SinkConfig.DAMAGE_MULTIPLIER.get());
            if (Float.isFinite(damage) && damage > 0 && !entity.isInvulnerableTo(entity.damageSources().drown())) {
                data.damageEvents++;
                entity.hurt(entity.damageSources().drown(), damage);
            }
        }
        if (!data.active() && data.residueTicks > 0) data.residueTicks--;
        finish(entity, data, before, time);
    }

    private static boolean finitePosition(LivingEntity entity) {
        return Double.isFinite(entity.getX()) && Double.isFinite(entity.getY()) && Double.isFinite(entity.getZ());
    }

    private static void reconcilePlayer(ServerPlayer player, SinkState data) {
        double dx = player.getX() - data.previousX, dy = player.getY() - data.previousY, dz = player.getZ() - data.previousZ;
        // Unknown server-side teleports from other mods must not drag an entity back into a pit.
        // Forge teleport events are handled explicitly; this is a fallback for direct position APIs.
        if (dx * dx + dy * dy + dz * dz > 16 * 16) {
            data.leaveContact();
            data.hasPrevious = false;
            removeModifier(player);
            return;
        }
        var limit = SinkMath.limitPlayerStep(new SinkMath.Motion(dx, dy, dz), data.lastVertical,
                player.getAttributeValue(Attributes.MOVEMENT_SPEED), data.material == MaterialProfile.MUD && data.mudDepth == 0);
        if (Math.abs(dx - limit.x()) + Math.abs(dy - limit.y()) + Math.abs(dz - limit.z()) > 0.001) {
            player.connection.teleport(data.previousX + limit.x(), data.previousY + limit.y(), data.previousZ + limit.z(),
                    player.getYRot(), player.getXRot());
        }
    }

    private static void finish(LivingEntity entity, SinkState data, boolean before, long time) {
        data.previousX = entity.getX(); data.previousY = entity.getY(); data.previousZ = entity.getZ();
        data.hasPrevious = true;
        if (before != data.active() || (data.active() || data.air < 300) && time % 5 == 0
                || data.air == 300 && data.lastSyncedAir != 300) {
            SinkNetwork.sync(entity, data);
            data.lastSyncedAir = data.air;
        }
        data.wasActive = data.active();
        if (data.trace != null) data.trace.record(entity, data, time);
    }

    public static void removeModifier(LivingEntity entity) {
        var speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) speed.removeModifier(SLOWNESS_ID);
    }

    private static void setModifier(LivingEntity entity, double factor) {
        var speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null) return;
        var existing = speed.getModifier(SLOWNESS_ID);
        double amount = SinkMath.clamp(factor, 0.02, 1) - 1;
        if (existing != null && Math.abs(existing.getAmount() - amount) < 0.0001) return;
        speed.removeModifier(SLOWNESS_ID);
        speed.addTransientModifier(new AttributeModifier(SLOWNESS_ID, "MFQM temporary stuck", amount,
                AttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    public static void cleanup(LivingEntity entity, SinkState data, boolean refillAir) {
        removeModifier(entity);
        data.leaveContact();
        if (refillAir) data.air = 300;
    }
}
