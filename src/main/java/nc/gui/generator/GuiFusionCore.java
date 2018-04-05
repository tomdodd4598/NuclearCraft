package nc.gui.generator;

import java.util.List;

import com.google.common.collect.Lists;

import nc.Global;
import nc.config.NCConfig;
import nc.container.generator.ContainerFusionCore;
import nc.gui.GuiFluidRenderer;
import nc.gui.NCGui;
import nc.gui.NCGuiButton;
import nc.gui.NCGuiToggleButton;
import nc.network.PacketEmptyTankButton;
import nc.network.PacketGetFluidInTank;
import nc.network.PacketHandler;
import nc.network.PacketToggleTanksSharedButton;
import nc.tile.energy.ITileEnergy;
import nc.tile.generator.TileFusionCore;
import nc.util.Lang;
import nc.util.UnitHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

public class GuiFusionCore extends NCGui {
	
	public static int tick;
	
	public static FluidStack fluid0, fluid1, fluid2, fluid3, fluid4, fluid5, fluid6, fluid7 = null;
	
	private final InventoryPlayer playerInventory;
	protected TileFusionCore tile;
	protected final ResourceLocation gui_textures;

	public GuiFusionCore(EntityPlayer player, TileFusionCore tile) {
		super(new ContainerFusionCore(player, tile));
		playerInventory = player.inventory;
		this.tile = tile;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "fusion_core" + ".png");
		xSize = 218;
		ySize = 187;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawTooltip(Lang.localise("gui.container.change_tanks_mode"), mouseX, mouseY, 195, 5, 18, 18);
		
		drawFluidTooltip(fluid0, tile.tanks[0], mouseX, mouseY, 38, 6, 6, 46);
		drawFluidTooltip(fluid1, tile.tanks[1], mouseX, mouseY, 38, 55, 6, 46);
		drawFluidTooltip(fluid2, tile.tanks[2], mouseX, mouseY, 172, 6, 6, 46);
		drawFluidTooltip(fluid3, tile.tanks[3], mouseX, mouseY, 182, 6, 6, 46);
		drawFluidTooltip(fluid4, tile.tanks[4], mouseX, mouseY, 172, 55, 6, 46);
		drawFluidTooltip(fluid5, tile.tanks[5], mouseX, mouseY, 182, 55, 6, 46);
		
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 6, 95);
		drawHeatTooltip(mouseX, mouseY, 18, 6, 6, 95);
		drawEfficiencyTooltip(mouseX, mouseY, 28, 6, 6, 95);
	}

	protected int widthHalf(String s) {
		return fontRenderer.getStringWidth(s)/2;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = tile.isGenerating() || tile.canProcess() ? -1 : (tile.complete == 1 ? 15641088 : 15597568);
		String name = Lang.localise("gui.container.fusion_core.reactor");
		fontRenderer.drawString(name, 108 - widthHalf(name), 10, fontColor);
		String size = tile.complete == 1 ? (Lang.localise("gui.container.fusion_core.size") + " " + tile.size) : tile.problem;
		fontRenderer.drawString(size, 108 - widthHalf(size), 21, fontColor);
		String energy = Lang.localise("gui.container.fusion_core.energy") + " " + UnitHelper.prefix(tile.storage.getEnergyStored(), 6, "RF");
		fontRenderer.drawString(energy, 108 - widthHalf(energy), 32, fontColor);
		String power = Lang.localise("gui.container.fusion_core.power") + " " + UnitHelper.prefix((int) tile.processPower, 6, "RF/t");
		fontRenderer.drawString(power, 108 - widthHalf(power), 43, fontColor);
		String heat = Lang.localise("gui.container.fusion_core.heat") + " " + UnitHelper.prefix((int) tile.heat, 6, "K", 1);
		fontRenderer.drawString(heat, 108 - widthHalf(heat), 54, fontColor);
		String efficiency = Lang.localise("gui.container.fusion_core.efficiency") + " " + ((int) tile.efficiency) + "%";
		fontRenderer.drawString(efficiency, 108 - widthHalf(efficiency), 65, fontColor);
		String input1 = fluid0 != null ? fluid0.getLocalizedName() : (fluid6 != null ? fluid6.getLocalizedName() : Lang.localise("gui.container.fusion_core.empty"));
		String input2 = fluid1 != null ? fluid1.getLocalizedName() : (fluid7 != null ? fluid7.getLocalizedName() : Lang.localise("gui.container.fusion_core.empty"));
		fontRenderer.drawString(input1, 108 - widthHalf(input1), 76, fontColor);
		fontRenderer.drawString(input2, 108 - widthHalf(input2), 87, fontColor);
	}
	
	@Override
	public List<String> energyInfo(ITileEnergy tile) {
		String energy = UnitHelper.prefix(tile.getStorage().getEnergyStored(), tile.getStorage().getMaxEnergyStored(), 6, "RF");
		String power = UnitHelper.prefix(this.tile.getProcessPower(), 6, "RF/t");
		return Lists.newArrayList(TextFormatting.LIGHT_PURPLE + Lang.localise("gui.container.energy_stored") + TextFormatting.WHITE + " " + energy, TextFormatting.LIGHT_PURPLE + Lang.localise("gui.container.power_gen") + TextFormatting.WHITE + " " + power);
	}
	
	public List<String> heatInfo() {
		String heat = UnitHelper.prefix((int) tile.heat, (int) tile.getMaxHeat(), 6, "K", 1);
		String heatChange = UnitHelper.prefix((int) tile.heatChange, 6, "K/t", 0);
		String cooling = UnitHelper.prefix((int) tile.cooling, 6, "K/t", 0);
		if ((int) tile.cooling == 0 || !NCConfig.fusion_active_cooling) return Lists.newArrayList(TextFormatting.YELLOW + Lang.localise("gui.container.fusion_core.temperature") + TextFormatting.WHITE + " " + heat, TextFormatting.YELLOW + Lang.localise("gui.container.fusion_core.temperature_change") + TextFormatting.WHITE + " " + heatChange);
		return Lists.newArrayList(TextFormatting.YELLOW + Lang.localise("gui.container.fusion_core.temperature") + TextFormatting.WHITE + " " + heat, TextFormatting.YELLOW + Lang.localise("gui.container.fusion_core.temperature_change") + TextFormatting.WHITE + " " + heatChange, TextFormatting.BLUE + Lang.localise("gui.container.fusion_core.cooling_rate") + TextFormatting.WHITE + " " + cooling);
	}
	
	public void drawHeatTooltip(int mouseX, int mouseY, int x, int y, int width, int height) {
		drawTooltip(heatInfo(), mouseX, mouseY, x, y, width, height);
	}
	
	public List<String> efficiencyInfo() {
		String efficiency = ((int) tile.efficiency) + "%";
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
		
		double energy = Math.round(((double) tile.storage.getEnergyStored()) / ((double) tile.storage.getMaxEnergyStored()) * 95D);
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 95 - (int) energy, 218, 90 + 95 - (int) energy, 6, (int) energy);
		
		double h = Math.round((tile.heat / tile.getMaxHeat()) * 95D);
		drawTexturedModalRect(guiLeft + 18, guiTop + 6 + 95 - (int) h, 224, 90 + 95 - (int) h, 6, (int) h);
		
		double efficiency = Math.round((tile.efficiency / 100D) * 95D);
		drawTexturedModalRect(guiLeft + 28, guiTop + 6 + 95 - (int) efficiency, 230, 90 + 95 - (int) efficiency, 6, (int) efficiency);
		
		tick++;
		tick %= 10;
		
		if (tick == 0) {
			PacketHandler.instance.sendToServer(new PacketGetFluidInTank(tile.getPos(), 0, "nc.gui.generator.GuiFusionCore", "fluid0"));
			PacketHandler.instance.sendToServer(new PacketGetFluidInTank(tile.getPos(), 1, "nc.gui.generator.GuiFusionCore", "fluid1"));
			PacketHandler.instance.sendToServer(new PacketGetFluidInTank(tile.getPos(), 2, "nc.gui.generator.GuiFusionCore", "fluid2"));
			PacketHandler.instance.sendToServer(new PacketGetFluidInTank(tile.getPos(), 3, "nc.gui.generator.GuiFusionCore", "fluid3"));
			PacketHandler.instance.sendToServer(new PacketGetFluidInTank(tile.getPos(), 4, "nc.gui.generator.GuiFusionCore", "fluid4"));
			PacketHandler.instance.sendToServer(new PacketGetFluidInTank(tile.getPos(), 5, "nc.gui.generator.GuiFusionCore", "fluid5"));
			PacketHandler.instance.sendToServer(new PacketGetFluidInTank(tile.getPos(), 6, "nc.gui.generator.GuiFusionCore", "fluid6"));
			PacketHandler.instance.sendToServer(new PacketGetFluidInTank(tile.getPos(), 7, "nc.gui.generator.GuiFusionCore", "fluid7"));
		}
		
		GuiFluidRenderer.renderGuiTank(fluid0, tile.tanks[0].getCapacity(), guiLeft + 38, guiTop + 6, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(fluid1, tile.tanks[1].getCapacity(), guiLeft + 38, guiTop + 55, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(fluid2, tile.tanks[2].getCapacity(), guiLeft + 172, guiTop + 6, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(fluid3, tile.tanks[3].getCapacity(), guiLeft + 182, guiTop + 6, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(fluid4, tile.tanks[4].getCapacity(), guiLeft + 172, guiTop + 55, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(fluid5, tile.tanks[5].getCapacity(), guiLeft + 182, guiTop + 55, zLevel, 6, 46);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new NCGuiToggleButton.ToggleTanksSharedButton(0, guiLeft + 195, guiTop + 5, tile, true));
		buttonList.add(new NCGuiButton.BlankButton(1, guiLeft + 38, guiTop + 6, 6, 46));
		buttonList.add(new NCGuiButton.BlankButton(2, guiLeft + 38, guiTop + 55, 6, 46));
		buttonList.add(new NCGuiButton.BlankButton(3, guiLeft + 172, guiTop + 6, 6, 46));
		buttonList.add(new NCGuiButton.BlankButton(4, guiLeft + 182, guiTop + 6, 6, 46));
		buttonList.add(new NCGuiButton.BlankButton(5, guiLeft + 172, guiTop + 55, 6, 46));
		buttonList.add(new NCGuiButton.BlankButton(6, guiLeft + 182, guiTop + 55, 6, 46));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			if (guiButton.id == 0) {
				tile.setTanksShared(!tile.getTanksShared());
				PacketHandler.instance.sendToServer(new PacketToggleTanksSharedButton(tile));
			}
			for (int i = 1; i <= 6; i++) if (guiButton.id == i && isShiftKeyDown()) {
				PacketHandler.instance.sendToServer(new PacketEmptyTankButton(tile, i - 1));
			}
		}
	}
}
