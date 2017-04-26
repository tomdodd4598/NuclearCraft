package nc.gui.energy.processor;

import nc.container.energy.processor.ContainerFuelReprocessor;
import nc.tile.processor.TileEnergyItemProcessor;
import net.minecraft.entity.player.EntityPlayer;

public class GuiFuelReprocessor extends GuiEnergyProcessor {
	
	public GuiFuelReprocessor(EntityPlayer player, TileEnergyItemProcessor tile) {
		super("fuel_reprocessor", player, new ContainerFuelReprocessor(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 178;
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRendererObj.drawString(tile.storage.getEnergyStored() + " RF", 28, ySize - 94, 4210752);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		double e = Math.round(((double) tile.storage.getEnergyStored()) / ((double) tile.storage.getMaxEnergyStored()) * 86);
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 86 - (int) e, 176, 90 + 86 - (int) e, 16, (int) e);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 68, guiTop + 30, 176, 3, k, 38);
	}
}
