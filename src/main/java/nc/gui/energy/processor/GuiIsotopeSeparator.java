package nc.gui.energy.processor;

import nc.container.energy.processor.ContainerIsotopeSeparator;
import nc.tile.processor.TileEnergyItemProcessor;
import net.minecraft.entity.player.EntityPlayer;

public class GuiIsotopeSeparator extends GuiEnergyProcessor {

	public GuiIsotopeSeparator(EntityPlayer player, TileEnergyItemProcessor tile) {
		super("isotope_separator", player, new ContainerIsotopeSeparator(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRendererObj.drawString(tile.storage.getEnergyStored() + " RF", 28, ySize - 94, 4210752);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		double e = Math.round(((double) tile.storage.getEnergyStored()) / ((double) tile.storage.getMaxEnergyStored()) * 74);
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - (int) e, 176, 90 + 74 - (int) e, 16, (int) e);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 60, guiTop + 34, 176, 3, k, 18);
	}
}
