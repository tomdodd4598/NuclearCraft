package nc.gui.processor;

import nc.container.processor.ContainerSupercooler;
import nc.gui.GuiFluidRenderer;
import nc.gui.GuiItemRenderer;
import nc.gui.NCGuiButton;
import nc.init.NCItems;
import nc.network.PacketEmptyTankButton;
import nc.network.PacketGetFluidInTank;
import nc.network.PacketHandler;
import nc.tile.processor.TileEnergyFluidProcessor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;

public class GuiSupercooler extends GuiEnergyFluidProcessor {
	
	public static FluidStack fluid0, fluid1 = null;

	public GuiSupercooler(EntityPlayer player, TileEnergyFluidProcessor tile) {
		super("supercooler", player, new ContainerSupercooler(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		//fontRendererObj.drawString(tile.storage.getEnergyStored() + " RF", 28, ySize - 94, 4210752);
		
		GuiItemRenderer itemRenderer = new GuiItemRenderer(132, ySize - 102, 0.5F, NCItems.upgrade, 0);
		itemRenderer.draw();
		
		drawFluidTooltip(fluid0, mouseX, mouseY, 56, 35, 16, 16);
		drawFluidTooltip(fluid1, mouseX, mouseY, 112, 31, 24, 24);
		
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 16, 74);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		double e = Math.round(((double) tile.storage.getEnergyStored()) / ((double) tile.storage.getMaxEnergyStored()) * 74);
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - (int) e, 176, 90 + 74 - (int) e, 16, (int) e);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 74, guiTop + 35, 176, 3, k, 16);
		
		if (tick == 0) {
			PacketHandler.INSTANCE.sendToServer(new PacketGetFluidInTank(tile.getPos(), 0, "nc.gui.processor.GuiSupercooler", "fluid0"));
			PacketHandler.INSTANCE.sendToServer(new PacketGetFluidInTank(tile.getPos(), 1, "nc.gui.processor.GuiSupercooler", "fluid1"));
		}
		
		GuiFluidRenderer.renderGuiTank(fluid0, tile.tanks[0].getCapacity(), guiLeft + 56, guiTop + 35, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(fluid1, tile.tanks[1].getCapacity(), guiLeft + 112, guiTop + 31, zLevel, 24, 24);
	}
	
	public void initGui() {
		super.initGui();
		buttonList.add(new NCGuiButton.BlankButton(0, guiLeft + 56, guiTop + 35, 16, 16));
		buttonList.add(new NCGuiButton.BlankButton(1, guiLeft + 112, guiTop + 31, 24, 24));
	}
	
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 2; i++) if (guiButton.id == i && isShiftKeyDown()) {
				PacketHandler.INSTANCE.sendToServer(new PacketEmptyTankButton(tile, i));
			}
		}
	}
}
