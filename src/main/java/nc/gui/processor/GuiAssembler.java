package nc.gui.processor;

import java.io.IOException;

import nc.container.ContainerTile;
import nc.container.processor.ContainerAssembler;
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

public class GuiAssembler extends GuiItemProcessor {
	
	public GuiAssembler(EntityPlayer player, TileItemProcessor tile) {
		this(player, tile, new ContainerAssembler(player, tile));
	}
	
	private GuiAssembler(EntityPlayer player, TileItemProcessor tile, ContainerTile container) {
		super("assembler", player, tile, container);
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
		
		drawTexturedModalRect(guiLeft + 84, guiTop + 31, 176, 3, getCookProgressScaled(37), 38);
		
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
	
	public static class SideConfig extends GuiAssembler {
		
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
			drawTooltip(TextFormatting.BLUE + Lang.localise("gui.nc.container.input_item_config"), mouseX, mouseY, 45, 30, 18, 18);
			drawTooltip(TextFormatting.BLUE + Lang.localise("gui.nc.container.input_item_config"), mouseX, mouseY, 65, 30, 18, 18);
			drawTooltip(TextFormatting.BLUE + Lang.localise("gui.nc.container.input_item_config"), mouseX, mouseY, 45, 50, 18, 18);
			drawTooltip(TextFormatting.BLUE + Lang.localise("gui.nc.container.input_item_config"), mouseX, mouseY, 65, 50, 18, 18);
			drawTooltip(TextFormatting.GOLD + Lang.localise("gui.nc.container.output_item_config"), mouseX, mouseY, 121, 36, 26, 26);
			drawTooltip(TextFormatting.DARK_BLUE + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 131, 75, 18, 18);
			drawTooltip(TextFormatting.YELLOW + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 151, 75, 18, 18);
		}
		
		@Override
		protected void drawUpgradeRenderers() {}
		
		@Override
		protected void drawBackgroundExtras() {};
		
		@Override
		public void initButtons() {
			buttonList.add(new NCButton.SorptionConfig.ItemInput(0, guiLeft + 45, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.ItemInput(1, guiLeft + 65, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.ItemInput(2, guiLeft + 45, guiTop + 50));
			buttonList.add(new NCButton.SorptionConfig.ItemInput(3, guiLeft + 65, guiTop + 50));
			buttonList.add(new NCButton.SorptionConfig.ItemOutput(4, guiLeft + 121, guiTop + 36));
			buttonList.add(new NCButton.SorptionConfig.SpeedUpgrade(5, guiLeft + 131, guiTop + 75));
			buttonList.add(new NCButton.SorptionConfig.EnergyUpgrade(6, guiLeft + 151, guiTop + 75));
		}
		
		@Override
		protected void actionPerformed(GuiButton guiButton) {
			if (tile.getWorld().isRemote) {
				if (guiButton.id == 0) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Input(this, tile, 0));
				}
				else if (guiButton.id == 1) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Input(this, tile, 1));
				}
				else if (guiButton.id == 2) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Input(this, tile, 2));
				}
				else if (guiButton.id == 3) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Input(this, tile, 3));
				}
				else if (guiButton.id == 4) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Output(this, tile, 4));
				}
				else if (guiButton.id == 5) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.SpeedUpgrade(this, tile, 5));
				}
				else if (guiButton.id == 6) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.EnergyUpgrade(this, tile, 6));
				}
			}
		}
	}
}
