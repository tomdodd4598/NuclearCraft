package nc.render.entity;

import nc.Global;
import nc.entity.EntityFeralGhoul;
import nc.model.entity.ModelFeralGhoul;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RenderFeralGhoul extends RenderBiped<EntityFeralGhoul> {
	
	protected final ResourceLocation texture;
	
	public RenderFeralGhoul(RenderManager renderManager, String texture, String glow) {
		super(renderManager, new ModelFeralGhoul(), 0.5F);
		this.texture = new ResourceLocation(Global.MOD_ID + ":textures/entity/" + texture + ".png");
		if (glow != null) {
			addLayer(new LayerRendererFeralGhoulGlow(this, new ResourceLocation(Global.MOD_ID + ":textures/entity/" + glow + ".png")));
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityFeralGhoul entity) {
		return texture;
	}
}
