package nc.gui.processor;

import java.io.IOException;

import nc.container.ContainerTile;
import nc.container.processor.*;
import nc.gui.element.*;
import nc.network.PacketHandler;
import nc.network.gui.*;
import nc.tile.processor.TileFluidProcessor;
import nc.util.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiCentrifuge extends GuiFluidProcessor {
	
	public GuiCentrifuge(EntityPlayer player, TileFluidProcessor tile) {
		this(player, tile, new ContainerCentrifuge(player, tile));
	}
	
	protected GuiCentrifuge(EntityPlayer player, TileFluidProcessor tile, ContainerTile<TileFluidProcessor> container) {
		super("centrifuge", player, tile, container);
		xSize = 176;
		ySize = 178;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 16, 86);
		renderButtonTooltips(mouseX, mouseY);
	}
	
	public void renderButtonTooltips(int mouseX, int mouseY) {
		drawFluidTooltip(tile.getTanks().get(0), mouseX, mouseY, 40, 41, 16, 16);
		drawFluidTooltip(tile.getTanks().get(1), mouseX, mouseY, 96, 31, 16, 16);
		drawFluidTooltip(tile.getTanks().get(2), mouseX, mouseY, 116, 31, 16, 16);
		drawFluidTooltip(tile.getTanks().get(3), mouseX, mouseY, 136, 31, 16, 16);
		drawFluidTooltip(tile.getTanks().get(4), mouseX, mouseY, 96, 51, 16, 16);
		drawFluidTooltip(tile.getTanks().get(5), mouseX, mouseY, 116, 51, 16, 16);
		drawFluidTooltip(tile.getTanks().get(6), mouseX, mouseY, 136, 51, 16, 16);
		
		drawTooltip(Lang.localise("gui.nc.container.machine_side_config"), mouseX, mouseY, 27, 75, 18, 18);
		drawTooltip(Lang.localise("gui.nc.container.redstone_control"), mouseX, mouseY, 47, 75, 18, 18);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		if (tile.defaultProcessPower != 0) {
			int e = (int) Math.round(86D * tile.getEnergyStorage().getEnergyStored() / tile.getEnergyStorage().getMaxEnergyStored());
			drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 86 - e, 176, 90 + 86 - e, 16, e);
		}
		else {
			drawGradientRect(guiLeft + 8, guiTop + 6, guiLeft + 8 + 16, guiTop + 6 + 86, 0xFFC6C6C6, 0xFF8B8B8B);
		}
		
		drawTexturedModalRect(guiLeft + 58, guiTop + 30, 176, 3, getCookProgressScaled(37), 38);
		
		drawUpgradeRenderers();
		
		drawBackgroundExtras();
	}
	
	protected void drawBackgroundExtras() {
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(0), guiLeft + 40, guiTop + 41, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(1), guiLeft + 96, guiTop + 31, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(2), guiLeft + 116, guiTop + 31, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(3), guiLeft + 136, guiTop + 31, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(4), guiLeft + 96, guiTop + 51, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(5), guiLeft + 116, guiTop + 51, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(6), guiLeft + 136, guiTop + 51, zLevel, 16, 16);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		initButtons();
	}
	
	public void initButtons() {
		buttonList.add(new NCButton.EmptyTank(0, guiLeft + 40, guiTop + 41, 16, 16));
		buttonList.add(new NCButton.EmptyTank(1, guiLeft + 96, guiTop + 31, 16, 16));
		buttonList.add(new NCButton.EmptyTank(2, guiLeft + 116, guiTop + 31, 16, 16));
		buttonList.add(new NCButton.EmptyTank(3, guiLeft + 136, guiTop + 31, 16, 16));
		buttonList.add(new NCButton.EmptyTank(4, guiLeft + 96, guiTop + 51, 16, 16));
		buttonList.add(new NCButton.EmptyTank(5, guiLeft + 116, guiTop + 51, 16, 16));
		buttonList.add(new NCButton.EmptyTank(6, guiLeft + 136, guiTop + 51, 16, 16));
		
		buttonList.add(new NCButton.MachineConfig(7, guiLeft + 27, guiTop + 75));
		buttonList.add(new NCToggleButton.RedstoneControl(8, guiLeft + 47, guiTop + 75, tile));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 7; ++i) {
				if (guiButton.id == i && NCUtil.isModifierKeyDown()) {
					PacketHandler.instance.sendToServer(new EmptyTankPacket(tile, i));
					return;
				}
			}
			if (guiButton.id == 7) {
				PacketHandler.instance.sendToServer(new OpenSideConfigGuiPacket(tile));
			}
			else if (guiButton.id == 8) {
				tile.setRedstoneControl(!tile.getRedstoneControl());
				PacketHandler.instance.sendToServer(new ToggleRedstoneControlPacket(tile));
			}
		}
	}
	
	public static class SideConfig extends GuiCentrifuge {
		
		public SideConfig(EntityPlayer player, TileFluidProcessor tile) {
			super(player, tile, new ContainerMachineConfig<>(player, tile));
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
		
		@Override
		public void renderButtonTooltips(int mouseX, int mouseY) {
			drawTooltip(TextFormatting.DARK_AQUA + Lang.localise("gui.nc.container.input_tank_config"), mouseX, mouseY, 39, 40, 18, 18);
			drawTooltip(TextFormatting.RED + Lang.localise("gui.nc.container.output_tank_config"), mouseX, mouseY, 95, 30, 18, 18);
			drawTooltip(TextFormatting.RED + Lang.localise("gui.nc.container.output_tank_config"), mouseX, mouseY, 115, 30, 18, 18);
			drawTooltip(TextFormatting.RED + Lang.localise("gui.nc.container.output_tank_config"), mouseX, mouseY, 135, 30, 18, 18);
			drawTooltip(TextFormatting.RED + Lang.localise("gui.nc.container.output_tank_config"), mouseX, mouseY, 95, 50, 18, 18);
			drawTooltip(TextFormatting.RED + Lang.localise("gui.nc.container.output_tank_config"), mouseX, mouseY, 115, 50, 18, 18);
			drawTooltip(TextFormatting.RED + Lang.localise("gui.nc.container.output_tank_config"), mouseX, mouseY, 135, 50, 18, 18);
			drawTooltip(TextFormatting.DARK_BLUE + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 131, 75, 18, 18);
			drawTooltip(TextFormatting.YELLOW + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 151, 75, 18, 18);
		}
		
		@Override
		protected void drawUpgradeRenderers() {}
		
		@Override
		protected void drawBackgroundExtras() {}
		
		@Override
		public void initButtons() {
			buttonList.add(new NCButton.SorptionConfig.FluidInput(0, guiLeft + 39, guiTop + 40));
			buttonList.add(new NCButton.SorptionConfig.FluidOutputSmall(1, guiLeft + 95, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.FluidOutputSmall(2, guiLeft + 115, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.FluidOutputSmall(3, guiLeft + 135, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.FluidOutputSmall(4, guiLeft + 95, guiTop + 50));
			buttonList.add(new NCButton.SorptionConfig.FluidOutputSmall(5, guiLeft + 115, guiTop + 50));
			buttonList.add(new NCButton.SorptionConfig.FluidOutputSmall(6, guiLeft + 135, guiTop + 50));
			buttonList.add(new NCButton.SorptionConfig.SpeedUpgrade(7, guiLeft + 131, guiTop + 75));
			buttonList.add(new NCButton.SorptionConfig.EnergyUpgrade(8, guiLeft + 151, guiTop + 75));
		}
		
		@Override
		protected void actionPerformed(GuiButton guiButton) {
			if (tile.getWorld().isRemote) {
				if (guiButton.id == 0) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Input<>(this, tile, 0));
				}
				else if (guiButton.id == 1) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output<>(this, tile, 1));
				}
				else if (guiButton.id == 2) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output<>(this, tile, 2));
				}
				else if (guiButton.id == 3) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output<>(this, tile, 3));
				}
				else if (guiButton.id == 4) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output<>(this, tile, 4));
				}
				else if (guiButton.id == 5) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output<>(this, tile, 5));
				}
				else if (guiButton.id == 6) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output<>(this, tile, 6));
				}
				else if (guiButton.id == 7) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.SpeedUpgrade<>(this, tile, 0));
				}
				else if (guiButton.id == 8) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.EnergyUpgrade<>(this, tile, 1));
				}
			}
		}
	}
}
