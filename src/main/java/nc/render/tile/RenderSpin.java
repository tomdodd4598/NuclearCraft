package nc.render.tile;

import org.lwjgl.opengl.GL11;

import nc.Global;
import nc.block.tile.quantum.BlockSpin;
import nc.tile.quantum.TileSpin;
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
public class RenderSpin extends TileEntitySpecialRenderer<TileSpin> {
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	private IModel modelAmbient, modelUp, modelDown;
	private IBakedModel bakedModelAmbient, bakedModelUp, bakedModelDown;
	
	@Override
	public void render(TileSpin te, double posX, double posY, double posZ, float partialTicks, int destroyStage, float alpha) {
		if(!(te.getBlockType() instanceof BlockSpin)) return;
		
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		
		GlStateManager.translate(posX, posY, posZ);
		
		if (te.isMeasured()) {
			if (te.measuredSpin > 0.49D && te.measuredSpin < 0.51D) renderUp(te);
			else renderDown(te);
		}
		else renderAmbient(te);
		
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
	
	private IBakedModel getBakedModelAmbient() {
		if (bakedModelAmbient == null) {
			try {
				modelAmbient = ModelLoaderRegistry.getModel(new ResourceLocation(Global.MOD_ID, "block/spin"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			bakedModelAmbient = modelAmbient.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, location -> MC.getTextureMapBlocks().getAtlasSprite(location.toString()));
		}
		return bakedModelAmbient;
	}
	
	private IBakedModel getBakedModelUp() {
		if (bakedModelUp == null) {
			try {
				modelUp = ModelLoaderRegistry.getModel(new ResourceLocation(Global.MOD_ID, "block/spin_up"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			bakedModelUp = modelUp.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, location -> MC.getTextureMapBlocks().getAtlasSprite(location.toString()));
		}
		return bakedModelUp;
	}

	private IBakedModel getBakedModelDown() {
		if (bakedModelDown == null) {
			try {
				modelDown = ModelLoaderRegistry.getModel(new ResourceLocation(Global.MOD_ID, "block/spin_down"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			bakedModelDown = modelDown.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, location -> MC.getTextureMapBlocks().getAtlasSprite(location.toString()));
		}
		return bakedModelDown;
	}
	
	private void renderAmbient(TileSpin te) {
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		
		RenderHelper.disableStandardItemLighting();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		World world = getWorld();
		GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
		
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		MC.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
				world,
				getBakedModelAmbient(),
				world.getBlockState(te.getPos()),
				te.getPos(),
				Tessellator.getInstance().getBuffer(), false);
		tessellator.draw();
		
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
	
	private void renderUp(TileSpin te) {
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		
		RenderHelper.disableStandardItemLighting();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		World world = getWorld();
		GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
		
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		MC.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
				world,
				getBakedModelUp(),
				world.getBlockState(te.getPos()),
				te.getPos(),
				Tessellator.getInstance().getBuffer(), false);
		tessellator.draw();
		
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
	
	private void renderDown(TileSpin te) {
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		
		RenderHelper.disableStandardItemLighting();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		World world = getWorld();
		GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
		
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		MC.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
				world,
				getBakedModelDown(),
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