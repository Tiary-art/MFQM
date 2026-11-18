package art.tiary.mfqm.physics;

import net.minecraftforge.common.ForgeConfigSpec;

public final class SinkConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue PLAYER_OXYGEN;
    public static final ForgeConfigSpec.BooleanValue MOB_OXYGEN;
    public static final ForgeConfigSpec.DoubleValue DAMAGE_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue ENTRY_DEPTH, TERMINAL, DEPTH_GAIN;
    public static MotionMath.Policy motionPolicy() {
        return new MotionMath.Policy(ENTRY_DEPTH.get(), TERMINAL.get(), DEPTH_GAIN.get());
    }
    static {
        var builder = new ForgeConfigSpec.Builder();
        PLAYER_OXYGEN = builder.comment("Use the separate 300-tick oxygen reservoir for players.")
                .define("playerOxygen", true);
        MOB_OXYGEN = builder.define("mobOxygen", true);
        DAMAGE_MULTIPLIER = builder.defineInRange("drowningDamageMultiplier", 1.0, 0.0, 100.0);
        builder.push("motionCandidate");
        ENTRY_DEPTH = builder.comment("B1 candidate; blocks of first-step penetration, pending gameplay approval.")
                .defineInRange("entryDepth", 0.08, 0.01, 0.2);
        TERMINAL = builder.comment("Maximum continuous descent in blocks/game tick; not legacy parity.")
                .defineInRange("terminalDescent", 0.055, 0.02, 0.1);
        DEPTH_GAIN = builder.defineInRange("depthGain", 0.020, 0.0, 0.05);
        builder.pop();
        SPEC = builder.build();
    }
    private SinkConfig() { }
}
