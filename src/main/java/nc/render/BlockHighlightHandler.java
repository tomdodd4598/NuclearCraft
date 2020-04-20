/* All credit goes to McJty:
 * 'highlightBlock' comes from XNet: mcjty.xnet.RenderWorldLastEventHandler.renderHilightedBlock,
 * 'renderBlockOutline' comes from McJtyLib: mcjty.lib.client.BlockOutlineRenderer.renderHighLightedBlocksOutline,
 * the other methods comes from XNet: mcjty.xnet.ClientInfo.
 */

package nc.render;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import nc.NuclearCraft;
import nc.util.NCMath;
import nc.util.NCRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHighlightHandler {
	
	LongList expiredCache = new LongArrayList();
	
	private static Long2LongMap getHighlightMap() {
		return NuclearCraft.instance.blockOverlayTracker.getHighlightMap();
	}
	
	@SubscribeEvent
	public void highlightBlocks(RenderWorldLastEvent event) {
		for (Long2LongMap.Entry entry : getHighlightMap().long2LongEntrySet()) {
			highlightBlock(event, entry);
		}
		for (long expired : expiredCache) {
			getHighlightMap().remove(expired);
		}
		expiredCache.clear();
	}
	
	public void highlightBlock(RenderWorldLastEvent event, Long2LongMap.Entry entry) {
		BlockPos pos = BlockPos.fromLong(entry.getLongKey());
		if (pos == null) {
			expiredCache.add(entry.getLongKey());
			return;
		}
		
		long time = System.currentTimeMillis();
		
		if (time > entry.getLongValue()) {
			expiredCache.add(entry.getLongKey());
			return;
		}
		
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		double relativeX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		double relativeY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		double relativeZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
		
		float r = (float) NCMath.trapezoidalWave(time*0.18D, 0D);
		float g = (float) NCMath.trapezoidalWave(time*0.18D, 120D);
		float b = (float) NCMath.trapezoidalWave(time*0.18D, 240D);
		
		GlStateManager.pushMatrix();
		GlStateManager.color(r, g, b);
		GlStateManager.glLineWidth(2);
		GlStateManager.translate(-relativeX, -relativeY, -relativeZ);
		
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		
		NCRenderHelper.renderBlockFrame(pos, r, g, b, 1F);
		Tessellator.getInstance().draw();
		
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}
}
