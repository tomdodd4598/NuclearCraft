package nc.render.tile;

import nc.Global;
import nc.block.tile.generator.BlockFusionCore;
import nc.model.tile.ModelFusionCoreCentre;
import nc.model.tile.ModelFusionCoreFrame;
import nc.tile.generator.TileFusionCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFusionCore extends TileEntitySpecialRenderer<TileFusionCore> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(Global.MOD_ID + ":textures/models/fusion_core.png");
	private ModelFusionCoreFrame frame_model;
	private ModelFusionCoreCentre centre_model;
	
	public RenderFusionCore() {
		frame_model = new ModelFusionCoreFrame();
		centre_model = new ModelFusionCoreCentre();
	}
	
	@Override
	public void render(TileFusionCore tile, double posX, double posY, double posZ, float partialTicks, int destroyStage, float alpha) {
		if(!(tile.getBlockType() instanceof BlockFusionCore)) return;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, posZ);
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		frame_model.render(null, 0F, 0F, -0.1F, 0F, 0F, 0.0625F);
		GlStateManager.translate(0.5F, 0F, -0.5F);
		long angle = (((long)tile.efficiency) * System.currentTimeMillis() / 400) % 360;
		GlStateManager.rotate(angle, 0F, 1F, 0F);
		GlStateManager.translate(-0.5F, 0F, 0.5F);
		centre_model.render(null, 0F, 0F, -0.1F, 0F, 0F, 0.0625F);
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
}
