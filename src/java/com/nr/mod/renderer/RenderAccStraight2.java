package com.nr.mod.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.nr.mod.model.ModelAccStraight2;

public class RenderAccStraight2 extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation("nr:textures/model/AccStraight2.png");
	
	private ModelAccStraight2 model;
	
	public RenderAccStraight2() {
		this.model = new ModelAccStraight2();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		
		int i;

		if (tileentity.getWorldObj() == null)
		{
			i = 0;
		} else {
			Block block = tileentity.getBlockType();
			i = tileentity.getBlockMetadata();
			if ((block != null) && (i == 0))
			{
				i = tileentity.getBlockMetadata();
			}
		}
		
		GL11.glPushMatrix();
			GL11.glTranslatef((float)x - 0.5F, (float)y + 0.5F, (float)z + 0.5F);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			
			this.bindTexture(texture);
			
			GL11.glPushMatrix();
			GL11.glRotatef(180.0F, 180.0F, 0.0F, 0.0F);
			
			GL11.glRotatef(90F, 0F, 0F, 1.0F);
			this.model.renderModel(0.0625F);
			GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

}
