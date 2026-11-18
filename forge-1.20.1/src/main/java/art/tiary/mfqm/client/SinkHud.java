package art.tiary.mfqm.client;

import art.tiary.mfqm.MfqmReborn;
import art.tiary.mfqm.state.SinkCapability;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MfqmReborn.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class SinkHud {
    private SinkHud() { }
    @SubscribeEvent public static void register(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("sink_status", (gui, graphics, partialTick, width, height) -> {
            var mc = Minecraft.getInstance();
            if (mc.player == null || mc.options.hideGui) return;
            var data = SinkCapability.get(mc.player);
            if (data == null || !data.active() && data.air >= 300) return;
            String text = String.format(Locale.ROOT, "MFQM %s | depth %.0f%% | air %d/300",
                    data.active() ? data.material.id : "clear", data.immersion * 100, Math.max(0, data.air));
            graphics.drawString(mc.font, text, 6, 6, 0xFFFFFF, true);
        });
    }
}
