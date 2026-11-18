package art.tiary.mfqm.physics;

/** Candidate policy, not a measured reproduction of 1.7.10. Units: blocks/game tick. */
public final class MotionMath {
    public enum Phase { OUTSIDE, ENTERING, SUBMERGED, EXITING }
    public record Policy(double entryDepth, double terminal, double depthGain) {
        public Policy {
            if (!Double.isFinite(entryDepth) || !Double.isFinite(terminal) || !Double.isFinite(depthGain)
                    || entryDepth < 0.01 || entryDepth > 0.2 || terminal < 0.02 || terminal > 0.1
                    || depthGain < 0 || depthGain > 0.05) throw new IllegalArgumentException("motion policy bounds");
        }
    }
    private MotionMath() { }
    public static double velocity(MaterialProfile material, double depth, boolean moving, boolean struggle, Policy p) {
        double base = switch (material) { case QUICKSAND -> 0.010; case MUD -> 0.007; case SOFT_QUICKSAND -> 0.013; };
        double safeDepth = Double.isFinite(depth) ? SinkMath.clamp(depth, 0, 16) : 0;
        return -Math.min(p.terminal(), base + p.depthGain() * Math.min(1, safeDepth / 2)
                + (moving ? 0.006 : 0) + (struggle ? 0.015 : 0));
    }
    public static double entryStep(double feet, double surface, double proposed, Policy p) {
        if (!Double.isFinite(feet) || !Double.isFinite(surface) || !Double.isFinite(proposed)) return 0;
        return Math.max(proposed, Math.min(0, surface - p.entryDepth() - feet));
    }
    public static double downwardBudget(double elapsedTicks, Policy p) {
        return p.terminal() * SinkMath.clamp(elapsedTicks, 1, 4) + 0.08;
    }
    public static boolean controlledLanding(boolean contact, boolean passenger, double vertical, Policy p) {
        return contact && !passenger && Double.isFinite(vertical) && vertical <= 0.001 && vertical >= -p.terminal() - 0.001;
    }
}
