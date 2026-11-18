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

    public static final RegistryObject<Item> QUICKSAND = ITEMS.register("quicksand",
            () -> new BlockItem(ModBlocks.QUICKSAND.get(), new Item.Properties()));
    public static final RegistryObject<Item> MUD = ITEMS.register("mud",
            () -> new BlockItem(ModBlocks.MUD.get(), new Item.Properties()));
    public static final RegistryObject<Item> SOFT_QUICKSAND = ITEMS.register("soft_quicksand",
            () -> new BlockItem(ModBlocks.SOFT_QUICKSAND.get(), new Item.Properties()));

    private ModItems() { }
}
