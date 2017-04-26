package nc.gui.energy.generator;

import nc.Global;
import nc.container.energy.generator.ContainerFissionController;
import nc.tile.generator.TileFissionController;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiFissionController extends GuiContainer {
	
	private final InventoryPlayer playerInventory;
	protected TileFissionController tile;
	protected final ResourceLocation gui_textures;

	public GuiFissionController(EntityPlayer player, TileFissionController tile) {
		super(new ContainerFissionController(player, tile));
		playerInventory = player.inventory;
		this.tile = tile;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "fission_controller" + ".png");
		xSize = 176;
		ySize = 189;
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = tile.isGenerating ? -1 : (tile.complete == 1 ? 15641088 : 15597568);
		String s = tile.complete == 1 ? (tile.getLengthX() + "*" +  tile.getLengthY() + "*" +  tile.getLengthZ() + " " + I18n.translateToLocalFormatted("gui.container.fission_controller.reactor")) : tile.problem;
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, fontColor);
		//fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
		String energy = tile.storage.getEnergyStored() + " RF";
		fontRendererObj.drawString(energy, 28, ySize - 105, fontColor);
		String power = tile.processPower + " RF/t";
		fontRendererObj.drawString(power, 28, ySize - 116, fontColor);
		String fuel = tile.getFuelName();
		fontRendererObj.drawString(fuel, 28, ySize - 127, fontColor);
		String heat = tile.heat + " K";
		fontRendererObj.drawString(heat, 170 - fontRendererObj.getStringWidth(heat), ySize - 105, fontColor);
		String heatGen = tile.heatChange + " K/t";
		fontRendererObj.drawString(heatGen, 170 - fontRendererObj.getStringWidth(heatGen), ySize - 116, fontColor);
		String cells = I18n.translateToLocalFormatted("gui.container.fission_controller.cells") + " " + tile.cells;
		fontRendererObj.drawString(cells, 170 - fontRendererObj.getStringWidth(cells), ySize - 127, fontColor);
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
