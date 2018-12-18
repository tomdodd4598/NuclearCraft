package nc.gui.processor;

import nc.container.processor.ContainerFuelReprocessor;
import nc.gui.GuiItemRenderer;
import nc.init.NCItems;
import nc.tile.processor.TileItemProcessor;
import net.minecraft.entity.player.EntityPlayer;

public class GuiFuelReprocessor extends GuiItemProcessor {
	
	public GuiFuelReprocessor(EntityPlayer player, TileItemProcessor tile) {
		super("fuel_reprocessor", player, new ContainerFuelReprocessor(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 178;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 16, 86);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		new GuiItemRenderer(132, ySize - 102, 0.5F, NCItems.upgrade, 0).draw();
		new GuiItemRenderer(152, ySize - 102, 0.5F, NCItems.upgrade, 1).draw();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		double e = Math.round(((double) tile.getEnergyStorage().getEnergyStored()) / ((double) tile.getEnergyStorage().getMaxEnergyStored()) * 86);
		if (tile.baseProcessPower != 0) drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 86 - (int) e, 176, 90 + 86 - (int) e, 16, (int) e);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 68, guiTop + 30, 176, 3, k, 38);
	}
}
