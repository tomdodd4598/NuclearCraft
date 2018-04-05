package nc.gui.generator;

import java.util.List;

import com.google.common.collect.Lists;

import nc.Global;
import nc.container.generator.ContainerFissionController;
import nc.gui.NCGui;
import nc.tile.energy.ITileEnergy;
import nc.tile.generator.TileFissionController;
import nc.util.Lang;
import nc.util.UnitHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GuiFissionController extends NCGui {
	
	private final InventoryPlayer playerInventory;
	protected TileFissionController tile;
	protected final ResourceLocation gui_textures;

	public GuiFissionController(EntityPlayer player, TileFissionController tile) {
		super(new ContainerFissionController(player, tile));
		playerInventory = player.inventory;
		this.tile = tile;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "fission_controller" + ".png");
		xSize = 176;
		ySize = 177;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 6, 85);
		drawHeatTooltip(mouseX, mouseY, 18, 6, 6, 85);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = tile.isGenerating ? -1 : (tile.complete == 1 ? 15641088 : 15597568);
		String s = tile.complete == 1 ? (tile.getLengthX() + "*" +  tile.getLengthY() + "*" +  tile.getLengthZ() + " " + Lang.localise("gui.container.fission_controller.reactor")) : tile.problem;
		fontRenderer.drawString(s, 8 + xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, fontColor);
		String s2 = tile.problemPosBool == 0 ? "" : Lang.localise("gui.container.fission_controller.pos") + " " + tile.problemPos;
		fontRenderer.drawString(s2, 8 + xSize / 2 - fontRenderer.getStringWidth(s2) / 2, 17, fontColor);
		String fuel = tile.getFuelName().endsWith("OXIDE") ? tile.getFuelName().substring(0, tile.getFuelName().length() - 3) : tile.getFuelName();
		fontRenderer.drawString(fuel, 28, ySize - 104, fontColor);
		String cells = Lang.localise("gui.container.fission_controller.cells") + " " + tile.cells;
		fontRenderer.drawString(cells, 28, ySize - 93, fontColor);
		String power = UnitHelper.prefix(tile.processPower, 6, "RF/t");
		fontRenderer.drawString(power, 170 - fontRenderer.getStringWidth(power), ySize - 104, fontColor);
		String heatGen = UnitHelper.prefix(tile.heatChange, 6, "H/t");
		fontRenderer.drawString(heatGen, 170 - fontRenderer.getStringWidth(heatGen), ySize - 93, fontColor);
	}
	
	@Override
	public List<String> energyInfo(ITileEnergy tile) {
		String energy = UnitHelper.prefix(tile.getStorage().getEnergyStored(), tile.getStorage().getMaxEnergyStored(), 6, "RF");
		String power = UnitHelper.prefix(this.tile.getProcessPower(), 6, "RF/t");
		String efficiency = this.tile.efficiency + "%";
		return Lists.newArrayList(TextFormatting.LIGHT_PURPLE + Lang.localise("gui.container.energy_stored") + TextFormatting.WHITE + " " + energy, TextFormatting.LIGHT_PURPLE + Lang.localise("gui.container.power_gen") + TextFormatting.WHITE + " " + power, TextFormatting.LIGHT_PURPLE + Lang.localise("gui.container.fission_controller.efficiency") + TextFormatting.WHITE + " " + efficiency);
	}
	
	public List<String> heatInfo() {
		String heat = UnitHelper.prefix(tile.heat, tile.getMaxHeat(), 6, "H");
		String heatGen = UnitHelper.prefix(tile.heatChange, 6, "H/t");
		String cooling = UnitHelper.prefix(tile.cooling, 6, "H/t");
		String heatMult = this.tile.heatMult + "%";
		return Lists.newArrayList(TextFormatting.YELLOW + Lang.localise("gui.container.fission_controller.heat") + TextFormatting.WHITE + " " + heat, TextFormatting.YELLOW + Lang.localise("gui.container.fission_controller.heat_gen") + TextFormatting.WHITE + " " + heatGen, TextFormatting.BLUE + Lang.localise("gui.container.fission_controller.cooling") + TextFormatting.WHITE + " " + cooling, TextFormatting.YELLOW + Lang.localise("gui.container.fission_controller.heat_mult") + TextFormatting.WHITE + " " + heatMult);
	}
	
	public void drawHeatTooltip(int mouseX, int mouseY, int x, int y, int width, int height) {
		drawTooltip(heatInfo(), mouseX, mouseY, x, y, width, height);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(gui_textures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		double e = Math.round(((double) tile.storage.getEnergyStored()) / ((double) tile.storage.getMaxEnergyStored()) * 85);
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 85 - (int) e, 176, 90 + 85 - (int) e, 6, (int) e);
		
		double h = Math.round(((double) tile.heat) / ((double) tile.getMaxHeat()) * 85);
		drawTexturedModalRect(guiLeft + 18, guiTop + 6 + 85 - (int) h, 182, 90 + 85 - (int) h, 6, (int) h);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 74, guiTop + 35, 176, 3, k, 16);
	}
	
	protected int getCookProgressScaled(double pixels) {
		double i = tile.getField(0);
		double j = tile.getProcessTime();
		return j != 0D ? (int) Math.round(i * pixels / j) : 0;
	}
}
