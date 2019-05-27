package nc.gui.generator;

import java.util.List;

import com.google.common.collect.Lists;

import nc.Global;
import nc.config.NCConfig;
import nc.container.generator.ContainerFusionCore;
import nc.gui.NCGui;
import nc.gui.element.GuiFluidRenderer;
import nc.gui.element.NCGuiButton;
import nc.gui.element.NCGuiToggleButton;
import nc.network.PacketHandler;
import nc.network.gui.EmptyTankPacket;
import nc.network.gui.ToggleAlternateComparatorPacket;
import nc.network.gui.ToggleInputTanksSeparatedPacket;
import nc.network.gui.ToggleVoidExcessFluidOutputPacket;
import nc.network.gui.ToggleVoidUnusableFluidInputPacket;
import nc.tile.energy.ITileEnergy;
import nc.tile.generator.TileFusionCore;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.UnitHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GuiFusionCore extends NCGui {
	
	protected TileFusionCore tile;
	protected final ResourceLocation gui_textures;

	public GuiFusionCore(EntityPlayer player, TileFusionCore tile) {
		super(new ContainerFusionCore(player, tile));
		this.tile = tile;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "fusion_core" + ".png");
		xSize = 196;
		ySize = 187;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawTooltip(Lang.localise("gui.container.change_tanks_mode"), mouseX, mouseY, 171, 104, 18, 18);
		drawTooltip(Lang.localise("gui.container.void_leftover_fluid"), mouseX, mouseY, 171, 123, 18, 18);
		drawTooltip(Lang.localise("gui.container.void_outputs"), mouseX, mouseY, 171, 142, 18, 18);
		drawTooltip(Lang.localise("gui.container.comparator_mode"), mouseX, mouseY, 171, 162, 18, 18);
		
		drawFluidTooltip(tile.getTanks().get(0), mouseX, mouseY, 38, 6, 6, 46);
		drawFluidTooltip(tile.getTanks().get(1), mouseX, mouseY, 38, 55, 6, 46);
		drawFluidTooltip(tile.getTanks().get(2), mouseX, mouseY, 172, 6, 6, 46);
		drawFluidTooltip(tile.getTanks().get(3), mouseX, mouseY, 182, 6, 6, 46);
		drawFluidTooltip(tile.getTanks().get(4), mouseX, mouseY, 172, 55, 6, 46);
		drawFluidTooltip(tile.getTanks().get(5), mouseX, mouseY, 182, 55, 6, 46);
		
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 6, 95);
		drawHeatTooltip(mouseX, mouseY, 18, 6, 6, 95);
		drawEfficiencyTooltip(mouseX, mouseY, 28, 6, 6, 95);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = tile.isProcessing ? -1 : (tile.complete == 1 ? 15641088 : 15597568);
		String name = Lang.localise("gui.container.fusion_core.reactor");
		fontRenderer.drawString(name, 108 - widthHalf(name), 10, fontColor);
		String size = tile.complete == 1 ? (Lang.localise("gui.container.fusion_core.size") + " " + tile.size) : tile.problem;
		fontRenderer.drawString(size, 108 - widthHalf(size), 21, fontColor);
		String energy = Lang.localise("gui.container.fusion_core.energy") + " " + UnitHelper.prefix(tile.getEnergyStorage().getEnergyStored(), 6, "RF");
		fontRenderer.drawString(energy, 108 - widthHalf(energy), 32, fontColor);
		String power = Lang.localise("gui.container.fusion_core.power") + " " + UnitHelper.prefix((int)tile.processPower, 6, "RF/t");
		fontRenderer.drawString(power, 108 - widthHalf(power), 43, fontColor);
		String heat = Lang.localise("gui.container.fusion_core.heat") + " " + UnitHelper.prefix((int) tile.heat, 6, "K", 1);
		fontRenderer.drawString(heat, 108 - widthHalf(heat), 54, fontColor);
		String efficiency = Lang.localise("gui.container.fusion_core.efficiency") + " " + ((int) tile.efficiency) + "%";
		fontRenderer.drawString(efficiency, 108 - widthHalf(efficiency), 65, fontColor);
		String input1 = tile.getTanks().get(0).getFluid() != null ? tile.getTanks().get(0).getFluidLocalizedName() : (tile.getTanks().get(6).getFluid() != null ? tile.getTanks().get(6).getFluidLocalizedName() : TileFusionCore.NO_FUEL);
		String input2 = tile.getTanks().get(1).getFluid() != null ? tile.getTanks().get(1).getFluidLocalizedName() : (tile.getTanks().get(7).getFluid() != null ? tile.getTanks().get(7).getFluidLocalizedName() : TileFusionCore.NO_FUEL);
		fontRenderer.drawString(input1, 108 - widthHalf(input1), 76, fontColor);
		fontRenderer.drawString(input2, 108 - widthHalf(input2), 87, fontColor);
	}
	
	@Override
	public List<String> energyInfo(ITileEnergy tile) {
		String energy = UnitHelper.prefix(tile.getEnergyStorage().getEnergyStored(), tile.getEnergyStorage().getMaxEnergyStored(), 6, "RF");
		String power = UnitHelper.prefix((int)this.tile.processPower, 6, "RF/t");
		return Lists.newArrayList(TextFormatting.LIGHT_PURPLE + Lang.localise("gui.container.energy_stored") + TextFormatting.WHITE + " " + energy, TextFormatting.LIGHT_PURPLE + Lang.localise("gui.container.power_gen") + TextFormatting.WHITE + " " + power);
	}
	
	public List<String> heatInfo() {
		String heat = UnitHelper.prefix((int) tile.heat, tile.isHotEnough() ? (int) tile.getMaxHeat() : 8000, 6, "K", 1) + (tile.isHotEnough() ? "" : " [" + Math.round(tile.heat/80D) + "%]");
		String heatChange = UnitHelper.prefix((int) tile.heatChange, 6, "K/t", 0);
		String cooling = UnitHelper.prefix((int) tile.cooling, 6, "K/t", 0);
		if ((int) tile.cooling == 0 || !NCConfig.fusion_active_cooling) return Lists.newArrayList(TextFormatting.YELLOW + Lang.localise("gui.container.fusion_core.temperature") + TextFormatting.WHITE + " " + heat, TextFormatting.YELLOW + Lang.localise("gui.container.fusion_core.temperature_change") + TextFormatting.WHITE + " " + heatChange);
		int coolingPercentage = (int) (0.1D*tile.cooling/(5D*NCConfig.fusion_heat_generation));
		return Lists.newArrayList(TextFormatting.YELLOW + Lang.localise("gui.container.fusion_core.temperature") + TextFormatting.WHITE + " " + heat, TextFormatting.YELLOW + Lang.localise("gui.container.fusion_core.temperature_change") + TextFormatting.WHITE + " " + heatChange, TextFormatting.BLUE + Lang.localise("gui.container.fusion_core.cooling_rate") + TextFormatting.WHITE + " " + cooling + " [" + coolingPercentage + "%]");
	}
	
	public void drawHeatTooltip(int mouseX, int mouseY, int x, int y, int width, int height) {
		drawTooltip(heatInfo(), mouseX, mouseY, x, y, width, height);
	}
	
	public List<String> efficiencyInfo() {
		String efficiency = NCMath.round(tile.efficiency, 1) + "%";
		return Lists.newArrayList(TextFormatting.AQUA + Lang.localise("gui.container.fusion_core.efficiency") + TextFormatting.WHITE + " " + efficiency);
	}
	
	public void drawEfficiencyTooltip(int mouseX, int mouseY, int x, int y, int width, int height) {
		drawTooltip(efficiencyInfo(), mouseX, mouseY, x, y, width, height);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(gui_textures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		double energy = Math.round(((double) tile.getEnergyStorage().getEnergyStored()) / ((double) tile.getEnergyStorage().getMaxEnergyStored()) * 95D);
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 95 - (int) energy, 196, 90 + 95 - (int) energy, 6, (int) energy);
		
		double h = Math.round((tile.heat / (tile.isHotEnough() ? tile.getMaxHeat() : 8000)) * 95D);
		drawTexturedModalRect(guiLeft + 18, guiTop + 6 + 95 - (int) h, 202, 90 + 95 - (int) h, 6, (int) h);
		
		double efficiency = Math.round((tile.efficiency / 100D) * 95D);
		drawTexturedModalRect(guiLeft + 28, guiTop + 6 + 95 - (int) efficiency, 208, 90 + 95 - (int) efficiency, 6, (int) efficiency);
		
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(0), guiLeft + 38, guiTop + 6, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(1), guiLeft + 38, guiTop + 55, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(2), guiLeft + 172, guiTop + 6, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(3), guiLeft + 182, guiTop + 6, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(4), guiLeft + 172, guiTop + 55, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(5), guiLeft + 182, guiTop + 55, zLevel, 6, 46);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new NCGuiButton.EmptyTankButton(0, guiLeft + 38, guiTop + 6, 6, 46));
		buttonList.add(new NCGuiButton.EmptyTankButton(1, guiLeft + 38, guiTop + 55, 6, 46));
		buttonList.add(new NCGuiButton.EmptyTankButton(2, guiLeft + 172, guiTop + 6, 6, 46));
		buttonList.add(new NCGuiButton.EmptyTankButton(3, guiLeft + 182, guiTop + 6, 6, 46));
		buttonList.add(new NCGuiButton.EmptyTankButton(4, guiLeft + 172, guiTop + 55, 6, 46));
		buttonList.add(new NCGuiButton.EmptyTankButton(5, guiLeft + 182, guiTop + 55, 6, 46));
		buttonList.add(new NCGuiToggleButton.ToggleInputTanksSeparatedButton(6, guiLeft + 171, guiTop + 104, tile));
		buttonList.add(new NCGuiToggleButton.ToggleVoidUnusableFluidInputButton(7, guiLeft + 171, guiTop + 123, tile, 2));
		buttonList.add(new NCGuiToggleButton.ToggleVoidExcessFluidOutputButton(8, guiLeft + 171, guiTop + 142, tile, 2));
		buttonList.add(new NCGuiToggleButton.ToggleAlternateComparatorButton(9, guiLeft + 171, guiTop + 162, tile));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 6; i++) if (guiButton.id == i && isShiftKeyDown()) {
				PacketHandler.instance.sendToServer(new EmptyTankPacket(tile, i));
			}
			if (guiButton.id == 6) {
				tile.setInputTanksSeparated(!tile.getInputTanksSeparated());
				PacketHandler.instance.sendToServer(new ToggleInputTanksSeparatedPacket(tile));
			}
			if (guiButton.id == 7) {
				for (int i = 0; i < tile.getTanks().size(); i++) {
					tile.setVoidUnusableFluidInput(i, !tile.getVoidUnusableFluidInput(i));
					PacketHandler.instance.sendToServer(new ToggleVoidUnusableFluidInputPacket(tile, i));
				}
			}
			if (guiButton.id == 8) {
				for (int i = 0; i < tile.getTanks().size(); i++) {
					tile.setVoidExcessFluidOutput(i, !tile.getVoidExcessFluidOutput(i));
					PacketHandler.instance.sendToServer(new ToggleVoidExcessFluidOutputPacket(tile, i));
				}
			}
			if (guiButton.id == 9) {
				tile.setAlternateComparator(!tile.getAlternateComparator());
				PacketHandler.instance.sendToServer(new ToggleAlternateComparatorPacket(tile));
			}
		}
	}
}
