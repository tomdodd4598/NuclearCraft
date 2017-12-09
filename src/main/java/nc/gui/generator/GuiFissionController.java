package nc.gui.generator;

import java.util.List;

import com.google.common.collect.Lists;

import nc.Global;
import nc.container.generator.ContainerFissionController;
import nc.gui.GuiNC;
import nc.tile.energy.ITileEnergy;
import nc.tile.generator.TileFissionController;
import nc.util.NCMath;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class GuiFissionController extends GuiNC {
	
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

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = tile.isGenerating ? -1 : (tile.complete == 1 ? 15641088 : 15597568);
		String s = tile.complete == 1 ? (tile.getLengthX() + "*" +  tile.getLengthY() + "*" +  tile.getLengthZ() + " " + I18n.translateToLocalFormatted("gui.container.fission_controller.reactor")) : tile.problem;
		fontRenderer.drawString(s, 8 + xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, fontColor);
		String s2 = tile.problemPosBool == 0 ? "" : I18n.translateToLocalFormatted("gui.container.fission_controller.pos") + " " + tile.problemPos;
		fontRenderer.drawString(s2, 8 + xSize / 2 - fontRenderer.getStringWidth(s2) / 2, 17, fontColor);
		//fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
		/*String energy = tile.storage.getEnergyStored() + " RF";
		fontRenderer.drawString(energy, 28, ySize - 92, fontColor);*/
		String cells = I18n.translateToLocalFormatted("gui.container.fission_controller.cells") + " " + tile.cells;
		fontRenderer.drawString(cells, 28, ySize - 92, fontColor);
		String power = NCMath.prefix(tile.processPower, 5, "RF/t");
		fontRenderer.drawString(power, 28, ySize - 103, fontColor);
		String fuel = tile.getFuelName().endsWith("OXIDE") ? tile.getFuelName().substring(0, tile.getFuelName().length() - 3) : tile.getFuelName();
		fontRenderer.drawString(fuel, 28, ySize - 114, fontColor);
		String heat = NCMath.prefix(tile.heat, 5, "H");
		fontRenderer.drawString(heat, 170 - fontRenderer.getStringWidth(heat), ySize - 92, fontColor);
		String heatGen = NCMath.prefix(tile.heatChange, 5, "H/t");
		fontRenderer.drawString(heatGen, 170 - fontRenderer.getStringWidth(heatGen), ySize - 103, fontColor);
		String efficiency = I18n.translateToLocalFormatted("gui.container.fission_controller.efficiency") + " " + tile.efficiency + "%";
		fontRenderer.drawString(efficiency, 170 - fontRenderer.getStringWidth(efficiency), ySize - 114, fontColor);
		
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 6, 85);
		drawHeatTooltip(mouseX, mouseY, 18, 6, 6, 85);
	}
	
	public List<String> energyInfo(ITileEnergy tile) {
		String energy = NCMath.prefix(tile.getStorage().getEnergyStored(), tile.getStorage().getMaxEnergyStored(), 5, "RF");
		String power = NCMath.prefix(this.tile.getProcessPower(), 5, "RF/t");
		return Lists.newArrayList(TextFormatting.LIGHT_PURPLE + I18n.translateToLocalFormatted("gui.container.energy_stored") + TextFormatting.WHITE + " " + energy, TextFormatting.LIGHT_PURPLE + I18n.translateToLocalFormatted("gui.container.power_gen") + TextFormatting.WHITE + " " + power);
	}
	
	public List<String> heatInfo() {
		String heat = NCMath.prefix(tile.heat, tile.getMaxHeat(), 5, "H");
		return Lists.newArrayList(TextFormatting.YELLOW + I18n.translateToLocalFormatted("gui.container.fission_controller.heat") + TextFormatting.WHITE + " " + heat);
	}
	
	public void drawHeatTooltip(int mouseX, int mouseY, int x, int y, int width, int height) {
		drawTooltip(heatInfo(), mouseX, mouseY, x, y, width, height);
	}
	
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
