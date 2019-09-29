package nc.multiblock.gui;

import java.util.ArrayList;
import java.util.List;

import nc.Global;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.gui.element.MultiblockButton;
import nc.multiblock.network.ClearAllPacket;
import nc.network.PacketHandler;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.NCUtil;
import nc.util.StringHelper;
import nc.util.UnitHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public class GuiSolidFissionController extends GuiMultiblockController<FissionReactor> {
	
	protected final ResourceLocation gui_texture;
	
	public GuiSolidFissionController(FissionReactor multiblock, BlockPos controllerPos, Container container) {
		super(multiblock, controllerPos, container);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "solid_fission_controller" + ".png");
		xSize = 176;
		ySize = 114;
	}
	
	@Override
	protected ResourceLocation getGuiTexture() {
		return gui_texture;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		if (NCUtil.isModifierKeyDown()) drawTooltip(clearAllFluidsInfo(), mouseX, mouseY, 153, 71, 18, 18);
		
		drawHeatTooltip(mouseX, mouseY, 6, 103, 164, 6);
	}
	
	//TODO
	public List<String> heatInfo() {
		List<String> info = new ArrayList<String>();
		info.add(TextFormatting.YELLOW + Lang.localise("gui.nc.container.fission_controller.heat_stored") + " " + TextFormatting.WHITE + UnitHelper.prefix(multiblock.heatBuffer.getHeatStored(), multiblock.heatBuffer.getHeatCapacity(), 6, "H"));
		info.add(TextFormatting.YELLOW + Lang.localise("gui.nc.container.fission_controller.net_heating") + " " + TextFormatting.WHITE + UnitHelper.prefix(multiblock.getNetHeating(), 6, "H/t"));
		info.add(TextFormatting.BLUE + Lang.localise("gui.nc.container.fission_controller.cooling") + " " + TextFormatting.WHITE + UnitHelper.prefix(-multiblock.cooling, 6, "H/t"));
		return info;
	}
	
	public void drawHeatTooltip(int mouseX, int mouseY, int x, int y, int width, int height) {
		drawTooltip(heatInfo(), mouseX, mouseY, x, y, width, height);
	}
	
	//TODO
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isReactorOn ? -1 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" +  multiblock.getInteriorLengthY() + "*" +  multiblock.getInteriorLengthZ() + " " + Lang.localise("gui.nc.container.solid_fission_controller.reactor");
		fontRenderer.drawString(title, xSize / 2 - width(title) / 2, 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double)width(title)/width("-")));
		fontRenderer.drawString(underline, xSize / 2 - width(underline) / 2, 12, fontColor);
		
		String clusters = Lang.localise("gui.nc.container.fission_controller.clusters") + " " + multiblock.clusterCount;
		fontRenderer.drawString(clusters, xSize / 2 - width(clusters) / 2, 22, fontColor);
		
		String heatMult = Lang.localise("gui.nc.container.fission_controller.heat_mult") + " " + NCMath.decimalPlaces(100D*multiblock.meanHeatMult, 1) + "%";
		fontRenderer.drawString(heatMult, xSize / 2 - width(heatMult) / 2, 34, fontColor);
		
		String efficiency = Lang.localise("gui.nc.container.solid_fission_controller.efficiency") + " " + NCMath.decimalPlaces(100D*multiblock.meanEfficiency, 1) + "%";
		fontRenderer.drawString(efficiency, xSize / 2 - width(efficiency) / 2, 46, fontColor);
		
		String sparsity = NCUtil.isModifierKeyDown() ? Lang.localise("gui.nc.container.fission_controller.useful_parts") + " " + multiblock.usefulPartCount + "/" + multiblock.getInteriorVolume() : Lang.localise("gui.nc.container.fission_controller.sparsity") + " " + NCMath.decimalPlaces(100D*multiblock.sparsityEfficiencyMult, 1) + "%";
		fontRenderer.drawString(sparsity, xSize / 2 - width(sparsity) / 2, 58, fontColor);
		
		String temperature = Lang.localise("gui.nc.container.fission_controller.temperature") + " " + (NCUtil.isModifierKeyDown() ? multiblock.getTemperature() - 273 + " °C" : multiblock.getTemperature() + " K");
		fontRenderer.drawString(temperature, xSize / 2 - width(temperature) / 2, 76, fontColor);
		
		String heat_gen = Lang.localise("gui.nc.container.fission_controller.net_heating") + " " + UnitHelper.prefix(multiblock.getNetHeating(), 6, "H/t");
		fontRenderer.drawString(heat_gen, xSize / 2 - width(heat_gen) / 2, 88, fontColor);
	}
	
	//TODO
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		int h = (int)Math.round((double)multiblock.heatBuffer.getHeatStored()/(double)multiblock.heatBuffer.getHeatCapacity()*164);
		drawTexturedModalRect(guiLeft + 6, guiTop + 102, 3, 114, h, 6);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new MultiblockButton.ClearAll(0, guiLeft + 153, guiTop + 71));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (multiblock.WORLD.isRemote) {
			if (guiButton.id == 0 && NCUtil.isModifierKeyDown()) {
				PacketHandler.instance.sendToServer(new ClearAllPacket(controllerPos));
			}
		}
	}
}
