package nc.gui.processor;

import nc.container.processor.ContainerElectrolyser;
import nc.gui.GuiFluidRenderer;
import nc.gui.GuiItemRenderer;
import nc.init.NCItems;
import nc.tile.processor.TileEnergyFluidProcessor;
import net.minecraft.entity.player.EntityPlayer;

public class GuiElectrolyser extends GuiEnergyFluidProcessor {
	
	public GuiElectrolyser(EntityPlayer player, TileEnergyFluidProcessor tile) {
		super("electrolyser", player, new ContainerElectrolyser(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 178;
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRendererObj.drawString(tile.storage.getEnergyStored() + " RF", 28, ySize - 94, 4210752);
		
		GuiItemRenderer itemRenderer = new GuiItemRenderer(132, ySize - 102, 0.5F, NCItems.upgrade, 0);
		itemRenderer.draw();
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		double e = Math.round(((double) tile.storage.getEnergyStored()) / ((double) tile.storage.getMaxEnergyStored()) * 86);
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 86 - (int) e, 176, 90 + 86 - (int) e, 16, (int) e);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 68, guiTop + 30, 176, 3, k, 38);
		
		GuiFluidRenderer.renderGuiTank(tile.tanks[0], guiLeft + 50, guiTop + 41, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.tanks[1], guiLeft + 106, guiTop + 31, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.tanks[2], guiLeft + 126, guiTop + 31, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.tanks[3], guiLeft + 106, guiTop + 51, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.tanks[4], guiLeft + 126, guiTop + 51, zLevel, 16, 16);
	}
}
