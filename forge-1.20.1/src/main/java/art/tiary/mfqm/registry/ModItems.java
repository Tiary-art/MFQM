package art.tiary.mfqm.registry;

import art.tiary.mfqm.MfqmReborn;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MfqmReborn.MOD_ID);
    public static final RegistryObject<Item> DEV_TEST_BLOCK = ITEMS.register("dev_test_block",
            () -> new BlockItem(ModBlocks.DEV_TEST_BLOCK.get(), new Item.Properties()));

    private ModItems() { }
}
