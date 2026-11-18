package art.tiary.mfqm.physics;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class ContactSampler {
    public record Contact(MaterialProfile material, double surface, double immersion, double depthBlocks,
                          double centerX, double centerZ, boolean eyes, int cells, int mudDepth) { }
    private ContactSampler() { }

    public static Contact sample(ServerLevel level, LivingEntity entity) {
        var box = entity.getBoundingBox();
        MaterialProfile selected = null;
        double best = -1, surface = 0, cx = 0, cz = 0;
        int cells = 0, mudDepth = 0;
        // Bound atypically large entities; no chunk loads and no unbounded scans.
        int minX = Mth.floor(Math.max(box.minX + 1.0e-6, entity.getX() - 4));
        int maxX = Mth.floor(Math.min(box.maxX - 1.0e-6, entity.getX() + 4));
        int minZ = Mth.floor(Math.max(box.minZ + 1.0e-6, entity.getZ() - 4));
        int maxZ = Mth.floor(Math.min(box.maxZ - 1.0e-6, entity.getZ() + 4));
        int minY = Mth.floor(entity.getY() - 0.05);
        int maxY = Mth.floor(Math.min(box.maxY - 1.0e-6, entity.getY() + 4));
        for (int x = minX; x <= maxX; x++) for (int z = minZ; z <= maxZ; z++) {
            for (int y = maxY; y >= minY; y--) {
                BlockPos pos = new BlockPos(x, y, z);
                if (!level.hasChunkAt(pos)) continue;
                var state = level.getBlockState(pos);
                if (!(state.getBlock() instanceof SinkingBlock block)) continue;
                MaterialProfile profile = block.profile();
                double localSurface = y + profile.surfaceHeight;
                // Interior soft-sand cells fill to the cell above; only the top surface is 0.85.
                BlockPos top = pos;
                for (int step = 0; step < 16; step++) {
                    var above = top.above();
                    if (!level.hasChunkAt(above) || level.getBlockState(above).getBlock() != block) break;
                    top = above;
                }
                localSurface = top.getY() + profile.surfaceHeight;
                if (entity.getY() > localSurface + 0.05 || box.maxY <= y) continue;
                cells++;
                double overlap = Math.min(box.maxX, x + 1) - Math.max(box.minX, x);
                overlap *= Math.min(box.maxZ, z + 1) - Math.max(box.minZ, z);
                double score = overlap * Math.min(entity.getBbHeight(), Math.max(0, localSurface - entity.getY()));
                if (score > best + 1.0e-9 || Math.abs(score - best) < 1.0e-9
                        && (selected == null || profile.priority > selected.priority)) {
                    selected = profile;
                    best = score;
                    surface = localSurface;
                    cx = x + 0.5;
                    cz = z + 0.5;
                    mudDepth = block instanceof MudBlock
                            ? MudGeometry.effectiveDepth(MudBlock.grid(level, pos), state.getValue(MudBlock.DEPTH)) : 0;
                }
            }
        }
        BlockPos eye = BlockPos.containing(entity.getEyePosition());
        var eyeState = level.getBlockState(eye);
        boolean covered = false;
        if (eyeState.getBlock() instanceof SinkingBlock eyeBlock) {
            double eyeSurface = eye.getY() + eyeBlock.profile().surfaceHeight;
            if (level.getBlockState(eye.above()).getBlock() == eyeBlock) eyeSurface = eye.getY() + 1;
            covered = entity.getEyeY() < eyeSurface;
        }
        double depth = selected == null ? 0 : Math.max(0, surface - entity.getY());
        return new Contact(selected, surface, SinkMath.clamp(depth / Math.max(entity.getBbHeight(), 0.01), 0, 1),
                depth, cx - entity.getX(), cz - entity.getZ(), covered, cells, mudDepth);
    }
}
