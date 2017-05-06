package nc.render;

import org.lwjgl.opengl.GL11;

import nc.Global;
import nc.block.tile.generator.BlockFusionCore;
import nc.tile.generator.TileFusionCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FusionCoreTESR extends TileEntitySpecialRenderer<TileFusionCore> {
	
	private IModel modelWithCentre, modelWithoutCentre, modelCentre;
    private IBakedModel bakedModelWithCentre, bakedModelWithoutCentre, bakedModelCentre;
	
	public void renderTileEntityAt(TileFusionCore te, double posX, double posY, double posZ, float partialTicks, int destroyStage) {
		
		if(!(te.getBlockType() instanceof BlockFusionCore)) return;
		
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		
		GlStateManager.translate(posX + 0.5, posY, posZ + 0.5);
		//if (!te.isGenerating()) renderBodyWithCentre(te);
		//else {
			renderBodyWithoutCentre(te);
			renderCentre(te);
		//}
		
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
	
	private IBakedModel getBakedModelWithCentre() {
		
		if (bakedModelWithCentre == null) {
			try {
				modelWithCentre = ModelLoaderRegistry.getModel(new ResourceLocation(Global.MOD_ID, "block/fusion_core"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			bakedModelWithCentre = modelWithCentre.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
		}
		return bakedModelWithCentre;
	}
	
	private IBakedModel getBakedModelWithoutCentre() {
		
		if (bakedModelWithoutCentre == null) {
			try {
				modelWithoutCentre = ModelLoaderRegistry.getModel(new ResourceLocation(Global.MOD_ID, "block/fusion_core_1"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			bakedModelWithoutCentre = modelWithoutCentre.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
		}
		return bakedModelWithoutCentre;
	}
	
	private IBakedModel getBakedModelCentre() {
		
		if (bakedModelCentre == null) {
			try {
				modelCentre = ModelLoaderRegistry.getModel(new ResourceLocation(Global.MOD_ID, "block/fusion_core_2"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			bakedModelCentre = modelCentre.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
		}
		return bakedModelCentre;
	}
	
	private void renderBodyWithCentre(TileFusionCore te) {
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		
		GlStateManager.translate(-1, 0, -1);
		GlStateManager.scale(2, 2, 2);
		
		RenderHelper.disableStandardItemLighting();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		World world = getWorld();
		GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
		
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
				world,
				getBakedModelWithCentre(),
				world.getBlockState(te.getPos()),
				te.getPos(),
				Tessellator.getInstance().getBuffer(), false);
		tessellator.draw();
		
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
    }
	
	private void renderBodyWithoutCentre(TileFusionCore te) {
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		
		GlStateManager.translate(-1, 0, -1);
		GlStateManager.scale(2, 2, 2);
		
		RenderHelper.disableStandardItemLighting();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		World world = getWorld();
		GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
		
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
				world,
				getBakedModelWithoutCentre(),
				world.getBlockState(te.getPos()),
				te.getPos(),
				Tessellator.getInstance().getBuffer(), false);
		tessellator.draw();
		
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
    }
	
	private void renderCentre(TileFusionCore te) {
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		
		long angle = (((long)te.efficiency) * System.currentTimeMillis() / 400) % 360;
		GlStateManager.rotate(angle, 0, 1, 0);
		GlStateManager.scale(2, 2, 2);
		
		RenderHelper.disableStandardItemLighting();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		World world = getWorld();
		GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
		
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
				world,
				getBakedModelCentre(),
				world.getBlockState(te.getPos()),
				te.getPos(),
				Tessellator.getInstance().getBuffer(), false);
		tessellator.draw();
		
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
    }
}
