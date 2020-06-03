package nc.render.entity;

import nc.Global;
import nc.entity.EntityFeralGhoul;
import nc.model.entity.ModelFeralGhoul;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RenderFeralGhoul extends RenderLiving<EntityFeralGhoul> {
	
	private static final ResourceLocation FERAL_GHOUL_TEXTURES = new ResourceLocation(Global.MOD_ID + ":textures/entity/feral_ghoul.png");
	
	public RenderFeralGhoul(RenderManager renderManager) {
		super(renderManager, new ModelFeralGhoul(), 0.5F);
		addLayer(new LayerCustomHead(((ModelFeralGhoul) mainModel).bipedHead));
		addLayer(new LayerElytra(this));
		addLayer(new LayerHeldItem(this) {
			
			@Override
			protected void translateToHand(EnumHandSide side) {
				((ModelFeralGhoul) mainModel).postRenderArm(0.0625F, side);
			}
		});
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityFeralGhoul entity) {
		return FERAL_GHOUL_TEXTURES;
	}
	
	@Override
	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0F, 0.1875F, 0F);
	}
}
