package com.nr.mod.renderer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import com.nr.mod.entity.EntityNuclearMonster;
import com.nr.mod.model.ModelNuclearMonster;

public class RenderNuclearMonster extends RenderLiving {

	private static final ResourceLocation texture = new ResourceLocation("nr:textures/model/NuclearMonster.png");
	
	protected ModelNuclearMonster modelEntity;
	
	public RenderNuclearMonster(ModelBase p_i1262_1_, float p_i1262_2_) {
		super(p_i1262_1_, p_i1262_2_);
		modelEntity = ((ModelNuclearMonster) mainModel);
	}

	public void renderNuclearMonster(EntityNuclearMonster entity, double x, double y, double z, float u, float v) {
		super.doRender(entity, x, y, z, u, v);
	}
	
	public void doRenderLiving(EntityLiving entityLiving, double x, double y, double z, float u, float v) {
		renderNuclearMonster((EntityNuclearMonster)entityLiving, x, y, z, u, v);
	}
	
	public void doRender(Entity entity, double x, double y, double z, float u, float v) {
		renderNuclearMonster((EntityNuclearMonster)entity, x, y, z, u, v);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return texture;
	}
	
}
