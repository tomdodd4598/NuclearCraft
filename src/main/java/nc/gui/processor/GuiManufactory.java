package nc.gui.processor;

import java.io.IOException;

import nc.container.ContainerTile;
import nc.container.processor.ContainerMachineConfig;
import nc.container.processor.ContainerManufactory;
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

public class GuiManufactory extends GuiItemProcessor {
	
	public GuiManufactory(EntityPlayer player, TileItemProcessor tile) {
		this(player, tile, new ContainerManufactory(player, tile));
	}
	
	private GuiManufactory(EntityPlayer player, TileItemProcessor tile, ContainerTile container) {
		super("manufactory", player, tile, container);
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 16, 74);
		renderButtonTooltips(mouseX, mouseY);
	}
	
	public void renderButtonTooltips(int mouseX, int mouseY) {
		drawTooltip(Lang.localise("gui.nc.container.machine_side_config"), mouseX, mouseY, 27, 63, 18, 18);
		drawTooltip(Lang.localise("gui.nc.container.redstone_control"), mouseX, mouseY, 47, 63, 18, 18);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		if (tile.defaultProcessPower != 0) {
			int e = (int) Math.round(74D*tile.getEnergyStorage().getEnergyStored()/tile.getEnergyStorage().getMaxEnergyStored());
			drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - e, 176, 90 + 74 - e, 16, e);
		}
		else drawGradientRect(guiLeft + 8, guiTop + 6, guiLeft + 8 + 16, guiTop + 6 + 74, 0xFFC6C6C6, 0xFF8B8B8B);
		
		drawTexturedModalRect(guiLeft + 74, guiTop + 35, 176, 3, getCookProgressScaled(37), 16);
		
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
		buttonList.add(new NCButton.MachineConfig(0, guiLeft + 27, guiTop + 63));
		buttonList.add(new NCToggleButton.RedstoneControl(1, guiLeft + 47, guiTop + 63, tile));
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
	
	public static class SideConfig extends GuiManufactory {
		
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
			drawTooltip(TextFormatting.BLUE + Lang.localise("gui.nc.container.input_item_config"), mouseX, mouseY, 55, 34, 18, 18);
			drawTooltip(TextFormatting.GOLD + Lang.localise("gui.nc.container.output_item_config"), mouseX, mouseY, 111, 30, 26, 26);
			drawTooltip(TextFormatting.DARK_BLUE + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 131, 63, 18, 18);
			drawTooltip(TextFormatting.YELLOW + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 151, 63, 18, 18);
		}
		
		@Override
		protected void drawUpgradeRenderers() {}
		
		@Override
		protected void drawBackgroundExtras() {};
		
		@Override
		public void initButtons() {
			buttonList.add(new NCButton.SorptionConfig.ItemInput(0, guiLeft + 55, guiTop + 34));
			buttonList.add(new NCButton.SorptionConfig.ItemOutput(1, guiLeft + 111, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.SpeedUpgrade(2, guiLeft + 131, guiTop + 63));
			buttonList.add(new NCButton.SorptionConfig.EnergyUpgrade(3, guiLeft + 151, guiTop + 63));
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
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.SpeedUpgrade(this, tile, 2));
				}
				else if (guiButton.id == 3) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.EnergyUpgrade(this, tile, 3));
				}
			}
		}
	}
}
