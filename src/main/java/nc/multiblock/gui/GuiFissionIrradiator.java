package nc.multiblock.gui;

import java.util.List;

import com.google.common.collect.Lists;

import nc.Global;
import nc.gui.NCGui;
import nc.gui.element.GuiItemRenderer;
import nc.multiblock.container.ContainerFissionIrradiator;
import nc.multiblock.fission.tile.TileFissionIrradiator;
import nc.util.Lang;
import nc.util.UnitHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GuiFissionIrradiator extends NCGui {
	
	protected final TileFissionIrradiator irradiator;
	protected final ResourceLocation gui_textures;

	public GuiFissionIrradiator(EntityPlayer player, TileFissionIrradiator irradiator) {
		super(new ContainerFissionIrradiator(player, irradiator));
		this.irradiator = irradiator;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "fission_irradiator" + ".png");
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawTooltip(irradiator.clusterHeatCapacity >= 0L ? heatInfo() : noClusterInfo(), mouseX, mouseY, 8, 6, 16, 74);
	}
	
	public List<String> heatInfo() {
		String heat = UnitHelper.prefix(irradiator.clusterHeatStored, irradiator.clusterHeatCapacity, 5, "H");
		return Lists.newArrayList(TextFormatting.YELLOW + Lang.localise("gui.nc.container.fission_irradiator.heat_stored") + TextFormatting.WHITE + " " + heat);
	}
	
	public List<String> noClusterInfo() {
		return Lists.newArrayList(TextFormatting.RED + Lang.localise("gui.nc.container.no_cluster"));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = irradiator.getMultiblock() != null && irradiator.getMultiblock().isReactorOn ? -1 : 15641088;
		String s = Lang.localise("gui.nc.container.fission_irradiator.irradiator");
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, fontColor);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(gui_textures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if (!irradiator.getFilterStacks().get(0).isEmpty()) {
			new GuiItemRenderer(guiLeft + 56, guiTop + 35, 0.5F, irradiator.getFilterStacks().get(0)).draw();
		}
		mc.getTextureManager().bindTexture(gui_textures);
		
		if (irradiator.clusterHeatCapacity >= 0L) {
			int e = (int) Math.round(74D*irradiator.clusterHeatStored/irradiator.clusterHeatCapacity);
			drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - e, 176, 90 + 74 - e, 16, e);
		}
		else drawGradientRect(guiLeft + 8, guiTop + 6, guiLeft + 8 + 16, guiTop + 6 + 74, 0xFF777777, 0xFF535353);
		
		drawTexturedModalRect(guiLeft + 74, guiTop + 35, 176, 3, getCookProgressScaled(37), 16);
	}
	
	protected int getCookProgressScaled(int pixels) {
		if (irradiator.baseProcessTime/irradiator.getSpeedMultiplier() < 4D) {
			return irradiator.isProcessing ? pixels : 0;
		}
		double i = irradiator.time, j = irradiator.baseProcessTime;
		return j != 0D ? (int) Math.round(i * pixels / j) : 0;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		initButtons();
	}
	
	public void initButtons() {
		
	}
}
