package art.tiary.mfqm.integration;

import art.tiary.mfqm.MfqmReborn;
import art.tiary.mfqm.legacy.StuckLease;
import art.tiary.mfqm.physics.SinkConfig;
import art.tiary.mfqm.physics.SinkEngine;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.UUID;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Controlled injected contacts only: automatic material Level producers are BLOCKED. */
@Mod.EventBusSubscriber(modid = MfqmReborn.MOD_ID)
public final class StuckAdapter {
    private static final Map<LivingEntity, State> STATES = new WeakHashMap<>();
    private static final class State implements StuckLease.LevelPort {
        int level = -1;
        long updated = Long.MIN_VALUE;
        int testLevel, remaining;
        public boolean present() { return true; }
        public int level() { return level; }
        public void level(int value) { level = value; }
    }
    private StuckAdapter() { }
    public static boolean enabled() { return SinkConfig.EXPERIMENTAL_STUCK.get(); }
    public static boolean managed(LivingEntity entity) {
        if (entity.level().isClientSide) {
            var speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
            return speed != null && speed.getModifier(StuckLease.UUID_KEY) != null;
        }
        return enabled() && STATES.containsKey(entity);
    }
    /** Server thread, explicit ordered test inputs. No max aggregation or implicit material mapping. */
    public static void contact(LivingEntity entity, int level) {
        if (entity.level().isClientSide || !enabled() || SinkEngine.exempt(entity) || entity.isPassenger()) return;
        StuckLease.contact(STATES.computeIfAbsent(entity, ignored -> new State()), level);
        if (SinkConfig.PHASE7_DIAGNOSTICS.get()) MfqmReborn.LOGGER.info("PHASE7_CONTACT entity={} tick={} level={}", entity.getUUID(), entity.level().getGameTime(), level);
    }
    /** Bounded diagnostic input script, not a material producer. One Contact immediately before each Update. */
    public static void holdForTest(LivingEntity entity, int level, int ticks) {
        if (ticks < 1 || ticks > 200) throw new IllegalArgumentException("Test duration 1..200 ticks");
        contact(entity, level);
        State state = STATES.get(entity);
        if (state != null) { state.testLevel = level; state.remaining = ticks; }
    }
    public static void update(LivingEntity entity) {
        if (entity.level().isClientSide) return;
        if (!enabled() || SinkEngine.exempt(entity) || entity.isPassenger()) { clear(entity); return; }
        State state = STATES.get(entity);
        if (state == null || state.updated == entity.level().getGameTime()) return;
        state.updated = entity.level().getGameTime();
        SinkEngine.removeModifier(entity);
        var speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null) { clear(entity); return; }
        if (state.remaining > 0) { StuckLease.contact(state, state.testLevel); state.remaining--; }
        StuckLease.update(state, entity.horizontalCollision, new StuckLease.AttributePort() {
            public boolean contains(UUID id) { return speed.getModifier(id) != null; }
            public void remove(UUID id) { speed.removeModifier(id); }
            public void addTransient(UUID id, double amount, int operation, boolean saved) {
                if (operation != 2 || saved) throw new IllegalArgumentException("Unsupported legacy lease");
                speed.addTransientModifier(new AttributeModifier(id, "MFQM experimental legacy lease", amount,
                        AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
        });
        if (SinkConfig.PHASE7_DIAGNOSTICS.get()) MfqmReborn.LOGGER.info(
                "PHASE7_STUCK entity={} tick={} level={} remaining={} attribute={}", entity.getUUID(), state.updated, state.level, state.remaining, speed.getValue());
        if (state.level < 0) STATES.remove(entity);
    }
    public static String diagnosticState(LivingEntity entity) {
        State s = STATES.get(entity);
        return "enabled=" + enabled() + " worldTick=" + entity.level().getGameTime() + (s == null ? " no-state" : " updated=" + s.updated + " level=" + s.level + " remaining=" + s.remaining);
    }
    public static void clear(LivingEntity entity) {
        if (entity.level().isClientSide) return;
        if (STATES.containsKey(entity) && SinkConfig.PHASE7_DIAGNOSTICS.get()) MfqmReborn.LOGGER.info(
                "PHASE7_CLEAR entity={} tick={} enabled={} exempt={} caller={}", entity.getUUID(), entity.level().getGameTime(), enabled(), SinkEngine.exempt(entity), Thread.currentThread().getStackTrace()[2]);
        STATES.remove(entity);
        var speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) speed.removeModifier(StuckLease.UUID_KEY);
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void start(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !(event.level instanceof ServerLevel level)) return;
        for (var e : level.getAllEntities()) if (e instanceof LivingEntity living
                && (!enabled() || SinkEngine.exempt(living) || living.isPassenger())) clear(living);
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void end(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.level instanceof ServerLevel level)) return;
        for (var e : level.getAllEntities()) if (e instanceof LivingEntity living) update(living);
    }
    @SubscribeEvent public static void death(LivingDeathEvent e) { clear(e.getEntity()); }
    @SubscribeEvent public static void leave(EntityLeaveLevelEvent e) { if (e.getEntity() instanceof LivingEntity l) clear(l); }
    @SubscribeEvent public static void teleport(EntityTeleportEvent e) { if (e.getEntity() instanceof LivingEntity l) clear(l); }
    @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent e) { clear(e.getEntity()); }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent e) { clear(e.getEntity()); }
    @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent e) { clear(e.getEntity()); }
    @SubscribeEvent public static void respawn(PlayerEvent.PlayerRespawnEvent e) { clear(e.getEntity()); }
    @SubscribeEvent public static void clone(PlayerEvent.Clone e) { clear(e.getOriginal()); clear(e.getEntity()); }
}
