package nc.gui.generator;

import nc.Global;
import nc.container.generator.ContainerFusionCore;
import nc.gui.GuiFluidRenderer;
import nc.tile.generator.TileFusionCore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiFusionCore extends GuiContainer {
	
	private final InventoryPlayer playerInventory;
	protected TileFusionCore tile;
	protected final ResourceLocation gui_textures;

	public GuiFusionCore(EntityPlayer player, TileFusionCore tile) {
		super(new ContainerFusionCore(player, tile));
		playerInventory = player.inventory;
		this.tile = tile;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "fusion_core" + ".png");
		xSize = 196;
		ySize = 187;
	}
	
	protected int widthHalf(String s) {
		return fontRendererObj.getStringWidth(s)/2;
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = tile.isGenerating ? -1 : (tile.complete == 1 ? 15641088 : 15597568);
		String name = I18n.translateToLocalFormatted("gui.container.fusion_core.reactor");
		fontRendererObj.drawString(name, 108 - widthHalf(name), 10, fontColor);
		String size = tile.complete == 1 ? (I18n.translateToLocalFormatted("gui.container.fusion_core.size") + " " + tile.size) : tile.problem;
		fontRendererObj.drawString(size, 108 - widthHalf(size), 21, fontColor);
		String energy = I18n.translateToLocalFormatted("gui.container.fusion_core.energy") + " " + tile.storage.getEnergyStored() + " RF";
		fontRendererObj.drawString(energy, 108 - widthHalf(energy), 32, fontColor);
		String power = I18n.translateToLocalFormatted("gui.container.fusion_core.power") + " " + ((int) tile.processPower) + " RF/t";
		fontRendererObj.drawString(power, 108 - widthHalf(power), 43, fontColor);
		String heat = I18n.translateToLocalFormatted("gui.container.fusion_core.heat") + " " + ((int) tile.heat) + " kK";
		fontRendererObj.drawString(heat, 108 - widthHalf(heat), 54, fontColor);
		String efficiency = I18n.translateToLocalFormatted("gui.container.fusion_core.efficiency") + " " + ((int) tile.efficiency) + "%";
		fontRendererObj.drawString(efficiency, 108 - widthHalf(efficiency), 65, fontColor);
		String input1 = tile.tanks[0].getFluid() != null ? tile.tanks[0].getFluidName() : I18n.translateToLocalFormatted("gui.container.fusion_core.empty");
		String input2 = tile.tanks[1].getFluid() != null ? tile.tanks[1].getFluidName() : I18n.translateToLocalFormatted("gui.container.fusion_core.empty");
		String inputCap1 = input1.substring(0, 1).toUpperCase() + input1.substring(1);
		String inputCap2 = input2.substring(0, 1).toUpperCase() + input2.substring(1);
		String fuels1 = I18n.translateToLocalFormatted("gui.container.fusion_core.fuels");
		fontRendererObj.drawString(fuels1, 108 - widthHalf(fuels1), 76, fontColor);
		String fuels2 = inputCap1 + ", " + inputCap2;
		fontRendererObj.drawString(fuels2, 108 - widthHalf(fuels2), 87, fontColor);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(gui_textures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		double energy = Math.round(((double) tile.storage.getEnergyStored()) / ((double) tile.storage.getMaxEnergyStored()) * 95D);
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 95 - (int) energy, 196, 90 + 95 - (int) energy, 6, (int) energy);
		
		double h = Math.round((tile.heat / tile.getMaxHeat()) * 95D);
		drawTexturedModalRect(guiLeft + 18, guiTop + 6 + 95 - (int) h, 202, 90 + 95 - (int) h, 6, (int) h);
		
		double efficiency = Math.round((tile.efficiency / 100D) * 95D);
		drawTexturedModalRect(guiLeft + 28, guiTop + 6 + 95 - (int) efficiency, 208, 90 + 95 - (int) efficiency, 6, (int) efficiency);
		
		GuiFluidRenderer.renderGuiTank(tile.tanks[0], guiLeft + 38, guiTop + 6, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(tile.tanks[1], guiLeft + 38, guiTop + 55, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(tile.tanks[2], guiLeft + 172, guiTop + 6, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(tile.tanks[3], guiLeft + 182, guiTop + 6, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(tile.tanks[4], guiLeft + 172, guiTop + 55, zLevel, 6, 46);
		GuiFluidRenderer.renderGuiTank(tile.tanks[5], guiLeft + 182, guiTop + 55, zLevel, 6, 46);
	}
}
