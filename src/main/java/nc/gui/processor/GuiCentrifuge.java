package nc.gui.processor;

import nc.container.processor.ContainerCentrifuge;
import nc.gui.element.GuiFluidRenderer;
import nc.gui.element.GuiItemRenderer;
import nc.gui.element.NCGuiButton;
import nc.gui.element.NCGuiToggleButton;
import nc.init.NCItems;
import nc.network.PacketHandler;
import nc.network.gui.EmptyTankPacket;
import nc.network.gui.ToggleRedstoneControlPacket;
import nc.tile.processor.TileFluidProcessor;
import nc.util.Lang;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

public class GuiCentrifuge extends GuiFluidProcessor {
	
	public GuiCentrifuge(EntityPlayer player, TileFluidProcessor tile) {
		super("centrifuge", player, new ContainerCentrifuge(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 178;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawFluidTooltip(tile.getTanks().get(0), mouseX, mouseY, 50, 41, 16, 16);
		drawFluidTooltip(tile.getTanks().get(1), mouseX, mouseY, 106, 31, 16, 16);
		drawFluidTooltip(tile.getTanks().get(2), mouseX, mouseY, 126, 31, 16, 16);
		drawFluidTooltip(tile.getTanks().get(3), mouseX, mouseY, 106, 51, 16, 16);
		drawFluidTooltip(tile.getTanks().get(4), mouseX, mouseY, 126, 51, 16, 16);
		
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
		
		if (tile.defaultProcessPower != 0) {
			double e = Math.round(((double) tile.getEnergyStorage().getEnergyStored()) / ((double) tile.getEnergyStorage().getMaxEnergyStored()) * 86);
			drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 86 - (int) e, 176, 90 + 86 - (int) e, 16, (int) e);
		}
		else drawGradientRect(guiLeft + 8, guiTop + 6, guiLeft + 8 + 16, guiTop + 6 + 86, 0xFFC6C6C6, 0xFF8B8B8B);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 68, guiTop + 30, 176, 3, k, 38);
		
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(0), guiLeft + 50, guiTop + 41, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(1), guiLeft + 106, guiTop + 31, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(2), guiLeft + 126, guiTop + 31, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(3), guiLeft + 106, guiTop + 51, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(4), guiLeft + 126, guiTop + 51, zLevel, 16, 16);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new NCGuiButton.EmptyTankButton(0, guiLeft + 50, guiTop + 41, 16, 16));
		buttonList.add(new NCGuiButton.EmptyTankButton(1, guiLeft + 106, guiTop + 31, 16, 16));
		buttonList.add(new NCGuiButton.EmptyTankButton(2, guiLeft + 126, guiTop + 31, 16, 16));
		buttonList.add(new NCGuiButton.EmptyTankButton(3, guiLeft + 106, guiTop + 51, 16, 16));
		buttonList.add(new NCGuiButton.EmptyTankButton(4, guiLeft + 126, guiTop + 51, 16, 16));
		
		buttonList.add(new NCGuiToggleButton.ToggleRedstoneControlButton(5, guiLeft + 27, guiTop + 75, tile));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 5; i++) if (guiButton.id == i && isShiftKeyDown()) {
				PacketHandler.instance.sendToServer(new EmptyTankPacket(tile, i));
			}
			if (guiButton.id == 5) {
				tile.setRedstoneControl(!tile.getRedstoneControl());
				PacketHandler.instance.sendToServer(new ToggleRedstoneControlPacket(tile));
			}
		}
	}
}
