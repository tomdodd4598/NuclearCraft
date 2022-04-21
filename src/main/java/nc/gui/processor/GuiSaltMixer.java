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

public class GuiSaltMixer extends GuiFluidProcessor {
	
	public GuiSaltMixer(EntityPlayer player, TileFluidProcessor tile) {
		this(player, tile, new ContainerSaltMixer(player, tile));
	}
	
	protected GuiSaltMixer(EntityPlayer player, TileFluidProcessor tile, ContainerTile<TileFluidProcessor> container) {
		super("salt_mixer", player, tile, container);
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 16, 74);
		renderButtonTooltips(mouseX, mouseY);
	}
	
	public void renderButtonTooltips(int mouseX, int mouseY) {
		drawFluidTooltip(tile.getTanks().get(0), mouseX, mouseY, 46, 35, 16, 16);
		drawFluidTooltip(tile.getTanks().get(1), mouseX, mouseY, 66, 35, 16, 16);
		drawFluidTooltip(tile.getTanks().get(2), mouseX, mouseY, 122, 31, 24, 24);
		
		drawTooltip(Lang.localise("gui.nc.container.machine_side_config"), mouseX, mouseY, 27, 63, 18, 18);
		drawTooltip(Lang.localise("gui.nc.container.redstone_control"), mouseX, mouseY, 47, 63, 18, 18);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		if (tile.defaultProcessPower != 0) {
			int e = (int) Math.round(74D * tile.getEnergyStorage().getEnergyStored() / tile.getEnergyStorage().getMaxEnergyStored());
			drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - e, 176, 90 + 74 - e, 16, e);
		}
		else {
			drawGradientRect(guiLeft + 8, guiTop + 6, guiLeft + 8 + 16, guiTop + 6 + 74, 0xFFC6C6C6, 0xFF8B8B8B);
		}
		
		drawTexturedModalRect(guiLeft + 84, guiTop + 34, 176, 3, getCookProgressScaled(37), 18);
		
		drawUpgradeRenderers();
		
		drawBackgroundExtras();
	}
	
	protected void drawBackgroundExtras() {
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(0), guiLeft + 46, guiTop + 35, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(1), guiLeft + 66, guiTop + 35, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(2), guiLeft + 122, guiTop + 31, zLevel, 24, 24);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		initButtons();
	}
	
	public void initButtons() {
		buttonList.add(new NCButton.EmptyTank(0, guiLeft + 46, guiTop + 35, 16, 16));
		buttonList.add(new NCButton.EmptyTank(1, guiLeft + 66, guiTop + 35, 16, 16));
		buttonList.add(new NCButton.EmptyTank(2, guiLeft + 122, guiTop + 31, 24, 24));
		
		buttonList.add(new NCButton.MachineConfig(3, guiLeft + 27, guiTop + 63));
		buttonList.add(new NCToggleButton.RedstoneControl(4, guiLeft + 47, guiTop + 63, tile));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 3; ++i) {
				if (guiButton.id == i && NCUtil.isModifierKeyDown()) {
					PacketHandler.instance.sendToServer(new EmptyTankPacket(tile, i));
					return;
				}
			}
			if (guiButton.id == 3) {
				PacketHandler.instance.sendToServer(new OpenSideConfigGuiPacket(tile));
			}
			else if (guiButton.id == 4) {
				tile.setRedstoneControl(!tile.getRedstoneControl());
				PacketHandler.instance.sendToServer(new ToggleRedstoneControlPacket(tile));
			}
		}
	}
	
	public static class SideConfig extends GuiSaltMixer {
		
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
			drawTooltip(TextFormatting.DARK_AQUA + Lang.localise("gui.nc.container.input_tank_config"), mouseX, mouseY, 45, 34, 18, 18);
			drawTooltip(TextFormatting.DARK_AQUA + Lang.localise("gui.nc.container.input_tank_config"), mouseX, mouseY, 65, 34, 18, 18);
			drawTooltip(TextFormatting.RED + Lang.localise("gui.nc.container.output_tank_config"), mouseX, mouseY, 121, 30, 26, 26);
			drawTooltip(TextFormatting.DARK_BLUE + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 131, 63, 18, 18);
			drawTooltip(TextFormatting.YELLOW + Lang.localise("gui.nc.container.upgrade_config"), mouseX, mouseY, 151, 63, 18, 18);
		}
		
		@Override
		protected void drawUpgradeRenderers() {}
		
		@Override
		protected void drawBackgroundExtras() {}
		
		@Override
		public void initButtons() {
			buttonList.add(new NCButton.SorptionConfig.FluidInput(0, guiLeft + 45, guiTop + 34));
			buttonList.add(new NCButton.SorptionConfig.FluidInput(1, guiLeft + 65, guiTop + 34));
			buttonList.add(new NCButton.SorptionConfig.FluidOutput(2, guiLeft + 121, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.SpeedUpgrade(3, guiLeft + 131, guiTop + 63));
			buttonList.add(new NCButton.SorptionConfig.EnergyUpgrade(4, guiLeft + 151, guiTop + 63));
		}
		
		@Override
		protected void actionPerformed(GuiButton guiButton) {
			if (tile.getWorld().isRemote) {
				if (guiButton.id == 0) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Input<>(this, tile, 0));
				}
				else if (guiButton.id == 1) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Input<>(this, tile, 1));
				}
				else if (guiButton.id == 2) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output<>(this, tile, 2));
				}
				else if (guiButton.id == 3) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.SpeedUpgrade<>(this, tile, 0));
				}
				else if (guiButton.id == 4) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.EnergyUpgrade<>(this, tile, 1));
				}
			}
		}
	}
}
