package nc.gui.processor;

import nc.container.processor.ContainerFuelReprocessor;
import nc.gui.element.GuiItemRenderer;
import nc.gui.element.NCGuiToggleButton;
import nc.init.NCItems;
import nc.network.PacketHandler;
import nc.network.gui.ToggleRedstoneControlPacket;
import nc.tile.processor.TileItemProcessor;
import nc.util.Lang;
import net.minecraft.client.gui.GuiButton;
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
		drawTooltip(Lang.localise("gui.container.redstone_control"), mouseX, mouseY, 27, 75, 18, 18);
		
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
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new NCGuiToggleButton.ToggleRedstoneControlButton(0, guiLeft + 27, guiTop + 75, tile));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			if (guiButton.id == 0) {
				tile.setRedstoneControl(!tile.getRedstoneControl());
				PacketHandler.instance.sendToServer(new ToggleRedstoneControlPacket(tile));
			}
		}
	}
}
