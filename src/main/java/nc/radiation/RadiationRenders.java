package nc.radiation;

import org.lwjgl.opengl.GL11;

import baubles.api.BaubleType;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import nc.Global;
import nc.ModCheck;
import nc.capability.radiation.entity.IEntityRads;
import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.tile.radiation.TileGeigerCounter;
import nc.tile.radiation.TileRadiationScrubber;
import nc.util.GuiHelper;
import nc.util.Lang;
import nc.util.TextHelper;
import nc.util.UnitHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RadiationRenders {
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	private static final ResourceLocation RADS_BAR = new ResourceLocation(Global.MOD_ID + ":textures/hud/" + "rads_bar" + ".png");
	
	private static final String IMMUNE_FOR = Lang.localise("hud.nuclearcraft.rad_immune");
	
	/* Originally from coolAlias' 'Tutorial-Demo' - tutorial.client.gui.GuiManaBar */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void addRadiationInfo(RenderGameOverlayEvent.Post event) {
		if (!NCConfig.radiation_enabled_public) return;
		
		if(event.getType() != ElementType.HOTBAR) return;
		final EntityPlayer player = MC.player;
		if (!shouldShowHUD(player)) return;
		IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
		if (playerRads == null) return;
		
		ScaledResolution res = new ScaledResolution(MC);
		int barWidth = (int)(100D*playerRads.getTotalRads()/playerRads.getMaxRads());
		String info = playerRads.isImmune() ? IMMUNE_FOR + " " + UnitHelper.applyTimeUnitShort(playerRads.getRadiationImmunityTime(), 2, 1) : (playerRads.isRadiationNegligible() ? "0 Rads/t" : RadiationHelper.radsPrefix(playerRads.getRadiationLevel(), true));
		int infoWidth = MC.fontRenderer.getStringWidth(info);
		int overlayWidth = (int)Math.round(Math.max(104, infoWidth)*NCConfig.radiation_hud_size);
		int overlayHeight = (int)Math.round(19*NCConfig.radiation_hud_size);
		
		int xPos = (int)Math.round(NCConfig.radiation_hud_position_cartesian.length >= 2 ? NCConfig.radiation_hud_position_cartesian[0]*res.getScaledWidth() : GuiHelper.getRenderPositionXFromAngle(res, NCConfig.radiation_hud_position, overlayWidth, 3)/NCConfig.radiation_hud_size);
		int yPos = (int)Math.round(NCConfig.radiation_hud_position_cartesian.length >= 2 ? NCConfig.radiation_hud_position_cartesian[1]*res.getScaledHeight() : GuiHelper.getRenderPositionYFromAngle(res, NCConfig.radiation_hud_position, overlayHeight, 3)/NCConfig.radiation_hud_size);
		
		MC.getTextureManager().bindTexture(RADS_BAR);
		
		GlStateManager.pushMatrix();
		
		GlStateManager.scale(NCConfig.radiation_hud_size, NCConfig.radiation_hud_size, 1D);
		
		drawTexturedModalRect(xPos, yPos, 0, 0, 104, 10);
		drawTexturedModalRect(xPos + 2 + 100 - barWidth, yPos + 2, 100 - barWidth, 10, barWidth, 6);
		yPos += 12;
		if (NCConfig.radiation_hud_text_outline) {
			MC.fontRenderer.drawString(info, xPos + 1 + (104 - infoWidth)/2, yPos, 0);
			MC.fontRenderer.drawString(info, xPos - 1 + (104 - infoWidth)/2, yPos, 0);
			MC.fontRenderer.drawString(info, xPos + (104 - infoWidth)/2, yPos + 1, 0);
			MC.fontRenderer.drawString(info, xPos + (104 - infoWidth)/2, yPos - 1, 0);
		}
		MC.fontRenderer.drawString(info, xPos + (104 - infoWidth)/2, yPos, playerRads.isImmune() ? 0x55FF55 : TextHelper.getFormatColor(RadiationHelper.getRadiationTextColor(playerRads)));
		
		GlStateManager.popMatrix();
	}
	
	public static boolean shouldShowHUD(EntityPlayer player) {
		if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return false;
		if (!NCConfig.radiation_require_counter) return true;
		
		final ItemStack geiger_counter = new ItemStack(NCItems.geiger_counter), geiger_block = new ItemStack(NCItems.geiger_counter);
		
		if (ModCheck.baublesLoaded() && player.hasCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null)) {
			IBaublesItemHandler baublesHandler = player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
			if (baublesHandler == null) return false;
			
			for (int slot : BaubleType.TRINKET.getValidSlots()) {
				if (baublesHandler.getStackInSlot(slot).isItemEqual(geiger_counter)) return true;
			}
		}
		
		return player.inventory.hasItemStack(geiger_counter) || player.inventory.hasItemStack(geiger_block);
	}
	
	private static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		double zLevel = 0D;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x + 0, y + height, zLevel).tex((textureX + 0) * 0.00390625F, (textureY + height) * 0.00390625F).endVertex();
		bufferbuilder.pos(x + width, y + height, zLevel).tex((textureX + width) * 0.00390625F, (textureY + height) * 0.00390625F).endVertex();
		bufferbuilder.pos(x + width, y + 0, zLevel).tex((textureX + width) * 0.00390625F, (textureY + 0) * 0.00390625F).endVertex();
		bufferbuilder.pos(x + 0, y + 0, zLevel).tex((textureX + 0) * 0.00390625F, (textureY + 0) * 0.00390625F).endVertex();
		tessellator.draw();
	}
	
	/* Thanks to dizzyd for this method! */
	@SubscribeEvent
	public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		// Overlay renderer for the geiger counter and radiation scrubber blocks
		boolean chunkBorders = false;
		
		// Bail fast if rendering is disabled
		if (!NCConfig.radiation_chunk_boundaries) {
			return;
		}
		
		// Draw the chunk borders if we're either holding a geiger block OR looking at one
		for (EnumHand hand : EnumHand.values()) {
			ItemStack heldItem = MC.player.getHeldItem(hand);
			if (NCItems.geiger_counter == heldItem.getItem() || Item.getItemFromBlock(NCBlocks.radiation_scrubber) == heldItem.getItem()) {
				chunkBorders = true;
				break;
			}
		}
		
		if (!chunkBorders && MC.objectMouseOver != null && MC.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
			TileEntity te = MC.world.getTileEntity(MC.objectMouseOver.getBlockPos());
			if (!chunkBorders && (te instanceof TileGeigerCounter || te instanceof TileRadiationScrubber)) {
				chunkBorders = true;
			}
		}
		
		/* Logic below taken with minor changes from BluSunrize's Immersive Engineering: blusunrize.immersiveengineering.client.ClientEventHandler */
		
		if (chunkBorders) {
			EntityPlayer player = MC.player;
			double px = TileEntityRendererDispatcher.staticPlayerX;
			double py = TileEntityRendererDispatcher.staticPlayerY;
			double pz = TileEntityRendererDispatcher.staticPlayerZ;
			int chunkX = (int)player.posX >> 4<<4;
			int chunkZ = (int)player.posZ >> 4<<4;
			int y = Math.min((int)player.posY-2, player.getEntityWorld().getChunk(chunkX, chunkZ).getLowestHeight());
			float h = (float)Math.max(32, player.posY - y + 8);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder BufferBuilder = tessellator.getBuffer();
			
			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.disableCull();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			float r = 255;
			float g = 0;
			float b = 0;
			BufferBuilder.setTranslation(chunkX-px, y+2-py, chunkZ-pz);
			GlStateManager.glLineWidth(5f);
			BufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
			BufferBuilder.pos(0, 0, 0).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(0, h, 0).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(16, 0, 0).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(16, h, 0).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(16, 0, 16).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(16, h, 16).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(0, 0, 16).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(0, h, 16).color(r, g, b, .375f).endVertex();
			
			BufferBuilder.pos(0, 2, 0).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(16, 2, 0).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(0, 2, 0).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(0, 2, 16).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(0, 2, 16).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(16, 2, 16).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(16, 2, 0).color(r, g, b, .375f).endVertex();
			BufferBuilder.pos(16, 2, 16).color(r, g, b, .375f).endVertex();
			tessellator.draw();
			BufferBuilder.setTranslation(0, 0, 0);
			GlStateManager.shadeModel(GL11.GL_FLAT);
			GlStateManager.enableCull();
			GlStateManager.disableBlend();
			GlStateManager.enableTexture2D();
		}
	}
}
