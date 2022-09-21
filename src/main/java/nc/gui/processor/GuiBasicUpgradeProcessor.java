package nc.gui.processor;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import nc.gui.element.*;
import nc.init.NCItems;
import nc.network.PacketHandler;
import nc.network.gui.*;
import nc.tile.energy.ITileEnergy;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.processor.*;
import nc.util.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiBasicUpgradeProcessor<TILE extends TileBasicUpgradeProcessor<TILE>> extends GuiProcessor<TILE, BasicUpgradeProcessorContainerInfo<TILE>> {
	
	protected GuiItemRenderer speedUpgradeRenderer = null, energyUpgradeRenderer = null;
	
	public GuiBasicUpgradeProcessor(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
		super(inventory, player, tile, textureLocation);
	}
	
	@Override
	public void initButtons() {
		super.initButtons();
		buttonList.add(new NCButton.MachineConfig(info.getMachineConfigButtonID(), guiLeft + info.machineConfigGuiX, guiTop + info.machineConfigGuiY));
		buttonList.add(new NCToggleButton.RedstoneControl(info.getRedstoneControlButtonID(), guiLeft + info.redstoneControlGuiX, guiTop + info.redstoneControlGuiY, tile));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		if (guiName == null) {
			guiName = tile.getDisplayName().getUnformattedText();
		}
		fontRenderer.drawString(guiName, xSize / 2 - fontRenderer.getStringWidth(guiName) / 2, 6, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		int energyBarX = guiLeft + info.energyBarGuiX, energyBarY = guiTop + info.energyBarGuiY;
		if (info.defaultProcessPower != 0) {
			EnergyStorage energyStorage = tile.getEnergyStorage();
			int e = (int) Math.round((double) info.energyBarGuiHeight * energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored());
			drawTexturedModalRect(energyBarX, energyBarY + info.energyBarGuiHeight - e, info.energyBarGuiU, info.energyBarGuiV + info.energyBarGuiHeight - e, info.energyBarGuiWidth, e);
		}
		else {
			drawGradientRect(energyBarX, energyBarY, energyBarX + info.energyBarGuiWidth, energyBarY + info.energyBarGuiHeight, 0xFFC6C6C6, 0xFF8B8B8B);
		}
		
		drawTexturedModalRect(guiLeft + info.progressBarGuiX, guiTop + info.progressBarGuiY, info.progressBarGuiU, info.progressBarGuiV, getProgressBarWidth(), info.progressBarGuiHeight);
		
		drawUpgradeRenderers();
	}
	
	protected int getProgressBarWidth() {
		if (tile.baseProcessTime / tile.getSpeedMultiplier() < 4D) {
			return tile.isProcessing ? info.progressBarGuiWidth : 0;
		}
		double i = tile.time, j = tile.baseProcessTime;
		return j != 0D ? (int) Math.round(i * info.progressBarGuiWidth / j) : 0;
	}
	
	protected void drawUpgradeRenderers() {
		if (speedUpgradeRenderer == null) {
			speedUpgradeRenderer = new GuiItemRenderer(NCItems.upgrade, 0, guiLeft + info.speedUpgradeGuiX, guiTop + info.speedUpgradeGuiY, 0.5F);
		}
		speedUpgradeRenderer.draw();
		
		if (energyUpgradeRenderer == null) {
			energyUpgradeRenderer = new GuiItemRenderer(NCItems.upgrade, 1, guiLeft + info.energyUpgradeGuiX, guiTop + info.energyUpgradeGuiY, 0.5F);
		}
		energyUpgradeRenderer.draw();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (tile.getWorld().isRemote) {
			super.actionPerformed(button);
			if (button.id == info.getMachineConfigButtonID()) {
				PacketHandler.instance.sendToServer(new OpenSideConfigGuiPacket(tile));
			}
			else if (button.id == info.getRedstoneControlButtonID()) {
				tile.setRedstoneControl(!tile.getRedstoneControl());
				PacketHandler.instance.sendToServer(new ToggleRedstoneControlPacket(tile));
			}
		}
	}
	
	@Override
	public void renderButtonTooltips(int mouseX, int mouseY) {
		super.renderButtonTooltips(mouseX, mouseY);
		drawEnergyTooltip(tile, mouseX, mouseY, info.energyBarGuiX, info.energyBarGuiY, info.energyBarGuiWidth, info.energyBarGuiHeight);
		drawTooltip(Lang.localise("gui.nc.container.machine_side_config"), mouseX, mouseY, info.machineConfigGuiX, info.machineConfigGuiY, 18, 18);
		drawTooltip(Lang.localise("gui.nc.container.redstone_control"), mouseX, mouseY, info.redstoneControlGuiX, info.redstoneControlGuiY, 18, 18);
	}
	
	@Override
	public void drawEnergyTooltip(ITileEnergy tileEnergy, int mouseX, int mouseY, int x, int y, int drawWidth, int drawHeight) {
		if (info.defaultProcessPower != 0) {
			super.drawEnergyTooltip(tileEnergy, mouseX, mouseY, x, y, drawWidth, drawHeight);
		}
		else {
			drawNoEnergyTooltip(mouseX, mouseY, x, y, drawWidth, drawHeight);
		}
	}
	
	@Override
	public List<String> energyInfo(ITileEnergy tileEnergy) {
		String energy = UnitHelper.prefix(tileEnergy.getEnergyStorage().getEnergyStoredLong(), tileEnergy.getEnergyStorage().getMaxEnergyStoredLong(), 5, "RF");
		String power = UnitHelper.prefix(tile.getProcessPower(), 5, "RF/t");
		
		String speedMultiplier = "x" + NCMath.decimalPlaces(tile.getSpeedMultiplier(), 2);
		String powerMultiplier = "x" + NCMath.decimalPlaces(tile.getPowerMultiplier(), 2);
		
		return Lists.newArrayList(TextFormatting.LIGHT_PURPLE + Lang.localise("gui.nc.container.energy_stored") + TextFormatting.WHITE + " " + energy, TextFormatting.LIGHT_PURPLE + Lang.localise("gui.nc.container.process_power") + TextFormatting.WHITE + " " + power, TextFormatting.AQUA + Lang.localise("gui.nc.container.speed_multiplier") + TextFormatting.WHITE + " " + speedMultiplier, TextFormatting.AQUA + Lang.localise("gui.nc.container.power_multiplier") + TextFormatting.WHITE + " " + powerMultiplier);
	}
	
	public static class SideConfig<TILE extends TileBasicUpgradeProcessor<TILE>> extends GuiBasicUpgradeProcessor<TILE> {
		
		public SideConfig(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
		
		@Override
		public void initButtons() {
			for (int i = 0; i < info.itemInputSize; ++i) {
				int[] slotXYWH = info.itemInputGuiXYWH.get(i);
				buttonList.add(new NCButton.SorptionConfig.ItemInput(i, guiLeft + slotXYWH[0] - 1, guiTop + slotXYWH[1] - 1, slotXYWH[2] + 2, slotXYWH[3] + 2));
			}
			
			int offset = info.itemInputSize;
			for (int i = 0; i < info.fluidInputSize; ++i) {
				int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
				buttonList.add(new NCButton.SorptionConfig.FluidInput(i + offset, guiLeft + tankXYWH[0] - 1, guiTop + tankXYWH[1] - 1, tankXYWH[2] + 2, tankXYWH[3] + 2));
			}
			
			offset += info.fluidInputSize;
			for (int i = 0; i < info.itemOutputSize; ++i) {
				int[] slotXYWH = info.itemOutputGuiXYWH.get(i);
				buttonList.add(new NCButton.SorptionConfig.ItemOutput(i + offset, guiLeft + slotXYWH[0] - 1, guiTop + slotXYWH[1] - 1, slotXYWH[2] + 2, slotXYWH[3] + 2));
			}
			
			offset += info.itemOutputSize;
			for (int i = 0; i < info.fluidOutputSize; ++i) {
				int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
				buttonList.add(new NCButton.SorptionConfig.FluidOutput(i + offset, guiLeft + tankXYWH[0] - 1, guiTop + tankXYWH[1] - 1, tankXYWH[2] + 2, tankXYWH[3] + 2));
			}
			
			offset += info.fluidOutputSize;
			buttonList.add(new NCButton.SorptionConfig.SpeedUpgrade(offset, guiLeft + info.speedUpgradeGuiX - 1, guiTop + info.speedUpgradeGuiY - 1, 18, 18));
			buttonList.add(new NCButton.SorptionConfig.EnergyUpgrade(offset + 1, guiLeft + info.energyUpgradeGuiX - 1, guiTop + info.energyUpgradeGuiY - 1, 18, 18));
		}
		
		@Override
		protected void drawUpgradeRenderers() {}
		
		@Override
		protected void actionPerformed(GuiButton guiButton) {
			if (tile.getWorld().isRemote) {
				for (int i = 0; i < info.itemInputSize; ++i) {
					if (guiButton.id == i) {
						FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Input<>(this, tile, i));
						return;
					}
				}
				
				int offset = info.itemInputSize;
				for (int i = 0; i < info.fluidInputSize; ++i) {
					if (guiButton.id == i + offset) {
						FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Input<>(this, tile, i));
						return;
					}
				}
				
				offset += info.fluidInputSize;
				for (int i = 0; i < info.itemOutputSize; ++i) {
					if (guiButton.id == i + offset) {
						FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Output<>(this, tile, i + info.itemInputSize));
						return;
					}
				}
				
				offset += info.itemOutputSize;
				for (int i = 0; i < info.fluidOutputSize; ++i) {
					if (guiButton.id == i + offset) {
						FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output<>(this, tile, i + info.fluidInputSize));
						return;
					}
				}
				
				offset += info.fluidOutputSize;
				if (guiButton.id == offset) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.SpeedUpgrade<>(this, tile, info.getSpeedUpgradeSlot()));
				}
				else if (guiButton.id == offset + 1) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.EnergyUpgrade<>(this, tile, info.getEnergyUpgradeSlot()));
				}
			}
		}
		
		@Override
		public void renderButtonTooltips(int mouseX, int mouseY) {
			for (int i = 0; i < info.itemInputSize; ++i) {
				int[] slotXYWH = info.itemInputGuiXYWH.get(i);
				drawTooltip(TextFormatting.BLUE + Lang.localise("gui.nc.container.input_item_config"), mouseX, mouseY, slotXYWH[0] - 1, slotXYWH[1] - 1, slotXYWH[2] + 2, slotXYWH[3] + 2);
			}
			
			for (int i = 0; i < info.fluidInputSize; ++i) {
				int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
				drawTooltip(TextFormatting.DARK_AQUA + Lang.localise("gui.nc.container.input_tank_config"), mouseX, mouseY, tankXYWH[0] - 1, tankXYWH[1] - 1, tankXYWH[2] + 2, tankXYWH[3] + 2);
			}
			
			for (int i = 0; i < info.itemOutputSize; ++i) {
				int[] slotXYWH = info.itemOutputGuiXYWH.get(i);
				drawTooltip(TextFormatting.GOLD + Lang.localise("gui.nc.container.output_item_config"), mouseX, mouseY, slotXYWH[0] - 1, slotXYWH[1] - 1, slotXYWH[2] + 2, slotXYWH[3] + 2);
			}
			
			for (int i = 0; i < info.fluidOutputSize; ++i) {
				int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
				drawTooltip(TextFormatting.RED + Lang.localise("gui.nc.container.output_tank_config"), mouseX, mouseY, tankXYWH[0] - 1, tankXYWH[1] - 1, tankXYWH[2] + 2, tankXYWH[3] + 2);
			}
			
			drawTooltip(TextFormatting.DARK_BLUE + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 131, 63, 18, 18);
			drawTooltip(TextFormatting.YELLOW + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 151, 63, 18, 18);
		}
		
		@Override
		protected void keyTyped(char typedChar, int keyCode) throws IOException {
			if (isEscapeKeyDown(keyCode)) {
				PacketHandler.instance.sendToServer(new OpenTileGuiPacket(tile));
			}
			else {
				super.keyTyped(typedChar, keyCode);
			}
		}
	}
}
