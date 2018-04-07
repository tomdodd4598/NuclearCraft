package nc.gui.processor;

import nc.container.processor.ContainerCrystallizer;
import nc.gui.GuiFluidRenderer;
import nc.gui.GuiItemRenderer;
import nc.gui.NCGuiButton;
import nc.init.NCItems;
import nc.network.EmptyTankButtonPacket;
import nc.network.GetFluidInTankPacket;
import nc.network.PacketHandler;
import nc.tile.processor.TileItemFluidProcessor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;

public class GuiCrystallizer extends GuiItemFluidProcessor {
	
	public static FluidStack fluid0 = null;

	public GuiCrystallizer(EntityPlayer player, TileItemFluidProcessor tile) {
		super("crystallizer", player, new ContainerCrystallizer(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawFluidTooltip(fluid0, tile.tanks[0], mouseX, mouseY, 56, 35, 16, 16);
		
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
		
		double e = Math.round(((double) tile.storage.getEnergyStored()) / ((double) tile.storage.getMaxEnergyStored()) * 74);
		if (tile.baseProcessPower != 0) drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - (int) e, 176, 90 + 74 - (int) e, 16, (int) e);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 74, guiTop + 35, 176, 3, k, 16);
		
		if (tick == 0) PacketHandler.instance.sendToServer(new GetFluidInTankPacket(tile.getPos(), 0, "nc.gui.processor.GuiCrystallizer", "fluid0"));
		
		GuiFluidRenderer.renderGuiTank(fluid0, tile.tanks[0].getCapacity(), guiLeft + 56, guiTop + 35, zLevel, 16, 16);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new NCGuiButton.BlankButton(0, guiLeft + 56, guiTop + 35, 16, 16));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 1; i++) if (guiButton.id == i && isShiftKeyDown()) {
				PacketHandler.instance.sendToServer(new EmptyTankButtonPacket(tile, i));
			}
		}
	}
}
