package nc.render;

import nc.block.NCBlocks;
import nc.entity.EntityNukePrimed;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNukePrimed extends Render {
	
	private RenderBlocks blockRenderer = new RenderBlocks();
	
	public RenderNukePrimed()
    {
        this.shadowSize = 0.5F;
    }
	
	public void doRender(EntityNukePrimed entity, double x, double y, double z, float f1, float fl2)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        float f2;

        if ((float)entity.fuse - fl2 + 1.0F < 10.0F)
        {
            f2 = 1.0F - ((float)entity.fuse - fl2 + 1.0F) / 10.0F;

            if (f2 < 0.0F)
            {
                f2 = 0.0F;
            }

            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }

            f2 *= f2;
            f2 *= f2;
            float f3 = 1.0F + f2 * 0.3F;
            GL11.glScalef(f3, f3, f3);
        }

        f2 = (1.0F - ((float)entity.fuse - fl2 + 1.0F) / 100.0F) * 0.8F;
        this.bindEntityTexture(entity);
        this.blockRenderer.renderBlockAsItem(NCBlocks.nukeE, 0, entity.getBrightness(fl2));

        if (entity.fuse / 5 % 2 == 0)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f2);
            this.blockRenderer.renderBlockAsItem(NCBlocks.nukeE, 0, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.1F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glPopMatrix();
    }
	
	protected ResourceLocation getEntityTexture(EntityNukePrimed p_110775_1_)
    {
        return TextureMap.locationBlocksTexture;
    }

    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityNukePrimed)p_110775_1_);
    }

    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityNukePrimed)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}