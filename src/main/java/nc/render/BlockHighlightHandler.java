/* All credit goes to McJty:
 * 'highlightBlock' comes from XNet: mcjty.xnet.RenderWorldLastEventHandler.renderHilightedBlock,
 * 'renderBlockOutline' comes from McJtyLib: mcjty.lib.client.BlockOutlineRenderer.renderHighLightedBlocksOutline,
 * the other methods comes from XNet: mcjty.xnet.ClientInfo.
 */

package nc.render;

import org.lwjgl.opengl.GL11;

import nc.NuclearCraft;
import nc.util.NCMath;
import nc.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHighlightHandler {
	
	private static BlockHighlightTracker getOverlayTracker() {
		return NuclearCraft.instance.blockOverlayTracker;
	}
	
	@SubscribeEvent
	public void highlightBlock(RenderWorldLastEvent event) {
		BlockPos pos = getOverlayTracker().getHighlightPos();
		if (pos == null) return;
		
		long time = System.currentTimeMillis();
		
		if (time > getOverlayTracker().getHighlightExpireTimeMillis()) {
			getOverlayTracker().highlightBlock(null, -1);
			return;
		}
		
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		double relativeX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		double relativeY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		double relativeZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
		
		float r = (float)NCMath.trapezoidalWave(time*0.18D, 0D);
		float g = (float)NCMath.trapezoidalWave(time*0.18D, 120D);
		float b = (float)NCMath.trapezoidalWave(time*0.18D, 240D);
		
		GlStateManager.pushMatrix();
		GlStateManager.color(r, g, b);
		GlStateManager.glLineWidth(2);
		GlStateManager.translate(-relativeX, -relativeY, -relativeZ);
		
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		float x = pos.getX();
		float y = pos.getY();
		float z = pos.getZ();
		buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		RenderHelper.renderBlockOutline(buffer, x, y, z, r, g, b, 1F);
		
		tessellator.draw();
		
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}
}
