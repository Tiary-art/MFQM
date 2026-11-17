package art.tiary.mfqm;

import art.tiary.mfqm.registry.ModBlocks;
import art.tiary.mfqm.registry.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MfqmReborn.MOD_ID)
public final class MfqmReborn {
    public static final String MOD_ID = "mfqm";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MfqmReborn() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        bus.addListener(this::commonSetup);
        bus.addListener(this::creativeContents);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("MFQM Task 002 foundation initialized; development block only, no quicksand physics");
    }

    private void creativeContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModItems.DEV_TEST_BLOCK);
        }
    }
}
