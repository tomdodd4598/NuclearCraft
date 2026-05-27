package nc.render.entity;

import nc.entity.EntityFeralGhoul;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.GlStateManager.*;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class LayerRendererFeralGhoulGlow implements LayerRenderer<EntityFeralGhoul> {
	
	private final RenderFeralGhoul renderer;
	private final ResourceLocation glowTexture;
	
	public LayerRendererFeralGhoulGlow(RenderFeralGhoul renderer, ResourceLocation glowTexture) {
		this.renderer = renderer;
		this.glowTexture = glowTexture;
	}
	
	@Override
	public void doRenderLayer(EntityFeralGhoul entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		renderer.bindTexture(glowTexture);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		GlStateManager.depthMask(!entity.isInvisible());
		
		int packedLight = 61680;
		int lightX = packedLight % 65536;
		int lightY = packedLight / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightX, lightY);
		GlStateManager.enableLighting();
		GlStateManager.color(1F, 1F, 1F, 1F);
		renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		
		int entityLight = entity.getBrightnessForRender();
		lightX = entityLight % 65536;
		lightY = entityLight / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightX, lightY);
		renderer.setLightmap(entity);
		
		GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
