package nc.gui.processor;

import nc.container.processor.ContainerElectrolyser;
import nc.gui.GuiFluidRenderer;
import nc.gui.GuiItemRenderer;
import nc.gui.NCGuiButton;
import nc.init.NCItems;
import nc.network.EmptyTankButtonPacket;
import nc.network.GetFluidInTankPacket;
import nc.network.PacketHandler;
import nc.tile.processor.TileFluidProcessor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;

public class GuiElectrolyser extends GuiFluidProcessor {
	
	public static FluidStack fluid0, fluid1, fluid2, fluid3, fluid4 = null;
	
	public GuiElectrolyser(EntityPlayer player, TileFluidProcessor tile) {
		super("electrolyser", player, new ContainerElectrolyser(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 178;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawFluidTooltip(fluid0, tile.tanks.get(0), mouseX, mouseY, 50, 41, 16, 16);
		drawFluidTooltip(fluid1, tile.tanks.get(1), mouseX, mouseY, 106, 31, 16, 16);
		drawFluidTooltip(fluid2, tile.tanks.get(2), mouseX, mouseY, 126, 31, 16, 16);
		drawFluidTooltip(fluid3, tile.tanks.get(3), mouseX, mouseY, 106, 51, 16, 16);
		drawFluidTooltip(fluid4, tile.tanks.get(4), mouseX, mouseY, 126, 51, 16, 16);
		
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 16, 86);
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
		
		double e = Math.round(((double) tile.getEnergyStorage().getEnergyStored()) / ((double) tile.getEnergyStorage().getMaxEnergyStored()) * 86);
		if (tile.baseProcessPower != 0) drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 86 - (int) e, 176, 90 + 86 - (int) e, 16, (int) e);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 68, guiTop + 30, 176, 3, k, 38);
		
		if (tick == 0) {
			PacketHandler.instance.sendToServer(new GetFluidInTankPacket(tile.getPos(), 0, "nc.gui.processor.GuiElectrolyser", "fluid0"));
			PacketHandler.instance.sendToServer(new GetFluidInTankPacket(tile.getPos(), 1, "nc.gui.processor.GuiElectrolyser", "fluid1"));
			PacketHandler.instance.sendToServer(new GetFluidInTankPacket(tile.getPos(), 2, "nc.gui.processor.GuiElectrolyser", "fluid2"));
			PacketHandler.instance.sendToServer(new GetFluidInTankPacket(tile.getPos(), 3, "nc.gui.processor.GuiElectrolyser", "fluid3"));
			PacketHandler.instance.sendToServer(new GetFluidInTankPacket(tile.getPos(), 4, "nc.gui.processor.GuiElectrolyser", "fluid4"));
		}
		
		GuiFluidRenderer.renderGuiTank(fluid0, tile.tanks.get(0).getCapacity(), guiLeft + 50, guiTop + 41, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(fluid1, tile.tanks.get(1).getCapacity(), guiLeft + 106, guiTop + 31, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(fluid2, tile.tanks.get(2).getCapacity(), guiLeft + 126, guiTop + 31, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(fluid3, tile.tanks.get(3).getCapacity(), guiLeft + 106, guiTop + 51, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(fluid4, tile.tanks.get(4).getCapacity(), guiLeft + 126, guiTop + 51, zLevel, 16, 16);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new NCGuiButton.EmptyTankButton(0, guiLeft + 50, guiTop + 41, 16, 16));
		buttonList.add(new NCGuiButton.EmptyTankButton(1, guiLeft + 106, guiTop + 31, 16, 16));
		buttonList.add(new NCGuiButton.EmptyTankButton(2, guiLeft + 126, guiTop + 31, 16, 16));
		buttonList.add(new NCGuiButton.EmptyTankButton(3, guiLeft + 106, guiTop + 51, 16, 16));
		buttonList.add(new NCGuiButton.EmptyTankButton(4, guiLeft + 126, guiTop + 51, 16, 16));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 5; i++) if (guiButton.id == i && isShiftKeyDown()) {
				PacketHandler.instance.sendToServer(new EmptyTankButtonPacket(tile, i));
			}
		}
	}
}
