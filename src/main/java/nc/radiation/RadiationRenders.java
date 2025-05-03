package nc.radiation;

import nc.Global;
import nc.capability.radiation.entity.IEntityRads;
import nc.init.*;
import nc.tile.radiation.*;
import nc.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.GL11;

import static nc.config.NCConfig.*;

@SideOnly(Side.CLIENT)
public class RadiationRenders {
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	private static final ResourceLocation RADS_BAR = new ResourceLocation(Global.MOD_ID + ":textures/hud/" + "rads_bar" + ".png");
	
	private static final String IMMUNE = Lang.localize("hud.nuclearcraft.rad_immune");
	private static final String IMMUNE_FOR = Lang.localize("hud.nuclearcraft.rad_immune_for");
	
	/**
	 * Originally from coolAlias' 'Tutorial-Demo' - tutorial.client.gui.GuiManaBar
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void addRadiationInfo(RenderGameOverlayEvent.Post event) {
		if (!radiation_enabled_public) {
			return;
		}
		
		if (event.getType() != ElementType.HOTBAR) {
			return;
		}
		final EntityPlayer player = MC.player;
		if (!RadiationHelper.shouldShowHUD(player)) {
			return;
		}
		IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
		if (playerRads == null) {
			return;
		}
		
		ScaledResolution res = new ScaledResolution(MC);
		int barWidth = NCMath.toInt(100D * playerRads.getTotalRads() / playerRads.getMaxRads());
		String info = playerRads.isImmune() ? playerRads.getRadiationImmunityStage() ? IMMUNE : IMMUNE_FOR + " " + UnitHelper.applyTimeUnitShort(playerRads.getRadiationImmunityTime(), 3, 1) : playerRads.isRadiationNegligible() ? "0 Rad/t" : RadiationHelper.radsPrefix(playerRads.getRadiationLevel(), true);
		int infoWidth = MC.fontRenderer.getStringWidth(info);
		int overlayWidth = NCMath.toInt(Math.round(Math.max(104, infoWidth) * radiation_hud_size));
		int overlayHeight = NCMath.toInt(Math.round(19 * radiation_hud_size));
		
		int xPos = NCMath.toInt(Math.round(radiation_hud_position_cartesian.length >= 2 ? radiation_hud_position_cartesian[0] * res.getScaledWidth() : GuiHelper.getRenderPositionXFromAngle(res, radiation_hud_position, overlayWidth, 3) / radiation_hud_size));
		int yPos = NCMath.toInt(Math.round(radiation_hud_position_cartesian.length >= 2 ? radiation_hud_position_cartesian[1] * res.getScaledHeight() : GuiHelper.getRenderPositionYFromAngle(res, radiation_hud_position, overlayHeight, 3) / radiation_hud_size));
		
		MC.getTextureManager().bindTexture(RADS_BAR);
		
		GlStateManager.pushMatrix();
		
		GlStateManager.scale(radiation_hud_size, radiation_hud_size, 1D);
		
		drawTexturedModalRect(xPos, yPos, 0, 0, 104, 10);
		drawTexturedModalRect(xPos + 2 + 100 - barWidth, yPos + 2, 100 - barWidth, 10, barWidth, 6);
		yPos += 12;
		if (radiation_hud_text_outline) {
			MC.fontRenderer.drawString(info, xPos + 1 + (104 - infoWidth) / 2, yPos, 0);
			MC.fontRenderer.drawString(info, xPos - 1 + (104 - infoWidth) / 2, yPos, 0);
			MC.fontRenderer.drawString(info, xPos + (104 - infoWidth) / 2, yPos + 1, 0);
			MC.fontRenderer.drawString(info, xPos + (104 - infoWidth) / 2, yPos - 1, 0);
		}
		MC.fontRenderer.drawString(info, xPos + (104 - infoWidth) / 2, yPos, playerRads.isImmune() ? 0x55FF55 : TextHelper.T2I_MAP.getInt(RadiationHelper.getRadiationTextColor(playerRads)));
		
		GlStateManager.popMatrix();
	}
	
	private static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		double zLevel = 0D;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		builder.begin(7, DefaultVertexFormats.POSITION_TEX);
		builder.pos(x, y + height, zLevel).tex(textureX * 0.00390625F, (textureY + height) * 0.00390625F).endVertex();
		builder.pos(x + width, y + height, zLevel).tex((textureX + width) * 0.00390625F, (textureY + height) * 0.00390625F).endVertex();
		builder.pos(x + width, y, zLevel).tex((textureX + width) * 0.00390625F, textureY * 0.00390625F).endVertex();
		builder.pos(x, y, zLevel).tex(textureX * 0.00390625F, textureY * 0.00390625F).endVertex();
		tessellator.draw();
	}
	
	/**
	 * Thanks to dizzyd for this method!
	 */
	@SubscribeEvent
	public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		// Overlay renderer for the Geiger counter and radiation scrubber blocks
		boolean chunkBorders = false;
		
		// Bail fast if rendering is disabled
		if (!radiation_chunk_boundaries) {
			return;
		}
		
		for (EnumHand hand : EnumHand.values()) {
			ItemStack heldStack = MC.player.getHeldItem(hand);
			Item heldItem = heldStack.getItem();
			if (NCItems.geiger_counter == heldItem || Item.getItemFromBlock(NCBlocks.geiger_block) == heldItem || Item.getItemFromBlock(NCBlocks.radiation_scrubber) == heldItem) {
				chunkBorders = true;
				break;
			}
		}
		
		if (!chunkBorders && MC.objectMouseOver != null && MC.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
			TileEntity te = MC.world.getTileEntity(MC.objectMouseOver.getBlockPos());
			if (te instanceof TileGeigerCounter || te instanceof TileRadiationScrubber) {
				chunkBorders = true;
			}
		}
		
		// Logic below taken with minor changes from BluSunrize's Immersive Engineering: blusunrize.immersiveengineering.client.ClientEventHandler
		
		if (chunkBorders) {
			EntityPlayer player = MC.player;
			double px = TileEntityRendererDispatcher.staticPlayerX;
			double py = TileEntityRendererDispatcher.staticPlayerY;
			double pz = TileEntityRendererDispatcher.staticPlayerZ;
			int chunkX = (int) player.posX >> 4 << 4;
			int chunkZ = (int) player.posZ >> 4 << 4;
			int y = Math.min(NCMath.toInt(player.posY - 2D), player.getEntityWorld().getChunk(chunkX, chunkZ).getLowestHeight());
			float h = (float) Math.max(32, player.posY - y + 8);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder builder = tessellator.getBuffer();
			
			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.disableCull();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			float r = 255;
			float g = 0;
			float b = 0;
			builder.setTranslation(chunkX - px, y + 2 - py, chunkZ - pz);
			GlStateManager.glLineWidth(5f);
			builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
			builder.pos(0, 0, 0).color(r, g, b, 0.375F).endVertex();
			builder.pos(0, h, 0).color(r, g, b, 0.375F).endVertex();
			builder.pos(16, 0, 0).color(r, g, b, 0.375F).endVertex();
			builder.pos(16, h, 0).color(r, g, b, 0.375F).endVertex();
			builder.pos(16, 0, 16).color(r, g, b, 0.375F).endVertex();
			builder.pos(16, h, 16).color(r, g, b, 0.375F).endVertex();
			builder.pos(0, 0, 16).color(r, g, b, 0.375F).endVertex();
			builder.pos(0, h, 16).color(r, g, b, 0.375F).endVertex();
			
			builder.pos(0, 2, 0).color(r, g, b, 0.375F).endVertex();
			builder.pos(16, 2, 0).color(r, g, b, 0.375F).endVertex();
			builder.pos(0, 2, 0).color(r, g, b, 0.375F).endVertex();
			builder.pos(0, 2, 16).color(r, g, b, 0.375F).endVertex();
			builder.pos(0, 2, 16).color(r, g, b, 0.375F).endVertex();
			builder.pos(16, 2, 16).color(r, g, b, 0.375F).endVertex();
			builder.pos(16, 2, 0).color(r, g, b, 0.375F).endVertex();
			builder.pos(16, 2, 16).color(r, g, b, 0.375F).endVertex();
			tessellator.draw();
			builder.setTranslation(0, 0, 0);
			GlStateManager.shadeModel(GL11.GL_FLAT);
			GlStateManager.enableCull();
			GlStateManager.disableBlend();
			GlStateManager.enableTexture2D();
		}
	}
}
