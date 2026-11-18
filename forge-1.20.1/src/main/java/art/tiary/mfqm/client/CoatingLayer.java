package art.tiary.mfqm.client;

import art.tiary.mfqm.MfqmReborn;
import art.tiary.mfqm.state.SinkCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Independent translucent layer; never replaces the player's skin or resource pack. Armor/hand coverage remains partial. */
@Mod.EventBusSubscriber(modid=MfqmReborn.MOD_ID,value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
public final class CoatingLayer extends RenderLayer<AbstractClientPlayer,PlayerModel<AbstractClientPlayer>> {
    public CoatingLayer(PlayerRenderer parent) { super(parent); }
    @SubscribeEvent public static void layers(EntityRenderersEvent.AddLayers e) {
        for(String skin:e.getSkins()) { PlayerRenderer r=e.getSkin(skin); if(r!=null) r.addLayer(new CoatingLayer(r)); }
    }
    @Override public void render(PoseStack poses,MultiBufferSource buffers,int light,AbstractClientPlayer p,float swing,float amount,float partial,float age,float yaw,float pitch) {
        var d=SinkCapability.get(p);
        if(d==null || d.residueTicks<=0 || p.isInvisible()) return;
        String texture=d.residueType.equals("mud") ? "mud" : d.residueType.equals("soft_quicksand") ? "red_sand" : "sand";
        float alpha=Math.min(.65f,d.residueTicks/500f);
        poses.pushPose(); poses.scale(1.008f,1.008f,1.008f);
        getParentModel().renderToBuffer(poses,buffers.getBuffer(RenderType.entityTranslucent(new ResourceLocation("minecraft","textures/block/"+texture+".png"))),light,OverlayTexture.NO_OVERLAY,1,1,1,alpha);
        poses.popPose();
    }
}
