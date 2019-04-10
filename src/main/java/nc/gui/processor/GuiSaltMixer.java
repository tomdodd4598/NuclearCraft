package nc.gui.processor;

import nc.container.processor.ContainerSaltMixer;
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

public class GuiSaltMixer extends GuiFluidProcessor {
	
	public GuiSaltMixer(EntityPlayer player, TileFluidProcessor tile) {
		super("salt_mixer", player, new ContainerSaltMixer(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawFluidTooltip(tile.getTanks().get(0), mouseX, mouseY, 46, 35, 16, 16);
		drawFluidTooltip(tile.getTanks().get(1), mouseX, mouseY, 66, 35, 16, 16);
		drawFluidTooltip(tile.getTanks().get(2), mouseX, mouseY, 122, 31, 24, 24);
		
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
		drawTexturedModalRect(guiLeft + 84, guiTop + 34, 176, 3, k, 18);
		
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(0), guiLeft + 46, guiTop + 35, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(1), guiLeft + 66, guiTop + 35, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(2), guiLeft + 122, guiTop + 31, zLevel, 24, 24);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new NCGuiButton.EmptyTankButton(0, guiLeft + 46, guiTop + 35, 16, 16));
		buttonList.add(new NCGuiButton.EmptyTankButton(1, guiLeft + 66, guiTop + 35, 16, 16));
		buttonList.add(new NCGuiButton.EmptyTankButton(2, guiLeft + 122, guiTop + 31, 24, 24));
		
		buttonList.add(new NCGuiToggleButton.ToggleRedstoneControlButton(3, guiLeft + 27, guiTop + 63, tile));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 3; i++) if (guiButton.id == i && isShiftKeyDown()) {
				PacketHandler.instance.sendToServer(new EmptyTankPacket(tile, i));
			}
			if (guiButton.id == 3) {
				tile.setRedstoneControl(!tile.getRedstoneControl());
				PacketHandler.instance.sendToServer(new ToggleRedstoneControlPacket(tile));
			}
		}
	}
}
