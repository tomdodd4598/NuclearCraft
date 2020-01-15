package nc.gui.processor;

import java.io.IOException;

import nc.container.ContainerTile;
import nc.container.processor.ContainerFuelReprocessor;
import nc.container.processor.ContainerMachineConfig;
import nc.gui.element.NCButton;
import nc.gui.element.NCToggleButton;
import nc.network.PacketHandler;
import nc.network.gui.OpenSideConfigGuiPacket;
import nc.network.gui.OpenTileGuiPacket;
import nc.network.gui.ToggleRedstoneControlPacket;
import nc.tile.processor.TileItemProcessor;
import nc.util.Lang;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiFuelReprocessor extends GuiItemProcessor {
	
	public GuiFuelReprocessor(EntityPlayer player, TileItemProcessor tile) {
		this(player, tile, new ContainerFuelReprocessor(player, tile));
	}
	
	private GuiFuelReprocessor(EntityPlayer player, TileItemProcessor tile, ContainerTile container) {
		super("fuel_reprocessor", player, tile, container);
		xSize = 176;
		ySize = 178;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 16, 86);
		renderButtonTooltips(mouseX, mouseY);
	}
	
	public void renderButtonTooltips(int mouseX, int mouseY) {
		drawTooltip(Lang.localise("gui.nc.container.machine_side_config"), mouseX, mouseY, 27, 75, 18, 18);
		drawTooltip(Lang.localise("gui.nc.container.redstone_control"), mouseX, mouseY, 47, 75, 18, 18);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		if (tile.defaultProcessPower != 0) {
			int e = (int) Math.round(86D*tile.getEnergyStorage().getEnergyStored()/tile.getEnergyStorage().getMaxEnergyStored());
			drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 86 - e, 176, 90 + 86 - e, 16, e);
		}
		else drawGradientRect(guiLeft + 8, guiTop + 6, guiLeft + 8 + 16, guiTop + 6 + 86, 0xFFC6C6C6, 0xFF8B8B8B);
		
		drawTexturedModalRect(guiLeft + 58, guiTop + 30, 176, 3, getCookProgressScaled(37), 38);
		
		drawUpgradeRenderers();
		
		drawBackgroundExtras();
	}
	
	protected void drawBackgroundExtras() {};
	
	@Override
	public void initGui() {
		super.initGui();
		initButtons();
	}
	
	public void initButtons() {
		buttonList.add(new NCButton.MachineConfig(0, guiLeft + 27, guiTop + 75));
		buttonList.add(new NCToggleButton.RedstoneControl(1, guiLeft + 47, guiTop + 75, tile));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			if (guiButton.id == 0) {
				PacketHandler.instance.sendToServer(new OpenSideConfigGuiPacket(tile));
			}
			else if (guiButton.id == 1) {
				tile.setRedstoneControl(!tile.getRedstoneControl());
				PacketHandler.instance.sendToServer(new ToggleRedstoneControlPacket(tile));
			}
		}
	}
	
	public static class SideConfig extends GuiFuelReprocessor {
		
		public SideConfig(EntityPlayer player, TileItemProcessor tile) {
			super(player, tile, new ContainerMachineConfig(player, tile));
		}
		
		@Override
		protected void keyTyped(char typedChar, int keyCode) throws IOException {
			if (isEscapeKeyDown(keyCode)) {
				PacketHandler.instance.sendToServer(new OpenTileGuiPacket(tile));
			}
			else super.keyTyped(typedChar, keyCode);
		}
		
		@Override
		public void renderButtonTooltips(int mouseX, int mouseY) {
			drawTooltip(TextFormatting.BLUE + Lang.localise("gui.nc.container.input_item_config"), mouseX, mouseY, 39, 40, 18, 18);
			drawTooltip(TextFormatting.GOLD + Lang.localise("gui.nc.container.output_item_config"), mouseX, mouseY, 95, 30, 18, 18);
			drawTooltip(TextFormatting.GOLD + Lang.localise("gui.nc.container.output_item_config"), mouseX, mouseY, 115, 30, 18, 18);
			drawTooltip(TextFormatting.GOLD + Lang.localise("gui.nc.container.output_item_config"), mouseX, mouseY, 135, 30, 18, 18);
			drawTooltip(TextFormatting.GOLD + Lang.localise("gui.nc.container.output_item_config"), mouseX, mouseY, 95, 50, 18, 18);
			drawTooltip(TextFormatting.GOLD + Lang.localise("gui.nc.container.output_item_config"), mouseX, mouseY, 115, 50, 18, 18);
			drawTooltip(TextFormatting.GOLD + Lang.localise("gui.nc.container.output_item_config"), mouseX, mouseY, 135, 50, 18, 18);
			drawTooltip(TextFormatting.DARK_BLUE + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 131, 75, 18, 18);
			drawTooltip(TextFormatting.YELLOW + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 151, 75, 18, 18);
		}
		
		@Override
		protected void drawUpgradeRenderers() {}
		
		@Override
		protected void drawBackgroundExtras() {};
		
		@Override
		public void initButtons() {
			buttonList.add(new NCButton.SorptionConfig.ItemInput(0, guiLeft + 39, guiTop + 40));
			buttonList.add(new NCButton.SorptionConfig.ItemOutputSmall(1, guiLeft + 95, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.ItemOutputSmall(2, guiLeft + 115, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.ItemOutputSmall(3, guiLeft + 135, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.ItemOutputSmall(4, guiLeft + 95, guiTop + 50));
			buttonList.add(new NCButton.SorptionConfig.ItemOutputSmall(5, guiLeft + 115, guiTop + 50));
			buttonList.add(new NCButton.SorptionConfig.ItemOutputSmall(6, guiLeft + 135, guiTop + 50));
			buttonList.add(new NCButton.SorptionConfig.SpeedUpgrade(7, guiLeft + 131, guiTop + 75));
			buttonList.add(new NCButton.SorptionConfig.EnergyUpgrade(8, guiLeft + 151, guiTop + 75));
		}
		
		@Override
		protected void actionPerformed(GuiButton guiButton) {
			if (tile.getWorld().isRemote) {
				if (guiButton.id == 0) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Input(this, tile, 0));
				}
				else if (guiButton.id == 1) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Output(this, tile, 1));
				}
				else if (guiButton.id == 2) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Output(this, tile, 2));
				}
				else if (guiButton.id == 3) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Output(this, tile, 3));
				}
				else if (guiButton.id == 4) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Output(this, tile, 4));
				}
				else if (guiButton.id == 5) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Output(this, tile, 5));
				}
				else if (guiButton.id == 6) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Output(this, tile, 6));
				}
				else if (guiButton.id == 7) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.SpeedUpgrade(this, tile, 7));
				}
				else if (guiButton.id == 8) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.EnergyUpgrade(this, tile, 8));
				}
			}
		}
	}
}
