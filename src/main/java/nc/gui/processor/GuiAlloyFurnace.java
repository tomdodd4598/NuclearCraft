package nc.gui.processor;

import nc.container.processor.ContainerAlloyFurnace;
import nc.tile.processor.TileEnergyItemProcessor;
import net.minecraft.entity.player.EntityPlayer;

public class GuiAlloyFurnace extends GuiEnergyProcessor {
	
	public GuiAlloyFurnace(EntityPlayer player, TileEnergyItemProcessor tile) {
		super("alloy_furnace", player, new ContainerAlloyFurnace(player, tile));
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
		drawTexturedModalRect(guiLeft + 84, guiTop + 35, 176, 3, k, 16);
	}
}
