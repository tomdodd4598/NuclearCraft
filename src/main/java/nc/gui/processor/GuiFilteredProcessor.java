package nc.gui.processor;

import java.util.List;

import nc.gui.element.*;
import nc.network.PacketHandler;
import nc.network.gui.*;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.ITileFiltered;
import nc.tile.fluid.ITileFilteredFluid;
import nc.tile.internal.fluid.Tank;
import nc.tile.inventory.ITileFilteredInventory;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class GuiFilteredProcessor<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO> & ITileFiltered, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends GuiProcessor<TILE, PACKET, INFO> {
	
	public GuiFilteredProcessor(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
		super(inventory, player, tile, textureLocation);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawMainBackground();
		drawBars();
		drawFilters();
		drawTanks();
	}
	
	protected void drawFilters() {
		if (tile instanceof ITileFilteredInventory) {
			NonNullList<ItemStack> filterStacks = ((ITileFilteredInventory) tile).getFilterStacks();
			for (int i = 0; i < info.itemInputSize; ++i) {
				int[] stackXY = info.itemInputStackXY.get(i);
				new GuiItemRenderer(filterStacks.get(i), guiLeft + stackXY[0], guiTop + stackXY[1], 0.5F).draw();
			}
			
			for (int i = 0; i < info.itemOutputSize; ++i) {
				int[] stackXY = info.itemOutputStackXY.get(i);
				new GuiItemRenderer(filterStacks.get(i + info.itemInputSize), guiLeft + stackXY[0], guiTop + stackXY[1], 0.5F).draw();
			}
		}
		
		if (tile instanceof ITileFilteredFluid) {
			List<Tank> filterTanks = ((ITileFilteredFluid) tile).getFilterTanks();
			for (int i = 0; i < info.fluidInputSize; ++i) {
				Tank filterTank = filterTanks.get(i);
				if (!filterTank.isEmpty()) {
					int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
					GuiFluidRenderer.renderGuiTank(filterTank.getFluid(), 1000, 1000, guiLeft + tankXYWH[0], guiTop + tankXYWH[1], zLevel, tankXYWH[2], tankXYWH[3], 127);
				}
			}
			
			for (int i = 0; i < info.fluidOutputSize; ++i) {
				Tank filterTank = filterTanks.get(i + info.fluidInputSize);
				if (!filterTank.isEmpty()) {
					int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
					GuiFluidRenderer.renderGuiTank(filterTank.getFluid(), 1000, 1000, guiLeft + tankXYWH[0], guiTop + tankXYWH[1], zLevel, tankXYWH[2], tankXYWH[3], 127);
				}
			}
		}
	}
	
	@Override
	protected void clearTankAction(int tankNumber) {
		if (tile instanceof ITileFilteredFluid && tile.getTanks().get(tankNumber).isEmpty()) {
			PacketHandler.instance.sendToServer(new ClearFilterTankPacket((ITileFilteredFluid) tile, tankNumber));
		}
		else {
			super.clearTankAction(tankNumber);
		}
	}
	
	@Override
	protected void renderTankTooltips(int mouseX, int mouseY) {
		if (tile instanceof ITileFilteredFluid) {
			List<Tank> tanks = tile.getTanks(), filterTanks = ((ITileFilteredFluid) tile).getFilterTanks();
			for (int i = 0; i < info.fluidInputSize; ++i) {
				int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
				drawFilteredFluidTooltip(tanks.get(i), filterTanks.get(i), mouseX, mouseY, tankXYWH[0], tankXYWH[1], tankXYWH[2], tankXYWH[3]);
			}
			
			for (int i = 0; i < info.fluidOutputSize; ++i) {
				int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
				drawFilteredFluidTooltip(tanks.get(i + info.fluidInputSize), filterTanks.get(i + info.fluidInputSize), mouseX, mouseY, tankXYWH[0], tankXYWH[1], tankXYWH[2], tankXYWH[3]);
			}
		}
		else {
			super.renderTankTooltips(mouseX, mouseY);
		}
	}
}
