package art.tiary.mfqm.registry;

import art.tiary.mfqm.MfqmReborn;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MfqmReborn.MOD_ID);
    // Ordinary solid block: deliberately no custom sinking, suffocation or rescue behavior.
    public static final RegistryObject<Block> DEV_TEST_BLOCK = BLOCKS.register("dev_test_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SAND).strength(0.5F)));

    private ModBlocks() { }
}
