package nc.multiblock.gui;

import java.util.List;

import com.google.common.collect.Lists;

import nc.gui.NCGui;
import nc.multiblock.*;
import nc.multiblock.tile.*;
import nc.network.multiblock.MultiblockUpdatePacket;
import nc.util.Lang;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public abstract class GuiMultiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket, GUITILE extends IMultiblockGuiPart<MULTIBLOCK, T, PACKET, GUITILE>> extends NCGui {
	
	protected final GUITILE tile;
	protected MULTIBLOCK multiblock;
	
	public GuiMultiblock(EntityPlayer player, GUITILE tile) {
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
