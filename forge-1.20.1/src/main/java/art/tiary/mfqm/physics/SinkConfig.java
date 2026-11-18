package art.tiary.mfqm.physics;

import net.minecraftforge.common.ForgeConfigSpec;

public final class SinkConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue PLAYER_OXYGEN;
    public static final ForgeConfigSpec.BooleanValue MOB_OXYGEN;
    public static final ForgeConfigSpec.DoubleValue DAMAGE_MULTIPLIER;
    static {
        var builder = new ForgeConfigSpec.Builder();
        PLAYER_OXYGEN = builder.comment("Use the separate 300-tick oxygen reservoir for players.")
                .define("playerOxygen", true);
        MOB_OXYGEN = builder.define("mobOxygen", true);
        DAMAGE_MULTIPLIER = builder.defineInRange("drowningDamageMultiplier", 1.0, 0.0, 100.0);
        SPEC = builder.build();
    }
    private SinkConfig() { }
}
