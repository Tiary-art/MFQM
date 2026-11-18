package art.tiary.mfqm.physics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/** Blocks supply shapes only. All physiological and motion effects are settled centrally. */
public class SinkingBlock extends Block implements SinkingMedium {
    private final MaterialProfile profile;

    public SinkingBlock(MaterialProfile profile, BlockBehaviour.Properties properties) {
        super(properties.noOcclusion().dynamicShape().isSuffocating((s, l, p) -> false)
                .isViewBlocking((s, l, p) -> false));
        this.profile = profile;
    }

    public double motionScale() { return 1; }

    public MaterialProfile profile() { return profile; }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    public double surfaceHeight(BlockState state, BlockGetter level, BlockPos pos) { return profile.surfaceHeight; }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Vanilla ray reach and subsequent solid occlusion remain unchanged. Only the enclosing
        // cell is skipped when the ray starts inside its material (no through-wall targeting).
        if (context instanceof net.minecraft.world.phys.shapes.EntityCollisionContext ec && ec.getEntity() != null) {
            var eye=ec.getEntity().getEyePosition();
            if (BlockPos.containing(eye).equals(pos) && eye.y < pos.getY()+surfaceHeight(state,level,pos)) return Shapes.empty();
        }
        return Block.box(0, 0, 0, 16, surfaceHeight(state, level, pos) * 16, 16);
    }
}
