package nc.multiblock.gui;

import java.util.List;

import com.google.common.collect.Lists;

import nc.gui.NCGui;
import nc.multiblock.Multiblock;
import nc.multiblock.tile.IMultiblockGuiPart;
import nc.util.Lang;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public abstract class GuiMultiblock<MULTIBLOCK extends Multiblock, TILE extends IMultiblockGuiPart<MULTIBLOCK>> extends NCGui {
	
	protected final TILE tile;
	protected MULTIBLOCK multiblock;
	
	public GuiMultiblock(EntityPlayer player, TILE tile) {
		super(tile.getContainer(player));
		this.tile = tile;
		this.multiblock = tile.getMultiblock();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(getGuiTexture());
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	protected abstract ResourceLocation getGuiTexture();
	
	public List<String> clearAllInfo() {
		return Lists.newArrayList(TextFormatting.ITALIC + Lang.localise("gui.nc.container.shift_clear_multiblock"));
	}
}
