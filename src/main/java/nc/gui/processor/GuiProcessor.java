package nc.gui.processor;

import nc.gui.GuiInfoTile;
import nc.gui.element.*;
import nc.network.gui.*;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.internal.fluid.Tank;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import nc.util.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.*;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.IOException;
import java.util.List;

public abstract class GuiProcessor<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends GuiInfoTile<TILE, PACKET, INFO> {
	
	public GuiProcessor(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
		super(inventory, player, tile, textureLocation);
		
		xSize = info.guiWidth;
		ySize = info.guiHeight;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		initButtons();
	}
	
	protected void initButtons() {
		initTankButtons();
		initConfigButtons();
	}
	
	protected void initTankButtons() {
		for (int i = 0; i < info.fluidInputSize; ++i) {
			int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
			buttonList.add(new NCButton.ClearTank(i, guiLeft + tankXYWH[0], guiTop + tankXYWH[1], tankXYWH[2], tankXYWH[3]));
		}
		
		for (int i = 0; i < info.fluidOutputSize; ++i) {
			int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
			buttonList.add(new NCButton.ClearTank(i + info.fluidInputSize, guiLeft + tankXYWH[0], guiTop + tankXYWH[1], tankXYWH[2], tankXYWH[3]));
		}
	}
	
	protected void initConfigButtons() {
		if (info.machineConfigGuiX >= 0 && info.machineConfigGuiY >= 0) {
			buttonList.add(new NCButton.MachineConfig(info.getMachineConfigButtonID(), guiLeft + info.machineConfigGuiX, guiTop + info.machineConfigGuiY));
		}
		if (info.redstoneControlGuiX >= 0 && info.redstoneControlGuiY >= 0) {
			buttonList.add(new NCToggleButton.RedstoneControl(info.getRedstoneControlButtonID(), guiLeft + info.redstoneControlGuiX, guiTop + info.redstoneControlGuiY, tile));
		}
	}
	
	protected void initSorptionButtons() {
		for (int i = 0; i < info.itemInputSize; ++i) {
			addSorptionButton(NCButton.SorptionConfig.ItemInput::new, info.itemInputSorptionButtonID[i], info.itemInputGuiXYWH.get(i));
		}
		
		for (int i = 0; i < info.fluidInputSize; ++i) {
			addSorptionButton(NCButton.SorptionConfig.FluidInput::new, info.fluidInputSorptionButtonID[i], info.fluidInputGuiXYWH.get(i));
		}
		
		for (int i = 0; i < info.itemOutputSize; ++i) {
			addSorptionButton(NCButton.SorptionConfig.ItemOutput::new, info.itemOutputSorptionButtonID[i], info.itemOutputGuiXYWH.get(i));
		}
		
		for (int i = 0; i < info.fluidOutputSize; ++i) {
			addSorptionButton(NCButton.SorptionConfig.FluidOutput::new, info.fluidOutputSorptionButtonID[i], info.fluidOutputGuiXYWH.get(i));
		}
	}
	
	protected void addSorptionButton(SorptionButtonFunction function, int id, int[] slotXYWH) {
		buttonList.add(function.apply(id, guiLeft + slotXYWH[0] - 1, guiTop + slotXYWH[1] - 1, slotXYWH[2] + 2, slotXYWH[3] + 2));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(guiName.get(), xSize / 2 - nameWidth.get() / 2, 6, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawMainBackground();
		drawBars();
		drawTanks();
	}
	
	protected void drawMainBackground() {
		defaultStateAndBind();
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	protected void drawTanks() {
		List<Tank> tanks = tile.getTanks();
		for (int i = 0; i < info.fluidInputSize; ++i) {
			int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
			GuiFluidRenderer.renderGuiTank(tanks.get(i), guiLeft + tankXYWH[0], guiTop + tankXYWH[1], zLevel, tankXYWH[2], tankXYWH[3]);
		}
		
		for (int i = 0; i < info.fluidOutputSize; ++i) {
			int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
			GuiFluidRenderer.renderGuiTank(tanks.get(i + info.fluidInputSize), guiLeft + tankXYWH[0], guiTop + tankXYWH[1], zLevel, tankXYWH[2], tankXYWH[3]);
		}
	}
	
	protected void drawBars() {
		drawProgressBar();
		
		if (tile.hasCapability(CapabilityEnergy.ENERGY, null)) {
			drawEnergyBar();
		}
	}
	
	protected void drawProgressBar() {
		drawTexturedModalRect(guiLeft + info.progressBarGuiX, guiTop + info.progressBarGuiY, info.progressBarGuiU, info.progressBarGuiV, getProgressBarWidth(), info.progressBarGuiH);
	}
	
	protected int getProgressBarWidth() {
		double baseProcessTime = tile.getBaseProcessTime();
		if (baseProcessTime / tile.getSpeedMultiplier() < 2D) {
			return tile.getIsProcessing() ? info.progressBarGuiW : 0;
		}
		else {
			return baseProcessTime == 0D ? 0 : NCMath.toInt(Math.round(tile.getCurrentTime() * info.progressBarGuiW / baseProcessTime));
		}
	}
	
	protected void drawEnergyBar() {
		int energyX = guiLeft + info.energyBarGuiX, energyY = guiTop + info.energyBarGuiY;
		if (info.defaultProcessPower != 0) {
			int e = getEnergyBarHeight(), d = info.energyBarGuiH - e;
			drawTexturedModalRect(energyX, energyY + d, info.energyBarGuiU, info.energyBarGuiV + d, info.energyBarGuiW, e);
		}
		else {
			drawGradientRect(energyX, energyY, energyX + info.energyBarGuiW, energyY + info.energyBarGuiH, 0xFFC6C6C6, 0xFF8B8B8B);
		}
	}
	
	protected int getEnergyBarHeight() {
		IEnergyStorage energyStorage = tile.getCapability(CapabilityEnergy.ENERGY, null);
		return NCMath.toInt(Math.round((double) info.energyBarGuiH * energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored()));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (tile.getWorld().isRemote) {
			buttonActionPerformed(button);
		}
	}
	
	protected boolean buttonActionPerformed(GuiButton button) {
		if (NCUtil.isModifierKeyDown()) {
			if (button.id >= 0 && button.id < info.getTankCount()) {
				clearTankAction(button.id);
				return true;
			}
		}
		
		return configButtonActionPerformed(button);
	}
	
	protected void clearTankAction(int tankNumber) {
		new ClearTankPacket(tile, tankNumber).sendToServer();
	}
	
	protected boolean configButtonActionPerformed(GuiButton button) {
		if (button.id == info.getMachineConfigButtonID()) {
			new OpenSideConfigGuiPacket(tile).sendToServer();
			return true;
		}
		else if (button.id == info.getRedstoneControlButtonID()) {
			tile.setRedstoneControl(!tile.getRedstoneControl());
			new ToggleRedstoneControlPacket(tile).sendToServer();
			return true;
		}
		else {
			return false;
		}
	}
	
	protected boolean sorptionButtonActionPerformed(GuiButton button) {
		for (int i = 0; i < info.itemInputSize; ++i) {
			if (button.id == info.itemInputSorptionButtonID[i]) {
				FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Input<>(this, tile, info.itemInputSlots[i]));
				return true;
			}
		}
		
		for (int i = 0; i < info.fluidInputSize; ++i) {
			if (button.id == info.fluidInputSorptionButtonID[i]) {
				FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Input<>(this, tile, info.fluidInputTanks[i]));
				return true;
			}
		}
		
		for (int i = 0; i < info.itemOutputSize; ++i) {
			if (button.id == info.itemOutputSorptionButtonID[i]) {
				FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Output<>(this, tile, info.itemOutputSlots[i]));
				return true;
			}
		}
		
		for (int i = 0; i < info.fluidOutputSize; ++i) {
			if (button.id == info.fluidOutputSorptionButtonID[i]) {
				FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output<>(this, tile, info.fluidOutputTanks[i]));
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected void renderTooltips(int mouseX, int mouseY) {
		renderProcessorTooltips(mouseX, mouseY);
	}
	
	protected void renderProcessorTooltips(int mouseX, int mouseY) {
		renderTankTooltips(mouseX, mouseY);
		renderBarTooltips(mouseX, mouseY);
		renderConfigButtonTooltips(mouseX, mouseY);
	}
	
	protected void renderTankTooltips(int mouseX, int mouseY) {
		List<Tank> tanks = tile.getTanks();
		for (int i = 0; i < info.fluidInputSize; ++i) {
			int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
			drawFluidTooltip(tanks.get(i), mouseX, mouseY, tankXYWH[0], tankXYWH[1], tankXYWH[2], tankXYWH[3]);
		}
		
		for (int i = 0; i < info.fluidOutputSize; ++i) {
			int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
			drawFluidTooltip(tanks.get(i + info.fluidInputSize), mouseX, mouseY, tankXYWH[0], tankXYWH[1], tankXYWH[2], tankXYWH[3]);
		}
	}
	
	protected void renderBarTooltips(int mouseX, int mouseY) {
		IEnergyStorage energyStorage = tile.getCapability(CapabilityEnergy.ENERGY, null);
		if (energyStorage != null) {
			drawEnergyTooltip(energyStorage, mouseX, mouseY, info.energyBarGuiX, info.energyBarGuiY, info.energyBarGuiW, info.energyBarGuiH);
		}
	}
	
	protected void renderConfigButtonTooltips(int mouseX, int mouseY) {
		if (info.machineConfigGuiX >= 0 && info.machineConfigGuiY >= 0) {
			drawTooltip(Lang.localize("gui.nc.container.machine_side_config"), mouseX, mouseY, info.machineConfigGuiX, info.machineConfigGuiY, 18, 18);
		}
		if (info.redstoneControlGuiX >= 0 && info.redstoneControlGuiY >= 0) {
			drawTooltip(Lang.localize("gui.nc.container.redstone_control"), mouseX, mouseY, info.redstoneControlGuiX, info.redstoneControlGuiY, 18, 18);
		}
	}
	
	protected void renderSorptionButtonTooltips(int mouseX, int mouseY) {
		for (int i = 0; i < info.itemInputSize; ++i) {
			drawSorptionButtonTooltip(mouseX, mouseY, TextFormatting.BLUE, "gui.nc.container.input_item_config", info.itemInputGuiXYWH.get(i));
		}
		
		for (int i = 0; i < info.fluidInputSize; ++i) {
			drawSorptionButtonTooltip(mouseX, mouseY, TextFormatting.DARK_AQUA, "gui.nc.container.input_tank_config", info.fluidInputGuiXYWH.get(i));
		}
		
		for (int i = 0; i < info.itemOutputSize; ++i) {
			drawSorptionButtonTooltip(mouseX, mouseY, TextFormatting.GOLD, "gui.nc.container.output_item_config", info.itemOutputGuiXYWH.get(i));
		}
		
		for (int i = 0; i < info.fluidOutputSize; ++i) {
			drawSorptionButtonTooltip(mouseX, mouseY, TextFormatting.RED, "gui.nc.container.output_tank_config", info.fluidOutputGuiXYWH.get(i));
		}
	}
	
	protected void drawSorptionButtonTooltip(int mouseX, int mouseY, TextFormatting formatting, String unlocalized, int[] slotXYWH) {
		drawTooltip(formatting + Lang.localize(unlocalized), mouseX, mouseY, slotXYWH[0] - 1, slotXYWH[1] - 1, slotXYWH[2] + 2, slotXYWH[3] + 2);
	}
	
	@Override
	protected void drawEnergyTooltip(IEnergyStorage energyStorage, int mouseX, int mouseY, int x, int y, int drawWidth, int drawHeight) {
		if (info.defaultProcessPower != 0) {
			super.drawEnergyTooltip(energyStorage, mouseX, mouseY, x, y, drawWidth, drawHeight);
		}
		else {
			drawNoEnergyTooltip(mouseX, mouseY, x, y, drawWidth, drawHeight);
		}
	}
	
	@Override
	protected List<String> energyInfo(IEnergyStorage energyStorage) {
		List<String> info = super.energyInfo(energyStorage);
		info.add(TextFormatting.LIGHT_PURPLE + Lang.localize("gui.nc.container.process_power") + TextFormatting.WHITE + " " + UnitHelper.prefix(tile.getProcessPower(), 5, "RF/t"));
		return info;
	}
	
	public static class SideConfig<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends GuiProcessor<TILE, PACKET, INFO> {
		
		public SideConfig(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
		
		@Override
		public void initButtons() {
			initSorptionButtons();
		}
		
		@Override
		protected boolean buttonActionPerformed(GuiButton button) {
			return sorptionButtonActionPerformed(button);
		}
		
		@Override
		public void renderProcessorTooltips(int mouseX, int mouseY) {
			renderSorptionButtonTooltips(mouseX, mouseY);
		}
		
		@Override
		protected void keyTyped(char typedChar, int keyCode) throws IOException {
			if (isEscapeKeyDown(keyCode)) {
				new OpenTileGuiPacket(tile).sendToServer();
			}
			else {
				super.keyTyped(typedChar, keyCode);
			}
		}
	}
}
