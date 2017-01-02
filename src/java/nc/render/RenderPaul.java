package nc.render;

import nc.entity.EntityPaul;
import nc.model.ModelPaul;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderPaul extends RenderLiving {

	private static final ResourceLocation texture = new ResourceLocation("nc:textures/model/paul.png");
	
	protected ModelPaul modelEntity;
	
	public RenderPaul(ModelBase p_i1262_1_, float p_i1262_2_) {
		super(p_i1262_1_, p_i1262_2_);
		modelEntity = ((ModelPaul) mainModel);
	}

	public void renderPaul(EntityPaul entity, double x, double y, double z, float u, float v) {
		super.doRender(entity, x, y, z, u, v);
	}
	
	public void doRenderLiving(EntityLiving entityLiving, double x, double y, double z, float u, float v) {
		renderPaul((EntityPaul)entityLiving, x, y, z, u, v);
	}
	
	public void doRender(Entity entity, double x, double y, double z, float u, float v) {
		renderPaul((EntityPaul)entity, x, y, z, u, v);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return texture;
	}
	
}
