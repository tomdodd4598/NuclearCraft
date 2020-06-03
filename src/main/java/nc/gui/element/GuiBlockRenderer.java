/** Massive thanks to CrazyPants, maker of EnderIO and related mods, for letting me use this code! */

package nc.gui.element;

import java.util.List;

import javax.annotation.*;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;

public class GuiBlockRenderer {
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	private static TextureAtlasSprite getTexture(IBlockState state, EnumFacing facing) {
		IBakedModel ibakedmodel = MC.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
		List<BakedQuad> quadList = ibakedmodel.getQuads(state, facing, 0L);
		TextureAtlasSprite sprite = quadList.isEmpty() ? ibakedmodel.getParticleTexture() : quadList.get(0).getSprite();
		return sprite == null ? getMissingSprite() : sprite;
	}
	
	private static @Nonnull TextureAtlasSprite getMissingSprite() {
		return MC.getTextureMapBlocks().getMissingSprite();
	}
	
	public static void renderGuiBlock(@Nullable IBlockState state, EnumFacing facing, int x, int y, double zLevel, int width, int height) {
		if (state == null || facing == null) {
			return;
		}
		TextureAtlasSprite icon = getTexture(state, facing);
		
		MC.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		GlStateManager.enableBlend();
		for (int i = 0; i < width; i += 16) {
			for (int j = 0; j < height; j += 16) {
				int drawWidth = Math.min(width - i, 16);
				int drawHeight = Math.min(height - j, 16);
				
				int drawX = x + i;
				int drawY = y + j;
				
				double minU = icon.getMinU();
				double maxU = icon.getMaxU();
				double minV = icon.getMinV();
				double maxV = icon.getMaxV();
				
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				buffer.pos(drawX, drawY + drawHeight, 0).tex(minU, minV + (maxV - minV) * drawHeight / 16F).endVertex();
				buffer.pos(drawX + drawWidth, drawY + drawHeight, 0).tex(minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F).endVertex();
				buffer.pos(drawX + drawWidth, drawY, 0).tex(minU + (maxU - minU) * drawWidth / 16F, minV).endVertex();
				buffer.pos(drawX, drawY, 0).tex(minU, minV).endVertex();
				tessellator.draw();
			}
		}
		GlStateManager.disableBlend();
	}
}
