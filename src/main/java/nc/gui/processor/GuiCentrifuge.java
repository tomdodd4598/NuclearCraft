package nc.gui.processor;

import java.io.IOException;

import nc.container.ContainerTile;
import nc.container.processor.ContainerCentrifuge;
import nc.container.processor.ContainerMachineConfig;
import nc.gui.element.GuiFluidRenderer;
import nc.gui.element.NCButton;
import nc.gui.element.NCToggleButton;
import nc.network.PacketHandler;
import nc.network.gui.EmptyTankPacket;
import nc.network.gui.OpenSideConfigGuiPacket;
import nc.network.gui.OpenTileGuiPacket;
import nc.network.gui.ToggleRedstoneControlPacket;
import nc.tile.processor.TileFluidProcessor;
import nc.util.Lang;
import nc.util.NCUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiCentrifuge extends GuiFluidProcessor {
	
	public GuiCentrifuge(EntityPlayer player, TileFluidProcessor tile) {
		this(player, tile, new ContainerCentrifuge(player, tile));
	}
	
	private GuiCentrifuge(EntityPlayer player, TileFluidProcessor tile, ContainerTile container) {
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
		drawFluidTooltip(tile.getTanks().get(0), mouseX, mouseY, 50, 41, 16, 16);
		drawFluidTooltip(tile.getTanks().get(1), mouseX, mouseY, 106, 31, 16, 16);
		drawFluidTooltip(tile.getTanks().get(2), mouseX, mouseY, 126, 31, 16, 16);
		drawFluidTooltip(tile.getTanks().get(3), mouseX, mouseY, 106, 51, 16, 16);
		drawFluidTooltip(tile.getTanks().get(4), mouseX, mouseY, 126, 51, 16, 16);
		
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
		
		drawTexturedModalRect(guiLeft + 68, guiTop + 30, 176, 3, getCookProgressScaled(37), 38);
		
		drawUpgradeRenderers();
		
		drawBackgroundExtras();
	}
	
	protected void drawBackgroundExtras() {
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(0), guiLeft + 50, guiTop + 41, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(1), guiLeft + 106, guiTop + 31, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(2), guiLeft + 126, guiTop + 31, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(3), guiLeft + 106, guiTop + 51, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(4), guiLeft + 126, guiTop + 51, zLevel, 16, 16);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		initButtons();
	}
	
	public void initButtons() {
		buttonList.add(new NCButton.EmptyTank(0, guiLeft + 50, guiTop + 41, 16, 16));
		buttonList.add(new NCButton.EmptyTank(1, guiLeft + 106, guiTop + 31, 16, 16));
		buttonList.add(new NCButton.EmptyTank(2, guiLeft + 126, guiTop + 31, 16, 16));
		buttonList.add(new NCButton.EmptyTank(3, guiLeft + 106, guiTop + 51, 16, 16));
		buttonList.add(new NCButton.EmptyTank(4, guiLeft + 126, guiTop + 51, 16, 16));
		
		buttonList.add(new NCButton.MachineConfig(5, guiLeft + 27, guiTop + 75));
		buttonList.add(new NCToggleButton.RedstoneControl(6, guiLeft + 47, guiTop + 75, tile));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 5; i++) if (guiButton.id == i && NCUtil.isModifierKeyDown()) {
				PacketHandler.instance.sendToServer(new EmptyTankPacket(tile, i));
				return;
			}
			if (guiButton.id == 5) {
				PacketHandler.instance.sendToServer(new OpenSideConfigGuiPacket(tile));
			}
			else if (guiButton.id == 6) {
				tile.setRedstoneControl(!tile.getRedstoneControl());
				PacketHandler.instance.sendToServer(new ToggleRedstoneControlPacket(tile));
			}
		}
	}
	
	public static class SideConfig extends GuiCentrifuge {
		
		public SideConfig(EntityPlayer player, TileFluidProcessor tile) {
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
			drawTooltip(TextFormatting.DARK_AQUA + Lang.localise("gui.nc.container.input_tank_config"), mouseX, mouseY, 49, 40, 18, 18);
			drawTooltip(TextFormatting.RED + Lang.localise("gui.nc.container.output_tank_config"), mouseX, mouseY, 105, 30, 18, 18);
			drawTooltip(TextFormatting.RED + Lang.localise("gui.nc.container.output_tank_config"), mouseX, mouseY, 125, 30, 18, 18);
			drawTooltip(TextFormatting.RED + Lang.localise("gui.nc.container.output_tank_config"), mouseX, mouseY, 105, 50, 18, 18);
			drawTooltip(TextFormatting.RED + Lang.localise("gui.nc.container.output_tank_config"), mouseX, mouseY, 125, 50, 18, 18);
			drawTooltip(TextFormatting.DARK_BLUE + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 131, 75, 18, 18);
			drawTooltip(TextFormatting.YELLOW + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 151, 75, 18, 18);
		}
		
		@Override
		protected void drawUpgradeRenderers() {}
		
		@Override
		protected void drawBackgroundExtras() {};
		
		@Override
		public void initButtons() {
			buttonList.add(new NCButton.SorptionConfig.FluidInput(0, guiLeft + 49, guiTop + 40));
			buttonList.add(new NCButton.SorptionConfig.FluidOutputSmall(1, guiLeft + 105, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.FluidOutputSmall(2, guiLeft + 125, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.FluidOutputSmall(3, guiLeft + 105, guiTop + 50));
			buttonList.add(new NCButton.SorptionConfig.FluidOutputSmall(4, guiLeft + 125, guiTop + 50));
			buttonList.add(new NCButton.SorptionConfig.SpeedUpgrade(5, guiLeft + 131, guiTop + 75));
			buttonList.add(new NCButton.SorptionConfig.EnergyUpgrade(6, guiLeft + 151, guiTop + 75));
		}
		
		@Override
		protected void actionPerformed(GuiButton guiButton) {
			if (tile.getWorld().isRemote) {
				if (guiButton.id == 0) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Input(this, tile, 0));
				}
				else if (guiButton.id == 1) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output(this, tile, 1));
				}
				else if (guiButton.id == 2) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output(this, tile, 2));
				}
				else if (guiButton.id == 3) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output(this, tile, 3));
				}
				else if (guiButton.id == 4) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output(this, tile, 4));
				}
				else if (guiButton.id == 5) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.SpeedUpgrade(this, tile, 0));
				}
				else if (guiButton.id == 6) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.EnergyUpgrade(this, tile, 1));
				}
			}
		}
	}
}
