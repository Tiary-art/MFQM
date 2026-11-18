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
public class SinkingBlock extends Block {
    private final MaterialProfile profile;

    public SinkingBlock(MaterialProfile profile, BlockBehaviour.Properties properties) {
        super(properties.noOcclusion().dynamicShape().isSuffocating((s, l, p) -> false)
                .isViewBlocking((s, l, p) -> false));
        this.profile = profile;
    }

    public MaterialProfile profile() { return profile; }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0, 0, 0, 16, profile.surfaceHeight * 16, 16);
    }
}
