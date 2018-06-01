package nc.gui.processor;

import nc.container.processor.ContainerRockCrusher;
import nc.gui.GuiItemRenderer;
import nc.init.NCItems;
import nc.tile.processor.TileItemProcessor;
import net.minecraft.entity.player.EntityPlayer;

public class GuiRockCrusher extends GuiItemProcessor {

	public GuiRockCrusher(EntityPlayer player, TileItemProcessor tile) {
		super("rock_crusher", player, new ContainerRockCrusher(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 16, 74);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		GuiItemRenderer itemRenderer = new GuiItemRenderer(132, ySize - 102, 0.5F, NCItems.upgrade, 0);
		itemRenderer.draw();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		double e = Math.round(((double) tile.getEnergyStorage().getEnergyStored()) / ((double) tile.getEnergyStorage().getMaxEnergyStored()) * 74);
		if (tile.baseProcessPower != 0) drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - (int) e, 176, 90 + 74 - (int) e, 16, (int) e);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 56, guiTop + 35, 176, 3, k, 18);
	}
}
