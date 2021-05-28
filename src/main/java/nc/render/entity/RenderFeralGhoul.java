package nc.render.entity;

import nc.Global;
import nc.entity.EntityFeralGhoul;
import nc.model.entity.ModelFeralGhoul;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RenderFeralGhoul extends RenderBiped<EntityFeralGhoul> {
	
	private static final ResourceLocation FERAL_GHOUL_TEXTURES = new ResourceLocation(Global.MOD_ID + ":textures/entity/feral_ghoul.png");
	
	public RenderFeralGhoul(RenderManager renderManager) {
		super(renderManager, new ModelFeralGhoul(), 0.5F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityFeralGhoul entity) {
		return FERAL_GHOUL_TEXTURES;
	}
}
