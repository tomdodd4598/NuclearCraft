package nc.gui.processor;

import nc.container.processor.ContainerSaltMixer;
import nc.gui.GuiFluidRenderer;
import nc.gui.GuiItemRenderer;
import nc.gui.NCGuiButton;
import nc.init.NCItems;
import nc.network.PacketEmptyTankButton;
import nc.network.PacketGetFluidInTank;
import nc.network.PacketHandler;
import nc.tile.processor.TileFluidProcessor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;

public class GuiSaltMixer extends GuiFluidProcessor {
	
	public static FluidStack fluid0, fluid1, fluid2 = null;
	
	public GuiSaltMixer(EntityPlayer player, TileFluidProcessor tile) {
		super("salt_mixer", player, new ContainerSaltMixer(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		//fontRendererObj.drawString(tile.storage.getEnergyStored() + " RF", 28, ySize - 94, 4210752);
		
		GuiItemRenderer itemRenderer = new GuiItemRenderer(132, ySize - 102, 0.5F, NCItems.upgrade, 0);
		itemRenderer.draw();
		
		drawFluidTooltip(fluid0, tile.tanks[0], mouseX, mouseY, 46, 35, 16, 16);
		drawFluidTooltip(fluid1, tile.tanks[1], mouseX, mouseY, 66, 35, 16, 16);
		drawFluidTooltip(fluid2, tile.tanks[2], mouseX, mouseY, 122, 31, 24, 24);
		
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 16, 74);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		double e = Math.round(((double) tile.storage.getEnergyStored()) / ((double) tile.storage.getMaxEnergyStored()) * 74);
		if (tile.baseProcessPower != 0) drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - (int) e, 176, 90 + 74 - (int) e, 16, (int) e);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 84, guiTop + 34, 176, 3, k, 18);
		
		if (tick == 0) {
			PacketHandler.INSTANCE.sendToServer(new PacketGetFluidInTank(tile.getPos(), 0, "nc.gui.processor.GuiSaltMixer", "fluid0"));
			PacketHandler.INSTANCE.sendToServer(new PacketGetFluidInTank(tile.getPos(), 1, "nc.gui.processor.GuiSaltMixer", "fluid1"));
			PacketHandler.INSTANCE.sendToServer(new PacketGetFluidInTank(tile.getPos(), 2, "nc.gui.processor.GuiSaltMixer", "fluid2"));
		}
		
		GuiFluidRenderer.renderGuiTank(fluid0, tile.tanks[0].getCapacity(), guiLeft + 46, guiTop + 35, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(fluid1, tile.tanks[1].getCapacity(), guiLeft + 66, guiTop + 35, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(fluid2, tile.tanks[2].getCapacity(), guiLeft + 122, guiTop + 31, zLevel, 24, 24);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new NCGuiButton.BlankButton(0, guiLeft + 46, guiTop + 35, 16, 16));
		buttonList.add(new NCGuiButton.BlankButton(1, guiLeft + 66, guiTop + 35, 16, 16));
		buttonList.add(new NCGuiButton.BlankButton(2, guiLeft + 122, guiTop + 31, 24, 24));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 3; i++) if (guiButton.id == i && isShiftKeyDown()) {
				PacketHandler.INSTANCE.sendToServer(new PacketEmptyTankButton(tile, i));
			}
		}
	}
}
