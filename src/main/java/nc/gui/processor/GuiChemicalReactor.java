package nc.gui.processor;

import nc.container.processor.ContainerChemicalReactor;
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

public class GuiChemicalReactor extends GuiFluidProcessor {
	
	public static FluidStack fluid0, fluid1, fluid2, fluid3 = null;

	public GuiChemicalReactor(EntityPlayer player, TileFluidProcessor tile) {
		super("chemical_reactor", player, new ContainerChemicalReactor(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawFluidTooltip(fluid0, tile.tanks.get(0), mouseX, mouseY, 32, 35, 16, 16);
		drawFluidTooltip(fluid1, tile.tanks.get(1), mouseX, mouseY, 52, 35, 16, 16);
		drawFluidTooltip(fluid2, tile.tanks.get(2), mouseX, mouseY, 108, 31, 24, 24);
		drawFluidTooltip(fluid3, tile.tanks.get(3), mouseX, mouseY, 136, 31, 24, 24);
		
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
		drawTexturedModalRect(guiLeft + 70, guiTop + 34, 176, 3, k, 18);
		
		if (tick == 0) sendTankInfo();
		
		GuiFluidRenderer.renderGuiTank(fluid0, tile.tanks.get(0).getCapacity(), guiLeft + 32, guiTop + 35, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(fluid1, tile.tanks.get(1).getCapacity(), guiLeft + 52, guiTop + 35, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(fluid2, tile.tanks.get(2).getCapacity(), guiLeft + 108, guiTop + 31, zLevel, 24, 24);
		GuiFluidRenderer.renderGuiTank(fluid3, tile.tanks.get(3).getCapacity(), guiLeft + 136, guiTop + 31, zLevel, 24, 24);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new NCGuiButton.EmptyTankButton(0, guiLeft + 32, guiTop + 35, 16, 16));
		buttonList.add(new NCGuiButton.EmptyTankButton(1, guiLeft + 52, guiTop + 35, 16, 16));
		buttonList.add(new NCGuiButton.EmptyTankButton(2, guiLeft + 108, guiTop + 31, 24, 24));
		buttonList.add(new NCGuiButton.EmptyTankButton(3, guiLeft + 136, guiTop + 31, 24, 24));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 4; i++) if (guiButton.id == i && isShiftKeyDown()) {
				PacketHandler.instance.sendToServer(new EmptyTankButtonPacket(tile, i));
			}
		}
	}
	
	@Override
	protected void sendTankInfo() {
		PacketHandler.instance.sendToServer(new GetFluidInTankPacket(tile.getPos(), 0, "nc.gui.processor.GuiChemicalReactor", "fluid0"));
		PacketHandler.instance.sendToServer(new GetFluidInTankPacket(tile.getPos(), 1, "nc.gui.processor.GuiChemicalReactor", "fluid1"));
		PacketHandler.instance.sendToServer(new GetFluidInTankPacket(tile.getPos(), 2, "nc.gui.processor.GuiChemicalReactor", "fluid2"));
		PacketHandler.instance.sendToServer(new GetFluidInTankPacket(tile.getPos(), 3, "nc.gui.processor.GuiChemicalReactor", "fluid3"));
	}
}
