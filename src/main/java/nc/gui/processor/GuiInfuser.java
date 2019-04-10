package nc.gui.processor;

import nc.container.processor.ContainerInfuser;
import nc.gui.element.GuiFluidRenderer;
import nc.gui.element.GuiItemRenderer;
import nc.gui.element.NCGuiButton;
import nc.gui.element.NCGuiToggleButton;
import nc.init.NCItems;
import nc.network.PacketHandler;
import nc.network.gui.EmptyTankPacket;
import nc.network.gui.ToggleRedstoneControlPacket;
import nc.tile.processor.TileItemFluidProcessor;
import nc.util.Lang;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

public class GuiInfuser extends GuiItemFluidProcessor {
	
	public GuiInfuser(EntityPlayer player, TileItemFluidProcessor tile) {
		super("infuser", player, new ContainerInfuser(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawFluidTooltip(tile.getTanks().get(0), mouseX, mouseY, 66, 35, 16, 16);
		
		drawTooltip(Lang.localise("gui.container.redstone_control"), mouseX, mouseY, 27, 63, 18, 18);
		
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 16, 74);
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
		
		double e = Math.round(((double) tile.getEnergyStorage().getEnergyStored()) / ((double) tile.getEnergyStorage().getMaxEnergyStored()) * 74);
		if (tile.baseProcessPower != 0) drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - (int) e, 176, 90 + 74 - (int) e, 16, (int) e);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 84, guiTop + 35, 176, 3, k, 16);
		
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(0), guiLeft + 66, guiTop + 35, zLevel, 16, 16);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new NCGuiButton.EmptyTankButton(0, guiLeft + 66, guiTop + 35, 16, 16));
		
		buttonList.add(new NCGuiToggleButton.ToggleRedstoneControlButton(1, guiLeft + 27, guiTop + 63, tile));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 1; i++) if (guiButton.id == i && isShiftKeyDown()) {
				PacketHandler.instance.sendToServer(new EmptyTankPacket(tile, i));
			}
			if (guiButton.id == 1) {
				tile.setRedstoneControl(!tile.getRedstoneControl());
				PacketHandler.instance.sendToServer(new ToggleRedstoneControlPacket(tile));
			}
		}
	}
}
